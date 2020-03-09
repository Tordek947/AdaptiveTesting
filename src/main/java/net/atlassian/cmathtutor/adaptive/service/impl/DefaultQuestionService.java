package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.QuestionRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionAnswerService;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultQuestionService implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionAnswerService questionAnswerService;

    @Autowired
    private GradeService gradeService;

    @Override
    public Question getById(Integer id) {
	Question question = questionRepository.findByIdFetchQuestionAnswer(id).orElseThrow(NotFoundException::new);
	return question;
    }

    @Override
    @Transactional
    public Question create(Question question, Integer testId) {
	prepareForCreation(question, testId);
	replaceGradesWithExistedOnes(Stream.of(question), testId);
	question = questionRepository.save(question);
	Integer questionId = question.getId();
	questionAnswerService.create(question.getQuestionAnswers(), questionId);
	return question;
    }

    private void prepareForCreation(Question question, Integer testId) {
	question.setId(null);
	question.setTestId(testId);
	question.getQuestionAnswers().stream().flatMap(qa -> qa.getGradeMarkChangeRules().stream())
		.map(GradeMarkChangeRule::getGrade)
		.forEach(grade -> grade.setTestId(testId));
    }

    private void replaceGradesWithExistedOnes(Stream<Question> questions, Integer testId) {
	Map<String, Grade> gradesByCode = gradeService.getAllByTestId(testId).stream()
		.collect(Collectors.toMap(Grade::getCode, Function.identity()));
	questions.flatMap(q -> q.getQuestionAnswers().stream()).flatMap(qa -> qa.getGradeMarkChangeRules().stream())
		.forEach(gradeMarkChangeRule -> gradeMarkChangeRule
			.setGrade(gradesByCode.get(gradeMarkChangeRule.getGrade().getCode())));
    }

    @Transactional
    @Override
    public List<Question> create(List<Question> questions, Integer testId) {
	for (Question question : questions) {
	    prepareForCreation(question, testId);
	}
	replaceGradesWithExistedOnes(questions.stream(), testId);
	List<Question> savedQuestions = questionRepository.saveAll(questions);
	for (Question question : savedQuestions) {
	    Integer questionId = question.getId();
	    questionAnswerService.create(question.getQuestionAnswers(), questionId);
	}
	return questionRepository.saveAll(savedQuestions);
    }

}
