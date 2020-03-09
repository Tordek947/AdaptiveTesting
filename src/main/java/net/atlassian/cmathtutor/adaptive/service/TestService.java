package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.Test;

public interface TestService {

    Collection<Test> getAll();

    Test getById(Integer id);

    Test getByName(String name);

    Test create(Test test);

    Test updateNameById(String name, Integer id);

    void deleteById(Integer id);
}
