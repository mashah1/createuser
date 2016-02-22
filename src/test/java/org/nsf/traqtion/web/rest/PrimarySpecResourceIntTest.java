package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.PrimarySpec;
import org.nsf.traqtion.repository.PrimarySpecRepository;
import org.nsf.traqtion.repository.search.PrimarySpecSearchRepository;

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
 * Test class for the PrimarySpecResource REST controller.
 *
 * @see PrimarySpecResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PrimarySpecResourceIntTest {

    private static final String DEFAULT_ATTRIBUTE = "AAAAA";
    private static final String UPDATED_ATTRIBUTE = "BBBBB";
    private static final String DEFAULT_ATTRIBUTE1 = "AAAAA";
    private static final String UPDATED_ATTRIBUTE1 = "BBBBB";

    @Inject
    private PrimarySpecRepository primarySpecRepository;

    @Inject
    private PrimarySpecSearchRepository primarySpecSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPrimarySpecMockMvc;

    private PrimarySpec primarySpec;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PrimarySpecResource primarySpecResource = new PrimarySpecResource();
        ReflectionTestUtils.setField(primarySpecResource, "primarySpecSearchRepository", primarySpecSearchRepository);
        ReflectionTestUtils.setField(primarySpecResource, "primarySpecRepository", primarySpecRepository);
        this.restPrimarySpecMockMvc = MockMvcBuilders.standaloneSetup(primarySpecResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        primarySpec = new PrimarySpec();
        primarySpec.setAttribute(DEFAULT_ATTRIBUTE);
        primarySpec.setAttribute1(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void createPrimarySpec() throws Exception {
        int databaseSizeBeforeCreate = primarySpecRepository.findAll().size();

        // Create the PrimarySpec

        restPrimarySpecMockMvc.perform(post("/api/primarySpecs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primarySpec)))
                .andExpect(status().isCreated());

        // Validate the PrimarySpec in the database
        List<PrimarySpec> primarySpecs = primarySpecRepository.findAll();
        assertThat(primarySpecs).hasSize(databaseSizeBeforeCreate + 1);
        PrimarySpec testPrimarySpec = primarySpecs.get(primarySpecs.size() - 1);
        assertThat(testPrimarySpec.getAttribute()).isEqualTo(DEFAULT_ATTRIBUTE);
        assertThat(testPrimarySpec.getAttribute1()).isEqualTo(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void getAllPrimarySpecs() throws Exception {
        // Initialize the database
        primarySpecRepository.saveAndFlush(primarySpec);

        // Get all the primarySpecs
        restPrimarySpecMockMvc.perform(get("/api/primarySpecs?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(primarySpec.getId().intValue())))
                .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE.toString())))
                .andExpect(jsonPath("$.[*].attribute1").value(hasItem(DEFAULT_ATTRIBUTE1.toString())));
    }

    @Test
    @Transactional
    public void getPrimarySpec() throws Exception {
        // Initialize the database
        primarySpecRepository.saveAndFlush(primarySpec);

        // Get the primarySpec
        restPrimarySpecMockMvc.perform(get("/api/primarySpecs/{id}", primarySpec.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(primarySpec.getId().intValue()))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE.toString()))
            .andExpect(jsonPath("$.attribute1").value(DEFAULT_ATTRIBUTE1.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPrimarySpec() throws Exception {
        // Get the primarySpec
        restPrimarySpecMockMvc.perform(get("/api/primarySpecs/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePrimarySpec() throws Exception {
        // Initialize the database
        primarySpecRepository.saveAndFlush(primarySpec);

		int databaseSizeBeforeUpdate = primarySpecRepository.findAll().size();

        // Update the primarySpec
        primarySpec.setAttribute(UPDATED_ATTRIBUTE);
        primarySpec.setAttribute1(UPDATED_ATTRIBUTE1);

        restPrimarySpecMockMvc.perform(put("/api/primarySpecs")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(primarySpec)))
                .andExpect(status().isOk());

        // Validate the PrimarySpec in the database
        List<PrimarySpec> primarySpecs = primarySpecRepository.findAll();
        assertThat(primarySpecs).hasSize(databaseSizeBeforeUpdate);
        PrimarySpec testPrimarySpec = primarySpecs.get(primarySpecs.size() - 1);
        assertThat(testPrimarySpec.getAttribute()).isEqualTo(UPDATED_ATTRIBUTE);
        assertThat(testPrimarySpec.getAttribute1()).isEqualTo(UPDATED_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void deletePrimarySpec() throws Exception {
        // Initialize the database
        primarySpecRepository.saveAndFlush(primarySpec);

		int databaseSizeBeforeDelete = primarySpecRepository.findAll().size();

        // Get the primarySpec
        restPrimarySpecMockMvc.perform(delete("/api/primarySpecs/{id}", primarySpec.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<PrimarySpec> primarySpecs = primarySpecRepository.findAll();
        assertThat(primarySpecs).hasSize(databaseSizeBeforeDelete - 1);
    }
}
