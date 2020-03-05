package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.atlassian.cmathtutor.adaptive.domain.entity.Test;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.TestRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;
import net.atlassian.cmathtutor.adaptive.service.TestService;

@Service
public class DefaultTestService implements TestService {

    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionService questionService;
    @Autowired
    private GradeService gradeService;

    @Override
    public List<Test> getAll() {
	return testRepository.findAll();
    }

    @Override
    public Test getById(Integer id) {
	return testRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @Override
    public Test getByName(String name) {
	return testRepository.findByName(name).orElseThrow(NotFoundException::new);
    }

    @Override
    @Transactional
    public Test create(Test test) {
	test.setId(null);
	test.setCreated(Calendar.getInstance().getTime());
	Integer testId = testRepository.save(test).getId();
	gradeService.create(test.getGrades().values(), testId);
	questionService.create(test.getQuestions(), testId);
	return getById(testId);
    }

}
