package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Values;
import com.mycompany.myapp.repository.ValuesRepository;
import com.mycompany.myapp.repository.search.ValuesSearchRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.ArrayList;
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
 * Integration tests for the {@link ValuesResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ValuesResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ValuesRepository valuesRepository;

    @Mock
    private ValuesRepository valuesRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ValuesSearchRepositoryMockConfiguration
     */
    @Autowired
    private ValuesSearchRepository mockValuesSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValuesMockMvc;

    private Values values;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Values createEntity(EntityManager em) {
        Values values = new Values()
            .value(DEFAULT_VALUE)
            .timestamp(DEFAULT_TIMESTAMP);
        return values;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Values createUpdatedEntity(EntityManager em) {
        Values values = new Values()
            .value(UPDATED_VALUE)
            .timestamp(UPDATED_TIMESTAMP);
        return values;
    }

    @BeforeEach
    public void initTest() {
        values = createEntity(em);
    }

    @Test
    @Transactional
    public void createValues() throws Exception {
        int databaseSizeBeforeCreate = valuesRepository.findAll().size();
        // Create the Values
        restValuesMockMvc.perform(post("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(values)))
            .andExpect(status().isCreated());

        // Validate the Values in the database
        List<Values> valuesList = valuesRepository.findAll();
        assertThat(valuesList).hasSize(databaseSizeBeforeCreate + 1);
        Values testValues = valuesList.get(valuesList.size() - 1);
        assertThat(testValues.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testValues.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);

        // Validate the Values in Elasticsearch
        verify(mockValuesSearchRepository, times(1)).save(testValues);
    }

    @Test
    @Transactional
    public void createValuesWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valuesRepository.findAll().size();

        // Create the Values with an existing ID
        values.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValuesMockMvc.perform(post("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(values)))
            .andExpect(status().isBadRequest());

        // Validate the Values in the database
        List<Values> valuesList = valuesRepository.findAll();
        assertThat(valuesList).hasSize(databaseSizeBeforeCreate);

        // Validate the Values in Elasticsearch
        verify(mockValuesSearchRepository, times(0)).save(values);
    }


    @Test
    @Transactional
    public void getAllValues() throws Exception {
        // Initialize the database
        valuesRepository.saveAndFlush(values);

        // Get all the valuesList
        restValuesMockMvc.perform(get("/api/values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(values.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllValuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(valuesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restValuesMockMvc.perform(get("/api/values?eagerload=true"))
            .andExpect(status().isOk());

        verify(valuesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllValuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(valuesRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restValuesMockMvc.perform(get("/api/values?eagerload=true"))
            .andExpect(status().isOk());

        verify(valuesRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getValues() throws Exception {
        // Initialize the database
        valuesRepository.saveAndFlush(values);

        // Get the values
        restValuesMockMvc.perform(get("/api/values/{id}", values.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(values.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }
    @Test
    @Transactional
    public void getNonExistingValues() throws Exception {
        // Get the values
        restValuesMockMvc.perform(get("/api/values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValues() throws Exception {
        // Initialize the database
        valuesRepository.saveAndFlush(values);

        int databaseSizeBeforeUpdate = valuesRepository.findAll().size();

        // Update the values
        Values updatedValues = valuesRepository.findById(values.getId()).get();
        // Disconnect from session so that the updates on updatedValues are not directly saved in db
        em.detach(updatedValues);
        updatedValues
            .value(UPDATED_VALUE)
            .timestamp(UPDATED_TIMESTAMP);

        restValuesMockMvc.perform(put("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedValues)))
            .andExpect(status().isOk());

        // Validate the Values in the database
        List<Values> valuesList = valuesRepository.findAll();
        assertThat(valuesList).hasSize(databaseSizeBeforeUpdate);
        Values testValues = valuesList.get(valuesList.size() - 1);
        assertThat(testValues.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testValues.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);

        // Validate the Values in Elasticsearch
        verify(mockValuesSearchRepository, times(1)).save(testValues);
    }

    @Test
    @Transactional
    public void updateNonExistingValues() throws Exception {
        int databaseSizeBeforeUpdate = valuesRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValuesMockMvc.perform(put("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(values)))
            .andExpect(status().isBadRequest());

        // Validate the Values in the database
        List<Values> valuesList = valuesRepository.findAll();
        assertThat(valuesList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Values in Elasticsearch
        verify(mockValuesSearchRepository, times(0)).save(values);
    }

    @Test
    @Transactional
    public void deleteValues() throws Exception {
        // Initialize the database
        valuesRepository.saveAndFlush(values);

        int databaseSizeBeforeDelete = valuesRepository.findAll().size();

        // Delete the values
        restValuesMockMvc.perform(delete("/api/values/{id}", values.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Values> valuesList = valuesRepository.findAll();
        assertThat(valuesList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Values in Elasticsearch
        verify(mockValuesSearchRepository, times(1)).deleteById(values.getId());
    }

    @Test
    @Transactional
    public void searchValues() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        valuesRepository.saveAndFlush(values);
        when(mockValuesSearchRepository.search(queryStringQuery("id:" + values.getId())))
            .thenReturn(Collections.singletonList(values));

        // Search the values
        restValuesMockMvc.perform(get("/api/_search/values?query=id:" + values.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(values.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }
}
