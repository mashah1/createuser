package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Manufacturer;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Manufacturer entity.
 */
public interface ManufacturerRepository extends JpaRepository<Manufacturer,Long> {

}
