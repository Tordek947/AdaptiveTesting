package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.atlassian.cmathtutor.adaptive.domain.entity.Grade;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.GradeRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;

@Service
public class DefaultGradeService implements GradeService {

    @Autowired
    private GradeRepository gradeRepository;

    @Override
    public Grade getById(Integer id) {
	return gradeRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Grade create(Grade grade, Integer testId) {
	prepareForCreation(grade, testId);
	return gradeRepository.save(grade);
    }

    private void prepareForCreation(Grade grade, Integer testId) {
	grade.setTestId(testId);
	grade.setId(null);
    }

    @Override
    public List<Grade> create(Iterable<Grade> grades, Integer testId) {
	for (Grade grade : grades) {
	    prepareForCreation(grade, testId);
	}
	return gradeRepository.saveAll(grades);
    }

    @Override
    public List<Grade> getAllByTestId(Integer testId) {
	return gradeRepository.findAllByTestId(testId);
    }

}
