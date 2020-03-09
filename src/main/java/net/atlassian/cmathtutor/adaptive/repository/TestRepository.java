package net.atlassian.cmathtutor.adaptive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import net.atlassian.cmathtutor.adaptive.domain.entity.Test;

public interface TestRepository extends CrudRepository<Test, Integer> {

    Optional<Test> findByName(String name);

    @Override
    @Modifying
    @Query("delete Test t where t.id = :id")
    void deleteById(Integer id);
}
