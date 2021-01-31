package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.JhipsterSampleApplicationApp;
import com.mycompany.myapp.domain.WebUser;
import com.mycompany.myapp.repository.WebUserRepository;
import com.mycompany.myapp.repository.search.WebUserSearchRepository;

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
 * Integration tests for the {@link WebUserResource} REST controller.
 */
@SpringBootTest(classes = JhipsterSampleApplicationApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class WebUserResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_EMAIL = "AAAAAAAAAA";
    private static final String UPDATED_EMAIL = "BBBBBBBBBB";

    private static final String DEFAULT_PASSWORD = "AAAAAAAAAA";
    private static final String UPDATED_PASSWORD = "BBBBBBBBBB";

    @Autowired
    private WebUserRepository webUserRepository;

    @Mock
    private WebUserRepository webUserRepositoryMock;

    /**
     * This repository is mocked in the com.mycompany.myapp.repository.search test package.
     *
     * @see com.mycompany.myapp.repository.search.WebUserSearchRepositoryMockConfiguration
     */
    @Autowired
    private WebUserSearchRepository mockWebUserSearchRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWebUserMockMvc;

    private WebUser webUser;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebUser createEntity(EntityManager em) {
        WebUser webUser = new WebUser()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME)
            .email(DEFAULT_EMAIL)
            .password(DEFAULT_PASSWORD);
        return webUser;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WebUser createUpdatedEntity(EntityManager em) {
        WebUser webUser = new WebUser()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);
        return webUser;
    }

    @BeforeEach
    public void initTest() {
        webUser = createEntity(em);
    }

    @Test
    @Transactional
    public void createWebUser() throws Exception {
        int databaseSizeBeforeCreate = webUserRepository.findAll().size();
        // Create the WebUser
        restWebUserMockMvc.perform(post("/api/web-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(webUser)))
            .andExpect(status().isCreated());

        // Validate the WebUser in the database
        List<WebUser> webUserList = webUserRepository.findAll();
        assertThat(webUserList).hasSize(databaseSizeBeforeCreate + 1);
        WebUser testWebUser = webUserList.get(webUserList.size() - 1);
        assertThat(testWebUser.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testWebUser.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
        assertThat(testWebUser.getEmail()).isEqualTo(DEFAULT_EMAIL);
        assertThat(testWebUser.getPassword()).isEqualTo(DEFAULT_PASSWORD);

        // Validate the WebUser in Elasticsearch
        verify(mockWebUserSearchRepository, times(1)).save(testWebUser);
    }

    @Test
    @Transactional
    public void createWebUserWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = webUserRepository.findAll().size();

        // Create the WebUser with an existing ID
        webUser.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restWebUserMockMvc.perform(post("/api/web-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(webUser)))
            .andExpect(status().isBadRequest());

        // Validate the WebUser in the database
        List<WebUser> webUserList = webUserRepository.findAll();
        assertThat(webUserList).hasSize(databaseSizeBeforeCreate);

        // Validate the WebUser in Elasticsearch
        verify(mockWebUserSearchRepository, times(0)).save(webUser);
    }


    @Test
    @Transactional
    public void getAllWebUsers() throws Exception {
        // Initialize the database
        webUserRepository.saveAndFlush(webUser);

        // Get all the webUserList
        restWebUserMockMvc.perform(get("/api/web-users?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllWebUsersWithEagerRelationshipsIsEnabled() throws Exception {
        when(webUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebUserMockMvc.perform(get("/api/web-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(webUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllWebUsersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(webUserRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restWebUserMockMvc.perform(get("/api/web-users?eagerload=true"))
            .andExpect(status().isOk());

        verify(webUserRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getWebUser() throws Exception {
        // Initialize the database
        webUserRepository.saveAndFlush(webUser);

        // Get the webUser
        restWebUserMockMvc.perform(get("/api/web-users/{id}", webUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(webUser.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME))
            .andExpect(jsonPath("$.email").value(DEFAULT_EMAIL))
            .andExpect(jsonPath("$.password").value(DEFAULT_PASSWORD));
    }
    @Test
    @Transactional
    public void getNonExistingWebUser() throws Exception {
        // Get the webUser
        restWebUserMockMvc.perform(get("/api/web-users/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateWebUser() throws Exception {
        // Initialize the database
        webUserRepository.saveAndFlush(webUser);

        int databaseSizeBeforeUpdate = webUserRepository.findAll().size();

        // Update the webUser
        WebUser updatedWebUser = webUserRepository.findById(webUser.getId()).get();
        // Disconnect from session so that the updates on updatedWebUser are not directly saved in db
        em.detach(updatedWebUser);
        updatedWebUser
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME)
            .email(UPDATED_EMAIL)
            .password(UPDATED_PASSWORD);

        restWebUserMockMvc.perform(put("/api/web-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedWebUser)))
            .andExpect(status().isOk());

        // Validate the WebUser in the database
        List<WebUser> webUserList = webUserRepository.findAll();
        assertThat(webUserList).hasSize(databaseSizeBeforeUpdate);
        WebUser testWebUser = webUserList.get(webUserList.size() - 1);
        assertThat(testWebUser.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testWebUser.getLastName()).isEqualTo(UPDATED_LAST_NAME);
        assertThat(testWebUser.getEmail()).isEqualTo(UPDATED_EMAIL);
        assertThat(testWebUser.getPassword()).isEqualTo(UPDATED_PASSWORD);

        // Validate the WebUser in Elasticsearch
        verify(mockWebUserSearchRepository, times(1)).save(testWebUser);
    }

    @Test
    @Transactional
    public void updateNonExistingWebUser() throws Exception {
        int databaseSizeBeforeUpdate = webUserRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWebUserMockMvc.perform(put("/api/web-users")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(webUser)))
            .andExpect(status().isBadRequest());

        // Validate the WebUser in the database
        List<WebUser> webUserList = webUserRepository.findAll();
        assertThat(webUserList).hasSize(databaseSizeBeforeUpdate);

        // Validate the WebUser in Elasticsearch
        verify(mockWebUserSearchRepository, times(0)).save(webUser);
    }

    @Test
    @Transactional
    public void deleteWebUser() throws Exception {
        // Initialize the database
        webUserRepository.saveAndFlush(webUser);

        int databaseSizeBeforeDelete = webUserRepository.findAll().size();

        // Delete the webUser
        restWebUserMockMvc.perform(delete("/api/web-users/{id}", webUser.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WebUser> webUserList = webUserRepository.findAll();
        assertThat(webUserList).hasSize(databaseSizeBeforeDelete - 1);

        // Validate the WebUser in Elasticsearch
        verify(mockWebUserSearchRepository, times(1)).deleteById(webUser.getId());
    }

    @Test
    @Transactional
    public void searchWebUser() throws Exception {
        // Configure the mock search repository
        // Initialize the database
        webUserRepository.saveAndFlush(webUser);
        when(mockWebUserSearchRepository.search(queryStringQuery("id:" + webUser.getId())))
            .thenReturn(Collections.singletonList(webUser));

        // Search the webUser
        restWebUserMockMvc.perform(get("/api/_search/web-users?query=id:" + webUser.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(webUser.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)))
            .andExpect(jsonPath("$.[*].email").value(hasItem(DEFAULT_EMAIL)))
            .andExpect(jsonPath("$.[*].password").value(hasItem(DEFAULT_PASSWORD)));
    }
}
