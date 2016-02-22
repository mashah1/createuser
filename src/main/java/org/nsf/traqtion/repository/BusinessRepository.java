package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Business;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Business entity.
 */
public interface BusinessRepository extends JpaRepository<Business,Long> {

}
