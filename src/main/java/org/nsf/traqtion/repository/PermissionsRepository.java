package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.Permissions;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Permissions entity.
 */
public interface PermissionsRepository extends JpaRepository<Permissions,Long> {

}
