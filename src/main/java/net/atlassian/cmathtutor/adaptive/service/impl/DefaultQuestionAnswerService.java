package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.repository.QuestionAnswerRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeMarkChangeRuleService;
import net.atlassian.cmathtutor.adaptive.service.QuestionAnswerService;

@Service
public class DefaultQuestionAnswerService implements QuestionAnswerService {

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;
    @Autowired
    private GradeMarkChangeRuleService gradeMarkChangeRuleService;

    @Transactional
    @Override
    public QuestionAnswer create(QuestionAnswer questionAnswer, Integer questionId) {
	prepareForCreation(questionAnswer, questionId);
	questionAnswer = questionAnswerRepository.save(questionAnswer);
	Long questionAnswerId = questionAnswer.getId();
	gradeMarkChangeRuleService.create(questionAnswer.getGradeMarkChangeRules(), questionAnswerId);
	return questionAnswerRepository.save(questionAnswer);
    }

    private void prepareForCreation(QuestionAnswer questionAnswer, Integer questionId) {
	questionAnswer.setId(null);
	questionAnswer.setQuestionId(questionId);
    }

    @Transactional
    @Override
    public List<QuestionAnswer> create(Iterable<QuestionAnswer> questionAnswers, Integer questionId) {
	for (QuestionAnswer questionAnswer : questionAnswers) {
	    prepareForCreation(questionAnswer, questionId);
	}
	List<QuestionAnswer> savedQuestionAnswers = questionAnswerRepository.saveAll(questionAnswers);
	for (QuestionAnswer questionAnswer : savedQuestionAnswers) {
	    Long questionAnswerId = questionAnswer.getId();
	    gradeMarkChangeRuleService.create(questionAnswer.getGradeMarkChangeRules(), questionAnswerId);
	}
	return questionAnswerRepository.saveAll(savedQuestionAnswers);
    }

}
