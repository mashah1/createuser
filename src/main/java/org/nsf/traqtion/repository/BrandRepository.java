package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Brand;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Brand entity.
 */
public interface BrandRepository extends JpaRepository<Brand,Long> {

}
