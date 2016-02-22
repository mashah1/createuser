package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.BusinessRelationship;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the BusinessRelationship entity.
 */
public interface BusinessRelationshipSearchRepository extends ElasticsearchRepository<BusinessRelationship, Long> {
}
