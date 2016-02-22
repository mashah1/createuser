package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.PrimarySpec;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the PrimarySpec entity.
 */
public interface PrimarySpecSearchRepository extends ElasticsearchRepository<PrimarySpec, Long> {
}
