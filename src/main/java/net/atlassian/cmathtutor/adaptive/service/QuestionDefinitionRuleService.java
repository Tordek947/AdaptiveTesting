package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;

public interface QuestionDefinitionRuleService {

    Collection<QuestionDefinitionRule> getAllByTestId(Integer testId);

    QuestionDefinitionRule create(QuestionDefinitionRule questionDefinitionRule, Integer testId);

    Collection<QuestionDefinitionRule> create(Collection<QuestionDefinitionRule> questionDefinitionRules,
	    Integer testId);
}
