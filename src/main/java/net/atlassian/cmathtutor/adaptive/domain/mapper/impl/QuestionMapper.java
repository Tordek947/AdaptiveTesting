package net.atlassian.cmathtutor.adaptive.domain.mapper.impl;

import java.util.Collection;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.atlassian.cmathtutor.adaptive.domain.data.CreateQuestionData;
import net.atlassian.cmathtutor.adaptive.domain.data.QuestionData;
import net.atlassian.cmathtutor.adaptive.domain.entity.Question;
import net.atlassian.cmathtutor.adaptive.domain.entity.QuestionAnswer;
import net.atlassian.cmathtutor.adaptive.domain.mapper.Mapper;

@Component
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class QuestionMapper implements Mapper<CreateQuestionData, QuestionData, Question> {

//    private EntityManager em;
    private QuestionAnswerMapper questionAnswerMapper;

    @Override
    public Question dataToEntity(CreateQuestionData data) {
	Set<QuestionAnswer> questionAnwsers = Mapper.collectionToSet(data.getQuestionAnwsers(),
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
	// Not working either
//	if (Hibernate.isPropertyInitialized(entity, "questionAnswers")) {
//	    System.out.println("Hibernate said, that question answers are INITIALIZED");
//	}
//	if (em.getEntityManagerFactory().getPersistenceUnitUtil().isLoaded(entity, "questionAnwsers")) {
//	    System.out.println("EM said, that question answers are LOADED");
	try {
	    Collection<QuestionAnswer> questionAnswers = entity.getQuestionAnswers();
	    questionData.setQuestionAnwsers(Mapper.collectionToList(questionAnswers,
		    questionAnswerMapper::entityToData));
	} catch (RuntimeException e) {
	    log.debug("Unable to load question answers, going to skip them...");
	}
//	}
	return questionData;
    }

}
