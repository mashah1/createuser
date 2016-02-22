package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Role;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Role entity.
 */
public interface RoleSearchRepository extends ElasticsearchRepository<Role, Long> {
}
