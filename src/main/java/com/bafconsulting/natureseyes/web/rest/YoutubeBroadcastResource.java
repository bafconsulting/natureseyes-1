package com.bafconsulting.natureseyes.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;
import com.bafconsulting.natureseyes.service.YoutubeBroadcastService;
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
 * REST controller for managing YoutubeBroadcast.
 */
@RestController
@RequestMapping("/api")
public class YoutubeBroadcastResource {

    private final Logger log = LoggerFactory.getLogger(YoutubeBroadcastResource.class);
        
    @Inject
    private YoutubeBroadcastService youtubeBroadcastService;
    
    /**
     * POST  /youtubeBroadcasts -> Create a new youtubeBroadcast.
     */
    @RequestMapping(value = "/youtubeBroadcasts",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YoutubeBroadcast> createYoutubeBroadcast(@RequestBody YoutubeBroadcast youtubeBroadcast) throws URISyntaxException {
        log.debug("REST request to save YoutubeBroadcast : {}", youtubeBroadcast);
        if (youtubeBroadcast.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("youtubeBroadcast", "idexists", "A new youtubeBroadcast cannot already have an ID")).body(null);
        }
        YoutubeBroadcast result = youtubeBroadcastService.save(youtubeBroadcast);
        return ResponseEntity.created(new URI("/api/youtubeBroadcasts/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("youtubeBroadcast", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /youtubeBroadcasts -> Updates an existing youtubeBroadcast.
     */
    @RequestMapping(value = "/youtubeBroadcasts",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YoutubeBroadcast> updateYoutubeBroadcast(@RequestBody YoutubeBroadcast youtubeBroadcast) throws URISyntaxException {
        log.debug("REST request to update YoutubeBroadcast : {}", youtubeBroadcast);
        if (youtubeBroadcast.getId() == null) {
            return createYoutubeBroadcast(youtubeBroadcast);
        }
        YoutubeBroadcast result = youtubeBroadcastService.save(youtubeBroadcast);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("youtubeBroadcast", youtubeBroadcast.getId().toString()))
            .body(result);
    }

    /**
     * GET  /youtubeBroadcasts -> get all the youtubeBroadcasts.
     */
    @RequestMapping(value = "/youtubeBroadcasts",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<YoutubeBroadcast>> getAllYoutubeBroadcasts(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of YoutubeBroadcasts");
        Page<YoutubeBroadcast> page = youtubeBroadcastService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/youtubeBroadcasts");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /youtubeBroadcasts/:id -> get the "id" youtubeBroadcast.
     */
    @RequestMapping(value = "/youtubeBroadcasts/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<YoutubeBroadcast> getYoutubeBroadcast(@PathVariable Long id) {
        log.debug("REST request to get YoutubeBroadcast : {}", id);
        YoutubeBroadcast youtubeBroadcast = youtubeBroadcastService.findOne(id);
        return Optional.ofNullable(youtubeBroadcast)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /youtubeBroadcasts/:id -> delete the "id" youtubeBroadcast.
     */
    @RequestMapping(value = "/youtubeBroadcasts/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteYoutubeBroadcast(@PathVariable Long id) {
        log.debug("REST request to delete YoutubeBroadcast : {}", id);
        youtubeBroadcastService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("youtubeBroadcast", id.toString())).build();
    }

    /**
     * SEARCH  /_search/youtubeBroadcasts/:query -> search for the youtubeBroadcast corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/youtubeBroadcasts/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<YoutubeBroadcast> searchYoutubeBroadcasts(@PathVariable String query) {
        log.debug("Request to search YoutubeBroadcasts for query {}", query);
        return youtubeBroadcastService.search(query);
    }
}
