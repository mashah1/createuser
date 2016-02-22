package org.nsf.traqtion.web.rest;

import org.nsf.traqtion.Application;
import org.nsf.traqtion.domain.UserCategory;
import org.nsf.traqtion.repository.UserCategoryRepository;
import org.nsf.traqtion.repository.search.UserCategorySearchRepository;

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
 * Test class for the UserCategoryResource REST controller.
 *
 * @see UserCategoryResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class UserCategoryResourceIntTest {


    @Inject
    private UserCategoryRepository userCategoryRepository;

    @Inject
    private UserCategorySearchRepository userCategorySearchRepository;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restUserCategoryMockMvc;

    private UserCategory userCategory;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        UserCategoryResource userCategoryResource = new UserCategoryResource();
        ReflectionTestUtils.setField(userCategoryResource, "userCategorySearchRepository", userCategorySearchRepository);
        ReflectionTestUtils.setField(userCategoryResource, "userCategoryRepository", userCategoryRepository);
        this.restUserCategoryMockMvc = MockMvcBuilders.standaloneSetup(userCategoryResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        userCategory = new UserCategory();
    }

    @Test
    @Transactional
    public void createUserCategory() throws Exception {
        int databaseSizeBeforeCreate = userCategoryRepository.findAll().size();

        // Create the UserCategory

        restUserCategoryMockMvc.perform(post("/api/userCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userCategory)))
                .andExpect(status().isCreated());

        // Validate the UserCategory in the database
        List<UserCategory> userCategorys = userCategoryRepository.findAll();
        assertThat(userCategorys).hasSize(databaseSizeBeforeCreate + 1);
        UserCategory testUserCategory = userCategorys.get(userCategorys.size() - 1);
    }

    @Test
    @Transactional
    public void getAllUserCategorys() throws Exception {
        // Initialize the database
        userCategoryRepository.saveAndFlush(userCategory);

        // Get all the userCategorys
        restUserCategoryMockMvc.perform(get("/api/userCategorys?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(userCategory.getId().intValue())));
    }

    @Test
    @Transactional
    public void getUserCategory() throws Exception {
        // Initialize the database
        userCategoryRepository.saveAndFlush(userCategory);

        // Get the userCategory
        restUserCategoryMockMvc.perform(get("/api/userCategorys/{id}", userCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(userCategory.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingUserCategory() throws Exception {
        // Get the userCategory
        restUserCategoryMockMvc.perform(get("/api/userCategorys/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateUserCategory() throws Exception {
        // Initialize the database
        userCategoryRepository.saveAndFlush(userCategory);

		int databaseSizeBeforeUpdate = userCategoryRepository.findAll().size();

        // Update the userCategory

        restUserCategoryMockMvc.perform(put("/api/userCategorys")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(userCategory)))
                .andExpect(status().isOk());

        // Validate the UserCategory in the database
        List<UserCategory> userCategorys = userCategoryRepository.findAll();
        assertThat(userCategorys).hasSize(databaseSizeBeforeUpdate);
        UserCategory testUserCategory = userCategorys.get(userCategorys.size() - 1);
    }

    @Test
    @Transactional
    public void deleteUserCategory() throws Exception {
        // Initialize the database
        userCategoryRepository.saveAndFlush(userCategory);

		int databaseSizeBeforeDelete = userCategoryRepository.findAll().size();

        // Get the userCategory
        restUserCategoryMockMvc.perform(delete("/api/userCategorys/{id}", userCategory.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<UserCategory> userCategorys = userCategoryRepository.findAll();
        assertThat(userCategorys).hasSize(databaseSizeBeforeDelete - 1);
    }
}
