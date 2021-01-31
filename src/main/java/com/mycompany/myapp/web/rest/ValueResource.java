package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Value;
import com.mycompany.myapp.repository.ValueRepository;
import com.mycompany.myapp.repository.search.ValueSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Value}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ValueResource {

    private final Logger log = LoggerFactory.getLogger(ValueResource.class);

    private static final String ENTITY_NAME = "value";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ValueRepository valueRepository;

    private final ValueSearchRepository valueSearchRepository;

    public ValueResource(ValueRepository valueRepository, ValueSearchRepository valueSearchRepository) {
        this.valueRepository = valueRepository;
        this.valueSearchRepository = valueSearchRepository;
    }

    /**
     * {@code POST  /values} : Create a new value.
     *
     * @param value the value to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new value, or with status {@code 400 (Bad Request)} if the value has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/values")
    public ResponseEntity<Value> createValue(@RequestBody Value value) throws URISyntaxException {
        log.debug("REST request to save Value : {}", value);
        if (value.getId() != null) {
            throw new BadRequestAlertException("A new value cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Value result = valueRepository.save(value);
        valueSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/values/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /values} : Updates an existing value.
     *
     * @param value the value to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated value,
     * or with status {@code 400 (Bad Request)} if the value is not valid,
     * or with status {@code 500 (Internal Server Error)} if the value couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/values")
    public ResponseEntity<Value> updateValue(@RequestBody Value value) throws URISyntaxException {
        log.debug("REST request to update Value : {}", value);
        if (value.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Value result = valueRepository.save(value);
        valueSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, value.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /values} : get all the values.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of values in body.
     */
    @GetMapping("/values")
    public List<Value> getAllValues(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Values");
        return valueRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /values/:id} : get the "id" value.
     *
     * @param id the id of the value to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the value, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/values/{id}")
    public ResponseEntity<Value> getValue(@PathVariable Long id) {
        log.debug("REST request to get Value : {}", id);
        Optional<Value> value = valueRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(value);
    }

    /**
     * {@code DELETE  /values/:id} : delete the "id" value.
     *
     * @param id the id of the value to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/values/{id}")
    public ResponseEntity<Void> deleteValue(@PathVariable Long id) {
        log.debug("REST request to delete Value : {}", id);
        valueRepository.deleteById(id);
        valueSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/values?query=:query} : search for the value corresponding
     * to the query.
     *
     * @param query the query of the value search.
     * @return the result of the search.
     */
    @GetMapping("/_search/values")
    public List<Value> searchValues(@RequestParam String query) {
        log.debug("REST request to search Values for query {}", query);
        return StreamSupport
            .stream(valueSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
