package net.atlassian.cmathtutor.adaptive.service;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;

public interface QuestionAnswerService {

    QuestionAnswer create(QuestionAnswer questionAnswer, Integer questionId);

    List<QuestionAnswer> create(Iterable<QuestionAnswer> questionAnswers, Integer questionId);
}
