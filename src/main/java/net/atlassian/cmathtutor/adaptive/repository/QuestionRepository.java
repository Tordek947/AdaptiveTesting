package net.atlassian.cmathtutor.adaptive.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import net.atlassian.cmathtutor.adaptive.domain.entity.Question;

public interface QuestionRepository extends CrudRepository<Question, Integer> {

    @Query(value = "select q from Question q left join fetch q.questionAnswers where q.id= :id")
    Optional<Question> findByIdFetchQuestionAnswer(@Param(value = "id") Integer id);
}
