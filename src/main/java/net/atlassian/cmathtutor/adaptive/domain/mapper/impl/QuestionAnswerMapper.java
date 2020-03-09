package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionAnswerData;
import net.atlassian.cmathtutor.adaptive.domain.data.MarkChangeRuleData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionAnswerData;
import net.atlassian.cmathtutor.adaptive.domain.entity.GradeMarkChangeRule;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionAnswerMapper implements Mapper<CreateQuestionAnswerData, QuestionAnswerData, QuestionAnswer> {

    private GradeMarkChangeRuleMapper gradeMarkChangeRuleMapper;

    @Override
    public QuestionAnswer dataToEntity(CreateQuestionAnswerData data) {
	List<GradeMarkChangeRule> gradeMarkChangeRules = Mapper
		.collectionToList(data.getGradeMarkChangeRules().entrySet(), gradeMarkChangeRuleMapper::dataToEntity);
	return QuestionAnswer.builder()
		.gradeMarkChangeRules(gradeMarkChangeRules)
		.isCorrect(data.getIsCorrect())
		.sentence(data.getSentence())
		.build();
    }

    @Override
    public QuestionAnswerData entityToData(QuestionAnswer entity) {
	Map<String, MarkChangeRuleData> gradeMarkChangeRules = Mapper
		.collectionToMap(entity.getGradeMarkChangeRules(), gradeMarkChangeRuleMapper::entityToData);
	return QuestionAnswerData.builder().gradeMarkChangeRules(gradeMarkChangeRules).id(entity.getId())
		.isCorrect(entity.getIsCorrect()).sentence(entity.getSentence()).build();
    }

}
