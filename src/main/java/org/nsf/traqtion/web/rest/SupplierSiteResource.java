package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.SupplierSite;
import org.nsf.traqtion.repository.SupplierSiteRepository;
import org.nsf.traqtion.repository.search.SupplierSiteSearchRepository;
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
 * REST controller for managing SupplierSite.
 */
@RestController
@RequestMapping("/api")
public class SupplierSiteResource {

    private final Logger log = LoggerFactory.getLogger(SupplierSiteResource.class);
        
    @Inject
    private SupplierSiteRepository supplierSiteRepository;
    
    @Inject
    private SupplierSiteSearchRepository supplierSiteSearchRepository;
    
    /**
     * POST  /supplierSites -> Create a new supplierSite.
     */
    @RequestMapping(value = "/supplierSites",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SupplierSite> createSupplierSite(@RequestBody SupplierSite supplierSite) throws URISyntaxException {
        log.debug("REST request to save SupplierSite : {}", supplierSite);
        if (supplierSite.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("supplierSite", "idexists", "A new supplierSite cannot already have an ID")).body(null);
        }
        SupplierSite result = supplierSiteRepository.save(supplierSite);
        supplierSiteSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/supplierSites/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("supplierSite", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /supplierSites -> Updates an existing supplierSite.
     */
    @RequestMapping(value = "/supplierSites",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SupplierSite> updateSupplierSite(@RequestBody SupplierSite supplierSite) throws URISyntaxException {
        log.debug("REST request to update SupplierSite : {}", supplierSite);
        if (supplierSite.getId() == null) {
            return createSupplierSite(supplierSite);
        }
        SupplierSite result = supplierSiteRepository.save(supplierSite);
        supplierSiteSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("supplierSite", supplierSite.getId().toString()))
            .body(result);
    }

    /**
     * GET  /supplierSites -> get all the supplierSites.
     */
    @RequestMapping(value = "/supplierSites",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SupplierSite> getAllSupplierSites() {
        log.debug("REST request to get all SupplierSites");
        return supplierSiteRepository.findAll();
            }

    /**
     * GET  /supplierSites/:id -> get the "id" supplierSite.
     */
    @RequestMapping(value = "/supplierSites/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SupplierSite> getSupplierSite(@PathVariable Long id) {
        log.debug("REST request to get SupplierSite : {}", id);
        SupplierSite supplierSite = supplierSiteRepository.findOne(id);
        return Optional.ofNullable(supplierSite)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /supplierSites/:id -> delete the "id" supplierSite.
     */
    @RequestMapping(value = "/supplierSites/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSupplierSite(@PathVariable Long id) {
        log.debug("REST request to delete SupplierSite : {}", id);
        supplierSiteRepository.delete(id);
        supplierSiteSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("supplierSite", id.toString())).build();
    }

    /**
     * SEARCH  /_search/supplierSites/:query -> search for the supplierSite corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/supplierSites/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SupplierSite> searchSupplierSites(@PathVariable String query) {
        log.debug("REST request to search SupplierSites for query {}", query);
        return StreamSupport
            .stream(supplierSiteSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
