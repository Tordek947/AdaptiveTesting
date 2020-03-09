package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.Question;

public interface QuestionService {

    Question getById(Integer id);

    Question create(Question question, Integer testId);

    Collection<Question> create(Collection<Question> questions, Integer testId);
}
