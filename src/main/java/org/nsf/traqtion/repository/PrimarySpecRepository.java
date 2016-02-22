package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.PrimarySpec;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrimarySpec entity.
 */
public interface PrimarySpecRepository extends JpaRepository<PrimarySpec,Long> {

}
