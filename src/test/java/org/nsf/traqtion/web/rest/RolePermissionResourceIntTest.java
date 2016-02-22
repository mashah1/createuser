package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.RolePermission;
import org.nsf.traqtion.repository.RolePermissionRepository;
import org.nsf.traqtion.repository.search.RolePermissionSearchRepository;

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
 * Test class for the RolePermissionResource REST controller.
 *
 * @see RolePermissionResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class RolePermissionResourceIntTest {


    @Inject
    private RolePermissionRepository rolePermissionRepository;

    @Inject
    private RolePermissionSearchRepository rolePermissionSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restRolePermissionMockMvc;

    private RolePermission rolePermission;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        RolePermissionResource rolePermissionResource = new RolePermissionResource();
        ReflectionTestUtils.setField(rolePermissionResource, "rolePermissionSearchRepository", rolePermissionSearchRepository);
        ReflectionTestUtils.setField(rolePermissionResource, "rolePermissionRepository", rolePermissionRepository);
        this.restRolePermissionMockMvc = MockMvcBuilders.standaloneSetup(rolePermissionResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        rolePermission = new RolePermission();
    }

    @Test
    @Transactional
    public void createRolePermission() throws Exception {
        int databaseSizeBeforeCreate = rolePermissionRepository.findAll().size();

        // Create the RolePermission

        restRolePermissionMockMvc.perform(post("/api/rolePermissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rolePermission)))
                .andExpect(status().isCreated());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        assertThat(rolePermissions).hasSize(databaseSizeBeforeCreate + 1);
        RolePermission testRolePermission = rolePermissions.get(rolePermissions.size() - 1);
    }

    @Test
    @Transactional
    public void getAllRolePermissions() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get all the rolePermissions
        restRolePermissionMockMvc.perform(get("/api/rolePermissions?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(rolePermission.getId().intValue())));
    }

    @Test
    @Transactional
    public void getRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

        // Get the rolePermission
        restRolePermissionMockMvc.perform(get("/api/rolePermissions/{id}", rolePermission.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(rolePermission.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingRolePermission() throws Exception {
        // Get the rolePermission
        restRolePermissionMockMvc.perform(get("/api/rolePermissions/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

		int databaseSizeBeforeUpdate = rolePermissionRepository.findAll().size();

        // Update the rolePermission

        restRolePermissionMockMvc.perform(put("/api/rolePermissions")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(rolePermission)))
                .andExpect(status().isOk());

        // Validate the RolePermission in the database
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        assertThat(rolePermissions).hasSize(databaseSizeBeforeUpdate);
        RolePermission testRolePermission = rolePermissions.get(rolePermissions.size() - 1);
    }

    @Test
    @Transactional
    public void deleteRolePermission() throws Exception {
        // Initialize the database
        rolePermissionRepository.saveAndFlush(rolePermission);

		int databaseSizeBeforeDelete = rolePermissionRepository.findAll().size();

        // Get the rolePermission
        restRolePermissionMockMvc.perform(delete("/api/rolePermissions/{id}", rolePermission.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<RolePermission> rolePermissions = rolePermissionRepository.findAll();
        assertThat(rolePermissions).hasSize(databaseSizeBeforeDelete - 1);
    }
}
