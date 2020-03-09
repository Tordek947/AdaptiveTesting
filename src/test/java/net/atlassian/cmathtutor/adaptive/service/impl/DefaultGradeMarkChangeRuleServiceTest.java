package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;

import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.GradeMarkChangeRuleRepository;

@ExtendWith(MockitoExtension.class)
class DefaultGradeMarkChangeRuleServiceTest {

    private static final Long GRADE_MARK_CHANGE_RULE_ID = 1L;
    private static final Long QUESTION_ANSWER_ID = 2L;

    DefaultGradeMarkChangeRuleService defaultGradeMarkChangeRuleService;

    @Mock
    private GradeMarkChangeRuleRepository gradeMarkChangeRuleRepository;

    @BeforeEach
    void setUp() {
	defaultGradeMarkChangeRuleService = new DefaultGradeMarkChangeRuleService(gradeMarkChangeRuleRepository);
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_getById_shouldThrow_NotFoundException() {
	when(gradeMarkChangeRuleRepository.findById(any())).thenReturn(Optional.empty());

	assertThrows(NotFoundException.class,
		() -> defaultGradeMarkChangeRuleService.getById(GRADE_MARK_CHANGE_RULE_ID));

	verify(gradeMarkChangeRuleRepository, only()).findById(GRADE_MARK_CHANGE_RULE_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_getById_shouldReturn_savedEntity() {
	GradeMarkChangeRule gradeMarkChangeRule = new GradeMarkChangeRule();
	when(gradeMarkChangeRuleRepository.findById(any())).thenReturn(Optional.of(gradeMarkChangeRule));

	assertThat(defaultGradeMarkChangeRuleService.getById(GRADE_MARK_CHANGE_RULE_ID),
		is(equalTo(gradeMarkChangeRule)));

	verify(gradeMarkChangeRuleRepository, only()).findById(GRADE_MARK_CHANGE_RULE_ID);
    }

    @Test
    void create_shouldSaveNewEntity() {
	GradeMarkChangeRule gradeMarkChangeRule = buildGradeMarkChangeRule();
	when(gradeMarkChangeRuleRepository.save(any())).thenReturn(gradeMarkChangeRule);

	GradeMarkChangeRule createdGradeMarkChangeRule = defaultGradeMarkChangeRuleService.create(gradeMarkChangeRule,
		QUESTION_ANSWER_ID);

	assertAll(() -> assertThat(createdGradeMarkChangeRule.getId(), is(equalTo(null))),
		() -> assertThat(createdGradeMarkChangeRule.getQuestionAnswerId(), is(equalTo(QUESTION_ANSWER_ID))));
	verify(gradeMarkChangeRuleRepository, only()).save(gradeMarkChangeRule);
    }

    private GradeMarkChangeRule buildGradeMarkChangeRule() {
	return GradeMarkChangeRule.builder()
		.id(GRADE_MARK_CHANGE_RULE_ID)
		.build();
    }

    @Test
    void create_shouldSaveNewEntities() {
	List<GradeMarkChangeRule> gradeMarkChangeRules = Lists.newArrayList(buildGradeMarkChangeRule(),
		buildGradeMarkChangeRule());
	when(gradeMarkChangeRuleRepository.saveAll(any())).thenReturn(gradeMarkChangeRules);

	List<GradeMarkChangeRule> createdGradeMarkChangeRules = defaultGradeMarkChangeRuleService.create(
		gradeMarkChangeRules,
		QUESTION_ANSWER_ID);

	assertAll(createdGradeMarkChangeRules.stream().map(gmcr -> new Executable() {

	    @Override
	    public void execute() throws Throwable {
		assertAll(() -> assertThat(gmcr.getId(), is(equalTo(null))),
			() -> assertThat(gmcr.getQuestionAnswerId(), is(equalTo(QUESTION_ANSWER_ID))));
	    }
	}).collect(Collectors.toList()));
	verify(gradeMarkChangeRuleRepository).saveAll(gradeMarkChangeRules);
    }

}
