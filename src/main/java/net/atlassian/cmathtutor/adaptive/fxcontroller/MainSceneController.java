package net.atlassian.cmathtutor.adaptive.fxcontroller;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Sets;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import lombok.extern.slf4j.Slf4j;
import net.atlassian.cmathtutor.adaptive.domain.data.GradeData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionAnswerData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.TestData;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.DecisionMakingParameter;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.TestStateParameter;
import net.atlassian.cmathtutor.adaptive.service.PredefinedTestService;
import net.rgielen.fxweaver.core.FxmlView;

@Component
@Slf4j
@FxmlView("/fxviews/MainScene.fxml")
public class MainSceneController implements Initializable {

    @FXML
    Label whatToDoLabel;
    @FXML
    Button initDatabaseButton;
    @FXML
    AnchorPane testContentAnchorPane;
    @FXML
    Label testNameLabel;
    @FXML
    Button startTestButton;
    @FXML
    AnchorPane gameProcessAnchorPane;
    @FXML
    Label questionNumberLabel;
    @FXML
    Label questionLabel;
    @FXML
    VBox gradeMarksStateVBox;
    private Map<String, Label> gradeMarksLabelsByGrade;
    @FXML
    AnchorPane ansersAnchorPane;
    @FXML
    VBox answerOptionsVBox;
    private ToggleGroup toggleGroup;
    @FXML
    Label checkAnswerResultLabel;
    @FXML
    TextArea logTextArea;
    @FXML
    Button nextButton;
    @FXML
    Button checkAnswerButton;

    @Autowired
    private RestTemplate restTemplate;

