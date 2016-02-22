package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.SpecStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SpecStatus entity.
 */
public interface SpecStatusSearchRepository extends ElasticsearchRepository<SpecStatus, Long> {
}
