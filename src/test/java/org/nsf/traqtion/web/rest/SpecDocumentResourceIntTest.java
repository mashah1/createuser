package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.SpecDocument;
import org.nsf.traqtion.repository.SpecDocumentRepository;
import org.nsf.traqtion.repository.search.SpecDocumentSearchRepository;

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
 * Test class for the SpecDocumentResource REST controller.
 *
 * @see SpecDocumentResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class SpecDocumentResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAA";
    private static final String UPDATED_NAME = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PATH = "AAAAA";
    private static final String UPDATED_PATH = "BBBBB";
    private static final String DEFAULT_TYPE = "AAAAA";
    private static final String UPDATED_TYPE = "BBBBB";
    private static final String DEFAULT_ATTRIBUTE1 = "AAAAA";
    private static final String UPDATED_ATTRIBUTE1 = "BBBBB";

    @Inject
    private SpecDocumentRepository specDocumentRepository;

    @Inject
    private SpecDocumentSearchRepository specDocumentSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restSpecDocumentMockMvc;

    private SpecDocument specDocument;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        SpecDocumentResource specDocumentResource = new SpecDocumentResource();
        ReflectionTestUtils.setField(specDocumentResource, "specDocumentSearchRepository", specDocumentSearchRepository);
        ReflectionTestUtils.setField(specDocumentResource, "specDocumentRepository", specDocumentRepository);
        this.restSpecDocumentMockMvc = MockMvcBuilders.standaloneSetup(specDocumentResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        specDocument = new SpecDocument();
        specDocument.setName(DEFAULT_NAME);
        specDocument.setDescription(DEFAULT_DESCRIPTION);
        specDocument.setPath(DEFAULT_PATH);
        specDocument.setType(DEFAULT_TYPE);
        specDocument.setAttribute1(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void createSpecDocument() throws Exception {
        int databaseSizeBeforeCreate = specDocumentRepository.findAll().size();

        // Create the SpecDocument

        restSpecDocumentMockMvc.perform(post("/api/specDocuments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specDocument)))
                .andExpect(status().isCreated());

        // Validate the SpecDocument in the database
        List<SpecDocument> specDocuments = specDocumentRepository.findAll();
        assertThat(specDocuments).hasSize(databaseSizeBeforeCreate + 1);
        SpecDocument testSpecDocument = specDocuments.get(specDocuments.size() - 1);
        assertThat(testSpecDocument.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testSpecDocument.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testSpecDocument.getPath()).isEqualTo(DEFAULT_PATH);
        assertThat(testSpecDocument.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testSpecDocument.getAttribute1()).isEqualTo(DEFAULT_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void getAllSpecDocuments() throws Exception {
        // Initialize the database
        specDocumentRepository.saveAndFlush(specDocument);

        // Get all the specDocuments
        restSpecDocumentMockMvc.perform(get("/api/specDocuments?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(specDocument.getId().intValue())))
                .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].path").value(hasItem(DEFAULT_PATH.toString())))
                .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
                .andExpect(jsonPath("$.[*].attribute1").value(hasItem(DEFAULT_ATTRIBUTE1.toString())));
    }

    @Test
    @Transactional
    public void getSpecDocument() throws Exception {
        // Initialize the database
        specDocumentRepository.saveAndFlush(specDocument);

        // Get the specDocument
        restSpecDocumentMockMvc.perform(get("/api/specDocuments/{id}", specDocument.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(specDocument.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.path").value(DEFAULT_PATH.toString()))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.attribute1").value(DEFAULT_ATTRIBUTE1.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingSpecDocument() throws Exception {
        // Get the specDocument
        restSpecDocumentMockMvc.perform(get("/api/specDocuments/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecDocument() throws Exception {
        // Initialize the database
        specDocumentRepository.saveAndFlush(specDocument);

		int databaseSizeBeforeUpdate = specDocumentRepository.findAll().size();

        // Update the specDocument
        specDocument.setName(UPDATED_NAME);
        specDocument.setDescription(UPDATED_DESCRIPTION);
        specDocument.setPath(UPDATED_PATH);
        specDocument.setType(UPDATED_TYPE);
        specDocument.setAttribute1(UPDATED_ATTRIBUTE1);

        restSpecDocumentMockMvc.perform(put("/api/specDocuments")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(specDocument)))
                .andExpect(status().isOk());

        // Validate the SpecDocument in the database
        List<SpecDocument> specDocuments = specDocumentRepository.findAll();
        assertThat(specDocuments).hasSize(databaseSizeBeforeUpdate);
        SpecDocument testSpecDocument = specDocuments.get(specDocuments.size() - 1);
        assertThat(testSpecDocument.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testSpecDocument.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testSpecDocument.getPath()).isEqualTo(UPDATED_PATH);
        assertThat(testSpecDocument.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testSpecDocument.getAttribute1()).isEqualTo(UPDATED_ATTRIBUTE1);
    }

    @Test
    @Transactional
    public void deleteSpecDocument() throws Exception {
        // Initialize the database
        specDocumentRepository.saveAndFlush(specDocument);

		int databaseSizeBeforeDelete = specDocumentRepository.findAll().size();

        // Get the specDocument
        restSpecDocumentMockMvc.perform(delete("/api/specDocuments/{id}", specDocument.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<SpecDocument> specDocuments = specDocumentRepository.findAll();
        assertThat(specDocuments).hasSize(databaseSizeBeforeDelete - 1);
    }
}
