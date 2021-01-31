package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Place;
import com.mycompany.myapp.repository.PlaceRepository;
import com.mycompany.myapp.repository.search.PlaceSearchRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.Place}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PlaceResource {

    private final Logger log = LoggerFactory.getLogger(PlaceResource.class);

    private static final String ENTITY_NAME = "place";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PlaceRepository placeRepository;

    private final PlaceSearchRepository placeSearchRepository;

    public PlaceResource(PlaceRepository placeRepository, PlaceSearchRepository placeSearchRepository) {
        this.placeRepository = placeRepository;
        this.placeSearchRepository = placeSearchRepository;
    }

    /**
     * {@code POST  /places} : Create a new place.
     *
     * @param place the place to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new place, or with status {@code 400 (Bad Request)} if the place has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/places")
    public ResponseEntity<Place> createPlace(@RequestBody Place place) throws URISyntaxException {
        log.debug("REST request to save Place : {}", place);
        if (place.getId() != null) {
            throw new BadRequestAlertException("A new place cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Place result = placeRepository.save(place);
        placeSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/places/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /places} : Updates an existing place.
     *
     * @param place the place to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated place,
     * or with status {@code 400 (Bad Request)} if the place is not valid,
     * or with status {@code 500 (Internal Server Error)} if the place couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/places")
    public ResponseEntity<Place> updatePlace(@RequestBody Place place) throws URISyntaxException {
        log.debug("REST request to update Place : {}", place);
        if (place.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Place result = placeRepository.save(place);
        placeSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, place.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /places} : get all the places.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of places in body.
     */
    @GetMapping("/places")
    public List<Place> getAllPlaces(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all Places");
        return placeRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /places/:id} : get the "id" place.
     *
     * @param id the id of the place to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the place, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/places/{id}")
    public ResponseEntity<Place> getPlace(@PathVariable Long id) {
        log.debug("REST request to get Place : {}", id);
        Optional<Place> place = placeRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(place);
    }

    /**
     * {@code DELETE  /places/:id} : delete the "id" place.
     *
     * @param id the id of the place to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/places/{id}")
    public ResponseEntity<Void> deletePlace(@PathVariable Long id) {
        log.debug("REST request to delete Place : {}", id);
        placeRepository.deleteById(id);
        placeSearchRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }

    /**
     * {@code SEARCH  /_search/places?query=:query} : search for the place corresponding
     * to the query.
     *
     * @param query the query of the place search.
     * @return the result of the search.
     */
    @GetMapping("/_search/places")
    public List<Place> searchPlaces(@RequestParam String query) {
        log.debug("REST request to search Places for query {}", query);
        return StreamSupport
            .stream(placeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
        .collect(Collectors.toList());
    }
}
