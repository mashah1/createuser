package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.BusinessRelationship;
import org.nsf.traqtion.repository.BusinessRelationshipRepository;
import org.nsf.traqtion.repository.search.BusinessRelationshipSearchRepository;
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
 * REST controller for managing BusinessRelationship.
 */
@RestController
@RequestMapping("/api")
public class BusinessRelationshipResource {

    private final Logger log = LoggerFactory.getLogger(BusinessRelationshipResource.class);
        
    @Inject
    private BusinessRelationshipRepository businessRelationshipRepository;
    
    @Inject
    private BusinessRelationshipSearchRepository businessRelationshipSearchRepository;
    
    /**
     * POST  /businessRelationships -> Create a new businessRelationship.
     */
    @RequestMapping(value = "/businessRelationships",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationship> createBusinessRelationship(@RequestBody BusinessRelationship businessRelationship) throws URISyntaxException {
        log.debug("REST request to save BusinessRelationship : {}", businessRelationship);
        if (businessRelationship.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("businessRelationship", "idexists", "A new businessRelationship cannot already have an ID")).body(null);
        }
        BusinessRelationship result = businessRelationshipRepository.save(businessRelationship);
        businessRelationshipSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/businessRelationships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("businessRelationship", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businessRelationships -> Updates an existing businessRelationship.
     */
    @RequestMapping(value = "/businessRelationships",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationship> updateBusinessRelationship(@RequestBody BusinessRelationship businessRelationship) throws URISyntaxException {
        log.debug("REST request to update BusinessRelationship : {}", businessRelationship);
        if (businessRelationship.getId() == null) {
            return createBusinessRelationship(businessRelationship);
        }
        BusinessRelationship result = businessRelationshipRepository.save(businessRelationship);
        businessRelationshipSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("businessRelationship", businessRelationship.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businessRelationships -> get all the businessRelationships.
     */
    @RequestMapping(value = "/businessRelationships",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessRelationship> getAllBusinessRelationships() {
        log.debug("REST request to get all BusinessRelationships");
        return businessRelationshipRepository.findAll();
            }

    /**
     * GET  /businessRelationships/:id -> get the "id" businessRelationship.
     */
    @RequestMapping(value = "/businessRelationships/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationship> getBusinessRelationship(@PathVariable Long id) {
        log.debug("REST request to get BusinessRelationship : {}", id);
        BusinessRelationship businessRelationship = businessRelationshipRepository.findOne(id);
        return Optional.ofNullable(businessRelationship)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businessRelationships/:id -> delete the "id" businessRelationship.
     */
    @RequestMapping(value = "/businessRelationships/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusinessRelationship(@PathVariable Long id) {
        log.debug("REST request to delete BusinessRelationship : {}", id);
        businessRelationshipRepository.delete(id);
        businessRelationshipSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("businessRelationship", id.toString())).build();
    }

    /**
     * SEARCH  /_search/businessRelationships/:query -> search for the businessRelationship corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/businessRelationships/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessRelationship> searchBusinessRelationships(@PathVariable String query) {
        log.debug("REST request to search BusinessRelationships for query {}", query);
        return StreamSupport
            .stream(businessRelationshipSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
