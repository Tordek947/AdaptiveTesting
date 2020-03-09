package net.atlassian.cmathtutor.adaptive.service.impl;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Sets;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.MinMarkRequirementData;
import net.atlassian.cmathtutor.adaptive.domain.data.csv.ScanGradeMarkChangeRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.csv.ScanGradeMinMarkRequirementData;
import net.atlassian.cmathtutor.adaptive.domain.data.csv.ScanQuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.Test;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;
import net.atlassian.cmathtutor.adaptive.domain.mapper.impl.QuestionDefinitionRuleMapper;
import net.atlassian.cmathtutor.adaptive.service.PredefinedTestService;
import net.atlassian.cmathtutor.adaptive.service.QuestionDefinitionRuleService;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;
import net.atlassian.cmathtutor.adaptive.service.TestService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultPredefinedTestService implements PredefinedTestService {

    private static final String CSV_HOME = "/csv/simple-english-credit/";

    private TestService testService;
    private QuestionService questionService;
    private QuestionDefinitionRuleService questionDefinitionRuleService;
    private QuestionDefinitionRuleMapper questionDefinitionRuleMapper;

    @Transactional
    @Override
    public Integer createSimpleEnglishCreditTest() {
	Map<String, Grade> grades = createGrades();
	Test test = Test.builder()
		.grades(grades)
		.name(SIMPLE_ENGLISH_CREDIT_NAME)
		.questions(Collections.emptySet())
		.build();
	test = testService.create(test);

	Integer minQuestioniId = createQuestions(test);
	createQuestionDefinitionRules(test, minQuestioniId);
	return test.getId();
    }

    private Map<String, Grade> createGrades() {
	List<Grade> grades = parseCsv("grades.csv", Grade.class);
	return grades.stream().collect(Collectors.toMap(Grade::getCode, Function.identity()));
    }

    private <T> List<T> parseCsv(String csvName, Class<T> clazz) {
	InputStream is = getClass().getResourceAsStream(CSV_HOME + csvName);
	List<T> entities;
	try (Reader reader = new BufferedReader(new InputStreamReader(is))) {

	    CsvToBean<T> csvToBean = new CsvToBeanBuilder<T>(reader)
		    .withIgnoreLeadingWhiteSpace(true)
		    .withType(clazz)
		    .withSeparator(';')
		    .build();

	    entities = csvToBean.parse();

	} catch (Exception ex) {
	    throw new RuntimeException("Unable to parse " + csvName, ex);
	}
	return entities;
    }

    private Integer createQuestions(Test test) {
	final String questionsCsvName = "questions.csv";
	List<Question> questions = parseCsv(questionsCsvName, Question.class);
	questions.forEach(q -> {
	    q.setQuestionAnswers(Sets.newHashSet());
	    q.setTestId(test.getId());
	});
	List<QuestionAnswer> answers = parseCsv("question_answers.csv", QuestionAnswer.class);
	Map<Integer, Question> questionsById = questions.stream()
		.collect(Collectors.toMap(Question::getId, Function.identity()));
	answers.forEach(qa -> {
	    qa.setGradeMarkChangeRules(Sets.newHashSet());
	    questionsById.get(qa.getQuestionId()).getQuestionAnswers().add(qa);
	});
	List<ScanGradeMarkChangeRuleData> scanGradeMarkChangeRules = parseCsv("grade_mark_change_rules.csv",
		ScanGradeMarkChangeRuleData.class);
	List<GradeMarkChangeRule> gradeMarkChangeRules = scanGradeMarkChangeRules.stream()
		.map(scanRule -> GradeMarkChangeRule.builder().grade(Grade.builder().code(scanRule.getGrade()).build())
			.markDelta(scanRule.getMarkDelta()).questionAnswerId(scanRule.getQuestionAnswerId()).build())
		.collect(Collectors.toList());
	Map<Long, QuestionAnswer> answersById = answers.stream()
		.collect(Collectors.toMap(QuestionAnswer::getId, Function.identity()));
	gradeMarkChangeRules.forEach(changeRule -> {
	    answersById.get(changeRule.getQuestionAnswerId()).getGradeMarkChangeRules().add(changeRule);
	});

	return questionService.create(questions, test.getId()).stream().map(Question::getId).min(Integer::compareTo)
		.orElseThrow(() -> new IllegalStateException("There are no questions prepared in " + questionsCsvName));
    }

    private void createQuestionDefinitionRules(Test test, Integer minQuestioniId) {
	Collection<ScanQuestionDefinitionRuleData> scanQuestionDefinitionRulesData = parseCsv(
		"question_definition_rules.csv", ScanQuestionDefinitionRuleData.class);
	Collection<CreateQuestionDefinitionRuleData> questionDefinitionRulesData = scanQuestionDefinitionRulesData
		.stream().map(scanQuestionDefinitionRuleData -> {
		    Map<String, MinMarkRequirementData> minGradeMarkRequirements = scanQuestionDefinitionRuleData
			    .getMinGradeMarkRequirements().stream()
			    .collect(Collectors.toMap(ScanGradeMinMarkRequirementData::getGradeCode,
				    scanGradeMinMarkRequirementData -> {
					return MinMarkRequirementData.builder()
						.value(scanGradeMinMarkRequirementData.getMinMark()).build();
				    }));
		    return CreateQuestionDefinitionRuleData.builder()
			    .minGradeMarkRequirements(minGradeMarkRequirements)
			    .questionIds(scanQuestionDefinitionRuleData.getQuestionIds())
			    .questionNumberFrom(scanQuestionDefinitionRuleData.getQuestionNumberFrom())
			    .questionNumberTo(scanQuestionDefinitionRuleData.getQuestionNumberTo())
			    .testId(test.getId())
			    .build();
		}).peek(qdr -> {
		    qdr.setQuestionIds(
			    qdr.getQuestionIds().stream().filter(Objects::nonNull).map(qid -> qid + minQuestioniId - 1)
				    .collect(Collectors.toList()));
		}).collect(Collectors.toList());
	List<QuestionDefinitionRule> questionDefinitionRules = Mapper.collectionToList(questionDefinitionRulesData,
		questionDefinitionRuleMapper::dataToEntity);
	questionDefinitionRuleService.create(questionDefinitionRules, test.getId());
    }

}
