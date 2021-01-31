package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Values;
import com.mycompany.myapp.repository.ValuesRepository;
import com.mycompany.myapp.repository.search.ValuesSearchRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Values}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ValuesResource {

    private final Logger log = LoggerFactory.getLogger(ValuesResource.class);

    private static final String ENTITY_NAME = "values";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValuesRepository valuesRepository;

    private final ValuesSearchRepository valuesSearchRepository;

    public ValuesResource(ValuesRepository valuesRepository, ValuesSearchRepository valuesSearchRepository) {
        this.valuesRepository = valuesRepository;
        this.valuesSearchRepository = valuesSearchRepository;
    }

    /**
     * {@code POST  /values} : Create a new values.
     *
     * @param values the values to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new values, or with status {@code 400 (Bad Request)} if the values has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/values")
    public ResponseEntity<Values> createValues(@RequestBody Values values) throws URISyntaxException {
        log.debug("REST request to save Values : {}", values);
        if (values.getId() != null) {
            throw new BadRequestAlertException("A new values cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Values result = valuesRepository.save(values);
        valuesSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /values} : Updates an existing values.
     *
     * @param values the values to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated values,
     * or with status {@code 400 (Bad Request)} if the values is not valid,
     * or with status {@code 500 (Internal Server Error)} if the values couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/values")
    public ResponseEntity<Values> updateValues(@RequestBody Values values) throws URISyntaxException {
        log.debug("REST request to update Values : {}", values);
        if (values.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Values result = valuesRepository.save(values);
        valuesSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, values.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /values} : get all the values.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of values in body.
     */
    @GetMapping("/values")
    public List<Values> getAllValues(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Values");
        return valuesRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /values/:id} : get the "id" values.
     *
     * @param id the id of the values to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the values, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/values/{id}")
    public ResponseEntity<Values> getValues(@PathVariable Long id) {
        log.debug("REST request to get Values : {}", id);
        Optional<Values> values = valuesRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(values);
    }

    /**
     * {@code DELETE  /values/:id} : delete the "id" values.
     *
     * @param id the id of the values to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/values/{id}")
    public ResponseEntity<Void> deleteValues(@PathVariable Long id) {
        log.debug("REST request to delete Values : {}", id);
        valuesRepository.deleteById(id);
        valuesSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/values?query=:query} : search for the values corresponding
     * to the query.
     *
     * @param query the query of the values search.
     * @return the result of the search.
     */
    @GetMapping("/_search/values")
    public List<Values> searchValues(@RequestParam String query) {
        log.debug("REST request to search Values for query {}", query);
        return StreamSupport
            .stream(valuesSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
