package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.UserBusinessRole;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserBusinessRole entity.
 */
public interface UserBusinessRoleRepository extends JpaRepository<UserBusinessRole,Long> {

}
