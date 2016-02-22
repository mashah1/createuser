package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.SupplierSite;
import org.nsf.traqtion.repository.SupplierSiteRepository;
import org.nsf.traqtion.repository.search.SupplierSiteSearchRepository;

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
 * Test class for the SupplierSiteResource REST controller.
 *
 * @see SupplierSiteResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SupplierSiteResourceIntTest {

    private static final String DEFAULT_ATTRIBUTE = "AAAAA";
    private static final String UPDATED_ATTRIBUTE = "BBBBB";
    private static final String DEFAULT_ATTRIBUTE1 = "AAAAA";
    private static final String UPDATED_ATTRIBUTE1 = "BBBBB";

    @Inject
    private SupplierSiteRepository supplierSiteRepository;

    @Inject
    private SupplierSiteSearchRepository supplierSiteSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSupplierSiteMockMvc;

    private SupplierSite supplierSite;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SupplierSiteResource supplierSiteResource = new SupplierSiteResource();
        ReflectionTestUtils.setField(supplierSiteResource, "supplierSiteSearchRepository", supplierSiteSearchRepository);
        ReflectionTestUtils.setField(supplierSiteResource, "supplierSiteRepository", supplierSiteRepository);
        this.restSupplierSiteMockMvc = MockMvcBuilders.standaloneSetup(supplierSiteResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        supplierSite = new SupplierSite();
        supplierSite.setAttribute(DEFAULT_ATTRIBUTE);
        supplierSite.setAttribute1(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void createSupplierSite() throws Exception {
        int databaseSizeBeforeCreate = supplierSiteRepository.findAll().size();

        // Create the SupplierSite

        restSupplierSiteMockMvc.perform(post("/api/supplierSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplierSite)))
                .andExpect(status().isCreated());

        // Validate the SupplierSite in the database
        List<SupplierSite> supplierSites = supplierSiteRepository.findAll();
        assertThat(supplierSites).hasSize(databaseSizeBeforeCreate + 1);
        SupplierSite testSupplierSite = supplierSites.get(supplierSites.size() - 1);
        assertThat(testSupplierSite.getAttribute()).isEqualTo(DEFAULT_ATTRIBUTE);
        assertThat(testSupplierSite.getAttribute1()).isEqualTo(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void getAllSupplierSites() throws Exception {
        // Initialize the database
        supplierSiteRepository.saveAndFlush(supplierSite);

        // Get all the supplierSites
        restSupplierSiteMockMvc.perform(get("/api/supplierSites?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(supplierSite.getId().intValue())))
                .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE.toString())))
                .andExpect(jsonPath("$.[*].attribute1").value(hasItem(DEFAULT_ATTRIBUTE1.toString())));
    }

    @Test
    @Transactional
    public void getSupplierSite() throws Exception {
        // Initialize the database
        supplierSiteRepository.saveAndFlush(supplierSite);

        // Get the supplierSite
        restSupplierSiteMockMvc.perform(get("/api/supplierSites/{id}", supplierSite.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(supplierSite.getId().intValue()))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE.toString()))
            .andExpect(jsonPath("$.attribute1").value(DEFAULT_ATTRIBUTE1.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSupplierSite() throws Exception {
        // Get the supplierSite
        restSupplierSiteMockMvc.perform(get("/api/supplierSites/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSupplierSite() throws Exception {
        // Initialize the database
        supplierSiteRepository.saveAndFlush(supplierSite);

		int databaseSizeBeforeUpdate = supplierSiteRepository.findAll().size();

        // Update the supplierSite
        supplierSite.setAttribute(UPDATED_ATTRIBUTE);
        supplierSite.setAttribute1(UPDATED_ATTRIBUTE1);

        restSupplierSiteMockMvc.perform(put("/api/supplierSites")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(supplierSite)))
                .andExpect(status().isOk());

        // Validate the SupplierSite in the database
        List<SupplierSite> supplierSites = supplierSiteRepository.findAll();
        assertThat(supplierSites).hasSize(databaseSizeBeforeUpdate);
        SupplierSite testSupplierSite = supplierSites.get(supplierSites.size() - 1);
        assertThat(testSupplierSite.getAttribute()).isEqualTo(UPDATED_ATTRIBUTE);
        assertThat(testSupplierSite.getAttribute1()).isEqualTo(UPDATED_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void deleteSupplierSite() throws Exception {
        // Initialize the database
        supplierSiteRepository.saveAndFlush(supplierSite);

		int databaseSizeBeforeDelete = supplierSiteRepository.findAll().size();

        // Get the supplierSite
        restSupplierSiteMockMvc.perform(delete("/api/supplierSites/{id}", supplierSite.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SupplierSite> supplierSites = supplierSiteRepository.findAll();
        assertThat(supplierSites).hasSize(databaseSizeBeforeDelete - 1);
    }
}
