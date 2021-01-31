package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.DeviceProfile;
import com.mycompany.myapp.repository.DeviceProfileRepository;
import com.mycompany.myapp.repository.search.DeviceProfileSearchRepository;

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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link DeviceProfileResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class DeviceProfileResourceIT {

    @Autowired
    private DeviceProfileRepository deviceProfileRepository;

    @Mock
    private DeviceProfileRepository deviceProfileRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.DeviceProfileSearchRepositoryMockConfiguration
     */
    @Autowired
    private DeviceProfileSearchRepository mockDeviceProfileSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDeviceProfileMockMvc;

    private DeviceProfile deviceProfile;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceProfile createEntity(EntityManager em) {
        DeviceProfile deviceProfile = new DeviceProfile();
        return deviceProfile;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DeviceProfile createUpdatedEntity(EntityManager em) {
        DeviceProfile deviceProfile = new DeviceProfile();
        return deviceProfile;
    }

    @BeforeEach
    public void initTest() {
        deviceProfile = createEntity(em);
    }

    @Test
    @Transactional
    public void createDeviceProfile() throws Exception {
        int databaseSizeBeforeCreate = deviceProfileRepository.findAll().size();
        // Create the DeviceProfile
        restDeviceProfileMockMvc.perform(post("/api/device-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceProfile)))
            .andExpect(status().isCreated());

        // Validate the DeviceProfile in the database
        List<DeviceProfile> deviceProfileList = deviceProfileRepository.findAll();
        assertThat(deviceProfileList).hasSize(databaseSizeBeforeCreate + 1);
        DeviceProfile testDeviceProfile = deviceProfileList.get(deviceProfileList.size() - 1);

        // Validate the DeviceProfile in Elasticsearch
        verify(mockDeviceProfileSearchRepository, times(1)).save(testDeviceProfile);
    }

    @Test
    @Transactional
    public void createDeviceProfileWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = deviceProfileRepository.findAll().size();

        // Create the DeviceProfile with an existing ID
        deviceProfile.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restDeviceProfileMockMvc.perform(post("/api/device-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceProfile)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceProfile in the database
        List<DeviceProfile> deviceProfileList = deviceProfileRepository.findAll();
        assertThat(deviceProfileList).hasSize(databaseSizeBeforeCreate);

        // Validate the DeviceProfile in Elasticsearch
        verify(mockDeviceProfileSearchRepository, times(0)).save(deviceProfile);
    }


    @Test
    @Transactional
    public void getAllDeviceProfiles() throws Exception {
        // Initialize the database
        deviceProfileRepository.saveAndFlush(deviceProfile);

        // Get all the deviceProfileList
        restDeviceProfileMockMvc.perform(get("/api/device-profiles?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceProfile.getId().intValue())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllDeviceProfilesWithEagerRelationshipsIsEnabled() throws Exception {
        when(deviceProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceProfileMockMvc.perform(get("/api/device-profiles?eagerload=true"))
            .andExpect(status().isOk());

        verify(deviceProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllDeviceProfilesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(deviceProfileRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restDeviceProfileMockMvc.perform(get("/api/device-profiles?eagerload=true"))
            .andExpect(status().isOk());

        verify(deviceProfileRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getDeviceProfile() throws Exception {
        // Initialize the database
        deviceProfileRepository.saveAndFlush(deviceProfile);

        // Get the deviceProfile
        restDeviceProfileMockMvc.perform(get("/api/device-profiles/{id}", deviceProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(deviceProfile.getId().intValue()));
    }
    @Test
    @Transactional
    public void getNonExistingDeviceProfile() throws Exception {
        // Get the deviceProfile
        restDeviceProfileMockMvc.perform(get("/api/device-profiles/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateDeviceProfile() throws Exception {
        // Initialize the database
        deviceProfileRepository.saveAndFlush(deviceProfile);

        int databaseSizeBeforeUpdate = deviceProfileRepository.findAll().size();

        // Update the deviceProfile
        DeviceProfile updatedDeviceProfile = deviceProfileRepository.findById(deviceProfile.getId()).get();
        // Disconnect from session so that the updates on updatedDeviceProfile are not directly saved in db
        em.detach(updatedDeviceProfile);

        restDeviceProfileMockMvc.perform(put("/api/device-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedDeviceProfile)))
            .andExpect(status().isOk());

        // Validate the DeviceProfile in the database
        List<DeviceProfile> deviceProfileList = deviceProfileRepository.findAll();
        assertThat(deviceProfileList).hasSize(databaseSizeBeforeUpdate);
        DeviceProfile testDeviceProfile = deviceProfileList.get(deviceProfileList.size() - 1);

        // Validate the DeviceProfile in Elasticsearch
        verify(mockDeviceProfileSearchRepository, times(1)).save(testDeviceProfile);
    }

    @Test
    @Transactional
    public void updateNonExistingDeviceProfile() throws Exception {
        int databaseSizeBeforeUpdate = deviceProfileRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDeviceProfileMockMvc.perform(put("/api/device-profiles")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(deviceProfile)))
            .andExpect(status().isBadRequest());

        // Validate the DeviceProfile in the database
        List<DeviceProfile> deviceProfileList = deviceProfileRepository.findAll();
        assertThat(deviceProfileList).hasSize(databaseSizeBeforeUpdate);

        // Validate the DeviceProfile in Elasticsearch
        verify(mockDeviceProfileSearchRepository, times(0)).save(deviceProfile);
    }

    @Test
    @Transactional
    public void deleteDeviceProfile() throws Exception {
        // Initialize the database
        deviceProfileRepository.saveAndFlush(deviceProfile);

        int databaseSizeBeforeDelete = deviceProfileRepository.findAll().size();

        // Delete the deviceProfile
        restDeviceProfileMockMvc.perform(delete("/api/device-profiles/{id}", deviceProfile.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<DeviceProfile> deviceProfileList = deviceProfileRepository.findAll();
        assertThat(deviceProfileList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the DeviceProfile in Elasticsearch
        verify(mockDeviceProfileSearchRepository, times(1)).deleteById(deviceProfile.getId());
    }

    @Test
    @Transactional
    public void searchDeviceProfile() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        deviceProfileRepository.saveAndFlush(deviceProfile);
        when(mockDeviceProfileSearchRepository.search(queryStringQuery("id:" + deviceProfile.getId())))
            .thenReturn(Collections.singletonList(deviceProfile));

        // Search the deviceProfile
        restDeviceProfileMockMvc.perform(get("/api/_search/device-profiles?query=id:" + deviceProfile.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(deviceProfile.getId().intValue())));
    }
}
