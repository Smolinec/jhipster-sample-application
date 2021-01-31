package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Temperature;
import com.mycompany.myapp.repository.TemperatureRepository;
import com.mycompany.myapp.repository.search.TemperatureSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Temperature}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TemperatureResource {

    private final Logger log = LoggerFactory.getLogger(TemperatureResource.class);

    private static final String ENTITY_NAME = "temperature";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TemperatureRepository temperatureRepository;

    private final TemperatureSearchRepository temperatureSearchRepository;

    public TemperatureResource(TemperatureRepository temperatureRepository, TemperatureSearchRepository temperatureSearchRepository) {
        this.temperatureRepository = temperatureRepository;
        this.temperatureSearchRepository = temperatureSearchRepository;
    }

    /**
     * {@code POST  /temperatures} : Create a new temperature.
     *
     * @param temperature the temperature to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new temperature, or with status {@code 400 (Bad Request)} if the temperature has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/temperatures")
    public ResponseEntity<Temperature> createTemperature(@RequestBody Temperature temperature) throws URISyntaxException {
        log.debug("REST request to save Temperature : {}", temperature);
        if (temperature.getId() != null) {
            throw new BadRequestAlertException("A new temperature cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Temperature result = temperatureRepository.save(temperature);
        temperatureSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/temperatures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /temperatures} : Updates an existing temperature.
     *
     * @param temperature the temperature to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated temperature,
     * or with status {@code 400 (Bad Request)} if the temperature is not valid,
     * or with status {@code 500 (Internal Server Error)} if the temperature couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/temperatures")
    public ResponseEntity<Temperature> updateTemperature(@RequestBody Temperature temperature) throws URISyntaxException {
        log.debug("REST request to update Temperature : {}", temperature);
        if (temperature.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Temperature result = temperatureRepository.save(temperature);
        temperatureSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, temperature.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /temperatures} : get all the temperatures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of temperatures in body.
     */
    @GetMapping("/temperatures")
    public List<Temperature> getAllTemperatures() {
        log.debug("REST request to get all Temperatures");
        return temperatureRepository.findAll();
    }

    /**
     * {@code GET  /temperatures/:id} : get the "id" temperature.
     *
     * @param id the id of the temperature to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the temperature, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/temperatures/{id}")
    public ResponseEntity<Temperature> getTemperature(@PathVariable Long id) {
        log.debug("REST request to get Temperature : {}", id);
        Optional<Temperature> temperature = temperatureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(temperature);
    }

    /**
     * {@code DELETE  /temperatures/:id} : delete the "id" temperature.
     *
     * @param id the id of the temperature to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/temperatures/{id}")
    public ResponseEntity<Void> deleteTemperature(@PathVariable Long id) {
        log.debug("REST request to delete Temperature : {}", id);
        temperatureRepository.deleteById(id);
        temperatureSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/temperatures?query=:query} : search for the temperature corresponding
     * to the query.
     *
     * @param query the query of the temperature search.
     * @return the result of the search.
     */
    @GetMapping("/_search/temperatures")
    public List<Temperature> searchTemperatures(@RequestParam String query) {
        log.debug("REST request to search Temperatures for query {}", query);
        return StreamSupport
            .stream(temperatureSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
