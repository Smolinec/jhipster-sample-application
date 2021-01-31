package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.DeviceConfiguration;
import com.mycompany.myapp.repository.DeviceConfigurationRepository;
import com.mycompany.myapp.repository.search.DeviceConfigurationSearchRepository;

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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DeviceConfigurationResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DeviceConfigurationResourceIT {

    private static final String DEFAULT_PRIMARY_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_PRIMARY_HOSTNAME = "BBBBBBBBBB";

    private static final String DEFAULT_SECONDARY_HOSTNAME = "AAAAAAAAAA";
    private static final String UPDATED_SECONDARY_HOSTNAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_PORT = 1;
    private static final Integer UPDATED_PORT = 2;

    @Autowired
    private DeviceConfigurationRepository deviceConfigurationRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DeviceConfigurationSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeviceConfigurationSearchRepository mockDeviceConfigurationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceConfigurationMockMvc;

    private DeviceConfiguration deviceConfiguration;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceConfiguration createEntity(EntityManager em) {
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration()
            .primaryHostname(DEFAULT_PRIMARY_HOSTNAME)
            .secondaryHostname(DEFAULT_SECONDARY_HOSTNAME)
            .port(DEFAULT_PORT);
        return deviceConfiguration;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceConfiguration createUpdatedEntity(EntityManager em) {
        DeviceConfiguration deviceConfiguration = new DeviceConfiguration()
            .primaryHostname(UPDATED_PRIMARY_HOSTNAME)
            .secondaryHostname(UPDATED_SECONDARY_HOSTNAME)
            .port(UPDATED_PORT);
        return deviceConfiguration;
    }

    @BeforeEach
    public void initTest() {
        deviceConfiguration = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceConfiguration() throws Exception {
        int databaseSizeBeforeCreate = deviceConfigurationRepository.findAll().size();
        // Create the DeviceConfiguration
        restDeviceConfigurationMockMvc.perform(post("/api/device-configurations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceConfiguration)))
            .andExpect(status().isCreated());

        // Validate the DeviceConfiguration in the database
        List<DeviceConfiguration> deviceConfigurationList = deviceConfigurationRepository.findAll();
        assertThat(deviceConfigurationList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceConfiguration testDeviceConfiguration = deviceConfigurationList.get(deviceConfigurationList.size() - 1);
        assertThat(testDeviceConfiguration.getPrimaryHostname()).isEqualTo(DEFAULT_PRIMARY_HOSTNAME);
        assertThat(testDeviceConfiguration.getSecondaryHostname()).isEqualTo(DEFAULT_SECONDARY_HOSTNAME);
        assertThat(testDeviceConfiguration.getPort()).isEqualTo(DEFAULT_PORT);

        // Validate the DeviceConfiguration in Elasticsearch
        verify(mockDeviceConfigurationSearchRepository, times(1)).save(testDeviceConfiguration);
    }

    @Test
    @Transactional
    public void createDeviceConfigurationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceConfigurationRepository.findAll().size();

        // Create the DeviceConfiguration with an existing ID
        deviceConfiguration.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceConfigurationMockMvc.perform(post("/api/device-configurations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceConfiguration in the database
        List<DeviceConfiguration> deviceConfigurationList = deviceConfigurationRepository.findAll();
        assertThat(deviceConfigurationList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeviceConfiguration in Elasticsearch
        verify(mockDeviceConfigurationSearchRepository, times(0)).save(deviceConfiguration);
    }


    @Test
    @Transactional
    public void getAllDeviceConfigurations() throws Exception {
        // Initialize the database
        deviceConfigurationRepository.saveAndFlush(deviceConfiguration);

        // Get all the deviceConfigurationList
        restDeviceConfigurationMockMvc.perform(get("/api/device-configurations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].primaryHostname").value(hasItem(DEFAULT_PRIMARY_HOSTNAME)))
            .andExpect(jsonPath("$.[*].secondaryHostname").value(hasItem(DEFAULT_SECONDARY_HOSTNAME)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));
    }
    
    @Test
    @Transactional
    public void getDeviceConfiguration() throws Exception {
        // Initialize the database
        deviceConfigurationRepository.saveAndFlush(deviceConfiguration);

        // Get the deviceConfiguration
        restDeviceConfigurationMockMvc.perform(get("/api/device-configurations/{id}", deviceConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceConfiguration.getId().intValue()))
            .andExpect(jsonPath("$.primaryHostname").value(DEFAULT_PRIMARY_HOSTNAME))
            .andExpect(jsonPath("$.secondaryHostname").value(DEFAULT_SECONDARY_HOSTNAME))
            .andExpect(jsonPath("$.port").value(DEFAULT_PORT));
    }
    @Test
    @Transactional
    public void getNonExistingDeviceConfiguration() throws Exception {
        // Get the deviceConfiguration
        restDeviceConfigurationMockMvc.perform(get("/api/device-configurations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceConfiguration() throws Exception {
        // Initialize the database
        deviceConfigurationRepository.saveAndFlush(deviceConfiguration);

        int databaseSizeBeforeUpdate = deviceConfigurationRepository.findAll().size();

        // Update the deviceConfiguration
        DeviceConfiguration updatedDeviceConfiguration = deviceConfigurationRepository.findById(deviceConfiguration.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceConfiguration are not directly saved in db
        em.detach(updatedDeviceConfiguration);
        updatedDeviceConfiguration
            .primaryHostname(UPDATED_PRIMARY_HOSTNAME)
            .secondaryHostname(UPDATED_SECONDARY_HOSTNAME)
            .port(UPDATED_PORT);

        restDeviceConfigurationMockMvc.perform(put("/api/device-configurations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceConfiguration)))
            .andExpect(status().isOk());

        // Validate the DeviceConfiguration in the database
        List<DeviceConfiguration> deviceConfigurationList = deviceConfigurationRepository.findAll();
        assertThat(deviceConfigurationList).hasSize(databaseSizeBeforeUpdate);
        DeviceConfiguration testDeviceConfiguration = deviceConfigurationList.get(deviceConfigurationList.size() - 1);
        assertThat(testDeviceConfiguration.getPrimaryHostname()).isEqualTo(UPDATED_PRIMARY_HOSTNAME);
        assertThat(testDeviceConfiguration.getSecondaryHostname()).isEqualTo(UPDATED_SECONDARY_HOSTNAME);
        assertThat(testDeviceConfiguration.getPort()).isEqualTo(UPDATED_PORT);

        // Validate the DeviceConfiguration in Elasticsearch
        verify(mockDeviceConfigurationSearchRepository, times(1)).save(testDeviceConfiguration);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceConfiguration() throws Exception {
        int databaseSizeBeforeUpdate = deviceConfigurationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceConfigurationMockMvc.perform(put("/api/device-configurations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceConfiguration)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceConfiguration in the database
        List<DeviceConfiguration> deviceConfigurationList = deviceConfigurationRepository.findAll();
        assertThat(deviceConfigurationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeviceConfiguration in Elasticsearch
        verify(mockDeviceConfigurationSearchRepository, times(0)).save(deviceConfiguration);
    }

    @Test
    @Transactional
    public void deleteDeviceConfiguration() throws Exception {
        // Initialize the database
        deviceConfigurationRepository.saveAndFlush(deviceConfiguration);

        int databaseSizeBeforeDelete = deviceConfigurationRepository.findAll().size();

        // Delete the deviceConfiguration
        restDeviceConfigurationMockMvc.perform(delete("/api/device-configurations/{id}", deviceConfiguration.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceConfiguration> deviceConfigurationList = deviceConfigurationRepository.findAll();
        assertThat(deviceConfigurationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeviceConfiguration in Elasticsearch
        verify(mockDeviceConfigurationSearchRepository, times(1)).deleteById(deviceConfiguration.getId());
    }

    @Test
    @Transactional
    public void searchDeviceConfiguration() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        deviceConfigurationRepository.saveAndFlush(deviceConfiguration);
        when(mockDeviceConfigurationSearchRepository.search(queryStringQuery("id:" + deviceConfiguration.getId())))
            .thenReturn(Collections.singletonList(deviceConfiguration));

        // Search the deviceConfiguration
        restDeviceConfigurationMockMvc.perform(get("/api/_search/device-configurations?query=id:" + deviceConfiguration.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceConfiguration.getId().intValue())))
            .andExpect(jsonPath("$.[*].primaryHostname").value(hasItem(DEFAULT_PRIMARY_HOSTNAME)))
            .andExpect(jsonPath("$.[*].secondaryHostname").value(hasItem(DEFAULT_SECONDARY_HOSTNAME)))
            .andExpect(jsonPath("$.[*].port").value(hasItem(DEFAULT_PORT)));
    }
}
