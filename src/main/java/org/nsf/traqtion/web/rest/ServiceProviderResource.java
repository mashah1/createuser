package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.ServiceProvider;
import org.nsf.traqtion.repository.ServiceProviderRepository;
import org.nsf.traqtion.repository.search.ServiceProviderSearchRepository;
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
 * REST controller for managing ServiceProvider.
 */
@RestController
@RequestMapping("/api")
public class ServiceProviderResource {

    private final Logger log = LoggerFactory.getLogger(ServiceProviderResource.class);
        
    @Inject
    private ServiceProviderRepository serviceProviderRepository;
    
    @Inject
    private ServiceProviderSearchRepository serviceProviderSearchRepository;
    
    /**
     * POST  /serviceProviders -> Create a new serviceProvider.
     */
    @RequestMapping(value = "/serviceProviders",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceProvider> createServiceProvider(@RequestBody ServiceProvider serviceProvider) throws URISyntaxException {
        log.debug("REST request to save ServiceProvider : {}", serviceProvider);
        if (serviceProvider.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("serviceProvider", "idexists", "A new serviceProvider cannot already have an ID")).body(null);
        }
        ServiceProvider result = serviceProviderRepository.save(serviceProvider);
        serviceProviderSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/serviceProviders/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("serviceProvider", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /serviceProviders -> Updates an existing serviceProvider.
     */
    @RequestMapping(value = "/serviceProviders",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceProvider> updateServiceProvider(@RequestBody ServiceProvider serviceProvider) throws URISyntaxException {
        log.debug("REST request to update ServiceProvider : {}", serviceProvider);
        if (serviceProvider.getId() == null) {
            return createServiceProvider(serviceProvider);
        }
        ServiceProvider result = serviceProviderRepository.save(serviceProvider);
        serviceProviderSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("serviceProvider", serviceProvider.getId().toString()))
            .body(result);
    }

    /**
     * GET  /serviceProviders -> get all the serviceProviders.
     */
    @RequestMapping(value = "/serviceProviders",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceProvider> getAllServiceProviders() {
        log.debug("REST request to get all ServiceProviders");
        return serviceProviderRepository.findAll();
            }

    /**
     * GET  /serviceProviders/:id -> get the "id" serviceProvider.
     */
    @RequestMapping(value = "/serviceProviders/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ServiceProvider> getServiceProvider(@PathVariable Long id) {
        log.debug("REST request to get ServiceProvider : {}", id);
        ServiceProvider serviceProvider = serviceProviderRepository.findOne(id);
        return Optional.ofNullable(serviceProvider)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /serviceProviders/:id -> delete the "id" serviceProvider.
     */
    @RequestMapping(value = "/serviceProviders/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteServiceProvider(@PathVariable Long id) {
        log.debug("REST request to delete ServiceProvider : {}", id);
        serviceProviderRepository.delete(id);
        serviceProviderSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("serviceProvider", id.toString())).build();
    }

    /**
     * SEARCH  /_search/serviceProviders/:query -> search for the serviceProvider corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/serviceProviders/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ServiceProvider> searchServiceProviders(@PathVariable String query) {
        log.debug("REST request to search ServiceProviders for query {}", query);
        return StreamSupport
            .stream(serviceProviderSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
