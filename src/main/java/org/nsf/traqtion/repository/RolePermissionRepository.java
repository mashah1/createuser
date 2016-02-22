package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.RolePermission;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RolePermission entity.
 */
public interface RolePermissionRepository extends JpaRepository<RolePermission,Long> {

}
