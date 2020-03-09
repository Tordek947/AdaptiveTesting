package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;

public interface GradeService {

    Grade getById(Integer id);

    Grade create(Grade grade, Integer testId);

    Collection<Grade> create(Collection<Grade> grades, Integer testId);

    Collection<Grade> getAllByTestId(Integer testId);

}
