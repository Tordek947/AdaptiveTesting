package net.atlassian.cmathtutor.adaptive.service;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;

public interface GradeService {

    Grade getById(Integer id);

    Grade create(Grade grade, Integer testId);

    List<Grade> create(Iterable<Grade> grades, Integer testId);

    List<Grade> getAllByTestId(Integer testId);

}
