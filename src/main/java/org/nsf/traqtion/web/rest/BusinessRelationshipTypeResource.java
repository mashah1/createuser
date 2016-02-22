package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.BusinessRelationshipType;
import org.nsf.traqtion.repository.BusinessRelationshipTypeRepository;
import org.nsf.traqtion.repository.search.BusinessRelationshipTypeSearchRepository;
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
 * REST controller for managing BusinessRelationshipType.
 */
@RestController
@RequestMapping("/api")
public class BusinessRelationshipTypeResource {

    private final Logger log = LoggerFactory.getLogger(BusinessRelationshipTypeResource.class);
        
    @Inject
    private BusinessRelationshipTypeRepository businessRelationshipTypeRepository;
    
    @Inject
    private BusinessRelationshipTypeSearchRepository businessRelationshipTypeSearchRepository;
    
    /**
     * POST  /businessRelationshipTypes -> Create a new businessRelationshipType.
     */
    @RequestMapping(value = "/businessRelationshipTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationshipType> createBusinessRelationshipType(@RequestBody BusinessRelationshipType businessRelationshipType) throws URISyntaxException {
        log.debug("REST request to save BusinessRelationshipType : {}", businessRelationshipType);
        if (businessRelationshipType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("businessRelationshipType", "idexists", "A new businessRelationshipType cannot already have an ID")).body(null);
        }
        BusinessRelationshipType result = businessRelationshipTypeRepository.save(businessRelationshipType);
        businessRelationshipTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/businessRelationshipTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("businessRelationshipType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businessRelationshipTypes -> Updates an existing businessRelationshipType.
     */
    @RequestMapping(value = "/businessRelationshipTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationshipType> updateBusinessRelationshipType(@RequestBody BusinessRelationshipType businessRelationshipType) throws URISyntaxException {
        log.debug("REST request to update BusinessRelationshipType : {}", businessRelationshipType);
        if (businessRelationshipType.getId() == null) {
            return createBusinessRelationshipType(businessRelationshipType);
        }
        BusinessRelationshipType result = businessRelationshipTypeRepository.save(businessRelationshipType);
        businessRelationshipTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("businessRelationshipType", businessRelationshipType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businessRelationshipTypes -> get all the businessRelationshipTypes.
     */
    @RequestMapping(value = "/businessRelationshipTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessRelationshipType> getAllBusinessRelationshipTypes() {
        log.debug("REST request to get all BusinessRelationshipTypes");
        return businessRelationshipTypeRepository.findAll();
            }

    /**
     * GET  /businessRelationshipTypes/:id -> get the "id" businessRelationshipType.
     */
    @RequestMapping(value = "/businessRelationshipTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessRelationshipType> getBusinessRelationshipType(@PathVariable Long id) {
        log.debug("REST request to get BusinessRelationshipType : {}", id);
        BusinessRelationshipType businessRelationshipType = businessRelationshipTypeRepository.findOne(id);
        return Optional.ofNullable(businessRelationshipType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businessRelationshipTypes/:id -> delete the "id" businessRelationshipType.
     */
    @RequestMapping(value = "/businessRelationshipTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusinessRelationshipType(@PathVariable Long id) {
        log.debug("REST request to delete BusinessRelationshipType : {}", id);
        businessRelationshipTypeRepository.delete(id);
        businessRelationshipTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("businessRelationshipType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/businessRelationshipTypes/:query -> search for the businessRelationshipType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/businessRelationshipTypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessRelationshipType> searchBusinessRelationshipTypes(@PathVariable String query) {
        log.debug("REST request to search BusinessRelationshipTypes for query {}", query);
        return StreamSupport
            .stream(businessRelationshipTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
