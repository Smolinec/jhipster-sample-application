package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SMSNotification;
import com.mycompany.myapp.repository.SMSNotificationRepository;
import com.mycompany.myapp.repository.search.SMSNotificationSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SMSNotification}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SMSNotificationResource {

    private final Logger log = LoggerFactory.getLogger(SMSNotificationResource.class);

    private static final String ENTITY_NAME = "sMSNotification";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SMSNotificationRepository sMSNotificationRepository;

    private final SMSNotificationSearchRepository sMSNotificationSearchRepository;

    public SMSNotificationResource(SMSNotificationRepository sMSNotificationRepository, SMSNotificationSearchRepository sMSNotificationSearchRepository) {
        this.sMSNotificationRepository = sMSNotificationRepository;
        this.sMSNotificationSearchRepository = sMSNotificationSearchRepository;
    }

    /**
     * {@code POST  /sms-notifications} : Create a new sMSNotification.
     *
     * @param sMSNotification the sMSNotification to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new sMSNotification, or with status {@code 400 (Bad Request)} if the sMSNotification has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/sms-notifications")
    public ResponseEntity<SMSNotification> createSMSNotification(@RequestBody SMSNotification sMSNotification) throws URISyntaxException {
        log.debug("REST request to save SMSNotification : {}", sMSNotification);
        if (sMSNotification.getId() != null) {
            throw new BadRequestAlertException("A new sMSNotification cannot already have an ID", ENTITY_NAME, "idexists");
        }
        SMSNotification result = sMSNotificationRepository.save(sMSNotification);
        sMSNotificationSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/sms-notifications/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /sms-notifications} : Updates an existing sMSNotification.
     *
     * @param sMSNotification the sMSNotification to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated sMSNotification,
     * or with status {@code 400 (Bad Request)} if the sMSNotification is not valid,
     * or with status {@code 500 (Internal Server Error)} if the sMSNotification couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/sms-notifications")
    public ResponseEntity<SMSNotification> updateSMSNotification(@RequestBody SMSNotification sMSNotification) throws URISyntaxException {
        log.debug("REST request to update SMSNotification : {}", sMSNotification);
        if (sMSNotification.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        SMSNotification result = sMSNotificationRepository.save(sMSNotification);
        sMSNotificationSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, sMSNotification.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /sms-notifications} : get all the sMSNotifications.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of sMSNotifications in body.
     */
    @GetMapping("/sms-notifications")
    public List<SMSNotification> getAllSMSNotifications() {
        log.debug("REST request to get all SMSNotifications");
        return sMSNotificationRepository.findAll();
    }

    /**
     * {@code GET  /sms-notifications/:id} : get the "id" sMSNotification.
     *
     * @param id the id of the sMSNotification to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the sMSNotification, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/sms-notifications/{id}")
    public ResponseEntity<SMSNotification> getSMSNotification(@PathVariable Long id) {
        log.debug("REST request to get SMSNotification : {}", id);
        Optional<SMSNotification> sMSNotification = sMSNotificationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(sMSNotification);
    }

    /**
     * {@code DELETE  /sms-notifications/:id} : delete the "id" sMSNotification.
     *
     * @param id the id of the sMSNotification to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/sms-notifications/{id}")
    public ResponseEntity<Void> deleteSMSNotification(@PathVariable Long id) {
        log.debug("REST request to delete SMSNotification : {}", id);
        sMSNotificationRepository.deleteById(id);
        sMSNotificationSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/sms-notifications?query=:query} : search for the sMSNotification corresponding
     * to the query.
     *
     * @param query the query of the sMSNotification search.
     * @return the result of the search.
     */
    @GetMapping("/_search/sms-notifications")
    public List<SMSNotification> searchSMSNotifications(@RequestParam String query) {
        log.debug("REST request to search SMSNotifications for query {}", query);
        return StreamSupport
            .stream(sMSNotificationSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
