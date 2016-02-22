package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.ServiceProvider;
import org.nsf.traqtion.repository.ServiceProviderRepository;
import org.nsf.traqtion.repository.search.ServiceProviderSearchRepository;

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
 * Test class for the ServiceProviderResource REST controller.
 *
 * @see ServiceProviderResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ServiceProviderResourceIntTest {


    @Inject
    private ServiceProviderRepository serviceProviderRepository;

    @Inject
    private ServiceProviderSearchRepository serviceProviderSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restServiceProviderMockMvc;

    private ServiceProvider serviceProvider;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ServiceProviderResource serviceProviderResource = new ServiceProviderResource();
        ReflectionTestUtils.setField(serviceProviderResource, "serviceProviderSearchRepository", serviceProviderSearchRepository);
        ReflectionTestUtils.setField(serviceProviderResource, "serviceProviderRepository", serviceProviderRepository);
        this.restServiceProviderMockMvc = MockMvcBuilders.standaloneSetup(serviceProviderResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        serviceProvider = new ServiceProvider();
    }

    @Test
    @Transactional
    public void createServiceProvider() throws Exception {
        int databaseSizeBeforeCreate = serviceProviderRepository.findAll().size();

        // Create the ServiceProvider

        restServiceProviderMockMvc.perform(post("/api/serviceProviders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceProvider)))
                .andExpect(status().isCreated());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAll();
        assertThat(serviceProviders).hasSize(databaseSizeBeforeCreate + 1);
        ServiceProvider testServiceProvider = serviceProviders.get(serviceProviders.size() - 1);
    }

    @Test
    @Transactional
    public void getAllServiceProviders() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get all the serviceProviders
        restServiceProviderMockMvc.perform(get("/api/serviceProviders?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(serviceProvider.getId().intValue())));
    }

    @Test
    @Transactional
    public void getServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get("/api/serviceProviders/{id}", serviceProvider.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(serviceProvider.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingServiceProvider() throws Exception {
        // Get the serviceProvider
        restServiceProviderMockMvc.perform(get("/api/serviceProviders/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

		int databaseSizeBeforeUpdate = serviceProviderRepository.findAll().size();

        // Update the serviceProvider

        restServiceProviderMockMvc.perform(put("/api/serviceProviders")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(serviceProvider)))
                .andExpect(status().isOk());

        // Validate the ServiceProvider in the database
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAll();
        assertThat(serviceProviders).hasSize(databaseSizeBeforeUpdate);
        ServiceProvider testServiceProvider = serviceProviders.get(serviceProviders.size() - 1);
    }

    @Test
    @Transactional
    public void deleteServiceProvider() throws Exception {
        // Initialize the database
        serviceProviderRepository.saveAndFlush(serviceProvider);

		int databaseSizeBeforeDelete = serviceProviderRepository.findAll().size();

        // Get the serviceProvider
        restServiceProviderMockMvc.perform(delete("/api/serviceProviders/{id}", serviceProvider.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ServiceProvider> serviceProviders = serviceProviderRepository.findAll();
        assertThat(serviceProviders).hasSize(databaseSizeBeforeDelete - 1);
    }
}
