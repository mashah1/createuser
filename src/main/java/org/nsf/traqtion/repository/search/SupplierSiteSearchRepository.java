package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.SupplierSite;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the SupplierSite entity.
 */
public interface SupplierSiteSearchRepository extends ElasticsearchRepository<SupplierSite, Long> {
}
