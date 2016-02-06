package com.bafconsulting.natureseyes.service;

import com.bafconsulting.natureseyes.domain.Broadcasts;
import com.bafconsulting.natureseyes.repository.BroadcastsRepository;
import com.bafconsulting.natureseyes.repository.search.BroadcastsSearchRepository;
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
 * Service Implementation for managing Broadcasts.
 */
@Service
@Transactional
public class BroadcastsService {

    private final Logger log = LoggerFactory.getLogger(BroadcastsService.class);
    
    @Inject
    private BroadcastsRepository broadcastsRepository;
    
    @Inject
    private BroadcastsSearchRepository broadcastsSearchRepository;
    
    /**
     * Save a broadcasts.
     * @return the persisted entity
     */
    public Broadcasts save(Broadcasts broadcasts) {
        log.debug("Request to save Broadcasts : {}", broadcasts);
        Broadcasts result = broadcastsRepository.save(broadcasts);
        broadcastsSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the broadcastss.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Broadcasts> findAll(Pageable pageable) {
        log.debug("Request to get all Broadcastss");
        Page<Broadcasts> result = broadcastsRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one broadcasts by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Broadcasts findOne(Long id) {
        log.debug("Request to get Broadcasts : {}", id);
        Broadcasts broadcasts = broadcastsRepository.findOne(id);
        return broadcasts;
    }

    /**
     *  delete the  broadcasts by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Broadcasts : {}", id);
        broadcastsRepository.delete(id);
        broadcastsSearchRepository.delete(id);
    }

    /**
     * search for the broadcasts corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Broadcasts> search(String query) {
        
        log.debug("REST request to search Broadcastss for query {}", query);
        return StreamSupport
            .stream(broadcastsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
