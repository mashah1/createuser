package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.PrimarySpec;
import org.nsf.traqtion.repository.PrimarySpecRepository;
import org.nsf.traqtion.repository.search.PrimarySpecSearchRepository;
import org.nsf.traqtion.web.rest.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
 * REST controller for managing PrimarySpec.
 */
@RestController
@RequestMapping("/api")
public class PrimarySpecResource {

    private final Logger log = LoggerFactory.getLogger(PrimarySpecResource.class);
        
    @Inject
    private PrimarySpecRepository primarySpecRepository;
    
    @Inject
    private PrimarySpecSearchRepository primarySpecSearchRepository;
    
    /**
     * POST  /primarySpecs -> Create a new primarySpec.
     */
    @RequestMapping(value = "/primarySpecs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySpec> createPrimarySpec(@RequestBody PrimarySpec primarySpec) throws URISyntaxException {
        log.debug("REST request to save PrimarySpec : {}", primarySpec);
        if (primarySpec.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("primarySpec", "idexists", "A new primarySpec cannot already have an ID")).body(null);
        }
        PrimarySpec result = primarySpecRepository.save(primarySpec);
        primarySpecSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/primarySpecs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("primarySpec", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /primarySpecs -> Updates an existing primarySpec.
     */
    @RequestMapping(value = "/primarySpecs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySpec> updatePrimarySpec(@RequestBody PrimarySpec primarySpec) throws URISyntaxException {
        log.debug("REST request to update PrimarySpec : {}", primarySpec);
        if (primarySpec.getId() == null) {
            return createPrimarySpec(primarySpec);
        }
        PrimarySpec result = primarySpecRepository.save(primarySpec);
        primarySpecSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("primarySpec", primarySpec.getId().toString()))
            .body(result);
    }

    /**
     * GET  /primarySpecs -> get all the primarySpecs.
     */
    @RequestMapping(value = "/primarySpecs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrimarySpec> getAllPrimarySpecs() {
        log.debug("REST request to get all PrimarySpecs");
        return primarySpecRepository.findAll();
            }

    /**
     * GET  /primarySpecs/:id -> get the "id" primarySpec.
     */
    @RequestMapping(value = "/primarySpecs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<PrimarySpec> getPrimarySpec(@PathVariable Long id) {
        log.debug("REST request to get PrimarySpec : {}", id);
        PrimarySpec primarySpec = primarySpecRepository.findOne(id);
        return Optional.ofNullable(primarySpec)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /primarySpecs/:id -> delete the "id" primarySpec.
     */
    @RequestMapping(value = "/primarySpecs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePrimarySpec(@PathVariable Long id) {
        log.debug("REST request to delete PrimarySpec : {}", id);
        primarySpecRepository.delete(id);
        primarySpecSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("primarySpec", id.toString())).build();
    }

    /**
     * SEARCH  /_search/primarySpecs/:query -> search for the primarySpec corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/primarySpecs/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<PrimarySpec> searchPrimarySpecs(@PathVariable String query) {
        log.debug("REST request to search PrimarySpecs for query {}", query);
        return StreamSupport
            .stream(primarySpecSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
