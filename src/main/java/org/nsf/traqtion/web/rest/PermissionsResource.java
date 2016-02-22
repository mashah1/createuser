package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.Permissions;
import org.nsf.traqtion.repository.PermissionsRepository;
import org.nsf.traqtion.repository.search.PermissionsSearchRepository;
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
 * REST controller for managing Permissions.
 */
@RestController
@RequestMapping("/api")
public class PermissionsResource {

    private final Logger log = LoggerFactory.getLogger(PermissionsResource.class);
        
    @Inject
    private PermissionsRepository permissionsRepository;
    
    @Inject
    private PermissionsSearchRepository permissionsSearchRepository;
    
    /**
     * POST  /permissionss -> Create a new permissions.
     */
    @RequestMapping(value = "/permissionss",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permissions> createPermissions(@RequestBody Permissions permissions) throws URISyntaxException {
        log.debug("REST request to save Permissions : {}", permissions);
        if (permissions.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("permissions", "idexists", "A new permissions cannot already have an ID")).body(null);
        }
        Permissions result = permissionsRepository.save(permissions);
        permissionsSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/permissionss/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("permissions", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /permissionss -> Updates an existing permissions.
     */
    @RequestMapping(value = "/permissionss",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permissions> updatePermissions(@RequestBody Permissions permissions) throws URISyntaxException {
        log.debug("REST request to update Permissions : {}", permissions);
        if (permissions.getId() == null) {
            return createPermissions(permissions);
        }
        Permissions result = permissionsRepository.save(permissions);
        permissionsSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("permissions", permissions.getId().toString()))
            .body(result);
    }

    /**
     * GET  /permissionss -> get all the permissionss.
     */
    @RequestMapping(value = "/permissionss",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Permissions> getAllPermissionss() {
        log.debug("REST request to get all Permissionss");
        return permissionsRepository.findAll();
            }

    /**
     * GET  /permissionss/:id -> get the "id" permissions.
     */
    @RequestMapping(value = "/permissionss/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Permissions> getPermissions(@PathVariable Long id) {
        log.debug("REST request to get Permissions : {}", id);
        Permissions permissions = permissionsRepository.findOne(id);
        return Optional.ofNullable(permissions)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /permissionss/:id -> delete the "id" permissions.
     */
    @RequestMapping(value = "/permissionss/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deletePermissions(@PathVariable Long id) {
        log.debug("REST request to delete Permissions : {}", id);
        permissionsRepository.delete(id);
        permissionsSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("permissions", id.toString())).build();
    }

    /**
     * SEARCH  /_search/permissionss/:query -> search for the permissions corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/permissionss/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Permissions> searchPermissionss(@PathVariable String query) {
        log.debug("REST request to search Permissionss for query {}", query);
        return StreamSupport
            .stream(permissionsSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
