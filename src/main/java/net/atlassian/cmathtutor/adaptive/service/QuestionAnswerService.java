package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;

public interface QuestionAnswerService {

    QuestionAnswer create(QuestionAnswer questionAnswer, Integer questionId);

    Collection<QuestionAnswer> create(Collection<QuestionAnswer> questionAnswers, Integer questionId);
}
