package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atlassian.cmathtutor.adaptive.domain.data.CreateTestData;
import net.atlassian.cmathtutor.adaptive.domain.data.TestData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Test;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
public class TestMapper implements Mapper<CreateTestData, TestData, Test> {

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private GradeMapper gradeMapper;

    @Override
    public Test dataToEntity(CreateTestData data) {
	return Test.builder()
		.creatorName(data.getCreatorName())
		.grades(Mapper.collectionToMap(data.getGrades().entrySet(), gradeMapper::dataToEntity))
		.name(data.getName())
		.questions(Mapper.collectionToSet(data.getQuestions(), questionMapper::dataToEntity))
		.build();
    }

    @Override
    public TestData entityToData(Test entity) {
	return TestData.builder()
		.created(entity.getCreated())
		.creatorName(entity.getCreatorName())
		.grades(Mapper.collectionToList(entity.getGrades().entrySet(), gradeMapper::entityToData))
		.id(entity.getId())
		.name(entity.getName())
		.questions(Mapper.collectionToList(entity.getQuestions(), questionMapper::entityToData))
		.build();
    }

}
