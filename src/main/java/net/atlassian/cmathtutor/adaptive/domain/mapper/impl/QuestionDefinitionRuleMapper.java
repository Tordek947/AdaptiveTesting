package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionDefinitionRuleData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionDefinitionRule;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionDefinitionRuleMapper
	implements Mapper<CreateQuestionDefinitionRuleData, QuestionDefinitionRuleData, QuestionDefinitionRule> {

    private MinGradeMarkRequirementMapper minGradeMarkRequirementMapper;
    private QuestionMapper questionMapper;

    @Override
    public QuestionDefinitionRule dataToEntity(CreateQuestionDefinitionRuleData data) {
	return QuestionDefinitionRule.builder()
		.minGradeMarkRequirements(Mapper.collectionToSet(data.getMinGradeMarkRequirements().entrySet(),
			minGradeMarkRequirementMapper::dataToEntity))
		.questionNumberFrom(data.getQuestionNumberFrom())
		.questionNumberTo(data.getQuestionNumberTo())
		.questions(Mapper.collectionToSet(data.getQuestionIds(), id -> Question.builder().id(id).build()))
		.testId(data.getTestId())
		.build();
    }

    @Override
    public QuestionDefinitionRuleData entityToData(QuestionDefinitionRule entity) {
	return QuestionDefinitionRuleData.builder()
		.id(entity.getId())
		.minGradeMarkRequirements(Mapper.collectionToMap(entity.getMinGradeMarkRequirements(),
			minGradeMarkRequirementMapper::entityToData))
		.questionNumberFrom(entity.getQuestionNumberFrom())
		.questionNumberTo(entity.getQuestionNumberTo())
		.questions(Mapper.collectionToList(entity.getQuestions(), questionMapper::entityToData))
		.build();
    }

}
