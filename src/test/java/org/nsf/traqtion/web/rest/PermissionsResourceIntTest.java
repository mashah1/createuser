package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.Permissions;
import org.nsf.traqtion.repository.PermissionsRepository;
import org.nsf.traqtion.repository.search.PermissionsSearchRepository;

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
 * Test class for the PermissionsResource REST controller.
 *
 * @see PermissionsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class PermissionsResourceIntTest {

    private static final String DEFAULT_ATTRIBUTE = "AAAAA";
    private static final String UPDATED_ATTRIBUTE = "BBBBB";

    @Inject
    private PermissionsRepository permissionsRepository;

    @Inject
    private PermissionsSearchRepository permissionsSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restPermissionsMockMvc;

    private Permissions permissions;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        PermissionsResource permissionsResource = new PermissionsResource();
        ReflectionTestUtils.setField(permissionsResource, "permissionsSearchRepository", permissionsSearchRepository);
        ReflectionTestUtils.setField(permissionsResource, "permissionsRepository", permissionsRepository);
        this.restPermissionsMockMvc = MockMvcBuilders.standaloneSetup(permissionsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        permissions = new Permissions();
        permissions.setAttribute(DEFAULT_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void createPermissions() throws Exception {
        int databaseSizeBeforeCreate = permissionsRepository.findAll().size();

        // Create the Permissions

        restPermissionsMockMvc.perform(post("/api/permissionss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permissions)))
                .andExpect(status().isCreated());

        // Validate the Permissions in the database
        List<Permissions> permissionss = permissionsRepository.findAll();
        assertThat(permissionss).hasSize(databaseSizeBeforeCreate + 1);
        Permissions testPermissions = permissionss.get(permissionss.size() - 1);
        assertThat(testPermissions.getAttribute()).isEqualTo(DEFAULT_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void getAllPermissionss() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get all the permissionss
        restPermissionsMockMvc.perform(get("/api/permissionss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(permissions.getId().intValue())))
                .andExpect(jsonPath("$.[*].attribute").value(hasItem(DEFAULT_ATTRIBUTE.toString())));
    }

    @Test
    @Transactional
    public void getPermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

        // Get the permissions
        restPermissionsMockMvc.perform(get("/api/permissionss/{id}", permissions.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(permissions.getId().intValue()))
            .andExpect(jsonPath("$.attribute").value(DEFAULT_ATTRIBUTE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPermissions() throws Exception {
        // Get the permissions
        restPermissionsMockMvc.perform(get("/api/permissionss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

		int databaseSizeBeforeUpdate = permissionsRepository.findAll().size();

        // Update the permissions
        permissions.setAttribute(UPDATED_ATTRIBUTE);

        restPermissionsMockMvc.perform(put("/api/permissionss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(permissions)))
                .andExpect(status().isOk());

        // Validate the Permissions in the database
        List<Permissions> permissionss = permissionsRepository.findAll();
        assertThat(permissionss).hasSize(databaseSizeBeforeUpdate);
        Permissions testPermissions = permissionss.get(permissionss.size() - 1);
        assertThat(testPermissions.getAttribute()).isEqualTo(UPDATED_ATTRIBUTE);
    }

    @Test
    @Transactional
    public void deletePermissions() throws Exception {
        // Initialize the database
        permissionsRepository.saveAndFlush(permissions);

		int databaseSizeBeforeDelete = permissionsRepository.findAll().size();

        // Get the permissions
        restPermissionsMockMvc.perform(delete("/api/permissionss/{id}", permissions.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Permissions> permissionss = permissionsRepository.findAll();
        assertThat(permissionss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
