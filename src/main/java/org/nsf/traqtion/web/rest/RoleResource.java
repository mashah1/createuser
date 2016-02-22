package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Role;
import org.nsf.traqtion.repository.RoleRepository;
import org.nsf.traqtion.repository.search.RoleSearchRepository;
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
 * REST controller for managing Role.
 */
@RestController
@RequestMapping("/api")
public class RoleResource {

    private final Logger log = LoggerFactory.getLogger(RoleResource.class);
        
    @Inject
    private RoleRepository roleRepository;
    
    @Inject
    private RoleSearchRepository roleSearchRepository;
    
    /**
     * POST  /roles -> Create a new role.
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> createRole(@RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to save Role : {}", role);
        if (role.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("role", "idexists", "A new role cannot already have an ID")).body(null);
        }
        Role result = roleRepository.save(role);
        roleSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/roles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("role", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /roles -> Updates an existing role.
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> updateRole(@RequestBody Role role) throws URISyntaxException {
        log.debug("REST request to update Role : {}", role);
        if (role.getId() == null) {
            return createRole(role);
        }
        Role result = roleRepository.save(role);
        roleSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("role", role.getId().toString()))
            .body(result);
    }

    /**
     * GET  /roles -> get all the roles.
     */
    @RequestMapping(value = "/roles",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Role> getAllRoles() {
        log.debug("REST request to get all Roles");
        return roleRepository.findAll();
            }

    /**
     * GET  /roles/:id -> get the "id" role.
     */
    @RequestMapping(value = "/roles/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Role> getRole(@PathVariable Long id) {
        log.debug("REST request to get Role : {}", id);
        Role role = roleRepository.findOne(id);
        return Optional.ofNullable(role)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /roles/:id -> delete the "id" role.
     */
    @RequestMapping(value = "/roles/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRole(@PathVariable Long id) {
        log.debug("REST request to delete Role : {}", id);
        roleRepository.delete(id);
        roleSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("role", id.toString())).build();
    }

    /**
     * SEARCH  /_search/roles/:query -> search for the role corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/roles/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Role> searchRoles(@PathVariable String query) {
        log.debug("REST request to search Roles for query {}", query);
        return StreamSupport
            .stream(roleSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
