package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.WebUser;
import com.mycompany.myapp.repository.WebUserRepository;
import com.mycompany.myapp.repository.search.WebUserSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.WebUser}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WebUserResource {

    private final Logger log = LoggerFactory.getLogger(WebUserResource.class);

    private static final String ENTITY_NAME = "webUser";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WebUserRepository webUserRepository;

    private final WebUserSearchRepository webUserSearchRepository;

    public WebUserResource(WebUserRepository webUserRepository, WebUserSearchRepository webUserSearchRepository) {
        this.webUserRepository = webUserRepository;
        this.webUserSearchRepository = webUserSearchRepository;
    }

    /**
     * {@code POST  /web-users} : Create a new webUser.
     *
     * @param webUser the webUser to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new webUser, or with status {@code 400 (Bad Request)} if the webUser has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/web-users")
    public ResponseEntity<WebUser> createWebUser(@RequestBody WebUser webUser) throws URISyntaxException {
        log.debug("REST request to save WebUser : {}", webUser);
        if (webUser.getId() != null) {
            throw new BadRequestAlertException("A new webUser cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WebUser result = webUserRepository.save(webUser);
        webUserSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/web-users/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /web-users} : Updates an existing webUser.
     *
     * @param webUser the webUser to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated webUser,
     * or with status {@code 400 (Bad Request)} if the webUser is not valid,
     * or with status {@code 500 (Internal Server Error)} if the webUser couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/web-users")
    public ResponseEntity<WebUser> updateWebUser(@RequestBody WebUser webUser) throws URISyntaxException {
        log.debug("REST request to update WebUser : {}", webUser);
        if (webUser.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        WebUser result = webUserRepository.save(webUser);
        webUserSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, webUser.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /web-users} : get all the webUsers.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of webUsers in body.
     */
    @GetMapping("/web-users")
    public List<WebUser> getAllWebUsers(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all WebUsers");
        return webUserRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /web-users/:id} : get the "id" webUser.
     *
     * @param id the id of the webUser to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the webUser, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/web-users/{id}")
    public ResponseEntity<WebUser> getWebUser(@PathVariable Long id) {
        log.debug("REST request to get WebUser : {}", id);
        Optional<WebUser> webUser = webUserRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(webUser);
    }

    /**
     * {@code DELETE  /web-users/:id} : delete the "id" webUser.
     *
     * @param id the id of the webUser to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/web-users/{id}")
    public ResponseEntity<Void> deleteWebUser(@PathVariable Long id) {
        log.debug("REST request to delete WebUser : {}", id);
        webUserRepository.deleteById(id);
        webUserSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/web-users?query=:query} : search for the webUser corresponding
     * to the query.
     *
     * @param query the query of the webUser search.
     * @return the result of the search.
     */
    @GetMapping("/_search/web-users")
    public List<WebUser> searchWebUsers(@RequestParam String query) {
        log.debug("REST request to search WebUsers for query {}", query);
        return StreamSupport
            .stream(webUserSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
