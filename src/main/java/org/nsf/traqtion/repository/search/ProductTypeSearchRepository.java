package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.ProductType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ProductType entity.
 */
public interface ProductTypeSearchRepository extends ElasticsearchRepository<ProductType, Long> {
}
