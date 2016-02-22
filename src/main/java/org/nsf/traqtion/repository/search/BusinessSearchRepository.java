package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Business;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Business entity.
 */
public interface BusinessSearchRepository extends ElasticsearchRepository<Business, Long> {
}
