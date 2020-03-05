package net.atlassian.cmathtutor.adaptive.repository;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;

public interface GradeRepository extends CrudRepository<Grade, Integer> {

    List<Grade> findAllByTestId(Integer testId);
}
