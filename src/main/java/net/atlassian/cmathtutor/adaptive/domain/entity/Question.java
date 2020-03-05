package net.atlassian.cmathtutor.adaptive.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.opencsv.bean.CsvBindByName;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "question")
@NoArgsConstructor
public class Question {

    @CsvBindByName
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @CsvBindByName
    @Column(name = "sentence", nullable = false)
    private String sentence;

    // TODO: Changed to EAGER, because EntityManager PersistenceUnitUtil cannot
    // define whether entity is loaded
    // because of both session and transaction are closed (at least when calling
    // from JavaFX application part,
    // not from RestController.
    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_id", nullable = false)
    private List<QuestionAnswer> questionAnswers;

    @Column(name = "test_id")
    private Integer testId;
}
