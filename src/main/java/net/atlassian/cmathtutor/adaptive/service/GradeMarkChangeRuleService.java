package net.atlassian.cmathtutor.adaptive.service;

import java.util.Collection;

import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;

public interface GradeMarkChangeRuleService {

    GradeMarkChangeRule getById(Long id);

    GradeMarkChangeRule create(GradeMarkChangeRule gradeMarkChangeRule, Long questionAnswerId);

    Collection<GradeMarkChangeRule> create(Collection<GradeMarkChangeRule> gradeMarkChangeRules, Long questionAnswerId);
}
