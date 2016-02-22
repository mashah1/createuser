package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.BusinessType;
import org.nsf.traqtion.repository.BusinessTypeRepository;
import org.nsf.traqtion.repository.search.BusinessTypeSearchRepository;
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
 * REST controller for managing BusinessType.
 */
@RestController
@RequestMapping("/api")
public class BusinessTypeResource {

    private final Logger log = LoggerFactory.getLogger(BusinessTypeResource.class);
        
    @Inject
    private BusinessTypeRepository businessTypeRepository;
    
    @Inject
    private BusinessTypeSearchRepository businessTypeSearchRepository;
    
    /**
     * POST  /businessTypes -> Create a new businessType.
     */
    @RequestMapping(value = "/businessTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessType> createBusinessType(@RequestBody BusinessType businessType) throws URISyntaxException {
        log.debug("REST request to save BusinessType : {}", businessType);
        if (businessType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("businessType", "idexists", "A new businessType cannot already have an ID")).body(null);
        }
        BusinessType result = businessTypeRepository.save(businessType);
        businessTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/businessTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("businessType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businessTypes -> Updates an existing businessType.
     */
    @RequestMapping(value = "/businessTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessType> updateBusinessType(@RequestBody BusinessType businessType) throws URISyntaxException {
        log.debug("REST request to update BusinessType : {}", businessType);
        if (businessType.getId() == null) {
            return createBusinessType(businessType);
        }
        BusinessType result = businessTypeRepository.save(businessType);
        businessTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("businessType", businessType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businessTypes -> get all the businessTypes.
     */
    @RequestMapping(value = "/businessTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessType> getAllBusinessTypes() {
        log.debug("REST request to get all BusinessTypes");
        return businessTypeRepository.findAll();
            }

    /**
     * GET  /businessTypes/:id -> get the "id" businessType.
     */
    @RequestMapping(value = "/businessTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<BusinessType> getBusinessType(@PathVariable Long id) {
        log.debug("REST request to get BusinessType : {}", id);
        BusinessType businessType = businessTypeRepository.findOne(id);
        return Optional.ofNullable(businessType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businessTypes/:id -> delete the "id" businessType.
     */
    @RequestMapping(value = "/businessTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusinessType(@PathVariable Long id) {
        log.debug("REST request to delete BusinessType : {}", id);
        businessTypeRepository.delete(id);
        businessTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("businessType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/businessTypes/:query -> search for the businessType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/businessTypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<BusinessType> searchBusinessTypes(@PathVariable String query) {
        log.debug("REST request to search BusinessTypes for query {}", query);
        return StreamSupport
            .stream(businessTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
