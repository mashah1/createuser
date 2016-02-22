package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.ProductStatus;
import org.nsf.traqtion.repository.ProductStatusRepository;
import org.nsf.traqtion.repository.search.ProductStatusSearchRepository;

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
 * Test class for the ProductStatusResource REST controller.
 *
 * @see ProductStatusResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class ProductStatusResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";

    @Inject
    private ProductStatusRepository productStatusRepository;

    @Inject
    private ProductStatusSearchRepository productStatusSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restProductStatusMockMvc;

    private ProductStatus productStatus;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        ProductStatusResource productStatusResource = new ProductStatusResource();
        ReflectionTestUtils.setField(productStatusResource, "productStatusSearchRepository", productStatusSearchRepository);
        ReflectionTestUtils.setField(productStatusResource, "productStatusRepository", productStatusRepository);
        this.restProductStatusMockMvc = MockMvcBuilders.standaloneSetup(productStatusResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        productStatus = new ProductStatus();
        productStatus.setName(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createProductStatus() throws Exception {
        int databaseSizeBeforeCreate = productStatusRepository.findAll().size();

        // Create the ProductStatus

        restProductStatusMockMvc.perform(post("/api/productStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productStatus)))
                .andExpect(status().isCreated());

        // Validate the ProductStatus in the database
        List<ProductStatus> productStatuss = productStatusRepository.findAll();
        assertThat(productStatuss).hasSize(databaseSizeBeforeCreate + 1);
        ProductStatus testProductStatus = productStatuss.get(productStatuss.size() - 1);
        assertThat(testProductStatus.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void getAllProductStatuss() throws Exception {
        // Initialize the database
        productStatusRepository.saveAndFlush(productStatus);

        // Get all the productStatuss
        restProductStatusMockMvc.perform(get("/api/productStatuss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(productStatus.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())));
    }

    @Test
    @Transactional
    public void getProductStatus() throws Exception {
        // Initialize the database
        productStatusRepository.saveAndFlush(productStatus);

        // Get the productStatus
        restProductStatusMockMvc.perform(get("/api/productStatuss/{id}", productStatus.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(productStatus.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingProductStatus() throws Exception {
        // Get the productStatus
        restProductStatusMockMvc.perform(get("/api/productStatuss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateProductStatus() throws Exception {
        // Initialize the database
        productStatusRepository.saveAndFlush(productStatus);

		int databaseSizeBeforeUpdate = productStatusRepository.findAll().size();

        // Update the productStatus
        productStatus.setName(UPDATED_NAME);

        restProductStatusMockMvc.perform(put("/api/productStatuss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(productStatus)))
                .andExpect(status().isOk());

        // Validate the ProductStatus in the database
        List<ProductStatus> productStatuss = productStatusRepository.findAll();
        assertThat(productStatuss).hasSize(databaseSizeBeforeUpdate);
        ProductStatus testProductStatus = productStatuss.get(productStatuss.size() - 1);
        assertThat(testProductStatus.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void deleteProductStatus() throws Exception {
        // Initialize the database
        productStatusRepository.saveAndFlush(productStatus);

		int databaseSizeBeforeDelete = productStatusRepository.findAll().size();

        // Get the productStatus
        restProductStatusMockMvc.perform(delete("/api/productStatuss/{id}", productStatus.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<ProductStatus> productStatuss = productStatusRepository.findAll();
        assertThat(productStatuss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
