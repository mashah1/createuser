package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.BusinessRelationshipType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BusinessRelationshipType entity.
 */
public interface BusinessRelationshipTypeSearchRepository extends ElasticsearchRepository<BusinessRelationshipType, Long> {
}
