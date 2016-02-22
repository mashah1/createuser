package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.BusinessRelationship;
import org.nsf.traqtion.repository.BusinessRelationshipRepository;
import org.nsf.traqtion.repository.search.BusinessRelationshipSearchRepository;

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
 * Test class for the BusinessRelationshipResource REST controller.
 *
 * @see BusinessRelationshipResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BusinessRelationshipResourceIntTest {


    @Inject
    private BusinessRelationshipRepository businessRelationshipRepository;

    @Inject
    private BusinessRelationshipSearchRepository businessRelationshipSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusinessRelationshipMockMvc;

    private BusinessRelationship businessRelationship;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessRelationshipResource businessRelationshipResource = new BusinessRelationshipResource();
        ReflectionTestUtils.setField(businessRelationshipResource, "businessRelationshipSearchRepository", businessRelationshipSearchRepository);
        ReflectionTestUtils.setField(businessRelationshipResource, "businessRelationshipRepository", businessRelationshipRepository);
        this.restBusinessRelationshipMockMvc = MockMvcBuilders.standaloneSetup(businessRelationshipResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        businessRelationship = new BusinessRelationship();
    }

    @Test
    @Transactional
    public void createBusinessRelationship() throws Exception {
        int databaseSizeBeforeCreate = businessRelationshipRepository.findAll().size();

        // Create the BusinessRelationship

        restBusinessRelationshipMockMvc.perform(post("/api/businessRelationships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessRelationship)))
                .andExpect(status().isCreated());

        // Validate the BusinessRelationship in the database
        List<BusinessRelationship> businessRelationships = businessRelationshipRepository.findAll();
        assertThat(businessRelationships).hasSize(databaseSizeBeforeCreate + 1);
        BusinessRelationship testBusinessRelationship = businessRelationships.get(businessRelationships.size() - 1);
    }

    @Test
    @Transactional
    public void getAllBusinessRelationships() throws Exception {
        // Initialize the database
        businessRelationshipRepository.saveAndFlush(businessRelationship);

        // Get all the businessRelationships
        restBusinessRelationshipMockMvc.perform(get("/api/businessRelationships?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(businessRelationship.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBusinessRelationship() throws Exception {
        // Initialize the database
        businessRelationshipRepository.saveAndFlush(businessRelationship);

        // Get the businessRelationship
        restBusinessRelationshipMockMvc.perform(get("/api/businessRelationships/{id}", businessRelationship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(businessRelationship.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessRelationship() throws Exception {
        // Get the businessRelationship
        restBusinessRelationshipMockMvc.perform(get("/api/businessRelationships/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessRelationship() throws Exception {
        // Initialize the database
        businessRelationshipRepository.saveAndFlush(businessRelationship);

		int databaseSizeBeforeUpdate = businessRelationshipRepository.findAll().size();

        // Update the businessRelationship

        restBusinessRelationshipMockMvc.perform(put("/api/businessRelationships")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessRelationship)))
                .andExpect(status().isOk());

        // Validate the BusinessRelationship in the database
        List<BusinessRelationship> businessRelationships = businessRelationshipRepository.findAll();
        assertThat(businessRelationships).hasSize(databaseSizeBeforeUpdate);
        BusinessRelationship testBusinessRelationship = businessRelationships.get(businessRelationships.size() - 1);
    }

    @Test
    @Transactional
    public void deleteBusinessRelationship() throws Exception {
        // Initialize the database
        businessRelationshipRepository.saveAndFlush(businessRelationship);

		int databaseSizeBeforeDelete = businessRelationshipRepository.findAll().size();

        // Get the businessRelationship
        restBusinessRelationshipMockMvc.perform(delete("/api/businessRelationships/{id}", businessRelationship.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BusinessRelationship> businessRelationships = businessRelationshipRepository.findAll();
        assertThat(businessRelationships).hasSize(databaseSizeBeforeDelete - 1);
    }
}
