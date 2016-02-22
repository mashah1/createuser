package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Permissions;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Permissions entity.
 */
public interface PermissionsSearchRepository extends ElasticsearchRepository<Permissions, Long> {
}
