package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;
import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.Question;

public interface QuestionService {

    Question getById(Integer id);

    Question create(Question question, Integer testId);

    List<Question> create(Collection<Question> questions, Integer testId);
}
