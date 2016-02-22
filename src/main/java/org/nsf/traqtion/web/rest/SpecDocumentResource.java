package org.nsf.traqtion.web.rest;

import com.codahale.metrics.annotation.Timed;
import org.nsf.traqtion.domain.SpecDocument;
import org.nsf.traqtion.repository.SpecDocumentRepository;
import org.nsf.traqtion.repository.search.SpecDocumentSearchRepository;
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
 * REST controller for managing SpecDocument.
 */
@RestController
@RequestMapping("/api")
public class SpecDocumentResource {

    private final Logger log = LoggerFactory.getLogger(SpecDocumentResource.class);
        
    @Inject
    private SpecDocumentRepository specDocumentRepository;
    
    @Inject
    private SpecDocumentSearchRepository specDocumentSearchRepository;
    
    /**
     * POST  /specDocuments -> Create a new specDocument.
     */
    @RequestMapping(value = "/specDocuments",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecDocument> createSpecDocument(@RequestBody SpecDocument specDocument) throws URISyntaxException {
        log.debug("REST request to save SpecDocument : {}", specDocument);
        if (specDocument.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("specDocument", "idexists", "A new specDocument cannot already have an ID")).body(null);
        }
        SpecDocument result = specDocumentRepository.save(specDocument);
        specDocumentSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/specDocuments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("specDocument", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /specDocuments -> Updates an existing specDocument.
     */
    @RequestMapping(value = "/specDocuments",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecDocument> updateSpecDocument(@RequestBody SpecDocument specDocument) throws URISyntaxException {
        log.debug("REST request to update SpecDocument : {}", specDocument);
        if (specDocument.getId() == null) {
            return createSpecDocument(specDocument);
        }
        SpecDocument result = specDocumentRepository.save(specDocument);
        specDocumentSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("specDocument", specDocument.getId().toString()))
            .body(result);
    }

    /**
     * GET  /specDocuments -> get all the specDocuments.
     */
    @RequestMapping(value = "/specDocuments",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpecDocument> getAllSpecDocuments() {
        log.debug("REST request to get all SpecDocuments");
        return specDocumentRepository.findAll();
            }

    /**
     * GET  /specDocuments/:id -> get the "id" specDocument.
     */
    @RequestMapping(value = "/specDocuments/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<SpecDocument> getSpecDocument(@PathVariable Long id) {
        log.debug("REST request to get SpecDocument : {}", id);
        SpecDocument specDocument = specDocumentRepository.findOne(id);
        return Optional.ofNullable(specDocument)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /specDocuments/:id -> delete the "id" specDocument.
     */
    @RequestMapping(value = "/specDocuments/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteSpecDocument(@PathVariable Long id) {
        log.debug("REST request to delete SpecDocument : {}", id);
        specDocumentRepository.delete(id);
        specDocumentSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("specDocument", id.toString())).build();
    }

    /**
     * SEARCH  /_search/specDocuments/:query -> search for the specDocument corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/specDocuments/{query}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<SpecDocument> searchSpecDocuments(@PathVariable String query) {
        log.debug("REST request to search SpecDocuments for query {}", query);
        return StreamSupport
            .stream(specDocumentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
