package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.repository.QuestionAnswerRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeMarkChangeRuleService;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionAnswerServiceTest {

    DefaultQuestionAnswerService defaultQuestionAnswerService;
    @Mock
    private GradeMarkChangeRuleService gradeMarkChangeRuleService;
    @Mock
    private QuestionAnswerRepository questionAnswerRepository;

    private static final Integer QUESTION_ID = 1;

    @BeforeEach
    void setUp() {
	defaultQuestionAnswerService = new DefaultQuestionAnswerService(questionAnswerRepository,
		gradeMarkChangeRuleService);
    }

    @Test
    void create_shouldSaveQuestionAnswerAndCreateQuestionAnswerDefinitionRules() {
	Set<GradeMarkChangeRule> gradeMarkChangeRules = Sets
		.newLinkedHashSet(Lists.newArrayList(new GradeMarkChangeRule(),
			new GradeMarkChangeRule()));
	QuestionAnswer questionAnswer = buildQuestionAnswer(gradeMarkChangeRules);
	when(questionAnswerRepository.save(any())).thenReturn(questionAnswer);

	QuestionAnswer createdQuestionAnswer = defaultQuestionAnswerService.create(questionAnswer, QUESTION_ID);

	assertAll(() -> assertThat(createdQuestionAnswer.getId(), is(equalTo(null))),
		() -> assertThat(createdQuestionAnswer.getQuestionId(), is(equalTo(QUESTION_ID))));
	verify(questionAnswerRepository, only()).save(questionAnswer);
	verify(gradeMarkChangeRuleService).create(gradeMarkChangeRules, createdQuestionAnswer.getId());
    }

    private QuestionAnswer buildQuestionAnswer(Set<GradeMarkChangeRule> gradeMarkChangeRules) {
	return QuestionAnswer.builder()
		.gradeMarkChangeRules(gradeMarkChangeRules)
		.build();
    }

    @Test
    void create_List_shouldSaveAllQuestionAnswersAndCreateQuestionAnswerDefinitionRules() {
	Set<GradeMarkChangeRule> gradeMarkChangeRules = Sets
		.newLinkedHashSet(Lists.newArrayList(new GradeMarkChangeRule(),
			new GradeMarkChangeRule()));
	Set<QuestionAnswer> questionAnswers = Sets
		.newLinkedHashSet(Lists.newArrayList(buildQuestionAnswer(gradeMarkChangeRules),
			buildQuestionAnswer(gradeMarkChangeRules)));
	when(questionAnswerRepository.saveAll(any())).thenReturn(questionAnswers);

	Collection<QuestionAnswer> createdQuestionAnswers = defaultQuestionAnswerService.create(questionAnswers,
		QUESTION_ID);

	assertAll(createdQuestionAnswers.stream().map(qa -> new Executable() {
	    @Override
	    public void execute() throws Throwable {
		assertAll(
			() -> assertThat(qa.getId(), is(equalTo(null))),
			() -> assertThat(qa.getQuestionId(), is(equalTo(QUESTION_ID))));
	    }
	}).collect(Collectors.toList()));

	verify(questionAnswerRepository, only()).saveAll(questionAnswers);
	verify(gradeMarkChangeRuleService, atLeastOnce()).create(gradeMarkChangeRules,
		createdQuestionAnswers.iterator().next().getId());
    }
}
