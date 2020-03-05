package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
public class QuestionMapper implements Mapper<CreateQuestionData, QuestionData, Question> {

//    @Autowired
//    private EntityManager em;

    @Autowired
    private QuestionAnswerMapper questionAnswerMapper;

    @Override
    public Question dataToEntity(CreateQuestionData data) {
	List<QuestionAnswer> questionAnwsers = Mapper.collectionToList(data.getQuestionAnwsers(),
		questionAnswerMapper::dataToEntity);
	return Question.builder()
		.questionAnswers(questionAnwsers)
		.sentence(data.getSentence())
		.build();
    }

    @Override
    public QuestionData entityToData(Question entity) {
	QuestionData questionData = QuestionData.builder()
		.id(entity.getId())
		.sentence(entity.getSentence())
		.build();
//	if (em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity, "questionAnwsers")) {
//	    System.out.println("EM said, that question answers are LOADED");
	questionData.setQuestionAnwsers(Mapper.collectionToList(entity.getQuestionAnswers(),
		questionAnswerMapper::entityToData));
//	}
	return questionData;
    }

}
