package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Supplier;
import org.nsf.traqtion.repository.SupplierRepository;
import org.nsf.traqtion.repository.search.SupplierSearchRepository;
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
 * REST controller for managing Supplier.
 */
@RestController
@RequestMapping("/api")
public class SupplierResource {

    private final Logger log = LoggerFactory.getLogger(SupplierResource.class);
        
    @Inject
    private SupplierRepository supplierRepository;
    
    @Inject
    private SupplierSearchRepository supplierSearchRepository;
    
    /**
     * POST  /suppliers -> Create a new supplier.
     */
    @RequestMapping(value = "/suppliers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supplier> createSupplier(@RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to save Supplier : {}", supplier);
        if (supplier.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("supplier", "idexists", "A new supplier cannot already have an ID")).body(null);
        }
        Supplier result = supplierRepository.save(supplier);
        supplierSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/suppliers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("supplier", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /suppliers -> Updates an existing supplier.
     */
    @RequestMapping(value = "/suppliers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supplier> updateSupplier(@RequestBody Supplier supplier) throws URISyntaxException {
        log.debug("REST request to update Supplier : {}", supplier);
        if (supplier.getId() == null) {
            return createSupplier(supplier);
        }
        Supplier result = supplierRepository.save(supplier);
        supplierSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("supplier", supplier.getId().toString()))
            .body(result);
    }

    /**
     * GET  /suppliers -> get all the suppliers.
     */
    @RequestMapping(value = "/suppliers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Supplier> getAllSuppliers() {
        log.debug("REST request to get all Suppliers");
        return supplierRepository.findAll();
            }

    /**
     * GET  /suppliers/:id -> get the "id" supplier.
     */
    @RequestMapping(value = "/suppliers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Supplier> getSupplier(@PathVariable Long id) {
        log.debug("REST request to get Supplier : {}", id);
        Supplier supplier = supplierRepository.findOne(id);
        return Optional.ofNullable(supplier)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /suppliers/:id -> delete the "id" supplier.
     */
    @RequestMapping(value = "/suppliers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSupplier(@PathVariable Long id) {
        log.debug("REST request to delete Supplier : {}", id);
        supplierRepository.delete(id);
        supplierSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("supplier", id.toString())).build();
    }

    /**
     * SEARCH  /_search/suppliers/:query -> search for the supplier corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/suppliers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Supplier> searchSuppliers(@PathVariable String query) {
        log.debug("REST request to search Suppliers for query {}", query);
        return StreamSupport
            .stream(supplierSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
