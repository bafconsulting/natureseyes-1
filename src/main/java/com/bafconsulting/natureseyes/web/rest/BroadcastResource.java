package com.bafconsulting.natureseyes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bafconsulting.natureseyes.domain.Broadcast;
import com.bafconsulting.natureseyes.service.BroadcastService;
import com.bafconsulting.natureseyes.web.rest.util.HeaderUtil;
import com.bafconsulting.natureseyes.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Broadcast.
 */
@RestController
@RequestMapping("/api")
public class BroadcastResource {

    private final Logger log = LoggerFactory.getLogger(BroadcastResource.class);
        
    @Inject
    private BroadcastService broadcastService;
    
    /**
     * POST  /broadcasts -> Create a new broadcast.
     */
    @RequestMapping(value = "/broadcasts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcast> createBroadcast(@RequestBody Broadcast broadcast) throws URISyntaxException {
        log.debug("REST request to save Broadcast : {}", broadcast);
        if (broadcast.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("broadcast", "idexists", "A new broadcast cannot already have an ID")).body(null);
        }
        Broadcast result = broadcastService.save(broadcast);
        return ResponseEntity.created(new URI("/api/broadcasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("broadcast", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /broadcasts -> Updates an existing broadcast.
     */
    @RequestMapping(value = "/broadcasts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcast> updateBroadcast(@RequestBody Broadcast broadcast) throws URISyntaxException {
        log.debug("REST request to update Broadcast : {}", broadcast);
        if (broadcast.getId() == null) {
            return createBroadcast(broadcast);
        }
        Broadcast result = broadcastService.save(broadcast);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("broadcast", broadcast.getId().toString()))
            .body(result);
    }

    /**
     * GET  /broadcasts -> get all the broadcasts.
     */
    @RequestMapping(value = "/broadcasts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Broadcast>> getAllBroadcasts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Broadcasts");
        Page<Broadcast> page = broadcastService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/broadcasts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /broadcasts/:id -> get the "id" broadcast.
     */
    @RequestMapping(value = "/broadcasts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcast> getBroadcast(@PathVariable Long id) {
        log.debug("REST request to get Broadcast : {}", id);
        Broadcast broadcast = broadcastService.findOne(id);
        return Optional.ofNullable(broadcast)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /broadcasts/:id -> delete the "id" broadcast.
     */
    @RequestMapping(value = "/broadcasts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBroadcast(@PathVariable Long id) {
        log.debug("REST request to delete Broadcast : {}", id);
        broadcastService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("broadcast", id.toString())).build();
    }

    /**
     * SEARCH  /_search/broadcasts/:query -> search for the broadcast corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/broadcasts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Broadcast> searchBroadcasts(@PathVariable String query) {
        log.debug("Request to search Broadcasts for query {}", query);
        return broadcastService.search(query);
    }
}
