package net.atlassian.cmathtutor.adaptive.service.impl;

import java.util.Calendar;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.entity.Test;
import net.atlassian.cmathtutor.adaptive.exception.NotFoundException;
import net.atlassian.cmathtutor.adaptive.repository.TestRepository;
import net.atlassian.cmathtutor.adaptive.service.GradeService;
import net.atlassian.cmathtutor.adaptive.service.QuestionService;
import net.atlassian.cmathtutor.adaptive.service.TestService;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DefaultTestService implements TestService {

    private TestRepository testRepository;
    private QuestionService questionService;
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
	return test;
    }

}
