package org.nsf.traqtion.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Specification.
 */
@Entity
@Table(name = "specification")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "specification")
public class Specification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "attribute")
    private String attribute;

    @Column(name = "attribute1")
    private String attribute1;

    @Column(name = "version")
    private String version;

    @ManyToOne
    @JoinColumn(name = "primary_spec_id")
    private PrimarySpec primarySpec;

    @OneToOne
    private SpecStatus specStatus;

    @OneToOne
    private SpecDocument document;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public String getAttribute1() {
        return attribute1;
    }

    public void setAttribute1(String attribute1) {
        this.attribute1 = attribute1;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public PrimarySpec getPrimarySpec() {
        return primarySpec;
    }

    public void setPrimarySpec(PrimarySpec primarySpec) {
        this.primarySpec = primarySpec;
    }

    public SpecStatus getSpecStatus() {
        return specStatus;
    }

    public void setSpecStatus(SpecStatus specStatus) {
        this.specStatus = specStatus;
    }

    public SpecDocument getDocument() {
        return document;
    }

    public void setDocument(SpecDocument specDocument) {
        this.document = specDocument;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Specification specification = (Specification) o;
        return Objects.equals(id, specification.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Specification{" +
            "id=" + id +
            ", attribute='" + attribute + "'" +
            ", attribute1='" + attribute1 + "'" +
            ", version='" + version + "'" +
            '}';
    }
}
