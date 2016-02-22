package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.SpecStatus;
import org.nsf.traqtion.repository.SpecStatusRepository;
import org.nsf.traqtion.repository.search.SpecStatusSearchRepository;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the SpecStatusResource REST controller.
 *
 * @see SpecStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpecStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private SpecStatusRepository specStatusRepository;

    @Inject
    private SpecStatusSearchRepository specStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpecStatusMockMvc;

    private SpecStatus specStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecStatusResource specStatusResource = new SpecStatusResource();
        ReflectionTestUtils.setField(specStatusResource, "specStatusSearchRepository", specStatusSearchRepository);
        ReflectionTestUtils.setField(specStatusResource, "specStatusRepository", specStatusRepository);
        this.restSpecStatusMockMvc = MockMvcBuilders.standaloneSetup(specStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        specStatus = new SpecStatus();
        specStatus.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSpecStatus() throws Exception {
        int databaseSizeBeforeCreate = specStatusRepository.findAll().size();

        // Create the SpecStatus

        restSpecStatusMockMvc.perform(post("/api/specStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specStatus)))
                .andExpect(status().isCreated());

        // Validate the SpecStatus in the database
        List<SpecStatus> specStatuss = specStatusRepository.findAll();
        assertThat(specStatuss).hasSize(databaseSizeBeforeCreate + 1);
        SpecStatus testSpecStatus = specStatuss.get(specStatuss.size() - 1);
        assertThat(testSpecStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllSpecStatuss() throws Exception {
        // Initialize the database
        specStatusRepository.saveAndFlush(specStatus);

        // Get all the specStatuss
        restSpecStatusMockMvc.perform(get("/api/specStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(specStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getSpecStatus() throws Exception {
        // Initialize the database
        specStatusRepository.saveAndFlush(specStatus);

        // Get the specStatus
        restSpecStatusMockMvc.perform(get("/api/specStatuss/{id}", specStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(specStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecStatus() throws Exception {
        // Get the specStatus
        restSpecStatusMockMvc.perform(get("/api/specStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecStatus() throws Exception {
        // Initialize the database
        specStatusRepository.saveAndFlush(specStatus);

		int databaseSizeBeforeUpdate = specStatusRepository.findAll().size();

        // Update the specStatus
        specStatus.setName(UPDATED_NAME);

        restSpecStatusMockMvc.perform(put("/api/specStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specStatus)))
                .andExpect(status().isOk());

        // Validate the SpecStatus in the database
        List<SpecStatus> specStatuss = specStatusRepository.findAll();
        assertThat(specStatuss).hasSize(databaseSizeBeforeUpdate);
        SpecStatus testSpecStatus = specStatuss.get(specStatuss.size() - 1);
        assertThat(testSpecStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteSpecStatus() throws Exception {
        // Initialize the database
        specStatusRepository.saveAndFlush(specStatus);

		int databaseSizeBeforeDelete = specStatusRepository.findAll().size();

        // Get the specStatus
        restSpecStatusMockMvc.perform(delete("/api/specStatuss/{id}", specStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SpecStatus> specStatuss = specStatusRepository.findAll();
        assertThat(specStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
