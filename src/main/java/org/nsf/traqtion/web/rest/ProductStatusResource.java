package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.ProductStatus;
import org.nsf.traqtion.repository.ProductStatusRepository;
import org.nsf.traqtion.repository.search.ProductStatusSearchRepository;
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
 * REST controller for managing ProductStatus.
 */
@RestController
@RequestMapping("/api")
public class ProductStatusResource {

    private final Logger log = LoggerFactory.getLogger(ProductStatusResource.class);
        
    @Inject
    private ProductStatusRepository productStatusRepository;
    
    @Inject
    private ProductStatusSearchRepository productStatusSearchRepository;
    
    /**
     * POST  /productStatuss -> Create a new productStatus.
     */
    @RequestMapping(value = "/productStatuss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductStatus> createProductStatus(@RequestBody ProductStatus productStatus) throws URISyntaxException {
        log.debug("REST request to save ProductStatus : {}", productStatus);
        if (productStatus.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productStatus", "idexists", "A new productStatus cannot already have an ID")).body(null);
        }
        ProductStatus result = productStatusRepository.save(productStatus);
        productStatusSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productStatuss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productStatus", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productStatuss -> Updates an existing productStatus.
     */
    @RequestMapping(value = "/productStatuss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductStatus> updateProductStatus(@RequestBody ProductStatus productStatus) throws URISyntaxException {
        log.debug("REST request to update ProductStatus : {}", productStatus);
        if (productStatus.getId() == null) {
            return createProductStatus(productStatus);
        }
        ProductStatus result = productStatusRepository.save(productStatus);
        productStatusSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productStatus", productStatus.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productStatuss -> get all the productStatuss.
     */
    @RequestMapping(value = "/productStatuss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductStatus> getAllProductStatuss() {
        log.debug("REST request to get all ProductStatuss");
        return productStatusRepository.findAll();
            }

    /**
     * GET  /productStatuss/:id -> get the "id" productStatus.
     */
    @RequestMapping(value = "/productStatuss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductStatus> getProductStatus(@PathVariable Long id) {
        log.debug("REST request to get ProductStatus : {}", id);
        ProductStatus productStatus = productStatusRepository.findOne(id);
        return Optional.ofNullable(productStatus)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productStatuss/:id -> delete the "id" productStatus.
     */
    @RequestMapping(value = "/productStatuss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductStatus(@PathVariable Long id) {
        log.debug("REST request to delete ProductStatus : {}", id);
        productStatusRepository.delete(id);
        productStatusSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productStatus", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productStatuss/:query -> search for the productStatus corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productStatuss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductStatus> searchProductStatuss(@PathVariable String query) {
        log.debug("REST request to search ProductStatuss for query {}", query);
        return StreamSupport
            .stream(productStatusSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
