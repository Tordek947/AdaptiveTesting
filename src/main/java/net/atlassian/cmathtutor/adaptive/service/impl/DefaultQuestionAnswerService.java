package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.repository.QuestionAnswerRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeMarkChangeRuleService;
import net.atlassian.cmathtutor.adaptive.service.QuestionAnswerService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultQuestionAnswerService implements QuestionAnswerService {

    private QuestionAnswerRepository questionAnswerRepository;
    private GradeMarkChangeRuleService gradeMarkChangeRuleService;

    @Transactional
    @Override
    public QuestionAnswer create(QuestionAnswer questionAnswer, Integer questionId) {
	prepareForCreation(questionAnswer, questionId);
	questionAnswer = questionAnswerRepository.save(questionAnswer);
	Long questionAnswerId = questionAnswer.getId();
	gradeMarkChangeRuleService.create(questionAnswer.getGradeMarkChangeRules(), questionAnswerId);
	return questionAnswer;
    }

    private void prepareForCreation(QuestionAnswer questionAnswer, Integer questionId) {
	questionAnswer.setId(null);
	questionAnswer.setQuestionId(questionId);
    }

    @Transactional
    @Override
    public Collection<QuestionAnswer> create(Collection<QuestionAnswer> questionAnswers, Integer questionId) {
	for (QuestionAnswer questionAnswer : questionAnswers) {
	    prepareForCreation(questionAnswer, questionId);
	}
	questionAnswers = questionAnswerRepository.saveAll(questionAnswers);
	for (QuestionAnswer questionAnswer : questionAnswers) {
	    Long questionAnswerId = questionAnswer.getId();
	    gradeMarkChangeRuleService.create(questionAnswer.getGradeMarkChangeRules(), questionAnswerId);
	}
	return questionAnswers;
    }

}
