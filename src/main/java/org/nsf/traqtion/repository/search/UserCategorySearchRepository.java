package org.nsf.traqtion.repository.search;

import org.nsf.traqtion.domain.UserCategory;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the UserCategory entity.
 */
public interface UserCategorySearchRepository extends ElasticsearchRepository<UserCategory, Long> {
}
