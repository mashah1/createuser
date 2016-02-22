package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.ProductType;
import org.nsf.traqtion.repository.ProductTypeRepository;
import org.nsf.traqtion.repository.search.ProductTypeSearchRepository;
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
 * REST controller for managing ProductType.
 */
@RestController
@RequestMapping("/api")
public class ProductTypeResource {

    private final Logger log = LoggerFactory.getLogger(ProductTypeResource.class);
        
    @Inject
    private ProductTypeRepository productTypeRepository;
    
    @Inject
    private ProductTypeSearchRepository productTypeSearchRepository;
    
    /**
     * POST  /productTypes -> Create a new productType.
     */
    @RequestMapping(value = "/productTypes",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductType> createProductType(@RequestBody ProductType productType) throws URISyntaxException {
        log.debug("REST request to save ProductType : {}", productType);
        if (productType.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("productType", "idexists", "A new productType cannot already have an ID")).body(null);
        }
        ProductType result = productTypeRepository.save(productType);
        productTypeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/productTypes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("productType", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /productTypes -> Updates an existing productType.
     */
    @RequestMapping(value = "/productTypes",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductType> updateProductType(@RequestBody ProductType productType) throws URISyntaxException {
        log.debug("REST request to update ProductType : {}", productType);
        if (productType.getId() == null) {
            return createProductType(productType);
        }
        ProductType result = productTypeRepository.save(productType);
        productTypeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("productType", productType.getId().toString()))
            .body(result);
    }

    /**
     * GET  /productTypes -> get all the productTypes.
     */
    @RequestMapping(value = "/productTypes",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductType> getAllProductTypes() {
        log.debug("REST request to get all ProductTypes");
        return productTypeRepository.findAll();
            }

    /**
     * GET  /productTypes/:id -> get the "id" productType.
     */
    @RequestMapping(value = "/productTypes/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<ProductType> getProductType(@PathVariable Long id) {
        log.debug("REST request to get ProductType : {}", id);
        ProductType productType = productTypeRepository.findOne(id);
        return Optional.ofNullable(productType)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /productTypes/:id -> delete the "id" productType.
     */
    @RequestMapping(value = "/productTypes/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteProductType(@PathVariable Long id) {
        log.debug("REST request to delete ProductType : {}", id);
        productTypeRepository.delete(id);
        productTypeSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("productType", id.toString())).build();
    }

    /**
     * SEARCH  /_search/productTypes/:query -> search for the productType corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/productTypes/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<ProductType> searchProductTypes(@PathVariable String query) {
        log.debug("REST request to search ProductTypes for query {}", query);
        return StreamSupport
            .stream(productTypeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
