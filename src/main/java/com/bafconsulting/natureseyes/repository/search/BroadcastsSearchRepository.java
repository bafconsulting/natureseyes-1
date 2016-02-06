package com.bafconsulting.natureseyes.repository.search;

import com.bafconsulting.natureseyes.domain.Broadcasts;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Broadcasts entity.
 */
public interface BroadcastsSearchRepository extends ElasticsearchRepository<Broadcasts, Long> {
}
