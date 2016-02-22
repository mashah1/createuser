package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.RolePermission;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the RolePermission entity.
 */
public interface RolePermissionSearchRepository extends ElasticsearchRepository<RolePermission, Long> {
}
