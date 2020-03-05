package net.atlassian.cmathtutor.adaptive.service;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.Test;

public interface TestService {

    List<Test> getAll();

    Test getById(Integer id);

    Test getByName(String name);

    Test create(Test test);
}
