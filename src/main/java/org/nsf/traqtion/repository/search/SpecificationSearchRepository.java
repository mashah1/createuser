package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Specification;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Specification entity.
 */
public interface SpecificationSearchRepository extends ElasticsearchRepository<Specification, Long> {
}
