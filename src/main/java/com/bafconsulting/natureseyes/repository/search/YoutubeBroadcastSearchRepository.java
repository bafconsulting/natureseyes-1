package com.bafconsulting.natureseyes.repository.search;

import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * Spring Data ElasticSearch repository for the YoutubeBroadcast entity.
 */
public interface YoutubeBroadcastSearchRepository extends ElasticsearchRepository<YoutubeBroadcast, Long> {
}
