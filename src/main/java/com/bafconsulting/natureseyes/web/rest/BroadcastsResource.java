package com.bafconsulting.natureseyes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bafconsulting.natureseyes.domain.Broadcasts;
import com.bafconsulting.natureseyes.service.BroadcastsService;
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
 * REST controller for managing Broadcasts.
 */
@RestController
@RequestMapping("/api")
public class BroadcastsResource {

    private final Logger log = LoggerFactory.getLogger(BroadcastsResource.class);
        
    @Inject
    private BroadcastsService broadcastsService;
    
    /**
     * POST  /broadcastss -> Create a new broadcasts.
     */
    @RequestMapping(value = "/broadcastss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcasts> createBroadcasts(@RequestBody Broadcasts broadcasts) throws URISyntaxException {
        log.debug("REST request to save Broadcasts : {}", broadcasts);
        if (broadcasts.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("broadcasts", "idexists", "A new broadcasts cannot already have an ID")).body(null);
        }
        Broadcasts result = broadcastsService.save(broadcasts);
        return ResponseEntity.created(new URI("/api/broadcastss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("broadcasts", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /broadcastss -> Updates an existing broadcasts.
     */
    @RequestMapping(value = "/broadcastss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcasts> updateBroadcasts(@RequestBody Broadcasts broadcasts) throws URISyntaxException {
        log.debug("REST request to update Broadcasts : {}", broadcasts);
        if (broadcasts.getId() == null) {
            return createBroadcasts(broadcasts);
        }
        Broadcasts result = broadcastsService.save(broadcasts);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("broadcasts", broadcasts.getId().toString()))
            .body(result);
    }

    /**
     * GET  /broadcastss -> get all the broadcastss.
     */
    @RequestMapping(value = "/broadcastss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Broadcasts>> getAllBroadcastss(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Broadcastss");
        Page<Broadcasts> page = broadcastsService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/broadcastss");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /broadcastss/:id -> get the "id" broadcasts.
     */
    @RequestMapping(value = "/broadcastss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Broadcasts> getBroadcasts(@PathVariable Long id) {
        log.debug("REST request to get Broadcasts : {}", id);
        Broadcasts broadcasts = broadcastsService.findOne(id);
        return Optional.ofNullable(broadcasts)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /broadcastss/:id -> delete the "id" broadcasts.
     */
    @RequestMapping(value = "/broadcastss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBroadcasts(@PathVariable Long id) {
        log.debug("REST request to delete Broadcasts : {}", id);
        broadcastsService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("broadcasts", id.toString())).build();
    }

    /**
     * SEARCH  /_search/broadcastss/:query -> search for the broadcasts corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/broadcastss/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Broadcasts> searchBroadcastss(@PathVariable String query) {
        log.debug("Request to search Broadcastss for query {}", query);
        return broadcastsService.search(query);
    }
}
