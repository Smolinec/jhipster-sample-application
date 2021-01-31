package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.DeviceProfile;
import com.mycompany.myapp.repository.DeviceProfileRepository;
import com.mycompany.myapp.repository.search.DeviceProfileSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.DeviceProfile}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class DeviceProfileResource {

    private final Logger log = LoggerFactory.getLogger(DeviceProfileResource.class);

    private static final String ENTITY_NAME = "deviceProfile";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DeviceProfileRepository deviceProfileRepository;

    private final DeviceProfileSearchRepository deviceProfileSearchRepository;

    public DeviceProfileResource(DeviceProfileRepository deviceProfileRepository, DeviceProfileSearchRepository deviceProfileSearchRepository) {
        this.deviceProfileRepository = deviceProfileRepository;
        this.deviceProfileSearchRepository = deviceProfileSearchRepository;
    }

    /**
     * {@code POST  /device-profiles} : Create a new deviceProfile.
     *
     * @param deviceProfile the deviceProfile to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new deviceProfile, or with status {@code 400 (Bad Request)} if the deviceProfile has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/device-profiles")
    public ResponseEntity<DeviceProfile> createDeviceProfile(@RequestBody DeviceProfile deviceProfile) throws URISyntaxException {
        log.debug("REST request to save DeviceProfile : {}", deviceProfile);
        if (deviceProfile.getId() != null) {
            throw new BadRequestAlertException("A new deviceProfile cannot already have an ID", ENTITY_NAME, "idexists");
        }
        DeviceProfile result = deviceProfileRepository.save(deviceProfile);
        deviceProfileSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/device-profiles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /device-profiles} : Updates an existing deviceProfile.
     *
     * @param deviceProfile the deviceProfile to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated deviceProfile,
     * or with status {@code 400 (Bad Request)} if the deviceProfile is not valid,
     * or with status {@code 500 (Internal Server Error)} if the deviceProfile couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/device-profiles")
    public ResponseEntity<DeviceProfile> updateDeviceProfile(@RequestBody DeviceProfile deviceProfile) throws URISyntaxException {
        log.debug("REST request to update DeviceProfile : {}", deviceProfile);
        if (deviceProfile.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        DeviceProfile result = deviceProfileRepository.save(deviceProfile);
        deviceProfileSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, deviceProfile.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /device-profiles} : get all the deviceProfiles.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of deviceProfiles in body.
     */
    @GetMapping("/device-profiles")
    public List<DeviceProfile> getAllDeviceProfiles(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all DeviceProfiles");
        return deviceProfileRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /device-profiles/:id} : get the "id" deviceProfile.
     *
     * @param id the id of the deviceProfile to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the deviceProfile, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/device-profiles/{id}")
    public ResponseEntity<DeviceProfile> getDeviceProfile(@PathVariable Long id) {
        log.debug("REST request to get DeviceProfile : {}", id);
        Optional<DeviceProfile> deviceProfile = deviceProfileRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(deviceProfile);
    }

    /**
     * {@code DELETE  /device-profiles/:id} : delete the "id" deviceProfile.
     *
     * @param id the id of the deviceProfile to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/device-profiles/{id}")
    public ResponseEntity<Void> deleteDeviceProfile(@PathVariable Long id) {
        log.debug("REST request to delete DeviceProfile : {}", id);
        deviceProfileRepository.deleteById(id);
        deviceProfileSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/device-profiles?query=:query} : search for the deviceProfile corresponding
     * to the query.
     *
     * @param query the query of the deviceProfile search.
     * @return the result of the search.
     */
    @GetMapping("/_search/device-profiles")
    public List<DeviceProfile> searchDeviceProfiles(@RequestParam String query) {
        log.debug("REST request to search DeviceProfiles for query {}", query);
        return StreamSupport
            .stream(deviceProfileSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
