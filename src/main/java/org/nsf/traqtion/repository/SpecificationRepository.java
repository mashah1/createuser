package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Specification;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Specification entity.
 */
public interface SpecificationRepository extends JpaRepository<Specification,Long> {

}
