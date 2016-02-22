package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.SpecDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SpecDocument entity.
 */
public interface SpecDocumentSearchRepository extends ElasticsearchRepository<SpecDocument, Long> {
}
