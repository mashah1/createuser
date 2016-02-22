package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.SupplierSite;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SupplierSite entity.
 */
public interface SupplierSiteRepository extends JpaRepository<SupplierSite,Long> {

}
