package org.nsf.traqtion.repository;

import org.nsf.traqtion.domain.SpecDocument;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the SpecDocument entity.
 */
public interface SpecDocumentRepository extends JpaRepository<SpecDocument,Long> {

}
