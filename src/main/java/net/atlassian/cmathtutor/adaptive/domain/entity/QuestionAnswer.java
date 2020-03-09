package net.atlassian.cmathtutor.adaptive.domain.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKey;
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
@Table(name = "question_answer")
@NoArgsConstructor
public class QuestionAnswer {

    @CsvBindByName
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @CsvBindByName
    @Column(name = "sentence", nullable = false)
    private String sentence;

    @CsvBindByName
    @Column(name = "is_correct", nullable = false)
    private Boolean isCorrect;

    @CsvBindByName
    @Column(name = "question_id", nullable = false, insertable = false, updatable = false)
    private Integer questionId;

    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE }, fetch = FetchType.EAGER)
    @JoinColumn(name = "question_answer_id", nullable = false)
    @MapKey(name = "grade")
    private Set<GradeMarkChangeRule> gradeMarkChangeRules;
}
