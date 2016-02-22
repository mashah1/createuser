package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.RolePermission;
import org.nsf.traqtion.repository.RolePermissionRepository;
import org.nsf.traqtion.repository.search.RolePermissionSearchRepository;
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
 * REST controller for managing RolePermission.
 */
@RestController
@RequestMapping("/api")
public class RolePermissionResource {

    private final Logger log = LoggerFactory.getLogger(RolePermissionResource.class);
        
    @Inject
    private RolePermissionRepository rolePermissionRepository;
    
    @Inject
    private RolePermissionSearchRepository rolePermissionSearchRepository;
    
    /**
     * POST  /rolePermissions -> Create a new rolePermission.
     */
    @RequestMapping(value = "/rolePermissions",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RolePermission> createRolePermission(@RequestBody RolePermission rolePermission) throws URISyntaxException {
        log.debug("REST request to save RolePermission : {}", rolePermission);
        if (rolePermission.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("rolePermission", "idexists", "A new rolePermission cannot already have an ID")).body(null);
        }
        RolePermission result = rolePermissionRepository.save(rolePermission);
        rolePermissionSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/rolePermissions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("rolePermission", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /rolePermissions -> Updates an existing rolePermission.
     */
    @RequestMapping(value = "/rolePermissions",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RolePermission> updateRolePermission(@RequestBody RolePermission rolePermission) throws URISyntaxException {
        log.debug("REST request to update RolePermission : {}", rolePermission);
        if (rolePermission.getId() == null) {
            return createRolePermission(rolePermission);
        }
        RolePermission result = rolePermissionRepository.save(rolePermission);
        rolePermissionSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("rolePermission", rolePermission.getId().toString()))
            .body(result);
    }

    /**
     * GET  /rolePermissions -> get all the rolePermissions.
     */
    @RequestMapping(value = "/rolePermissions",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RolePermission> getAllRolePermissions() {
        log.debug("REST request to get all RolePermissions");
        return rolePermissionRepository.findAll();
            }

    /**
     * GET  /rolePermissions/:id -> get the "id" rolePermission.
     */
    @RequestMapping(value = "/rolePermissions/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<RolePermission> getRolePermission(@PathVariable Long id) {
        log.debug("REST request to get RolePermission : {}", id);
        RolePermission rolePermission = rolePermissionRepository.findOne(id);
        return Optional.ofNullable(rolePermission)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /rolePermissions/:id -> delete the "id" rolePermission.
     */
    @RequestMapping(value = "/rolePermissions/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteRolePermission(@PathVariable Long id) {
        log.debug("REST request to delete RolePermission : {}", id);
        rolePermissionRepository.delete(id);
        rolePermissionSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("rolePermission", id.toString())).build();
    }

    /**
     * SEARCH  /_search/rolePermissions/:query -> search for the rolePermission corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/rolePermissions/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<RolePermission> searchRolePermissions(@PathVariable String query) {
        log.debug("REST request to search RolePermissions for query {}", query);
        return StreamSupport
            .stream(rolePermissionSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
