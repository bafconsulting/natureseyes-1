package com.bafconsulting.natureseyes.repository.search;

import com.bafconsulting.natureseyes.domain.Broadcast;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the Broadcast entity.
 */
public interface BroadcastSearchRepository extends ElasticsearchRepository<Broadcast, Long> {
}
