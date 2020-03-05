package net.atlassian.cmathtutor.adaptive.repository;

import java.util.Optional;

import net.atlassian.cmathtutor.adaptive.domain.entity.Test;

public interface TestRepository extends CrudRepository<Test, Integer> {

    Optional<Test> findByName(String name);
}
