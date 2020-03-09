package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import com.google.common.collect.Lists;

import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.TestRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;

@ExtendWith(MockitoExtension.class)
class DefaultTestServiceTest {

    DefaultTestService defaultTestService;
    @Mock
    private TestRepository testRepository;
    @Mock
    private QuestionService questionService;
    @Mock
    private GradeService gradeService;

    private final static Integer TEST_ID = 1;
    private final static String TEST_NAME = "Test name";
    private net.atlassian.cmathtutor.adaptive.domain.entity.Test test;
    private List<net.atlassian.cmathtutor.adaptive.domain.entity.Test> allSavedTests;

    @BeforeEach
    void setUp() {
	defaultTestService = new DefaultTestService(testRepository, questionService, gradeService);
	test = net.atlassian.cmathtutor.adaptive.domain.entity.Test.builder()
		.id(TEST_ID)
		.name(TEST_NAME)
		.grades(Collections.emptyMap())
		.questions(Collections.emptySet())
		.build();
	allSavedTests = Lists.newArrayList(test, new net.atlassian.cmathtutor.adaptive.domain.entity.Test());
    }

    @Test
    void getAll_shouldReturn_AllSavedEntities() {
	when(testRepository.findAll()).thenReturn(allSavedTests);

	assertThat(defaultTestService.getAll(), is(equalTo(allSavedTests)));

	verify(testRepository, only()).findAll();
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_getById_shouldThrow_NotFoundException() {
	when(testRepository.findById(any())).thenReturn(Optional.empty());

	assertThrows(NotFoundException.class, () -> defaultTestService.getById(TEST_ID));

	verify(testRepository, only()).findById(TEST_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_getById_shouldReturn_savedEntity() {
	when(testRepository.findById(any())).thenReturn(Optional.of(test));

	assertThat(defaultTestService.getById(TEST_ID), is(equalTo(test)));

	verify(testRepository, only()).findById(TEST_ID);
    }

    @Test
    void when_EntityWithSpecifiedName_IsAbsent_then_getByName_shouldThrow_NotFoundException() {
	when(testRepository.findByName(any())).thenReturn(Optional.empty());

	assertThrows(NotFoundException.class, () -> defaultTestService.getByName(TEST_NAME));

	verify(testRepository, only()).findByName(TEST_NAME);
    }

    @Test
    void when_EntityWithSpecifiedName_IsPresent_then_getByName_shouldReturn_savedEntity() {
	when(testRepository.findByName(any())).thenReturn(Optional.of(test));

	assertThat(defaultTestService.getByName(TEST_NAME), is(equalTo(test)));

	verify(testRepository, only()).findByName(TEST_NAME);
    }

    @Test
    void create_shouldSaveTestAndCreateGradesAndQuestions() {
	when(testRepository.save(any())).thenAnswer(new Answer<net.atlassian.cmathtutor.adaptive.domain.entity.Test>() {

	    @Override
	    public net.atlassian.cmathtutor.adaptive.domain.entity.Test answer(InvocationOnMock invocation)
		    throws Throwable {
		net.atlassian.cmathtutor.adaptive.domain.entity.Test testArg = invocation.getArgument(0);
		return net.atlassian.cmathtutor.adaptive.domain.entity.Test.builder()
			.created(testArg.getCreated())
			.creatorName(testArg.getCreatorName())
			.grades(testArg.getGrades())
			.id(TEST_ID)
			.name(test.getName())
			.questions(testArg.getQuestions())
			.build();
	    }
	});

	Date startDate = Calendar.getInstance().getTime();
	net.atlassian.cmathtutor.adaptive.domain.entity.Test createdTest = defaultTestService.create(test);
	Date endDate = Calendar.getInstance().getTime();

	assertAll(() -> assertThat(createdTest.getId(), is(equalTo(null))),
		() -> assertThat(createdTest.getCreated(),
			is(both(greaterThanOrEqualTo(startDate)).and(lessThanOrEqualTo(endDate)))));

	verify(testRepository, atLeastOnce()).save(test);
	verify(gradeService, only()).create(test.getGrades().values(), TEST_ID);
	verify(questionService, only()).create(test.getQuestions(), TEST_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_updateNameById_shouldReturn_EntityWithUpdatedName() {
	when(testRepository.findById(any())).thenReturn(Optional.of(test));
	when(testRepository.save(any())).thenAnswer(AdditionalAnswers.returnsFirstArg());
	String newName = "The new test name!";

	assertThat(defaultTestService.updateNameById(newName, TEST_ID).getName(), is(equalTo(newName)));

	verify(testRepository).findById(TEST_ID);
	verify(testRepository).save(test);
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_updateNameById_shouldThrow_NotFoundException() {
	when(testRepository.findById(any())).thenReturn(Optional.empty());
	String newName = "The new test name!";

	assertThrows(NotFoundException.class, () -> defaultTestService.updateNameById(newName, TEST_ID));

	verify(testRepository, only()).findById(TEST_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_deleteById_should_deleteById() {
	when(testRepository.existsById(any())).thenReturn(true);
	doNothing().when(testRepository).deleteById(any());

	defaultTestService.deleteById(TEST_ID);

	verify(testRepository).existsById(TEST_ID);
	verify(testRepository).deleteById(TEST_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_deleteById_shouldThrow_NotFoundException() {
	when(testRepository.existsById(any())).thenReturn(false);

	assertThrows(NotFoundException.class, () -> defaultTestService.deleteById(TEST_ID));

	verify(testRepository, only()).existsById(TEST_ID);
    }

}
