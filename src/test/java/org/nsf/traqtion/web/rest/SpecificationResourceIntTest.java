package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.Specification;
import org.nsf.traqtion.repository.SpecificationRepository;
import org.nsf.traqtion.repository.search.SpecificationSearchRepository;

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
 * Test class for the SpecificationResource REST controller.
 *
 * @see SpecificationResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpecificationResourceIntTest {

    private static final String DEFAULT_ATTRIBUTE = "AAAAA";
    private static final String UPDATED_ATTRIBUTE = "BBBBB";
    private static final String DEFAULT_ATTRIBUTE1 = "AAAAA";
    private static final String UPDATED_ATTRIBUTE1 = "BBBBB";
    private static final String DEFAULT_VERSION = "AAAAA";
    private static final String UPDATED_VERSION = "BBBBB";

    @Inject
    private SpecificationRepository specificationRepository;

    @Inject
    private SpecificationSearchRepository specificationSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpecificationMockMvc;

    private Specification specification;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecificationResource specificationResource = new SpecificationResource();
        ReflectionTestUtils.setField(specificationResource, "specificationSearchRepository", specificationSearchRepository);
        ReflectionTestUtils.setField(specificationResource, "specificationRepository", specificationRepository);
        this.restSpecificationMockMvc = MockMvcBuilders.standaloneSetup(specificationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        specification = new Specification();
        specification.setAttribute(DEFAULT_ATTRIBUTE);
        specification.setAttribute1(DEFAULT_ATTRIBUTE1);
        specification.setVersion(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void createSpecification() throws Exception {
        int databaseSizeBeforeCreate = specificationRepository.findAll().size();

        // Create the Specification

        restSpecificationMockMvc.perform(post("/api/specifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specification)))
                .andExpect(status().isCreated());

        // Validate the Specification in the database
        List<Specification> specifications = specificationRepository.findAll();
        assertThat(specifications).hasSize(databaseSizeBeforeCreate + 1);
        Specification testSpecification = specifications.get(specifications.size() - 1);
        assertThat(testSpecification.getAttribute()).isEqualTo(DEFAULT_ATTRIBUTE);
        assertThat(testSpecification.getAttribute1()).isEqualTo(DEFAULT_ATTRIBUTE1);
        assertThat(testSpecification.getVersion()).isEqualTo(DEFAULT_VERSION);
    }

    @Test
    @Transactional
    public void getAllSpecifications() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

        // Get all the specifications
        restSpecificationMockMvc.perform(get("/api/specifications?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(specification.getId().intValue())))
                .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE.toString())))
                .andExpect(jsonPath("$.[*].attribute1").value(hasItem(DEFAULT_ATTRIBUTE1.toString())))
                .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION.toString())));
    }

    @Test
    @Transactional
    public void getSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

        // Get the specification
        restSpecificationMockMvc.perform(get("/api/specifications/{id}", specification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(specification.getId().intValue()))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE.toString()))
            .andExpect(jsonPath("$.attribute1").value(DEFAULT_ATTRIBUTE1.toString()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecification() throws Exception {
        // Get the specification
        restSpecificationMockMvc.perform(get("/api/specifications/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

		int databaseSizeBeforeUpdate = specificationRepository.findAll().size();

        // Update the specification
        specification.setAttribute(UPDATED_ATTRIBUTE);
        specification.setAttribute1(UPDATED_ATTRIBUTE1);
        specification.setVersion(UPDATED_VERSION);

        restSpecificationMockMvc.perform(put("/api/specifications")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specification)))
                .andExpect(status().isOk());

        // Validate the Specification in the database
        List<Specification> specifications = specificationRepository.findAll();
        assertThat(specifications).hasSize(databaseSizeBeforeUpdate);
        Specification testSpecification = specifications.get(specifications.size() - 1);
        assertThat(testSpecification.getAttribute()).isEqualTo(UPDATED_ATTRIBUTE);
        assertThat(testSpecification.getAttribute1()).isEqualTo(UPDATED_ATTRIBUTE1);
        assertThat(testSpecification.getVersion()).isEqualTo(UPDATED_VERSION);
    }

    @Test
    @Transactional
    public void deleteSpecification() throws Exception {
        // Initialize the database
        specificationRepository.saveAndFlush(specification);

		int databaseSizeBeforeDelete = specificationRepository.findAll().size();

        // Get the specification
        restSpecificationMockMvc.perform(delete("/api/specifications/{id}", specification.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Specification> specifications = specificationRepository.findAll();
        assertThat(specifications).hasSize(databaseSizeBeforeDelete - 1);
    }
}
