package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.ProductStatus;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductStatus entity.
 */
public interface ProductStatusSearchRepository extends ElasticsearchRepository<ProductStatus, Long> {
}
