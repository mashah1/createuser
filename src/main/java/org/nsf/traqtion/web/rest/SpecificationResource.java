package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Specification;
import org.nsf.traqtion.repository.SpecificationRepository;
import org.nsf.traqtion.repository.search.SpecificationSearchRepository;
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
 * REST controller for managing Specification.
 */
@RestController
@RequestMapping("/api")
public class SpecificationResource {

    private final Logger log = LoggerFactory.getLogger(SpecificationResource.class);
        
    @Inject
    private SpecificationRepository specificationRepository;
    
    @Inject
    private SpecificationSearchRepository specificationSearchRepository;
    
    /**
     * POST  /specifications -> Create a new specification.
     */
    @RequestMapping(value = "/specifications",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specification> createSpecification(@RequestBody Specification specification) throws URISyntaxException {
        log.debug("REST request to save Specification : {}", specification);
        if (specification.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("specification", "idexists", "A new specification cannot already have an ID")).body(null);
        }
        Specification result = specificationRepository.save(specification);
        specificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/specifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("specification", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /specifications -> Updates an existing specification.
     */
    @RequestMapping(value = "/specifications",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specification> updateSpecification(@RequestBody Specification specification) throws URISyntaxException {
        log.debug("REST request to update Specification : {}", specification);
        if (specification.getId() == null) {
            return createSpecification(specification);
        }
        Specification result = specificationRepository.save(specification);
        specificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("specification", specification.getId().toString()))
            .body(result);
    }

    /**
     * GET  /specifications -> get all the specifications.
     */
    @RequestMapping(value = "/specifications",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Specification> getAllSpecifications() {
        log.debug("REST request to get all Specifications");
        return specificationRepository.findAll();
            }

    /**
     * GET  /specifications/:id -> get the "id" specification.
     */
    @RequestMapping(value = "/specifications/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Specification> getSpecification(@PathVariable Long id) {
        log.debug("REST request to get Specification : {}", id);
        Specification specification = specificationRepository.findOne(id);
        return Optional.ofNullable(specification)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /specifications/:id -> delete the "id" specification.
     */
    @RequestMapping(value = "/specifications/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpecification(@PathVariable Long id) {
        log.debug("REST request to delete Specification : {}", id);
        specificationRepository.delete(id);
        specificationSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("specification", id.toString())).build();
    }

    /**
     * SEARCH  /_search/specifications/:query -> search for the specification corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/specifications/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Specification> searchSpecifications(@PathVariable String query) {
        log.debug("REST request to search Specifications for query {}", query);
        return StreamSupport
            .stream(specificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
