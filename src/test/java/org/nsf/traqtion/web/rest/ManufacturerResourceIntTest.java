package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.Manufacturer;
import org.nsf.traqtion.repository.ManufacturerRepository;
import org.nsf.traqtion.repository.search.ManufacturerSearchRepository;

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
 * Test class for the ManufacturerResource REST controller.
 *
 * @see ManufacturerResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ManufacturerResourceIntTest {


    @Inject
    private ManufacturerRepository manufacturerRepository;

    @Inject
    private ManufacturerSearchRepository manufacturerSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restManufacturerMockMvc;

    private Manufacturer manufacturer;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ManufacturerResource manufacturerResource = new ManufacturerResource();
        ReflectionTestUtils.setField(manufacturerResource, "manufacturerSearchRepository", manufacturerSearchRepository);
        ReflectionTestUtils.setField(manufacturerResource, "manufacturerRepository", manufacturerRepository);
        this.restManufacturerMockMvc = MockMvcBuilders.standaloneSetup(manufacturerResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        manufacturer = new Manufacturer();
    }

    @Test
    @Transactional
    public void createManufacturer() throws Exception {
        int databaseSizeBeforeCreate = manufacturerRepository.findAll().size();

        // Create the Manufacturer

        restManufacturerMockMvc.perform(post("/api/manufacturers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(manufacturer)))
                .andExpect(status().isCreated());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        assertThat(manufacturers).hasSize(databaseSizeBeforeCreate + 1);
        Manufacturer testManufacturer = manufacturers.get(manufacturers.size() - 1);
    }

    @Test
    @Transactional
    public void getAllManufacturers() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get all the manufacturers
        restManufacturerMockMvc.perform(get("/api/manufacturers?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(manufacturer.getId().intValue())));
    }

    @Test
    @Transactional
    public void getManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

        // Get the manufacturer
        restManufacturerMockMvc.perform(get("/api/manufacturers/{id}", manufacturer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(manufacturer.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingManufacturer() throws Exception {
        // Get the manufacturer
        restManufacturerMockMvc.perform(get("/api/manufacturers/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

		int databaseSizeBeforeUpdate = manufacturerRepository.findAll().size();

        // Update the manufacturer

        restManufacturerMockMvc.perform(put("/api/manufacturers")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(manufacturer)))
                .andExpect(status().isOk());

        // Validate the Manufacturer in the database
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        assertThat(manufacturers).hasSize(databaseSizeBeforeUpdate);
        Manufacturer testManufacturer = manufacturers.get(manufacturers.size() - 1);
    }

    @Test
    @Transactional
    public void deleteManufacturer() throws Exception {
        // Initialize the database
        manufacturerRepository.saveAndFlush(manufacturer);

		int databaseSizeBeforeDelete = manufacturerRepository.findAll().size();

        // Get the manufacturer
        restManufacturerMockMvc.perform(delete("/api/manufacturers/{id}", manufacturer.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Manufacturer> manufacturers = manufacturerRepository.findAll();
        assertThat(manufacturers).hasSize(databaseSizeBeforeDelete - 1);
    }
}
