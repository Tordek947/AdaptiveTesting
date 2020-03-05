package net.atlassian.cmathtutor.adaptive.service;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;

public interface QuestionDefinitionRuleService {

    List<QuestionDefinitionRule> getAllByTestId(Integer testId);

    QuestionDefinitionRule create(QuestionDefinitionRule questionDefinitionRule, Integer testId);

    List<QuestionDefinitionRule> create(List<QuestionDefinitionRule> questionDefinitionRules, Integer testId);
}
