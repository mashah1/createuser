package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.SpecStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpecStatus entity.
 */
public interface SpecStatusRepository extends JpaRepository<SpecStatus,Long> {

}
