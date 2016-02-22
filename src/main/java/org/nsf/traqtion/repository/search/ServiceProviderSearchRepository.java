package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.ServiceProvider;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the ServiceProvider entity.
 */
public interface ServiceProviderSearchRepository extends ElasticsearchRepository<ServiceProvider, Long> {
}
