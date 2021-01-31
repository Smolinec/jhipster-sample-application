package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Temperature;
import com.mycompany.myapp.repository.TemperatureRepository;
import com.mycompany.myapp.repository.search.TemperatureSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;

import static com.mycompany.myapp.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link TemperatureResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class TemperatureResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATE_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATE_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final ZonedDateTime DEFAULT_LAST_UPDATE_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_LAST_UPDATE_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private TemperatureRepository temperatureRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.TemperatureSearchRepositoryMockConfiguration
     */
    @Autowired
    private TemperatureSearchRepository mockTemperatureSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTemperatureMockMvc;

    private Temperature temperature;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperature createEntity(EntityManager em) {
        Temperature temperature = new Temperature()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .createTimestamp(DEFAULT_CREATE_TIMESTAMP)
            .lastUpdateTimestamp(DEFAULT_LAST_UPDATE_TIMESTAMP);
        return temperature;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Temperature createUpdatedEntity(EntityManager em) {
        Temperature temperature = new Temperature()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .createTimestamp(UPDATED_CREATE_TIMESTAMP)
            .lastUpdateTimestamp(UPDATED_LAST_UPDATE_TIMESTAMP);
        return temperature;
    }

    @BeforeEach
    public void initTest() {
        temperature = createEntity(em);
    }

    @Test
    @Transactional
    public void createTemperature() throws Exception {
        int databaseSizeBeforeCreate = temperatureRepository.findAll().size();
        // Create the Temperature
        restTemperatureMockMvc.perform(post("/api/temperatures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temperature)))
            .andExpect(status().isCreated());

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll();
        assertThat(temperatureList).hasSize(databaseSizeBeforeCreate + 1);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testTemperature.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testTemperature.getCreateTimestamp()).isEqualTo(DEFAULT_CREATE_TIMESTAMP);
        assertThat(testTemperature.getLastUpdateTimestamp()).isEqualTo(DEFAULT_LAST_UPDATE_TIMESTAMP);

        // Validate the Temperature in Elasticsearch
        verify(mockTemperatureSearchRepository, times(1)).save(testTemperature);
    }

    @Test
    @Transactional
    public void createTemperatureWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = temperatureRepository.findAll().size();

        // Create the Temperature with an existing ID
        temperature.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restTemperatureMockMvc.perform(post("/api/temperatures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temperature)))
            .andExpect(status().isBadRequest());

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll();
        assertThat(temperatureList).hasSize(databaseSizeBeforeCreate);

        // Validate the Temperature in Elasticsearch
        verify(mockTemperatureSearchRepository, times(0)).save(temperature);
    }


    @Test
    @Transactional
    public void getAllTemperatures() throws Exception {
        // Initialize the database
        temperatureRepository.saveAndFlush(temperature);

        // Get all the temperatureList
        restTemperatureMockMvc.perform(get("/api/temperatures?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temperature.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createTimestamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].lastUpdateTimestamp").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_TIMESTAMP))));
    }
    
    @Test
    @Transactional
    public void getTemperature() throws Exception {
        // Initialize the database
        temperatureRepository.saveAndFlush(temperature);

        // Get the temperature
        restTemperatureMockMvc.perform(get("/api/temperatures/{id}", temperature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(temperature.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.createTimestamp").value(sameInstant(DEFAULT_CREATE_TIMESTAMP)))
            .andExpect(jsonPath("$.lastUpdateTimestamp").value(sameInstant(DEFAULT_LAST_UPDATE_TIMESTAMP)));
    }
    @Test
    @Transactional
    public void getNonExistingTemperature() throws Exception {
        // Get the temperature
        restTemperatureMockMvc.perform(get("/api/temperatures/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateTemperature() throws Exception {
        // Initialize the database
        temperatureRepository.saveAndFlush(temperature);

        int databaseSizeBeforeUpdate = temperatureRepository.findAll().size();

        // Update the temperature
        Temperature updatedTemperature = temperatureRepository.findById(temperature.getId()).get();
        // Disconnect from session so that the updates on updatedTemperature are not directly saved in db
        em.detach(updatedTemperature);
        updatedTemperature
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .createTimestamp(UPDATED_CREATE_TIMESTAMP)
            .lastUpdateTimestamp(UPDATED_LAST_UPDATE_TIMESTAMP);

        restTemperatureMockMvc.perform(put("/api/temperatures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedTemperature)))
            .andExpect(status().isOk());

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);
        Temperature testTemperature = temperatureList.get(temperatureList.size() - 1);
        assertThat(testTemperature.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testTemperature.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testTemperature.getCreateTimestamp()).isEqualTo(UPDATED_CREATE_TIMESTAMP);
        assertThat(testTemperature.getLastUpdateTimestamp()).isEqualTo(UPDATED_LAST_UPDATE_TIMESTAMP);

        // Validate the Temperature in Elasticsearch
        verify(mockTemperatureSearchRepository, times(1)).save(testTemperature);
    }

    @Test
    @Transactional
    public void updateNonExistingTemperature() throws Exception {
        int databaseSizeBeforeUpdate = temperatureRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTemperatureMockMvc.perform(put("/api/temperatures")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(temperature)))
            .andExpect(status().isBadRequest());

        // Validate the Temperature in the database
        List<Temperature> temperatureList = temperatureRepository.findAll();
        assertThat(temperatureList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Temperature in Elasticsearch
        verify(mockTemperatureSearchRepository, times(0)).save(temperature);
    }

    @Test
    @Transactional
    public void deleteTemperature() throws Exception {
        // Initialize the database
        temperatureRepository.saveAndFlush(temperature);

        int databaseSizeBeforeDelete = temperatureRepository.findAll().size();

        // Delete the temperature
        restTemperatureMockMvc.perform(delete("/api/temperatures/{id}", temperature.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Temperature> temperatureList = temperatureRepository.findAll();
        assertThat(temperatureList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Temperature in Elasticsearch
        verify(mockTemperatureSearchRepository, times(1)).deleteById(temperature.getId());
    }

    @Test
    @Transactional
    public void searchTemperature() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        temperatureRepository.saveAndFlush(temperature);
        when(mockTemperatureSearchRepository.search(queryStringQuery("id:" + temperature.getId())))
            .thenReturn(Collections.singletonList(temperature));

        // Search the temperature
        restTemperatureMockMvc.perform(get("/api/_search/temperatures?query=id:" + temperature.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(temperature.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].createTimestamp").value(hasItem(sameInstant(DEFAULT_CREATE_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].lastUpdateTimestamp").value(hasItem(sameInstant(DEFAULT_LAST_UPDATE_TIMESTAMP))));
    }
}
