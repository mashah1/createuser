package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.UserBusinessRole;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserBusinessRole entity.
 */
public interface UserBusinessRoleSearchRepository extends ElasticsearchRepository<UserBusinessRole, Long> {
}
