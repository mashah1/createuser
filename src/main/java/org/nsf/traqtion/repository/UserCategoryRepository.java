package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.UserCategory;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the UserCategory entity.
 */
public interface UserCategoryRepository extends JpaRepository<UserCategory,Long> {

}
