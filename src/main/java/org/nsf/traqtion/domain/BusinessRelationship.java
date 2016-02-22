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
 * A BusinessRelationship.
 */
@Entity
@Table(name = "business_relationship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "businessrelationship")
public class BusinessRelationship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    private Business relatedto;

    @OneToOne
    private BusinessRelationshipType businessRelationshipType;

    @OneToOne
    private Business related;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Business getRelatedto() {
        return relatedto;
    }

    public void setRelatedto(Business business) {
        this.relatedto = business;
    }

    public BusinessRelationshipType getBusinessRelationshipType() {
        return businessRelationshipType;
    }

    public void setBusinessRelationshipType(BusinessRelationshipType businessRelationshipType) {
        this.businessRelationshipType = businessRelationshipType;
    }

    public Business getRelated() {
        return related;
    }

    public void setRelated(Business business) {
        this.related = business;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BusinessRelationship businessRelationship = (BusinessRelationship) o;
        return Objects.equals(id, businessRelationship.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "BusinessRelationship{" +
            "id=" + id +
            '}';
    }
}
