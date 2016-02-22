package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.ProductStatus;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ProductStatus entity.
 */
public interface ProductStatusRepository extends JpaRepository<ProductStatus,Long> {

}
