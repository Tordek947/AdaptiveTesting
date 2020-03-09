package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;
import net.atlassian.cmathtutor.adaptive.repository.QuestionDefinitionRuleRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionDefinitionRuleService;

@AllArgsConstructor(onConstructor = @__(@Autowired))
@Component
public class DefaultQuestionDefinitionRuleService implements QuestionDefinitionRuleService {

    private QuestionDefinitionRuleRepository questionDefinitionRuleRepository;
    private GradeService gradeService;
    private EntityManager em;

    @Override
    public List<QuestionDefinitionRule> getAllByTestId(Integer testId) {
	List<QuestionDefinitionRule> questionDefinitionRules = questionDefinitionRuleRepository.findAllByTestId(testId);
	return questionDefinitionRules;
    }

    @Transactional
    @Override
    public QuestionDefinitionRule create(QuestionDefinitionRule questionDefinitionRule, Integer testId) {
	questionDefinitionRule.setId(null);
	replaceGradesInMinGradeMarkRequirementsWithExistingOnes(Stream.of(questionDefinitionRule), testId);
	refreshQuestions(Stream.of(questionDefinitionRule));
	return questionDefinitionRuleRepository.save(questionDefinitionRule);
    }

    private void replaceGradesInMinGradeMarkRequirementsWithExistingOnes(
	    Stream<QuestionDefinitionRule> questionDefinitionRules, Integer testId) {
	Map<String, Grade> gradesByCode = gradeService.getAllByTestId(testId).stream()
		.collect(Collectors.toMap(Grade::getCode, Function.identity()));
	questionDefinitionRules.flatMap(qdr -> qdr.getMinGradeMarkRequirements().stream())
		.forEach(minGradeMarkRequirement -> {
		    minGradeMarkRequirement.setGrade(gradesByCode.get(minGradeMarkRequirement.getGrade().getCode()));
		});
    }

    private void refreshQuestions(Stream<QuestionDefinitionRule> questionDefinitionRules) {
	questionDefinitionRules.forEach(questionDefinitionRule -> {
	    List<Question> refreshedQuestions = questionDefinitionRule.getQuestions().stream().map(em::merge)
		    .peek(em::refresh).collect(Collectors.toList());
	    questionDefinitionRule.setQuestions(refreshedQuestions);
	});
    }

    @Transactional
    @Override
    public List<QuestionDefinitionRule> create(List<QuestionDefinitionRule> questionDefinitionRules, Integer testId) {
	for (QuestionDefinitionRule questionDefinitionRule : questionDefinitionRules) {
	    questionDefinitionRule.setId(null);
	}
	replaceGradesInMinGradeMarkRequirementsWithExistingOnes(questionDefinitionRules.stream(), testId);
	refreshQuestions(questionDefinitionRules.stream());
	return questionDefinitionRuleRepository.saveAll(questionDefinitionRules);
    }

}
