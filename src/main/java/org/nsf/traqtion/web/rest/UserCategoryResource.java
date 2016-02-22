package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.UserCategory;
import org.nsf.traqtion.repository.UserCategoryRepository;
import org.nsf.traqtion.repository.search.UserCategorySearchRepository;
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
 * REST controller for managing UserCategory.
 */
@RestController
@RequestMapping("/api")
public class UserCategoryResource {

    private final Logger log = LoggerFactory.getLogger(UserCategoryResource.class);
        
    @Inject
    private UserCategoryRepository userCategoryRepository;
    
    @Inject
    private UserCategorySearchRepository userCategorySearchRepository;
    
    /**
     * POST  /userCategorys -> Create a new userCategory.
     */
    @RequestMapping(value = "/userCategorys",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCategory> createUserCategory(@RequestBody UserCategory userCategory) throws URISyntaxException {
        log.debug("REST request to save UserCategory : {}", userCategory);
        if (userCategory.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userCategory", "idexists", "A new userCategory cannot already have an ID")).body(null);
        }
        UserCategory result = userCategoryRepository.save(userCategory);
        userCategorySearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userCategorys/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userCategory", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userCategorys -> Updates an existing userCategory.
     */
    @RequestMapping(value = "/userCategorys",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCategory> updateUserCategory(@RequestBody UserCategory userCategory) throws URISyntaxException {
        log.debug("REST request to update UserCategory : {}", userCategory);
        if (userCategory.getId() == null) {
            return createUserCategory(userCategory);
        }
        UserCategory result = userCategoryRepository.save(userCategory);
        userCategorySearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userCategory", userCategory.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userCategorys -> get all the userCategorys.
     */
    @RequestMapping(value = "/userCategorys",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserCategory> getAllUserCategorys() {
        log.debug("REST request to get all UserCategorys");
        return userCategoryRepository.findAll();
            }

    /**
     * GET  /userCategorys/:id -> get the "id" userCategory.
     */
    @RequestMapping(value = "/userCategorys/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserCategory> getUserCategory(@PathVariable Long id) {
        log.debug("REST request to get UserCategory : {}", id);
        UserCategory userCategory = userCategoryRepository.findOne(id);
        return Optional.ofNullable(userCategory)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userCategorys/:id -> delete the "id" userCategory.
     */
    @RequestMapping(value = "/userCategorys/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserCategory(@PathVariable Long id) {
        log.debug("REST request to delete UserCategory : {}", id);
        userCategoryRepository.delete(id);
        userCategorySearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userCategory", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userCategorys/:query -> search for the userCategory corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userCategorys/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserCategory> searchUserCategorys(@PathVariable String query) {
        log.debug("REST request to search UserCategorys for query {}", query);
        return StreamSupport
            .stream(userCategorySearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
