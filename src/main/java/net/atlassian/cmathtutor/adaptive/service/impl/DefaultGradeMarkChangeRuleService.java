package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.GradeMarkChangeRuleRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeMarkChangeRuleService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultGradeMarkChangeRuleService implements GradeMarkChangeRuleService {

    private GradeMarkChangeRuleRepository gradeMarkChangeRuleRepository;

    @Override
    public GradeMarkChangeRule getById(Long id) {
	return gradeMarkChangeRuleRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public GradeMarkChangeRule create(GradeMarkChangeRule gradeMarkChangeRule, Long questionAnswerId) {
	prepareForCreation(gradeMarkChangeRule, questionAnswerId);
	return gradeMarkChangeRuleRepository.save(gradeMarkChangeRule);
    }

    private void prepareForCreation(GradeMarkChangeRule gradeMarkChangeRule, Long questionAnswerId) {
	gradeMarkChangeRule.setId(null);
	gradeMarkChangeRule.setQuestionAnswerId(questionAnswerId);
    }

    @Override
    public List<GradeMarkChangeRule> create(List<GradeMarkChangeRule> gradeMarkChangeRules, Long questionAnswerId) {
	for (GradeMarkChangeRule gradeMarkChangeRule : gradeMarkChangeRules) {
	    prepareForCreation(gradeMarkChangeRule, questionAnswerId);
	}
	return gradeMarkChangeRuleRepository.saveAll(gradeMarkChangeRules);
    }

}
