package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Supplier;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Supplier entity.
 */
public interface SupplierRepository extends JpaRepository<Supplier,Long> {

}