    private boolean isDatabaseInitialized;
    private TestData loadedTest;
    private TestStateParameter testStateParameter;
    private Random random;
    private QuestionData currentQuestion;
    private boolean isTestStarted = false;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
	random = new Random();
	toggleGroup = new ToggleGroup();
	whatToDoLabel.visibleProperty().bind(initDatabaseButton.disableProperty().not());
	isDatabaseInitialized = false;
	initDatabaseButton.setDisable(isDatabaseInitialized);
    }

    private void consumeLoadedTest(TestData testData) {
	loadedTest = testData;
	testNameLabel.setText(loadedTest.getName());
	startTestButton.setDisable(false);
	initDatabaseButton.setDisable(true);
	isDatabaseInitialized = true;

    }

    @FXML
    public void initializeDatabase(ActionEvent event) throws RestClientException, UnsupportedEncodingException {
	if (!isDatabaseInitialized) {
	    try {
		ResponseEntity<TestData> testResponseEntity = restTemplate.getForEntity(
			URI.create("http://localhost:8080/rest/tests/by?name="
				+ URLEncoder.encode(PredefinedTestService.SIMPLE_ENGLISH_CREDIT_NAME,
					StandardCharsets.UTF_8.toString())),
			TestData.class);
		consumeLoadedTest(testResponseEntity.getBody());
	    } catch (RestClientException e) {
		ResponseEntity<Integer> testIdResponseEntity = restTemplate.postForEntity(
			URI.create("http://localhost:8080/rest/tests/simple-english-credit"), null, Integer.class);
		ResponseEntity<TestData> testResponseEntity = restTemplate.getForEntity(
			URI.create("http://localhost:8080/rest/tests/" + testIdResponseEntity.getBody()),
			TestData.class);
		consumeLoadedTest(testResponseEntity.getBody());
	    } finally {
		log.debug(
			"The database was initialized " + (isDatabaseInitialized ? "SUCCESSFULLY" : "INSUCCESSFULLY"));
	    }
	}
    }

    @FXML
    public void startTest(ActionEvent event) {
	Map<String, Integer> gradeMarks = loadedTest.getGrades().stream()
		.collect(Collectors.toMap(GradeData::getCode, GradeData::getInitialMark));
	testStateParameter = TestStateParameter.builder().alreadyDisplayedQuestionIds(Sets.newHashSet())
		.currentQuestionNumber(0)
		.gradeMarks(gradeMarks)
		.testId(loadedTest.getId())
		.build();
	gradeMarksLabelsByGrade = gradeMarks.entrySet().stream()
		.collect(Collectors.toMap(Entry::getKey, e -> new Label(e.getKey() + ":" + e.getValue().toString())));
	if (isTestStarted) {
	    Platform.runLater(() -> {
		gradeMarksStateVBox.getChildren().clear();
	    });
	}
	Platform.runLater(() -> {
	    gradeMarksStateVBox.getChildren().addAll(gradeMarksLabelsByGrade.values());
	});
	isTestStarted = true;
	ansersAnchorPane.setVisible(true);
	next();
    }

    @FXML
    public void next() {
	nextButton.setDisable(true);
	testStateParameter.setCurrentQuestionNumber(testStateParameter.getCurrentQuestionNumber() + 1);
	checkAnswerResultLabel.setVisible(false);
	DecisionMakingParameter decisionMakingParameter = getNextDecisionMakingParameter();
	decisionMakingParameter.getAppliedQuestionDefinitionRules().stream().map(this::mapToLoggedRuleString)
		.forEach(qdr -> {
		    logTextArea.appendText(qdr + "\r\n");
		});
	List<QuestionData> availableQuestions = decisionMakingParameter.getAvailableQuestions();
	if (availableQuestions.size() == 0) {
	    finishTest();
	} else {
	    int randomQuestionNumber = random.nextInt(availableQuestions.size());
	    QuestionData chosenQuestionData = availableQuestions.get(randomQuestionNumber);
	    testStateParameter.getAlreadyDisplayedQuestionIds().add(chosenQuestionData.getId());
	    displayQuestion(getQuestionWithQuestionAnswers(chosenQuestionData.getId()));
	}
    }

    private DecisionMakingParameter getNextDecisionMakingParameter() {
	return restTemplate.postForEntity(
		URI.create("http://localhost:8080/rest/testprocesses/questions/available"),
		testStateParameter, DecisionMakingParameter.class).getBody();
    }

    private QuestionData getQuestionWithQuestionAnswers(Integer questionId) {
	return restTemplate.getForEntity(
		URI.create(String.format("http://localhost:8080/rest/tests/%s/questions/%s",
			testStateParameter.getTestId(), questionId)),
		QuestionData.class).getBody();
    }

    private String mapToLoggedRuleString(QuestionDefinitionRuleData questionDefinitionRule) {
	String result = questionDefinitionRule.getId() + ";" + questionDefinitionRule.getQuestionNumberFrom() + "-"
		+ questionDefinitionRule.getQuestionNumberTo() + ";";
	StringBuilder minGradeMarkRequirements = questionDefinitionRule.getMinGradeMarkRequirements().entrySet()
		.stream()
		.map(gmreq -> gmreq.getKey() + ":" + gmreq.getValue().getValue() + ",")
		.collect(StringBuilder::new, StringBuilder::append, StringBuilder::append);
	if (minGradeMarkRequirements.length() > 0) {
	    result += minGradeMarkRequirements.subSequence(0, minGradeMarkRequirements.length() - 1) + ";";
	}
	StringBuilder questions = questionDefinitionRule.getQuestions().stream().map(q -> q.getId() + ",").collect(
		StringBuilder::new,
		StringBuilder::append, StringBuilder::append);
	if (questions.length() > 0) {
	    result += questions.subSequence(0, questions.length() - 1);
	}
	return result;
    }

    private void finishTest() {
	log.debug("The test is finished!");
	ansersAnchorPane.setVisible(false);
	Entry<String, Integer> maxGradeMarkEntry = testStateParameter.getGradeMarks().entrySet().iterator().next();
	for (Entry<String, Integer> gradeMarkEntry : testStateParameter.getGradeMarks().entrySet()) {
	    if (gradeMarkEntry.getValue() > maxGradeMarkEntry.getValue()) {
		maxGradeMarkEntry = gradeMarkEntry;
	    }
	}
	questionLabel.setText("The test is FINISHED. Your grade is " + maxGradeMarkEntry.getKey());
    }

    private void displayQuestion(QuestionData questionData) {
	questionNumberLabel.setText(String.valueOf(testStateParameter.getCurrentQuestionNumber()));
	questionLabel.setText(questionData.getSentence());
	this.currentQuestion = questionData;
	final List<RadioButton> options = questionData.getQuestionAnwsers().stream().map(this::createAnswerRadioButton)
		.collect(Collectors.toList());
	Platform.runLater(() -> {
	    clearAnswerOptions();
	    options.forEach(o -> o.setToggleGroup(toggleGroup));
	    answerOptionsVBox.getChildren().addAll(options);
	});
	testStateParameter.getAlreadyDisplayedQuestionIds().add(questionData.getId());
	checkAnswerButton.setDisable(true);
    }

    private RadioButton createAnswerRadioButton(QuestionAnswerData answer) {
	RadioButton rb = new RadioButton(answer.getSentence());
	rb.setOnAction(this::onSelectAnswer);
	return rb;
    }

    private void onSelectAnswer(ActionEvent event) {
	this.checkAnswerButton.setDisable(false);
    }

    @FXML
    public void checkAnswer(ActionEvent event) {
	checkAnswerButton.setDisable(true);
	nextButton.setDisable(false);
	int optionIndex = toggleGroup.getToggles().indexOf(toggleGroup.getSelectedToggle());
	QuestionAnswerData selectedQuestionAnswer = currentQuestion.getQuestionAnwsers().get(optionIndex);
	log.info("Selected question answer: " + selectedQuestionAnswer);
	String answerCorrectness = selectedQuestionAnswer.getIsCorrect() ? "CORRECT" : "WRONG";
	checkAnswerResultLabel
		.setText("Your answer is " + answerCorrectness);
	logTextArea.appendText(answerCorrectness + "\r\n");
	checkAnswerResultLabel.setVisible(true);
	selectedQuestionAnswer.getGradeMarkChangeRules().entrySet().forEach(entry -> {
	    testStateParameter.getGradeMarks().merge(entry.getKey(), entry.getValue().getMarkDelta(),
		    this::sumOrSetZero);
	});
	Platform.runLater(() -> {
	    gradeMarksLabelsByGrade.entrySet().forEach(entry -> {
		Integer newGradeMark = testStateParameter.getGradeMarks().get(entry.getKey());
		entry.getValue().setText(entry.getKey() + ":" + newGradeMark.toString());
	    });
	});
    }

    private Integer sumOrSetZero(Integer left, Integer right) {
	Integer sum = left + right;
	if (sum < 0) {
	    return 0;
	}
	return sum;
    }

    private void clearAnswerOptions() {
	Toggle[] toggles = toggleGroup.getToggles().toArray(new Toggle[0]);
	for (Toggle t : toggles) {
	    t.setToggleGroup(null);
	}
	log.debug("toggleGroup after clearance: {}", toggleGroup.getToggles());
	answerOptionsVBox.getChildren().clear();
    }
}
