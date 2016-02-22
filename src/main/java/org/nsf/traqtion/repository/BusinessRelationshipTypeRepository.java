package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.BusinessRelationshipType;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the BusinessRelationshipType entity.
 */
public interface BusinessRelationshipTypeRepository extends JpaRepository<BusinessRelationshipType,Long> {

}
