package com.bafconsulting.natureseyes.service;

import com.bafconsulting.natureseyes.domain.Broadcast;
import com.bafconsulting.natureseyes.repository.BroadcastRepository;
import com.bafconsulting.natureseyes.repository.search.BroadcastSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Broadcast.
 */
@Service
@Transactional
public class BroadcastService {

    private final Logger log = LoggerFactory.getLogger(BroadcastService.class);
    
    @Inject
    private BroadcastRepository broadcastRepository;
    
    @Inject
    private BroadcastSearchRepository broadcastSearchRepository;
    
    /**
     * Save a broadcast.
     * @return the persisted entity
     */
    public Broadcast save(Broadcast broadcast) {
        log.debug("Request to save Broadcast : {}", broadcast);
        Broadcast result = broadcastRepository.save(broadcast);
        broadcastSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the broadcasts.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Broadcast> findAll(Pageable pageable) {
        log.debug("Request to get all Broadcasts");
        Page<Broadcast> result = broadcastRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one broadcast by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Broadcast findOne(Long id) {
        log.debug("Request to get Broadcast : {}", id);
        Broadcast broadcast = broadcastRepository.findOne(id);
        return broadcast;
    }

    /**
     *  delete the  broadcast by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Broadcast : {}", id);
        broadcastRepository.delete(id);
        broadcastSearchRepository.delete(id);
    }

    /**
     * search for the broadcast corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Broadcast> search(String query) {
        
        log.debug("REST request to search Broadcasts for query {}", query);
        return StreamSupport
            .stream(broadcastSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
