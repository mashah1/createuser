package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.SpecStatus;
import org.nsf.traqtion.repository.SpecStatusRepository;
import org.nsf.traqtion.repository.search.SpecStatusSearchRepository;
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
 * REST controller for managing SpecStatus.
 */
@RestController
@RequestMapping("/api")
public class SpecStatusResource {

    private final Logger log = LoggerFactory.getLogger(SpecStatusResource.class);
        
    @Inject
    private SpecStatusRepository specStatusRepository;
    
    @Inject
    private SpecStatusSearchRepository specStatusSearchRepository;
    
    /**
     * POST  /specStatuss -> Create a new specStatus.
     */
    @RequestMapping(value = "/specStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecStatus> createSpecStatus(@RequestBody SpecStatus specStatus) throws URISyntaxException {
        log.debug("REST request to save SpecStatus : {}", specStatus);
        if (specStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("specStatus", "idexists", "A new specStatus cannot already have an ID")).body(null);
        }
        SpecStatus result = specStatusRepository.save(specStatus);
        specStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/specStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("specStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /specStatuss -> Updates an existing specStatus.
     */
    @RequestMapping(value = "/specStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecStatus> updateSpecStatus(@RequestBody SpecStatus specStatus) throws URISyntaxException {
        log.debug("REST request to update SpecStatus : {}", specStatus);
        if (specStatus.getId() == null) {
            return createSpecStatus(specStatus);
        }
        SpecStatus result = specStatusRepository.save(specStatus);
        specStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("specStatus", specStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /specStatuss -> get all the specStatuss.
     */
    @RequestMapping(value = "/specStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpecStatus> getAllSpecStatuss() {
        log.debug("REST request to get all SpecStatuss");
        return specStatusRepository.findAll();
            }

    /**
     * GET  /specStatuss/:id -> get the "id" specStatus.
     */
    @RequestMapping(value = "/specStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecStatus> getSpecStatus(@PathVariable Long id) {
        log.debug("REST request to get SpecStatus : {}", id);
        SpecStatus specStatus = specStatusRepository.findOne(id);
        return Optional.ofNullable(specStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /specStatuss/:id -> delete the "id" specStatus.
     */
    @RequestMapping(value = "/specStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpecStatus(@PathVariable Long id) {
        log.debug("REST request to delete SpecStatus : {}", id);
        specStatusRepository.delete(id);
        specStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("specStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/specStatuss/:query -> search for the specStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/specStatuss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpecStatus> searchSpecStatuss(@PathVariable String query) {
        log.debug("REST request to search SpecStatuss for query {}", query);
        return StreamSupport
            .stream(specStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
