package net.atlassian.cmathtutor.adaptive.service.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.google.common.collect.Lists;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.GradeRepository;

@ExtendWith(MockitoExtension.class)
class DefaultGradeServiceTest {

    DefaultGradeService defaultGradeService;
    @Mock
    private GradeRepository gradeRepository;

    private static final Integer GRADE_ID = 1;
    private static final Integer TEST_ID = 2;

    private Grade grade;
    private Collection<Grade> grades;

    @BeforeEach
    void setUp() {
	grade = Grade.builder()
		.code("GradeCode")
		.id(GRADE_ID)
		.testId(TEST_ID)
		.build();
	grades = Lists.newArrayList(grade, new Grade());
	defaultGradeService = new DefaultGradeService(gradeRepository);
    }

    @Test
    void when_EntityWithSpecifiedId_IsAbsent_then_getById_shouldThrow_NotFoundException() {
	when(gradeRepository.findById(any())).thenReturn(Optional.empty());

	assertThrows(NotFoundException.class, () -> defaultGradeService.getById(GRADE_ID));

	verify(gradeRepository, only()).findById(GRADE_ID);
    }

    @Test
    void when_EntityWithSpecifiedId_IsPresent_then_getById_shouldReturn_savedEntity() {
	when(gradeRepository.findById(any())).thenReturn(Optional.of(grade));

	assertThat(defaultGradeService.getById(GRADE_ID), is(equalTo(grade)));

	verify(gradeRepository, only()).findById(GRADE_ID);
    }

    @Test
    void create_shouldSaveNewEntity() {
	when(gradeRepository.save(any())).thenReturn(grade);

	assertThat(defaultGradeService.create(grade, TEST_ID), is(equalTo(grade)));

	verify(gradeRepository, only()).save(grade);
    }

    @Test
    void create_shouldSaveNewEntities() {
	ArrayList<Grade> gradeList = Lists.newArrayList(grades);
	when(gradeRepository.saveAll(any())).thenReturn(gradeList);

	assertThat(defaultGradeService.create(grades, TEST_ID), is(equalTo(gradeList)));

	verify(gradeRepository, only()).saveAll(grades);
    }

    @Test
    void getAllByTestId_shouldReturn_allSavedGradesWithSpecifiedTestId() {
	ArrayList<Grade> gradeList = Lists.newArrayList(grades);
	when(gradeRepository.findAllByTestId(any())).thenReturn(gradeList);

	assertThat(defaultGradeService.getAllByTestId(TEST_ID), is(equalTo(gradeList)));

	verify(gradeRepository, only()).findAllByTestId(TEST_ID);
    }

}
