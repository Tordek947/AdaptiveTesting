package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.MinGradeMarkRequirement;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;
import net.atlassian.cmathtutor.adaptive.repository.QuestionDefinitionRuleRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionDefinitionRuleServiceTest {

    private static final Integer TEST_ID = 1;
    private static final Long QUESTION_DEFINITION_RULE_ID = 11L;
    private static final Integer GRADE_ID_1 = null;
    private static final Integer GRADE_ID_2 = null;
    private static final String GRADE_CODE_2 = "Grade_code_2";
    private static final String GRADE_CODE_1 = "Grade_Code_1";

    DefaultQuestionDefinitionRuleService defaultQuestionDefinitionRuleService;
    @Mock
    private QuestionDefinitionRuleRepository questionDefinitionRuleRepository;
    @Mock
    private GradeService gradeService;
    @Mock
    private EntityManager em;

    @BeforeEach
    void setUp() {
	defaultQuestionDefinitionRuleService = new DefaultQuestionDefinitionRuleService(
		questionDefinitionRuleRepository, gradeService, em);
    }

    @Test
    void getAllByTestId_should_findAllByTestId() {
	List<QuestionDefinitionRule> questionDefinitionRules = Lists.newArrayList(new QuestionDefinitionRule(),
		new QuestionDefinitionRule(), new QuestionDefinitionRule());
	when(questionDefinitionRuleRepository.findAllByTestId(any())).thenReturn(questionDefinitionRules);

	List<QuestionDefinitionRule> actual = defaultQuestionDefinitionRuleService.getAllByTestId(TEST_ID);

	assertThat(actual, is(equalTo(questionDefinitionRules)));
	verify(questionDefinitionRuleRepository, only()).findAllByTestId(TEST_ID);
    }

    @Test
    void create_shouldSaveNewEntity() {
	List<Grade> grades = buildGrades();
	List<Grade> savedGrades = Lists.newArrayList(Grade.builder().id(GRADE_ID_1).code(GRADE_CODE_1).build(),
		Grade.builder().id(GRADE_ID_2).code(GRADE_CODE_2).build());
	QuestionDefinitionRule questionDefinitionRule = buildQuestionDefinitionRule(grades);
	when(gradeService.getAllByTestId(any())).thenReturn(savedGrades);
	when(questionDefinitionRuleRepository.save(any())).thenReturn(questionDefinitionRule);

	QuestionDefinitionRule createdQuestionDefinitionRule = defaultQuestionDefinitionRuleService
		.create(questionDefinitionRule, TEST_ID);
	Collection<MinGradeMarkRequirement> minGradeMarkRequirements = createdQuestionDefinitionRule
		.getMinGradeMarkRequirements();
	Iterator<MinGradeMarkRequirement> minGradeMarkRequirementsIt = minGradeMarkRequirements.iterator();

	assertAll(() -> assertThat(createdQuestionDefinitionRule, is(sameInstance(questionDefinitionRule))),
		() -> assertThat(minGradeMarkRequirementsIt.next().getGrade(), is(equalTo(savedGrades.get(0)))),
		() -> assertThat(minGradeMarkRequirementsIt.next().getGrade(), is(equalTo(savedGrades.get(1)))));
	verify(questionDefinitionRuleRepository).save(questionDefinitionRule);
	verify(gradeService, only()).getAllByTestId(TEST_ID);
    }

    private ArrayList<Grade> buildGrades() {
	return Lists.newArrayList(Grade.builder().code(GRADE_CODE_1).build(),
		Grade.builder().code(GRADE_CODE_2).build());
    }

    private QuestionDefinitionRule buildQuestionDefinitionRule(List<Grade> grades) {
	return QuestionDefinitionRule.builder()
		.id(QUESTION_DEFINITION_RULE_ID)
		.minGradeMarkRequirements(grades.stream().map(grade -> MinGradeMarkRequirement.builder()
			.grade(grade)
			.build()).collect(Collectors.toCollection(Sets::newLinkedHashSet)))
		.questions(Sets.newLinkedHashSet(Lists.newArrayList(new Question(), new Question())))
		.build();
    }

    @Test
    void create_shouldSaveNewEntities() {
	List<Grade> grades = buildGrades();
	List<Grade> savedGrades = Lists.newArrayList(Grade.builder().id(GRADE_ID_1).code(GRADE_CODE_1).build(),
		Grade.builder().id(GRADE_ID_2).code(GRADE_CODE_2).build());
	ArrayList<QuestionDefinitionRule> questionDefinitionRules = Lists
		.newArrayList(buildQuestionDefinitionRule(grades), buildQuestionDefinitionRule(grades));
	when(gradeService.getAllByTestId(any())).thenReturn(savedGrades);
	when(questionDefinitionRuleRepository.saveAll(any())).thenReturn(questionDefinitionRules);

	Collection<QuestionDefinitionRule> createdQuestionDefinitionRules = defaultQuestionDefinitionRuleService
		.create(questionDefinitionRules, TEST_ID);
	List<Grade> usedGrades = createdQuestionDefinitionRules.stream()
		.flatMap(qdr -> qdr.getMinGradeMarkRequirements().stream())
		.map(MinGradeMarkRequirement::getGrade)
		.collect(Collectors.toList());

	assertAll(() -> assertThat(createdQuestionDefinitionRules, is(sameInstance(questionDefinitionRules))),
		() -> assertThat(usedGrades, hasItems(savedGrades.toArray(new Grade[0]))));
	verify(questionDefinitionRuleRepository, times(1)).saveAll(questionDefinitionRules);
	verify(gradeService, only()).getAllByTestId(TEST_ID);
    }

}
