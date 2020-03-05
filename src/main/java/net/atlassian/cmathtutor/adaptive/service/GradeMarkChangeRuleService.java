package net.atlassian.cmathtutor.adaptive.service;

import java.util.List;

import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;

public interface GradeMarkChangeRuleService {

    GradeMarkChangeRule getById(Long id);

    GradeMarkChangeRule create(GradeMarkChangeRule gradeMarkChangeRule, Long questionAnswerId);

    List<GradeMarkChangeRule> create(Iterable<GradeMarkChangeRule> gradeMarkChangeRules, Long questionAnswerId);
}
