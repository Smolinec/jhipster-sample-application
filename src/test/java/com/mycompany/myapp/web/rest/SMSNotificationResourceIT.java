package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.SMSNotification;
import com.mycompany.myapp.repository.SMSNotificationRepository;
import com.mycompany.myapp.repository.search.SMSNotificationSearchRepository;

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

import com.mycompany.myapp.domain.enumeration.AlertType;
/**
 * Integration tests for the {@link SMSNotificationResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class SMSNotificationResourceIT {

    private static final String DEFAULT_TEL_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_TEL_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_MESSAGE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_CREATED_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_CREATED_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final String DEFAULT_UUID_DEVICE = "AAAAAAAAAA";
    private static final String UPDATED_UUID_DEVICE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SENDING = false;
    private static final Boolean UPDATED_IS_SENDING = true;

    private static final ZonedDateTime DEFAULT_SENDING_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SENDING_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_IS_SEND = false;
    private static final Boolean UPDATED_IS_SEND = true;

    private static final ZonedDateTime DEFAULT_SEND_TIMESTAMP = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_SEND_TIMESTAMP = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final AlertType DEFAULT_ALERT_TYPE = AlertType.INFO;
    private static final AlertType UPDATED_ALERT_TYPE = AlertType.WARN;

    private static final ZonedDateTime DEFAULT_FEATURE_SEND = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_FEATURE_SEND = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    @Autowired
    private SMSNotificationRepository sMSNotificationRepository;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.SMSNotificationSearchRepositoryMockConfiguration
     */
    @Autowired
    private SMSNotificationSearchRepository mockSMSNotificationSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSMSNotificationMockMvc;

    private SMSNotification sMSNotification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SMSNotification createEntity(EntityManager em) {
        SMSNotification sMSNotification = new SMSNotification()
            .telNumber(DEFAULT_TEL_NUMBER)
            .message(DEFAULT_MESSAGE)
            .createdTimestamp(DEFAULT_CREATED_TIMESTAMP)
            .uuidDevice(DEFAULT_UUID_DEVICE)
            .isSending(DEFAULT_IS_SENDING)
            .sendingTimestamp(DEFAULT_SENDING_TIMESTAMP)
            .isSend(DEFAULT_IS_SEND)
            .sendTimestamp(DEFAULT_SEND_TIMESTAMP)
            .alertType(DEFAULT_ALERT_TYPE)
            .featureSend(DEFAULT_FEATURE_SEND);
        return sMSNotification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SMSNotification createUpdatedEntity(EntityManager em) {
        SMSNotification sMSNotification = new SMSNotification()
            .telNumber(UPDATED_TEL_NUMBER)
            .message(UPDATED_MESSAGE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .uuidDevice(UPDATED_UUID_DEVICE)
            .isSending(UPDATED_IS_SENDING)
            .sendingTimestamp(UPDATED_SENDING_TIMESTAMP)
            .isSend(UPDATED_IS_SEND)
            .sendTimestamp(UPDATED_SEND_TIMESTAMP)
            .alertType(UPDATED_ALERT_TYPE)
            .featureSend(UPDATED_FEATURE_SEND);
        return sMSNotification;
    }

    @BeforeEach
    public void initTest() {
        sMSNotification = createEntity(em);
    }

    @Test
    @Transactional
    public void createSMSNotification() throws Exception {
        int databaseSizeBeforeCreate = sMSNotificationRepository.findAll().size();
        // Create the SMSNotification
        restSMSNotificationMockMvc.perform(post("/api/sms-notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sMSNotification)))
            .andExpect(status().isCreated());

        // Validate the SMSNotification in the database
        List<SMSNotification> sMSNotificationList = sMSNotificationRepository.findAll();
        assertThat(sMSNotificationList).hasSize(databaseSizeBeforeCreate + 1);
        SMSNotification testSMSNotification = sMSNotificationList.get(sMSNotificationList.size() - 1);
        assertThat(testSMSNotification.getTelNumber()).isEqualTo(DEFAULT_TEL_NUMBER);
        assertThat(testSMSNotification.getMessage()).isEqualTo(DEFAULT_MESSAGE);
        assertThat(testSMSNotification.getCreatedTimestamp()).isEqualTo(DEFAULT_CREATED_TIMESTAMP);
        assertThat(testSMSNotification.getUuidDevice()).isEqualTo(DEFAULT_UUID_DEVICE);
        assertThat(testSMSNotification.isIsSending()).isEqualTo(DEFAULT_IS_SENDING);
        assertThat(testSMSNotification.getSendingTimestamp()).isEqualTo(DEFAULT_SENDING_TIMESTAMP);
        assertThat(testSMSNotification.isIsSend()).isEqualTo(DEFAULT_IS_SEND);
        assertThat(testSMSNotification.getSendTimestamp()).isEqualTo(DEFAULT_SEND_TIMESTAMP);
        assertThat(testSMSNotification.getAlertType()).isEqualTo(DEFAULT_ALERT_TYPE);
        assertThat(testSMSNotification.getFeatureSend()).isEqualTo(DEFAULT_FEATURE_SEND);

        // Validate the SMSNotification in Elasticsearch
        verify(mockSMSNotificationSearchRepository, times(1)).save(testSMSNotification);
    }

    @Test
    @Transactional
    public void createSMSNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = sMSNotificationRepository.findAll().size();

        // Create the SMSNotification with an existing ID
        sMSNotification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSMSNotificationMockMvc.perform(post("/api/sms-notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sMSNotification)))
            .andExpect(status().isBadRequest());

        // Validate the SMSNotification in the database
        List<SMSNotification> sMSNotificationList = sMSNotificationRepository.findAll();
        assertThat(sMSNotificationList).hasSize(databaseSizeBeforeCreate);

        // Validate the SMSNotification in Elasticsearch
        verify(mockSMSNotificationSearchRepository, times(0)).save(sMSNotification);
    }


    @Test
    @Transactional
    public void getAllSMSNotifications() throws Exception {
        // Initialize the database
        sMSNotificationRepository.saveAndFlush(sMSNotification);

        // Get all the sMSNotificationList
        restSMSNotificationMockMvc.perform(get("/api/sms-notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sMSNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].telNumber").value(hasItem(DEFAULT_TEL_NUMBER)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdTimestamp").value(hasItem(sameInstant(DEFAULT_CREATED_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].uuidDevice").value(hasItem(DEFAULT_UUID_DEVICE)))
            .andExpect(jsonPath("$.[*].isSending").value(hasItem(DEFAULT_IS_SENDING.booleanValue())))
            .andExpect(jsonPath("$.[*].sendingTimestamp").value(hasItem(sameInstant(DEFAULT_SENDING_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].isSend").value(hasItem(DEFAULT_IS_SEND.booleanValue())))
            .andExpect(jsonPath("$.[*].sendTimestamp").value(hasItem(sameInstant(DEFAULT_SEND_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].featureSend").value(hasItem(sameInstant(DEFAULT_FEATURE_SEND))));
    }
    
    @Test
    @Transactional
    public void getSMSNotification() throws Exception {
        // Initialize the database
        sMSNotificationRepository.saveAndFlush(sMSNotification);

        // Get the sMSNotification
        restSMSNotificationMockMvc.perform(get("/api/sms-notifications/{id}", sMSNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(sMSNotification.getId().intValue()))
            .andExpect(jsonPath("$.telNumber").value(DEFAULT_TEL_NUMBER))
            .andExpect(jsonPath("$.message").value(DEFAULT_MESSAGE))
            .andExpect(jsonPath("$.createdTimestamp").value(sameInstant(DEFAULT_CREATED_TIMESTAMP)))
            .andExpect(jsonPath("$.uuidDevice").value(DEFAULT_UUID_DEVICE))
            .andExpect(jsonPath("$.isSending").value(DEFAULT_IS_SENDING.booleanValue()))
            .andExpect(jsonPath("$.sendingTimestamp").value(sameInstant(DEFAULT_SENDING_TIMESTAMP)))
            .andExpect(jsonPath("$.isSend").value(DEFAULT_IS_SEND.booleanValue()))
            .andExpect(jsonPath("$.sendTimestamp").value(sameInstant(DEFAULT_SEND_TIMESTAMP)))
            .andExpect(jsonPath("$.alertType").value(DEFAULT_ALERT_TYPE.toString()))
            .andExpect(jsonPath("$.featureSend").value(sameInstant(DEFAULT_FEATURE_SEND)));
    }
    @Test
    @Transactional
    public void getNonExistingSMSNotification() throws Exception {
        // Get the sMSNotification
        restSMSNotificationMockMvc.perform(get("/api/sms-notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSMSNotification() throws Exception {
        // Initialize the database
        sMSNotificationRepository.saveAndFlush(sMSNotification);

        int databaseSizeBeforeUpdate = sMSNotificationRepository.findAll().size();

        // Update the sMSNotification
        SMSNotification updatedSMSNotification = sMSNotificationRepository.findById(sMSNotification.getId()).get();
        // Disconnect from session so that the updates on updatedSMSNotification are not directly saved in db
        em.detach(updatedSMSNotification);
        updatedSMSNotification
            .telNumber(UPDATED_TEL_NUMBER)
            .message(UPDATED_MESSAGE)
            .createdTimestamp(UPDATED_CREATED_TIMESTAMP)
            .uuidDevice(UPDATED_UUID_DEVICE)
            .isSending(UPDATED_IS_SENDING)
            .sendingTimestamp(UPDATED_SENDING_TIMESTAMP)
            .isSend(UPDATED_IS_SEND)
            .sendTimestamp(UPDATED_SEND_TIMESTAMP)
            .alertType(UPDATED_ALERT_TYPE)
            .featureSend(UPDATED_FEATURE_SEND);

        restSMSNotificationMockMvc.perform(put("/api/sms-notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSMSNotification)))
            .andExpect(status().isOk());

        // Validate the SMSNotification in the database
        List<SMSNotification> sMSNotificationList = sMSNotificationRepository.findAll();
        assertThat(sMSNotificationList).hasSize(databaseSizeBeforeUpdate);
        SMSNotification testSMSNotification = sMSNotificationList.get(sMSNotificationList.size() - 1);
        assertThat(testSMSNotification.getTelNumber()).isEqualTo(UPDATED_TEL_NUMBER);
        assertThat(testSMSNotification.getMessage()).isEqualTo(UPDATED_MESSAGE);
        assertThat(testSMSNotification.getCreatedTimestamp()).isEqualTo(UPDATED_CREATED_TIMESTAMP);
        assertThat(testSMSNotification.getUuidDevice()).isEqualTo(UPDATED_UUID_DEVICE);
        assertThat(testSMSNotification.isIsSending()).isEqualTo(UPDATED_IS_SENDING);
        assertThat(testSMSNotification.getSendingTimestamp()).isEqualTo(UPDATED_SENDING_TIMESTAMP);
        assertThat(testSMSNotification.isIsSend()).isEqualTo(UPDATED_IS_SEND);
        assertThat(testSMSNotification.getSendTimestamp()).isEqualTo(UPDATED_SEND_TIMESTAMP);
        assertThat(testSMSNotification.getAlertType()).isEqualTo(UPDATED_ALERT_TYPE);
        assertThat(testSMSNotification.getFeatureSend()).isEqualTo(UPDATED_FEATURE_SEND);

        // Validate the SMSNotification in Elasticsearch
        verify(mockSMSNotificationSearchRepository, times(1)).save(testSMSNotification);
    }

    @Test
    @Transactional
    public void updateNonExistingSMSNotification() throws Exception {
        int databaseSizeBeforeUpdate = sMSNotificationRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSMSNotificationMockMvc.perform(put("/api/sms-notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(sMSNotification)))
            .andExpect(status().isBadRequest());

        // Validate the SMSNotification in the database
        List<SMSNotification> sMSNotificationList = sMSNotificationRepository.findAll();
        assertThat(sMSNotificationList).hasSize(databaseSizeBeforeUpdate);

        // Validate the SMSNotification in Elasticsearch
        verify(mockSMSNotificationSearchRepository, times(0)).save(sMSNotification);
    }

    @Test
    @Transactional
    public void deleteSMSNotification() throws Exception {
        // Initialize the database
        sMSNotificationRepository.saveAndFlush(sMSNotification);

        int databaseSizeBeforeDelete = sMSNotificationRepository.findAll().size();

        // Delete the sMSNotification
        restSMSNotificationMockMvc.perform(delete("/api/sms-notifications/{id}", sMSNotification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<SMSNotification> sMSNotificationList = sMSNotificationRepository.findAll();
        assertThat(sMSNotificationList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the SMSNotification in Elasticsearch
        verify(mockSMSNotificationSearchRepository, times(1)).deleteById(sMSNotification.getId());
    }

    @Test
    @Transactional
    public void searchSMSNotification() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        sMSNotificationRepository.saveAndFlush(sMSNotification);
        when(mockSMSNotificationSearchRepository.search(queryStringQuery("id:" + sMSNotification.getId())))
            .thenReturn(Collections.singletonList(sMSNotification));

        // Search the sMSNotification
        restSMSNotificationMockMvc.perform(get("/api/_search/sms-notifications?query=id:" + sMSNotification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(sMSNotification.getId().intValue())))
            .andExpect(jsonPath("$.[*].telNumber").value(hasItem(DEFAULT_TEL_NUMBER)))
            .andExpect(jsonPath("$.[*].message").value(hasItem(DEFAULT_MESSAGE)))
            .andExpect(jsonPath("$.[*].createdTimestamp").value(hasItem(sameInstant(DEFAULT_CREATED_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].uuidDevice").value(hasItem(DEFAULT_UUID_DEVICE)))
            .andExpect(jsonPath("$.[*].isSending").value(hasItem(DEFAULT_IS_SENDING.booleanValue())))
            .andExpect(jsonPath("$.[*].sendingTimestamp").value(hasItem(sameInstant(DEFAULT_SENDING_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].isSend").value(hasItem(DEFAULT_IS_SEND.booleanValue())))
            .andExpect(jsonPath("$.[*].sendTimestamp").value(hasItem(sameInstant(DEFAULT_SEND_TIMESTAMP))))
            .andExpect(jsonPath("$.[*].alertType").value(hasItem(DEFAULT_ALERT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].featureSend").value(hasItem(sameInstant(DEFAULT_FEATURE_SEND))));
    }
}
