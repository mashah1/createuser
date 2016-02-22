package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.UserBusinessRole;
import org.nsf.traqtion.repository.UserBusinessRoleRepository;
import org.nsf.traqtion.repository.search.UserBusinessRoleSearchRepository;
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
 * REST controller for managing UserBusinessRole.
 */
@RestController
@RequestMapping("/api")
public class UserBusinessRoleResource {

    private final Logger log = LoggerFactory.getLogger(UserBusinessRoleResource.class);
        
    @Inject
    private UserBusinessRoleRepository userBusinessRoleRepository;
    
    @Inject
    private UserBusinessRoleSearchRepository userBusinessRoleSearchRepository;
    
    /**
     * POST  /userBusinessRoles -> Create a new userBusinessRole.
     */
    @RequestMapping(value = "/userBusinessRoles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserBusinessRole> createUserBusinessRole(@RequestBody UserBusinessRole userBusinessRole) throws URISyntaxException {
        log.debug("REST request to save UserBusinessRole : {}", userBusinessRole);
        if (userBusinessRole.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("userBusinessRole", "idexists", "A new userBusinessRole cannot already have an ID")).body(null);
        }
        UserBusinessRole result = userBusinessRoleRepository.save(userBusinessRole);
        userBusinessRoleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/userBusinessRoles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("userBusinessRole", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /userBusinessRoles -> Updates an existing userBusinessRole.
     */
    @RequestMapping(value = "/userBusinessRoles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserBusinessRole> updateUserBusinessRole(@RequestBody UserBusinessRole userBusinessRole) throws URISyntaxException {
        log.debug("REST request to update UserBusinessRole : {}", userBusinessRole);
        if (userBusinessRole.getId() == null) {
            return createUserBusinessRole(userBusinessRole);
        }
        UserBusinessRole result = userBusinessRoleRepository.save(userBusinessRole);
        userBusinessRoleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("userBusinessRole", userBusinessRole.getId().toString()))
            .body(result);
    }

    /**
     * GET  /userBusinessRoles -> get all the userBusinessRoles.
     */
    @RequestMapping(value = "/userBusinessRoles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserBusinessRole> getAllUserBusinessRoles() {
        log.debug("REST request to get all UserBusinessRoles");
        return userBusinessRoleRepository.findAll();
            }

    /**
     * GET  /userBusinessRoles/:id -> get the "id" userBusinessRole.
     */
    @RequestMapping(value = "/userBusinessRoles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<UserBusinessRole> getUserBusinessRole(@PathVariable Long id) {
        log.debug("REST request to get UserBusinessRole : {}", id);
        UserBusinessRole userBusinessRole = userBusinessRoleRepository.findOne(id);
        return Optional.ofNullable(userBusinessRole)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /userBusinessRoles/:id -> delete the "id" userBusinessRole.
     */
    @RequestMapping(value = "/userBusinessRoles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteUserBusinessRole(@PathVariable Long id) {
        log.debug("REST request to delete UserBusinessRole : {}", id);
        userBusinessRoleRepository.delete(id);
        userBusinessRoleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("userBusinessRole", id.toString())).build();
    }

    /**
     * SEARCH  /_search/userBusinessRoles/:query -> search for the userBusinessRole corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/userBusinessRoles/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<UserBusinessRole> searchUserBusinessRoles(@PathVariable String query) {
        log.debug("REST request to search UserBusinessRoles for query {}", query);
        return StreamSupport
            .stream(userBusinessRoleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
