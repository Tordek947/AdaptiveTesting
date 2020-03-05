package net.atlassian.cmathtutor.adaptive.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
@Table(name = "grade")
@NoArgsConstructor
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @CsvBindByName
    @Column(name = "code", nullable = false)
    private String code;

    @CsvBindByName
    @Column(name = "max_mark", nullable = false)
    private Integer maxMark;

    @CsvBindByName
    @Column(name = "initial_mark", nullable = false)
    private Integer initialMark;

    @Column(name = "test_id", nullable = false)
    private Integer testId;

}
