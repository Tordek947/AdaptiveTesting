package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

import net.atlassian.cmathtutor.adaptive.domain.data.parameter.DecisionMakingParameter;
import net.atlassian.cmathtutor.adaptive.domain.data.parameter.TestStateParameter;
import net.atlassian.cmathtutor.adaptive.domain.entity.MinGradeMarkRequirement;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.QuestionDefinitionRuleMapper;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.QuestionMapper;
import net.atlassian.cmathtutor.adaptive.service.QuestionDefinitionRuleService;
import net.atlassian.cmathtutor.adaptive.service.TestProcessService;

@Component
public class DefaultTestProcessService implements TestProcessService {

    @Autowired
    private QuestionDefinitionRuleService questionDefinitionRuleService;
    @Autowired
    private QuestionDefinitionRuleMapper questionDefinitionRuleMapper;
    @Autowired
    private QuestionMapper questionMapper;

    @Override
    public DecisionMakingParameter getAvailableQuestionsForCurrentTestState(TestStateParameter testState) {
	int currentQuestionNumber = testState.getCurrentQuestionNumber();
	List<QuestionDefinitionRule> questionDefinitionRules = questionDefinitionRuleService
		.getAllByTestId(testState.getTestId()).stream()
		.filter(qdr -> qdr.getQuestionNumberFrom() <= currentQuestionNumber)
		.filter(qdr -> qdr.getQuestionNumberTo() == null ? true
			: qdr.getQuestionNumberTo() >= currentQuestionNumber)
		.sorted(this::compareQuestionDefinitionRulesByTotalMark)
		.collect(Collectors.toList());

	Set<Integer> alreadyDisplayedQuestionIds = testState.getAlreadyDisplayedQuestionIds();
	Map<String, Integer> currentGradeMarks = testState.getGradeMarks();
	List<QuestionDefinitionRule> appliedQuestionDefinitionRules = Lists.newLinkedList();
	List<Question> suitableQuestions = questionDefinitionRules.stream()
		.filter(qdr -> isSuitableForCurrentGradeMarks(qdr, currentGradeMarks))
		.peek(appliedQuestionDefinitionRules::add)
		.map(QuestionDefinitionRule::getQuestions)
		.filter(qdr -> isEmptyOrContainsNotAlreadyDisplayed(qdr, alreadyDisplayedQuestionIds))
		.findFirst().orElse(Collections.emptyList());
	List<Question> availableQuestions = notAlreadyDisplayed(suitableQuestions, alreadyDisplayedQuestionIds);
	return DecisionMakingParameter.builder()
		.appliedQuestionDefinitionRules(Mapper.collectionToList(appliedQuestionDefinitionRules,
			questionDefinitionRuleMapper::entityToData))
		.availableQuestions(Mapper.collectionToList(availableQuestions, questionMapper::entityToData))
		.build();

    }

    private boolean isSuitableForCurrentGradeMarks(QuestionDefinitionRule questionDefinitionRule,
	    Map<String, Integer> currentGradeMarks) {
	return questionDefinitionRule.getMinGradeMarkRequirements().stream()
		.allMatch(minGradeMarkRequirement -> currentGradeMarks
			.get(minGradeMarkRequirement.getGrade().getCode()) >= minGradeMarkRequirement.getValue());
    }

    private boolean isEmptyOrContainsNotAlreadyDisplayed(List<Question> questions,
	    Set<Integer> alreadyDisplayedQuestionIds) {
	if (questions.isEmpty()) {
	    return true;
	}
	return questions.stream().filter(question -> !alreadyDisplayedQuestionIds.contains(question.getId())).findAny()
		.isPresent();
    }

    private List<Question> notAlreadyDisplayed(List<Question> questions, Set<Integer> alreadyDisplayedQuestionIds) {
	return questions.stream()
		.filter(question -> !alreadyDisplayedQuestionIds.contains(question.getId()))
		.collect(Collectors.toList());
    }

    private int compareQuestionDefinitionRulesByTotalMark(QuestionDefinitionRule left, QuestionDefinitionRule right) {
	int leftTotalMark = calculateTotalMark(left);
	int rightTotalMark = calculateTotalMark(right);
	return rightTotalMark - leftTotalMark;// to make it in reverse order (from 'more' to 'less')
    }

    private int calculateTotalMark(QuestionDefinitionRule questionDefinitionRule) {
	return questionDefinitionRule.getMinGradeMarkRequirements().stream()
		.map(MinGradeMarkRequirement::getValue)
		.mapToInt(Integer::intValue)
		.sum();
    }

}
