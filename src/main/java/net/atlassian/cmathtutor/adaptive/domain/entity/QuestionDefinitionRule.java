package net.atlassian.cmathtutor.adaptive.domain.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "question_definition_rule")
@NoArgsConstructor
public class QuestionDefinitionRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "question_number_from", nullable = false)
    private Integer questionNumberFrom;

    @Column(name = "question_number_to")
    private Integer questionNumberTo;

    @ElementCollection
    @CollectionTable(name = "min_grade_mark_requirement",
	    joinColumns = @JoinColumn(name = "question_definition_rule_id", nullable = false))
    private Set<MinGradeMarkRequirement> minGradeMarkRequirements;

    @ManyToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH },
	    fetch = FetchType.EAGER)
    @JoinTable(name = "question_definition_rule_has_question",
	    joinColumns = @JoinColumn(name = "question_definition_rule_id"),
	    inverseJoinColumns = @JoinColumn(name = "question_id"))
    private Set<Question> questions;

    @Column(name = "test_id")
    private Integer testId;
}
