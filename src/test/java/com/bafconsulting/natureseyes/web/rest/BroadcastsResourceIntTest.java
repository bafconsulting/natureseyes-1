package com.bafconsulting.natureseyes.web.rest;

import com.bafconsulting.natureseyes.Application;
import com.bafconsulting.natureseyes.domain.Broadcasts;
import com.bafconsulting.natureseyes.repository.BroadcastsRepository;
import com.bafconsulting.natureseyes.service.BroadcastsService;

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
 * Test class for the BroadcastsResource REST controller.
 *
 * @see BroadcastsResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BroadcastsResourceIntTest {

    private static final String DEFAULT_BROADCAST_NAME = "AAAAA";
    private static final String UPDATED_BROADCAST_NAME = "BBBBB";
    private static final String DEFAULT_BROADCAST_DESCRIPTION = "AAAAA";
    private static final String UPDATED_BROADCAST_DESCRIPTION = "BBBBB";

    @Inject
    private BroadcastsRepository broadcastsRepository;

    @Inject
    private BroadcastsService broadcastsService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBroadcastsMockMvc;

    private Broadcasts broadcasts;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BroadcastsResource broadcastsResource = new BroadcastsResource();
        ReflectionTestUtils.setField(broadcastsResource, "broadcastsService", broadcastsService);
        this.restBroadcastsMockMvc = MockMvcBuilders.standaloneSetup(broadcastsResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        broadcasts = new Broadcasts();
        broadcasts.setBroadcastName(DEFAULT_BROADCAST_NAME);
        broadcasts.setBroadcastDescription(DEFAULT_BROADCAST_DESCRIPTION);
    }

    @Test
    @Transactional
    public void createBroadcasts() throws Exception {
        int databaseSizeBeforeCreate = broadcastsRepository.findAll().size();

        // Create the Broadcasts

        restBroadcastsMockMvc.perform(post("/api/broadcastss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broadcasts)))
                .andExpect(status().isCreated());

        // Validate the Broadcasts in the database
        List<Broadcasts> broadcastss = broadcastsRepository.findAll();
        assertThat(broadcastss).hasSize(databaseSizeBeforeCreate + 1);
        Broadcasts testBroadcasts = broadcastss.get(broadcastss.size() - 1);
        assertThat(testBroadcasts.getBroadcastName()).isEqualTo(DEFAULT_BROADCAST_NAME);
        assertThat(testBroadcasts.getBroadcastDescription()).isEqualTo(DEFAULT_BROADCAST_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllBroadcastss() throws Exception {
        // Initialize the database
        broadcastsRepository.saveAndFlush(broadcasts);

        // Get all the broadcastss
        restBroadcastsMockMvc.perform(get("/api/broadcastss?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(broadcasts.getId().intValue())))
                .andExpect(jsonPath("$.[*].broadcastName").value(hasItem(DEFAULT_BROADCAST_NAME.toString())))
                .andExpect(jsonPath("$.[*].broadcastDescription").value(hasItem(DEFAULT_BROADCAST_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    public void getBroadcasts() throws Exception {
        // Initialize the database
        broadcastsRepository.saveAndFlush(broadcasts);

        // Get the broadcasts
        restBroadcastsMockMvc.perform(get("/api/broadcastss/{id}", broadcasts.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(broadcasts.getId().intValue()))
            .andExpect(jsonPath("$.broadcastName").value(DEFAULT_BROADCAST_NAME.toString()))
            .andExpect(jsonPath("$.broadcastDescription").value(DEFAULT_BROADCAST_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingBroadcasts() throws Exception {
        // Get the broadcasts
        restBroadcastsMockMvc.perform(get("/api/broadcastss/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroadcasts() throws Exception {
        // Initialize the database
        broadcastsRepository.saveAndFlush(broadcasts);

		int databaseSizeBeforeUpdate = broadcastsRepository.findAll().size();

        // Update the broadcasts
        broadcasts.setBroadcastName(UPDATED_BROADCAST_NAME);
        broadcasts.setBroadcastDescription(UPDATED_BROADCAST_DESCRIPTION);

        restBroadcastsMockMvc.perform(put("/api/broadcastss")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broadcasts)))
                .andExpect(status().isOk());

        // Validate the Broadcasts in the database
        List<Broadcasts> broadcastss = broadcastsRepository.findAll();
        assertThat(broadcastss).hasSize(databaseSizeBeforeUpdate);
        Broadcasts testBroadcasts = broadcastss.get(broadcastss.size() - 1);
        assertThat(testBroadcasts.getBroadcastName()).isEqualTo(UPDATED_BROADCAST_NAME);
        assertThat(testBroadcasts.getBroadcastDescription()).isEqualTo(UPDATED_BROADCAST_DESCRIPTION);
    }

    @Test
    @Transactional
    public void deleteBroadcasts() throws Exception {
        // Initialize the database
        broadcastsRepository.saveAndFlush(broadcasts);

		int databaseSizeBeforeDelete = broadcastsRepository.findAll().size();

        // Get the broadcasts
        restBroadcastsMockMvc.perform(delete("/api/broadcastss/{id}", broadcasts.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Broadcasts> broadcastss = broadcastsRepository.findAll();
        assertThat(broadcastss).hasSize(databaseSizeBeforeDelete - 1);
    }
}
