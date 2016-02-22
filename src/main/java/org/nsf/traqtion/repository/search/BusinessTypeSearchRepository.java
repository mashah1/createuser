package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.BusinessType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BusinessType entity.
 */
public interface BusinessTypeSearchRepository extends ElasticsearchRepository<BusinessType, Long> {
}
