package net.atlassian.cmathtutor.adaptive.repository;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;

public interface QuestionDefinitionRuleRepository extends CrudRepository<QuestionDefinitionRule, Long> {

    List<QuestionDefinitionRule> findAllByTestId(Integer testId);

}
