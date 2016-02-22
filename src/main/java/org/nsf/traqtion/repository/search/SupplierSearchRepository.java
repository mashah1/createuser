package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.Supplier;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Supplier entity.
 */
public interface SupplierSearchRepository extends ElasticsearchRepository<Supplier, Long> {
}
