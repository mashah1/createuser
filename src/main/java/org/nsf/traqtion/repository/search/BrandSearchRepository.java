package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Brand;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Brand entity.
 */
public interface BrandSearchRepository extends ElasticsearchRepository<Brand, Long> {
}
