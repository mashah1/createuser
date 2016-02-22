package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.BusinessRelationshipType;
import org.nsf.traqtion.repository.BusinessRelationshipTypeRepository;
import org.nsf.traqtion.repository.search.BusinessRelationshipTypeSearchRepository;

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
 * Test class for the BusinessRelationshipTypeResource REST controller.
 *
 * @see BusinessRelationshipTypeResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BusinessRelationshipTypeResourceIntTest {


    @Inject
    private BusinessRelationshipTypeRepository businessRelationshipTypeRepository;

    @Inject
    private BusinessRelationshipTypeSearchRepository businessRelationshipTypeSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusinessRelationshipTypeMockMvc;

    private BusinessRelationshipType businessRelationshipType;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessRelationshipTypeResource businessRelationshipTypeResource = new BusinessRelationshipTypeResource();
        ReflectionTestUtils.setField(businessRelationshipTypeResource, "businessRelationshipTypeSearchRepository", businessRelationshipTypeSearchRepository);
        ReflectionTestUtils.setField(businessRelationshipTypeResource, "businessRelationshipTypeRepository", businessRelationshipTypeRepository);
        this.restBusinessRelationshipTypeMockMvc = MockMvcBuilders.standaloneSetup(businessRelationshipTypeResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        businessRelationshipType = new BusinessRelationshipType();
    }

    @Test
    @Transactional
    public void createBusinessRelationshipType() throws Exception {
        int databaseSizeBeforeCreate = businessRelationshipTypeRepository.findAll().size();

        // Create the BusinessRelationshipType

        restBusinessRelationshipTypeMockMvc.perform(post("/api/businessRelationshipTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessRelationshipType)))
                .andExpect(status().isCreated());

        // Validate the BusinessRelationshipType in the database
        List<BusinessRelationshipType> businessRelationshipTypes = businessRelationshipTypeRepository.findAll();
        assertThat(businessRelationshipTypes).hasSize(databaseSizeBeforeCreate + 1);
        BusinessRelationshipType testBusinessRelationshipType = businessRelationshipTypes.get(businessRelationshipTypes.size() - 1);
    }

    @Test
    @Transactional
    public void getAllBusinessRelationshipTypes() throws Exception {
        // Initialize the database
        businessRelationshipTypeRepository.saveAndFlush(businessRelationshipType);

        // Get all the businessRelationshipTypes
        restBusinessRelationshipTypeMockMvc.perform(get("/api/businessRelationshipTypes?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(businessRelationshipType.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBusinessRelationshipType() throws Exception {
        // Initialize the database
        businessRelationshipTypeRepository.saveAndFlush(businessRelationshipType);

        // Get the businessRelationshipType
        restBusinessRelationshipTypeMockMvc.perform(get("/api/businessRelationshipTypes/{id}", businessRelationshipType.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(businessRelationshipType.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBusinessRelationshipType() throws Exception {
        // Get the businessRelationshipType
        restBusinessRelationshipTypeMockMvc.perform(get("/api/businessRelationshipTypes/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusinessRelationshipType() throws Exception {
        // Initialize the database
        businessRelationshipTypeRepository.saveAndFlush(businessRelationshipType);

		int databaseSizeBeforeUpdate = businessRelationshipTypeRepository.findAll().size();

        // Update the businessRelationshipType

        restBusinessRelationshipTypeMockMvc.perform(put("/api/businessRelationshipTypes")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(businessRelationshipType)))
                .andExpect(status().isOk());

        // Validate the BusinessRelationshipType in the database
        List<BusinessRelationshipType> businessRelationshipTypes = businessRelationshipTypeRepository.findAll();
        assertThat(businessRelationshipTypes).hasSize(databaseSizeBeforeUpdate);
        BusinessRelationshipType testBusinessRelationshipType = businessRelationshipTypes.get(businessRelationshipTypes.size() - 1);
    }

    @Test
    @Transactional
    public void deleteBusinessRelationshipType() throws Exception {
        // Initialize the database
        businessRelationshipTypeRepository.saveAndFlush(businessRelationshipType);

		int databaseSizeBeforeDelete = businessRelationshipTypeRepository.findAll().size();

        // Get the businessRelationshipType
        restBusinessRelationshipTypeMockMvc.perform(delete("/api/businessRelationshipTypes/{id}", businessRelationshipType.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<BusinessRelationshipType> businessRelationshipTypes = businessRelationshipTypeRepository.findAll();
        assertThat(businessRelationshipTypes).hasSize(databaseSizeBeforeDelete - 1);
    }
}
