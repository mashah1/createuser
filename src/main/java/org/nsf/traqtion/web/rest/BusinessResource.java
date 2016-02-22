package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Business;
import org.nsf.traqtion.repository.BusinessRepository;
import org.nsf.traqtion.repository.search.BusinessSearchRepository;
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
 * REST controller for managing Business.
 */
@RestController
@RequestMapping("/api")
public class BusinessResource {

    private final Logger log = LoggerFactory.getLogger(BusinessResource.class);
        
    @Inject
    private BusinessRepository businessRepository;
    
    @Inject
    private BusinessSearchRepository businessSearchRepository;
    
    /**
     * POST  /businesss -> Create a new business.
     */
    @RequestMapping(value = "/businesss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Business> createBusiness(@RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to save Business : {}", business);
        if (business.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("business", "idexists", "A new business cannot already have an ID")).body(null);
        }
        Business result = businessRepository.save(business);
        businessSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/businesss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("business", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /businesss -> Updates an existing business.
     */
    @RequestMapping(value = "/businesss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Business> updateBusiness(@RequestBody Business business) throws URISyntaxException {
        log.debug("REST request to update Business : {}", business);
        if (business.getId() == null) {
            return createBusiness(business);
        }
        Business result = businessRepository.save(business);
        businessSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("business", business.getId().toString()))
            .body(result);
    }

    /**
     * GET  /businesss -> get all the businesss.
     */
    @RequestMapping(value = "/businesss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Business> getAllBusinesss() {
        log.debug("REST request to get all Businesss");
        return businessRepository.findAll();
            }

    /**
     * GET  /businesss/:id -> get the "id" business.
     */
    @RequestMapping(value = "/businesss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Business> getBusiness(@PathVariable Long id) {
        log.debug("REST request to get Business : {}", id);
        Business business = businessRepository.findOne(id);
        return Optional.ofNullable(business)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /businesss/:id -> delete the "id" business.
     */
    @RequestMapping(value = "/businesss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBusiness(@PathVariable Long id) {
        log.debug("REST request to delete Business : {}", id);
        businessRepository.delete(id);
        businessSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("business", id.toString())).build();
    }

    /**
     * SEARCH  /_search/businesss/:query -> search for the business corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/businesss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Business> searchBusinesss(@PathVariable String query) {
        log.debug("REST request to search Businesss for query {}", query);
        return StreamSupport
            .stream(businessSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
