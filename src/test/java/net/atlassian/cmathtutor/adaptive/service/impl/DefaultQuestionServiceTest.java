package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.QuestionRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionAnswerService;

@ExtendWith(MockitoExtension.class)
class DefaultQuestionServiceTest {

    DefaultQuestionService defaultQuestionService;
    @Mock
    private GradeService gradeService;
    @Mock
    private QuestionAnswerService questionAnswerService;
    @Mock
    private QuestionRepository questionRepository;

    private static final Integer QUESTION_ID = 1;
    private static final Integer TEST_ID = 2;
    private static final String GRADE_CODE_1 = "GradeCode1";
    private static final String GRADE_CODE_2 = "GradeCode2";
    private static final Integer GRADE_ID_1 = 1;
    private static final Integer GRADE_ID_2 = 2;

    @BeforeEach
    void setUp() {
	defaultQuestionService = new DefaultQuestionService(questionRepository, questionAnswerService, gradeService);
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_getById_shouldThrow_NotFoundException() {
	when(questionRepository.findByIdFetchQuestionAnswer(any())).thenReturn(Optional.empty());

	assertThrows(NotFoundException.class, () -> defaultQuestionService.getById(QUESTION_ID));

	verify(questionRepository, only()).findByIdFetchQuestionAnswer(QUESTION_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_getById_shouldReturn_savedEntity() {
	Question question = new Question();
	when(questionRepository.findByIdFetchQuestionAnswer(any())).thenReturn(Optional.of(question));

	assertThat(defaultQuestionService.getById(QUESTION_ID), is(equalTo(question)));

	verify(questionRepository, only()).findByIdFetchQuestionAnswer(QUESTION_ID);
    }

    @Test
    void create_shouldSaveNewEntity() {
	Question question = buildQuestion();
	List<Grade> savedGrades = Lists.newArrayList(Grade.builder().id(GRADE_ID_1).code(GRADE_CODE_1).build(),
		Grade.builder().id(GRADE_ID_2).code(GRADE_CODE_2).build());
	when(gradeService.getAllByTestId(any())).thenReturn(savedGrades);
	when(questionRepository.save(any())).thenReturn(question);

	Question createdQuestion = defaultQuestionService.create(question, TEST_ID);
	Iterator<GradeMarkChangeRule> gradeMarkChangeRulesIt = createdQuestion.getQuestionAnswers().iterator().next()
		.getGradeMarkChangeRules().iterator();

	assertAll(() -> assertThat(createdQuestion, is(sameInstance(question))),
		() -> assertThat(gradeMarkChangeRulesIt.next().getGrade(), is(equalTo(savedGrades.get(0)))),
		() -> assertThat(gradeMarkChangeRulesIt.next().getGrade(), is(equalTo(savedGrades.get(1)))));
	verify(questionRepository).save(question);
	verify(questionAnswerService, atLeastOnce()).create(question.getQuestionAnswers(), createdQuestion.getId());
	verify(gradeService, only()).getAllByTestId(TEST_ID);
    }

    private Question buildQuestion() {
	return Question.builder()
		.id(QUESTION_ID)
		.questionAnswers(Sets.newLinkedHashSet(Lists.newArrayList(
			QuestionAnswer.builder()
				.questionId(QUESTION_ID)
				.gradeMarkChangeRules(Sets.newLinkedHashSet(Lists.newArrayList(
					GradeMarkChangeRule.builder()
						.grade(Grade.builder().code(GRADE_CODE_1).build())
						.build(),
					GradeMarkChangeRule.builder()
						.grade(Grade.builder().code(GRADE_CODE_2).build())
						.build())))
				.build())))
		.testId(TEST_ID)
		.build();
    }

    @Test
    void create_shouldSaveNewEntities() {
	List<Question> questions = Lists.newArrayList(buildQuestion(), buildQuestion());
	List<Grade> savedGrades = Lists.newArrayList(Grade.builder().id(GRADE_ID_1).code(GRADE_CODE_1).build(),
		Grade.builder().id(GRADE_ID_2).code(GRADE_CODE_2).build());
	when(gradeService.getAllByTestId(any())).thenReturn(savedGrades);
	when(questionRepository.saveAll(any())).thenReturn(questions);

	Collection<Question> createdQuestions = defaultQuestionService.create(questions, TEST_ID);
	List<Grade> usedGrades = createdQuestions.stream()
		.flatMap(q -> q.getQuestionAnswers().iterator().next().getGradeMarkChangeRules().stream())
		.map(GradeMarkChangeRule::getGrade)
		.collect(Collectors.toList());

	assertAll(() -> assertThat(createdQuestions, is(sameInstance(questions))),
		() -> assertThat(usedGrades, hasItems(savedGrades.toArray(new Grade[0]))));
	verify(questionRepository, atLeastOnce()).saveAll(questions);
	verify(questionAnswerService, atLeastOnce()).create(questions.get(0).getQuestionAnswers(),
		createdQuestions.iterator().next().getId());
	verify(gradeService, only()).getAllByTestId(TEST_ID);
    }

}
