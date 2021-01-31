package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.Value;
import com.mycompany.myapp.repository.ValueRepository;
import com.mycompany.myapp.repository.search.ValueSearchRepository;

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
 * Integration tests for the {@link ValueResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class ValueResourceIT {

    private static final Double DEFAULT_VALUE = 1D;
    private static final Double UPDATED_VALUE = 2D;

    private static final ZonedDateTime DEFAULT_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private ValueRepository valueRepository;

    @Mock
    private ValueRepository valueRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.ValueSearchRepositoryMockConfiguration
     */
    @Autowired
    private ValueSearchRepository mockValueSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restValueMockMvc;

    private Value value;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Value createEntity(EntityManager em) {
        Value value = new Value()
            .value(DEFAULT_VALUE)
            .timestamp(DEFAULT_TIMESTAMP);
        return value;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Value createUpdatedEntity(EntityManager em) {
        Value value = new Value()
            .value(UPDATED_VALUE)
            .timestamp(UPDATED_TIMESTAMP);
        return value;
    }

    @BeforeEach
    public void initTest() {
        value = createEntity(em);
    }

    @Test
    @Transactional
    public void createValue() throws Exception {
        int databaseSizeBeforeCreate = valueRepository.findAll().size();
        // Create the Value
        restValueMockMvc.perform(post("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(value)))
            .andExpect(status().isCreated());

        // Validate the Value in the database
        List<Value> valueList = valueRepository.findAll();
        assertThat(valueList).hasSize(databaseSizeBeforeCreate + 1);
        Value testValue = valueList.get(valueList.size() - 1);
        assertThat(testValue.getValue()).isEqualTo(DEFAULT_VALUE);
        assertThat(testValue.getTimestamp()).isEqualTo(DEFAULT_TIMESTAMP);

        // Validate the Value in Elasticsearch
        verify(mockValueSearchRepository, times(1)).save(testValue);
    }

    @Test
    @Transactional
    public void createValueWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = valueRepository.findAll().size();

        // Create the Value with an existing ID
        value.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restValueMockMvc.perform(post("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(value)))
            .andExpect(status().isBadRequest());

        // Validate the Value in the database
        List<Value> valueList = valueRepository.findAll();
        assertThat(valueList).hasSize(databaseSizeBeforeCreate);

        // Validate the Value in Elasticsearch
        verify(mockValueSearchRepository, times(0)).save(value);
    }


    @Test
    @Transactional
    public void getAllValues() throws Exception {
        // Initialize the database
        valueRepository.saveAndFlush(value);

        // Get all the valueList
        restValueMockMvc.perform(get("/api/values?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(value.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllValuesWithEagerRelationshipsIsEnabled() throws Exception {
        when(valueRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restValueMockMvc.perform(get("/api/values?eagerload=true"))
            .andExpect(status().isOk());

        verify(valueRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllValuesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(valueRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restValueMockMvc.perform(get("/api/values?eagerload=true"))
            .andExpect(status().isOk());

        verify(valueRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getValue() throws Exception {
        // Initialize the database
        valueRepository.saveAndFlush(value);

        // Get the value
        restValueMockMvc.perform(get("/api/values/{id}", value.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(value.getId().intValue()))
            .andExpect(jsonPath("$.value").value(DEFAULT_VALUE.doubleValue()))
            .andExpect(jsonPath("$.timestamp").value(sameInstant(DEFAULT_TIMESTAMP)));
    }
    @Test
    @Transactional
    public void getNonExistingValue() throws Exception {
        // Get the value
        restValueMockMvc.perform(get("/api/values/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateValue() throws Exception {
        // Initialize the database
        valueRepository.saveAndFlush(value);

        int databaseSizeBeforeUpdate = valueRepository.findAll().size();

        // Update the value
        Value updatedValue = valueRepository.findById(value.getId()).get();
        // Disconnect from session so that the updates on updatedValue are not directly saved in db
        em.detach(updatedValue);
        updatedValue
            .value(UPDATED_VALUE)
            .timestamp(UPDATED_TIMESTAMP);

        restValueMockMvc.perform(put("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedValue)))
            .andExpect(status().isOk());

        // Validate the Value in the database
        List<Value> valueList = valueRepository.findAll();
        assertThat(valueList).hasSize(databaseSizeBeforeUpdate);
        Value testValue = valueList.get(valueList.size() - 1);
        assertThat(testValue.getValue()).isEqualTo(UPDATED_VALUE);
        assertThat(testValue.getTimestamp()).isEqualTo(UPDATED_TIMESTAMP);

        // Validate the Value in Elasticsearch
        verify(mockValueSearchRepository, times(1)).save(testValue);
    }

    @Test
    @Transactional
    public void updateNonExistingValue() throws Exception {
        int databaseSizeBeforeUpdate = valueRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restValueMockMvc.perform(put("/api/values")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(value)))
            .andExpect(status().isBadRequest());

        // Validate the Value in the database
        List<Value> valueList = valueRepository.findAll();
        assertThat(valueList).hasSize(databaseSizeBeforeUpdate);

        // Validate the Value in Elasticsearch
        verify(mockValueSearchRepository, times(0)).save(value);
    }

    @Test
    @Transactional
    public void deleteValue() throws Exception {
        // Initialize the database
        valueRepository.saveAndFlush(value);

        int databaseSizeBeforeDelete = valueRepository.findAll().size();

        // Delete the value
        restValueMockMvc.perform(delete("/api/values/{id}", value.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Value> valueList = valueRepository.findAll();
        assertThat(valueList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the Value in Elasticsearch
        verify(mockValueSearchRepository, times(1)).deleteById(value.getId());
    }

    @Test
    @Transactional
    public void searchValue() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        valueRepository.saveAndFlush(value);
        when(mockValueSearchRepository.search(queryStringQuery("id:" + value.getId())))
            .thenReturn(Collections.singletonList(value));

        // Search the value
        restValueMockMvc.perform(get("/api/_search/values?query=id:" + value.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(value.getId().intValue())))
            .andExpect(jsonPath("$.[*].value").value(hasItem(DEFAULT_VALUE.doubleValue())))
            .andExpect(jsonPath("$.[*].timestamp").value(hasItem(sameInstant(DEFAULT_TIMESTAMP))));
    }
}
