package com.bafconsulting.natureseyes.web.rest;

import com.bafconsulting.natureseyes.Application;
import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;
import com.bafconsulting.natureseyes.repository.YoutubeBroadcastRepository;
import com.bafconsulting.natureseyes.service.YoutubeBroadcastService;

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
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the YoutubeBroadcastResource REST controller.
 *
 * @see YoutubeBroadcastResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
@IntegrationTest
public class YoutubeBroadcastResourceIntTest {

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME.withZone(ZoneId.of("Z"));

    private static final String DEFAULT_TITLE = "AAAAA";
    private static final String UPDATED_TITLE = "BBBBB";
    private static final String DEFAULT_DESCRIPTION = "AAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBB";
    private static final String DEFAULT_PRIVACY_STATUS = "AAAAA";
    private static final String UPDATED_PRIVACY_STATUS = "BBBBB";

    private static final ZonedDateTime DEFAULT_SCHEDULED_START_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SCHEDULED_START_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SCHEDULED_START_TIME_STR = dateTimeFormatter.format(DEFAULT_SCHEDULED_START_TIME);

    private static final ZonedDateTime DEFAULT_SCHEDULED_END_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneId.systemDefault());
    private static final ZonedDateTime UPDATED_SCHEDULED_END_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);
    private static final String DEFAULT_SCHEDULED_END_TIME_STR = dateTimeFormatter.format(DEFAULT_SCHEDULED_END_TIME);

    @Inject
    private YoutubeBroadcastRepository youtubeBroadcastRepository;

    @Inject
    private YoutubeBroadcastService youtubeBroadcastService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restYoutubeBroadcastMockMvc;

    private YoutubeBroadcast youtubeBroadcast;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        YoutubeBroadcastResource youtubeBroadcastResource = new YoutubeBroadcastResource();
        ReflectionTestUtils.setField(youtubeBroadcastResource, "youtubeBroadcastService", youtubeBroadcastService);
        this.restYoutubeBroadcastMockMvc = MockMvcBuilders.standaloneSetup(youtubeBroadcastResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        youtubeBroadcast = new YoutubeBroadcast();
        youtubeBroadcast.setTitle(DEFAULT_TITLE);
        youtubeBroadcast.setDescription(DEFAULT_DESCRIPTION);
        youtubeBroadcast.setPrivacyStatus(DEFAULT_PRIVACY_STATUS);
        youtubeBroadcast.setScheduledStartTime(DEFAULT_SCHEDULED_START_TIME);
        youtubeBroadcast.setScheduledEndTime(DEFAULT_SCHEDULED_END_TIME);
    }

    @Test
    @Transactional
    public void createYoutubeBroadcast() throws Exception {
        int databaseSizeBeforeCreate = youtubeBroadcastRepository.findAll().size();

        // Create the YoutubeBroadcast

        restYoutubeBroadcastMockMvc.perform(post("/api/youtubeBroadcasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(youtubeBroadcast)))
                .andExpect(status().isCreated());

        // Validate the YoutubeBroadcast in the database
        List<YoutubeBroadcast> youtubeBroadcasts = youtubeBroadcastRepository.findAll();
        assertThat(youtubeBroadcasts).hasSize(databaseSizeBeforeCreate + 1);
        YoutubeBroadcast testYoutubeBroadcast = youtubeBroadcasts.get(youtubeBroadcasts.size() - 1);
        assertThat(testYoutubeBroadcast.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testYoutubeBroadcast.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testYoutubeBroadcast.getPrivacyStatus()).isEqualTo(DEFAULT_PRIVACY_STATUS);
        assertThat(testYoutubeBroadcast.getScheduledStartTime()).isEqualTo(DEFAULT_SCHEDULED_START_TIME);
        assertThat(testYoutubeBroadcast.getScheduledEndTime()).isEqualTo(DEFAULT_SCHEDULED_END_TIME);
    }

    @Test
    @Transactional
    public void getAllYoutubeBroadcasts() throws Exception {
        // Initialize the database
        youtubeBroadcastRepository.saveAndFlush(youtubeBroadcast);

        // Get all the youtubeBroadcasts
        restYoutubeBroadcastMockMvc.perform(get("/api/youtubeBroadcasts?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(youtubeBroadcast.getId().intValue())))
                .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE.toString())))
                .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
                .andExpect(jsonPath("$.[*].privacyStatus").value(hasItem(DEFAULT_PRIVACY_STATUS.toString())))
                .andExpect(jsonPath("$.[*].scheduledStartTime").value(hasItem(DEFAULT_SCHEDULED_START_TIME_STR)))
                .andExpect(jsonPath("$.[*].scheduledEndTime").value(hasItem(DEFAULT_SCHEDULED_END_TIME_STR)));
    }

    @Test
    @Transactional
    public void getYoutubeBroadcast() throws Exception {
        // Initialize the database
        youtubeBroadcastRepository.saveAndFlush(youtubeBroadcast);

        // Get the youtubeBroadcast
        restYoutubeBroadcastMockMvc.perform(get("/api/youtubeBroadcasts/{id}", youtubeBroadcast.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(youtubeBroadcast.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.privacyStatus").value(DEFAULT_PRIVACY_STATUS.toString()))
            .andExpect(jsonPath("$.scheduledStartTime").value(DEFAULT_SCHEDULED_START_TIME_STR))
            .andExpect(jsonPath("$.scheduledEndTime").value(DEFAULT_SCHEDULED_END_TIME_STR));
    }

    @Test
    @Transactional
    public void getNonExistingYoutubeBroadcast() throws Exception {
        // Get the youtubeBroadcast
        restYoutubeBroadcastMockMvc.perform(get("/api/youtubeBroadcasts/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateYoutubeBroadcast() throws Exception {
        // Initialize the database
        youtubeBroadcastRepository.saveAndFlush(youtubeBroadcast);

		int databaseSizeBeforeUpdate = youtubeBroadcastRepository.findAll().size();

        // Update the youtubeBroadcast
        youtubeBroadcast.setTitle(UPDATED_TITLE);
        youtubeBroadcast.setDescription(UPDATED_DESCRIPTION);
        youtubeBroadcast.setPrivacyStatus(UPDATED_PRIVACY_STATUS);
        youtubeBroadcast.setScheduledStartTime(UPDATED_SCHEDULED_START_TIME);
        youtubeBroadcast.setScheduledEndTime(UPDATED_SCHEDULED_END_TIME);

        restYoutubeBroadcastMockMvc.perform(put("/api/youtubeBroadcasts")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(youtubeBroadcast)))
                .andExpect(status().isOk());

        // Validate the YoutubeBroadcast in the database
        List<YoutubeBroadcast> youtubeBroadcasts = youtubeBroadcastRepository.findAll();
        assertThat(youtubeBroadcasts).hasSize(databaseSizeBeforeUpdate);
        YoutubeBroadcast testYoutubeBroadcast = youtubeBroadcasts.get(youtubeBroadcasts.size() - 1);
        assertThat(testYoutubeBroadcast.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testYoutubeBroadcast.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testYoutubeBroadcast.getPrivacyStatus()).isEqualTo(UPDATED_PRIVACY_STATUS);
        assertThat(testYoutubeBroadcast.getScheduledStartTime()).isEqualTo(UPDATED_SCHEDULED_START_TIME);
        assertThat(testYoutubeBroadcast.getScheduledEndTime()).isEqualTo(UPDATED_SCHEDULED_END_TIME);
    }

    @Test
    @Transactional
    public void deleteYoutubeBroadcast() throws Exception {
        // Initialize the database
        youtubeBroadcastRepository.saveAndFlush(youtubeBroadcast);

		int databaseSizeBeforeDelete = youtubeBroadcastRepository.findAll().size();

        // Get the youtubeBroadcast
        restYoutubeBroadcastMockMvc.perform(delete("/api/youtubeBroadcasts/{id}", youtubeBroadcast.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<YoutubeBroadcast> youtubeBroadcasts = youtubeBroadcastRepository.findAll();
        assertThat(youtubeBroadcasts).hasSize(databaseSizeBeforeDelete - 1);
    }
}
