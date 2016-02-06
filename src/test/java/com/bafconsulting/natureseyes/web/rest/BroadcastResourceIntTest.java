package com.bafconsulting.natureseyes.web.rest;

import com.bafconsulting.natureseyes.Application;
import com.bafconsulting.natureseyes.domain.Broadcast;
import com.bafconsulting.natureseyes.repository.BroadcastRepository;
import com.bafconsulting.natureseyes.service.BroadcastService;

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
 * Test class for the BroadcastResource REST controller.
 *
 * @see BroadcastResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class BroadcastResourceIntTest {


    @Inject
    private BroadcastRepository broadcastRepository;

    @Inject
    private BroadcastService broadcastService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restBroadcastMockMvc;

    private Broadcast broadcast;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        BroadcastResource broadcastResource = new BroadcastResource();
        ReflectionTestUtils.setField(broadcastResource, "broadcastService", broadcastService);
        this.restBroadcastMockMvc = MockMvcBuilders.standaloneSetup(broadcastResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        broadcast = new Broadcast();
    }

    @Test
    @Transactional
    public void createBroadcast() throws Exception {
        int databaseSizeBeforeCreate = broadcastRepository.findAll().size();

        // Create the Broadcast

        restBroadcastMockMvc.perform(post("/api/broadcasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broadcast)))
                .andExpect(status().isCreated());

        // Validate the Broadcast in the database
        List<Broadcast> broadcasts = broadcastRepository.findAll();
        assertThat(broadcasts).hasSize(databaseSizeBeforeCreate + 1);
        Broadcast testBroadcast = broadcasts.get(broadcasts.size() - 1);
    }

    @Test
    @Transactional
    public void getAllBroadcasts() throws Exception {
        // Initialize the database
        broadcastRepository.saveAndFlush(broadcast);

        // Get all the broadcasts
        restBroadcastMockMvc.perform(get("/api/broadcasts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(broadcast.getId().intValue())));
    }

    @Test
    @Transactional
    public void getBroadcast() throws Exception {
        // Initialize the database
        broadcastRepository.saveAndFlush(broadcast);

        // Get the broadcast
        restBroadcastMockMvc.perform(get("/api/broadcasts/{id}", broadcast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(broadcast.getId().intValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBroadcast() throws Exception {
        // Get the broadcast
        restBroadcastMockMvc.perform(get("/api/broadcasts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBroadcast() throws Exception {
        // Initialize the database
        broadcastRepository.saveAndFlush(broadcast);

		int databaseSizeBeforeUpdate = broadcastRepository.findAll().size();

        // Update the broadcast

        restBroadcastMockMvc.perform(put("/api/broadcasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(broadcast)))
                .andExpect(status().isOk());

        // Validate the Broadcast in the database
        List<Broadcast> broadcasts = broadcastRepository.findAll();
        assertThat(broadcasts).hasSize(databaseSizeBeforeUpdate);
        Broadcast testBroadcast = broadcasts.get(broadcasts.size() - 1);
    }

    @Test
    @Transactional
    public void deleteBroadcast() throws Exception {
        // Initialize the database
        broadcastRepository.saveAndFlush(broadcast);

		int databaseSizeBeforeDelete = broadcastRepository.findAll().size();

        // Get the broadcast
        restBroadcastMockMvc.perform(delete("/api/broadcasts/{id}", broadcast.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<Broadcast> broadcasts = broadcastRepository.findAll();
        assertThat(broadcasts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
