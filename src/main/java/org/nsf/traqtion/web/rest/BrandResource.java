package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Brand;
import org.nsf.traqtion.repository.BrandRepository;
import org.nsf.traqtion.repository.search.BrandSearchRepository;
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
 * REST controller for managing Brand.
 */
@RestController
@RequestMapping("/api")
public class BrandResource {

    private final Logger log = LoggerFactory.getLogger(BrandResource.class);
        
    @Inject
    private BrandRepository brandRepository;
    
    @Inject
    private BrandSearchRepository brandSearchRepository;
    
    /**
     * POST  /brands -> Create a new brand.
     */
    @RequestMapping(value = "/brands",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brand> createBrand(@RequestBody Brand brand) throws URISyntaxException {
        log.debug("REST request to save Brand : {}", brand);
        if (brand.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("brand", "idexists", "A new brand cannot already have an ID")).body(null);
        }
        Brand result = brandRepository.save(brand);
        brandSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/brands/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("brand", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /brands -> Updates an existing brand.
     */
    @RequestMapping(value = "/brands",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brand> updateBrand(@RequestBody Brand brand) throws URISyntaxException {
        log.debug("REST request to update Brand : {}", brand);
        if (brand.getId() == null) {
            return createBrand(brand);
        }
        Brand result = brandRepository.save(brand);
        brandSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("brand", brand.getId().toString()))
            .body(result);
    }

    /**
     * GET  /brands -> get all the brands.
     */
    @RequestMapping(value = "/brands",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Brand> getAllBrands() {
        log.debug("REST request to get all Brands");
        return brandRepository.findAll();
            }

    /**
     * GET  /brands/:id -> get the "id" brand.
     */
    @RequestMapping(value = "/brands/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Brand> getBrand(@PathVariable Long id) {
        log.debug("REST request to get Brand : {}", id);
        Brand brand = brandRepository.findOne(id);
        return Optional.ofNullable(brand)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /brands/:id -> delete the "id" brand.
     */
    @RequestMapping(value = "/brands/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBrand(@PathVariable Long id) {
        log.debug("REST request to delete Brand : {}", id);
        brandRepository.delete(id);
        brandSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("brand", id.toString())).build();
    }

    /**
     * SEARCH  /_search/brands/:query -> search for the brand corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/brands/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Brand> searchBrands(@PathVariable String query) {
        log.debug("REST request to search Brands for query {}", query);
        return StreamSupport
            .stream(brandSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
