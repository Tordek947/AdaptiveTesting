package net.atlassian.cmathtutor.adaptive.domain.entity;

import java.util.Date;
import java.util.Map;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Entity
@Table(name = "test")
@NoArgsConstructor
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Temporal(TemporalType.DATE)
    @Column(name = "created", nullable = false)
    private Date created;

    @Column(name = "creator_name")
    private String creatorName;

    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH },
	    fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    private Set<Question> questions;

    @OneToMany(cascade = { CascadeType.DETACH, CascadeType.MERGE, CascadeType.REFRESH },
	    fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumn(name = "test_id")
    @MapKey(name = "code")
    private Map<String, Grade> grades;
}
