package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.UserBusinessRole;
import org.nsf.traqtion.repository.UserBusinessRoleRepository;
import org.nsf.traqtion.repository.search.UserBusinessRoleSearchRepository;

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
 * Test class for the UserBusinessRoleResource REST controller.
 *
 * @see UserBusinessRoleResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserBusinessRoleResourceIntTest {

    private static final String DEFAULT_IS_ACTIVE = "AAAAA";
    private static final String UPDATED_IS_ACTIVE = "BBBBB";

    @Inject
    private UserBusinessRoleRepository userBusinessRoleRepository;

    @Inject
    private UserBusinessRoleSearchRepository userBusinessRoleSearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserBusinessRoleMockMvc;

    private UserBusinessRole userBusinessRole;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserBusinessRoleResource userBusinessRoleResource = new UserBusinessRoleResource();
        ReflectionTestUtils.setField(userBusinessRoleResource, "userBusinessRoleSearchRepository", userBusinessRoleSearchRepository);
        ReflectionTestUtils.setField(userBusinessRoleResource, "userBusinessRoleRepository", userBusinessRoleRepository);
        this.restUserBusinessRoleMockMvc = MockMvcBuilders.standaloneSetup(userBusinessRoleResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userBusinessRole = new UserBusinessRole();
        userBusinessRole.setIsActive(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void createUserBusinessRole() throws Exception {
        int databaseSizeBeforeCreate = userBusinessRoleRepository.findAll().size();

        // Create the UserBusinessRole

        restUserBusinessRoleMockMvc.perform(post("/api/userBusinessRoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBusinessRole)))
                .andExpect(status().isCreated());

        // Validate the UserBusinessRole in the database
        List<UserBusinessRole> userBusinessRoles = userBusinessRoleRepository.findAll();
        assertThat(userBusinessRoles).hasSize(databaseSizeBeforeCreate + 1);
        UserBusinessRole testUserBusinessRole = userBusinessRoles.get(userBusinessRoles.size() - 1);
        assertThat(testUserBusinessRole.getIsActive()).isEqualTo(DEFAULT_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void getAllUserBusinessRoles() throws Exception {
        // Initialize the database
        userBusinessRoleRepository.saveAndFlush(userBusinessRole);

        // Get all the userBusinessRoles
        restUserBusinessRoleMockMvc.perform(get("/api/userBusinessRoles?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userBusinessRole.getId().intValue())))
                .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.toString())));
    }

    @Test
    @Transactional
    public void getUserBusinessRole() throws Exception {
        // Initialize the database
        userBusinessRoleRepository.saveAndFlush(userBusinessRole);

        // Get the userBusinessRole
        restUserBusinessRoleMockMvc.perform(get("/api/userBusinessRoles/{id}", userBusinessRole.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userBusinessRole.getId().intValue()))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingUserBusinessRole() throws Exception {
        // Get the userBusinessRole
        restUserBusinessRoleMockMvc.perform(get("/api/userBusinessRoles/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserBusinessRole() throws Exception {
        // Initialize the database
        userBusinessRoleRepository.saveAndFlush(userBusinessRole);

		int databaseSizeBeforeUpdate = userBusinessRoleRepository.findAll().size();

        // Update the userBusinessRole
        userBusinessRole.setIsActive(UPDATED_IS_ACTIVE);

        restUserBusinessRoleMockMvc.perform(put("/api/userBusinessRoles")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userBusinessRole)))
                .andExpect(status().isOk());

        // Validate the UserBusinessRole in the database
        List<UserBusinessRole> userBusinessRoles = userBusinessRoleRepository.findAll();
        assertThat(userBusinessRoles).hasSize(databaseSizeBeforeUpdate);
        UserBusinessRole testUserBusinessRole = userBusinessRoles.get(userBusinessRoles.size() - 1);
        assertThat(testUserBusinessRole.getIsActive()).isEqualTo(UPDATED_IS_ACTIVE);
    }

    @Test
    @Transactional
    public void deleteUserBusinessRole() throws Exception {
        // Initialize the database
        userBusinessRoleRepository.saveAndFlush(userBusinessRole);

		int databaseSizeBeforeDelete = userBusinessRoleRepository.findAll().size();

        // Get the userBusinessRole
        restUserBusinessRoleMockMvc.perform(delete("/api/userBusinessRoles/{id}", userBusinessRole.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserBusinessRole> userBusinessRoles = userBusinessRoleRepository.findAll();
        assertThat(userBusinessRoles).hasSize(databaseSizeBeforeDelete - 1);
    }
}
