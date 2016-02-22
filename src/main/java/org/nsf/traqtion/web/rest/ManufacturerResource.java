package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Manufacturer;
import org.nsf.traqtion.repository.ManufacturerRepository;
import org.nsf.traqtion.repository.search.ManufacturerSearchRepository;
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
 * REST controller for managing Manufacturer.
 */
@RestController
@RequestMapping("/api")
public class ManufacturerResource {

    private final Logger log = LoggerFactory.getLogger(ManufacturerResource.class);
        
    @Inject
    private ManufacturerRepository manufacturerRepository;
    
    @Inject
    private ManufacturerSearchRepository manufacturerSearchRepository;
    
    /**
     * POST  /manufacturers -> Create a new manufacturer.
     */
    @RequestMapping(value = "/manufacturers",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Manufacturer> createManufacturer(@RequestBody Manufacturer manufacturer) throws URISyntaxException {
        log.debug("REST request to save Manufacturer : {}", manufacturer);
        if (manufacturer.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("manufacturer", "idexists", "A new manufacturer cannot already have an ID")).body(null);
        }
        Manufacturer result = manufacturerRepository.save(manufacturer);
        manufacturerSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/manufacturers/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("manufacturer", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /manufacturers -> Updates an existing manufacturer.
     */
    @RequestMapping(value = "/manufacturers",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Manufacturer> updateManufacturer(@RequestBody Manufacturer manufacturer) throws URISyntaxException {
        log.debug("REST request to update Manufacturer : {}", manufacturer);
        if (manufacturer.getId() == null) {
            return createManufacturer(manufacturer);
        }
        Manufacturer result = manufacturerRepository.save(manufacturer);
        manufacturerSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("manufacturer", manufacturer.getId().toString()))
            .body(result);
    }

    /**
     * GET  /manufacturers -> get all the manufacturers.
     */
    @RequestMapping(value = "/manufacturers",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Manufacturer> getAllManufacturers() {
        log.debug("REST request to get all Manufacturers");
        return manufacturerRepository.findAll();
            }

    /**
     * GET  /manufacturers/:id -> get the "id" manufacturer.
     */
    @RequestMapping(value = "/manufacturers/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Manufacturer> getManufacturer(@PathVariable Long id) {
        log.debug("REST request to get Manufacturer : {}", id);
        Manufacturer manufacturer = manufacturerRepository.findOne(id);
        return Optional.ofNullable(manufacturer)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /manufacturers/:id -> delete the "id" manufacturer.
     */
    @RequestMapping(value = "/manufacturers/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteManufacturer(@PathVariable Long id) {
        log.debug("REST request to delete Manufacturer : {}", id);
        manufacturerRepository.delete(id);
        manufacturerSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("manufacturer", id.toString())).build();
    }

    /**
     * SEARCH  /_search/manufacturers/:query -> search for the manufacturer corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/manufacturers/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Manufacturer> searchManufacturers(@PathVariable String query) {
        log.debug("REST request to search Manufacturers for query {}", query);
        return StreamSupport
            .stream(manufacturerSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
