package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.BusinessRelationship;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BusinessRelationship entity.
 */
public interface BusinessRelationshipRepository extends JpaRepository<BusinessRelationship,Long> {

}
