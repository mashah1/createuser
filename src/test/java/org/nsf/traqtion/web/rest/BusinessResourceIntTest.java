package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.Business;
import org.nsf.traqtion.repository.BusinessRepository;
import org.nsf.traqtion.repository.search.BusinessSearchRepository;

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
 * Test class for the BusinessResource REST controller.
 *
 * @see BusinessResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BusinessResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DISPLAY_NAME = "AAAAA";
    private static final String UPDATED_DISPLAY_NAME = "BBBBB";
    private static final String DEFAULT_ADDRESS = "AAAAA";
    private static final String UPDATED_ADDRESS = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";

    @Inject
    private BusinessRepository businessRepository;

    @Inject
    private BusinessSearchRepository businessSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBusinessMockMvc;

    private Business business;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BusinessResource businessResource = new BusinessResource();
        ReflectionTestUtils.setField(businessResource, "businessSearchRepository", businessSearchRepository);
        ReflectionTestUtils.setField(businessResource, "businessRepository", businessRepository);
        this.restBusinessMockMvc = MockMvcBuilders.standaloneSetup(businessResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        business = new Business();
        business.setName(DEFAULT_NAME);
        business.setDisplayName(DEFAULT_DISPLAY_NAME);
        business.setAddress(DEFAULT_ADDRESS);
        business.setDescription(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createBusiness() throws Exception {
        int databaseSizeBeforeCreate = businessRepository.findAll().size();

        // Create the Business

        restBusinessMockMvc.perform(post("/api/businesss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(business)))
                .andExpect(status().isCreated());

        // Validate the Business in the database
        List<Business> businesss = businessRepository.findAll();
        assertThat(businesss).hasSize(databaseSizeBeforeCreate + 1);
        Business testBusiness = businesss.get(businesss.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testBusiness.getDisplayName()).isEqualTo(DEFAULT_DISPLAY_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testBusiness.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBusinesss() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get all the businesss
        restBusinessMockMvc.perform(get("/api/businesss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(business.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].displayName").value(hasItem(DEFAULT_DISPLAY_NAME.toString())))
                .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

        // Get the business
        restBusinessMockMvc.perform(get("/api/businesss/{id}", business.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(business.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.displayName").value(DEFAULT_DISPLAY_NAME.toString()))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBusiness() throws Exception {
        // Get the business
        restBusinessMockMvc.perform(get("/api/businesss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

		int databaseSizeBeforeUpdate = businessRepository.findAll().size();

        // Update the business
        business.setName(UPDATED_NAME);
        business.setDisplayName(UPDATED_DISPLAY_NAME);
        business.setAddress(UPDATED_ADDRESS);
        business.setDescription(UPDATED_DESCRIPTION);

        restBusinessMockMvc.perform(put("/api/businesss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(business)))
                .andExpect(status().isOk());

        // Validate the Business in the database
        List<Business> businesss = businessRepository.findAll();
        assertThat(businesss).hasSize(databaseSizeBeforeUpdate);
        Business testBusiness = businesss.get(businesss.size() - 1);
        assertThat(testBusiness.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testBusiness.getDisplayName()).isEqualTo(UPDATED_DISPLAY_NAME);
        assertThat(testBusiness.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testBusiness.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteBusiness() throws Exception {
        // Initialize the database
        businessRepository.saveAndFlush(business);

		int databaseSizeBeforeDelete = businessRepository.findAll().size();

        // Get the business
        restBusinessMockMvc.perform(delete("/api/businesss/{id}", business.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Business> businesss = businessRepository.findAll();
        assertThat(businesss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
