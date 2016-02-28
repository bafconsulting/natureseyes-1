package com.bafconsulting.natureseyes.service;

import com.bafconsulting.natureseyes.domain.SocialUserConnection;

import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;
import com.bafconsulting.natureseyes.repository.YoutubeBroadcastRepository;
import com.bafconsulting.natureseyes.repository.search.YoutubeBroadcastSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

//import org.springframework.security.core.userdetails.User;

/*
 * Authentication
 */

import com.bafconsulting.natureseyes.security.SecurityUtils;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.support.OAuth1ConnectionFactory;
import org.springframework.social.connect.support.OAuth2ConnectionFactory;
import org.springframework.social.support.URIBuilder;

/**
 * Youtube API imports
 */
import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.util.DateTime;

//import com.google.api.services.samples.youtube.cmdline.Auth;
import com.bafconsulting.natureseyes.repository.CustomSocialConnectionRepository;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;
import com.google.common.collect.Lists;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing YoutubeBroadcast.
 */
@Service
@Transactional
public class YoutubeBroadcastService {

    private final Logger log = LoggerFactory.getLogger(YoutubeBroadcastService.class);
    
    @Inject
    private YoutubeBroadcastRepository youtubeBroadcastRepository;
    
    @Inject
    private YoutubeBroadcastSearchRepository youtubeBroadcastSearchRepository;
    
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    
    @Inject
    private static YouTube youtube;
    
    /**
     * Save a youtubeBroadcast.
     * @return the persisted entity
     */
    public YoutubeBroadcast save(YoutubeBroadcast youtubeBroadcast) {
        log.debug("Request to save YoutubeBroadcast : {}", youtubeBroadcast);
        YoutubeBroadcast result = youtubeBroadcastRepository.save(youtubeBroadcast);

        //CustomSocial
        
        //business logic for youtube broadcast insert
        
        GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        Plus plus = new Plus.builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
            .setApplicationName("Google-PlusSample/1.0")
            .build();
        
        
        // This OAuth 2.0 access scope allows for full read/write access to the
        // authenticated user's account.
        List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
            // Authorize the request.
            Credential credential = Auth.authorize(scopes, "createbroadcast");

            //User user = SecurityUtils.getCurrentUser();
            //user.
            Connection.
            
            // This object is used to make YouTube Data API requests.
            youtube = new YouTube.Builder(Auth.HTTP_TRANSPORT, Auth.JSON_FACTORY, credential)
                    .setApplicationName("youtube-cmdline-createbroadcast-sample").build();

            // Prompt the user to enter a title for the broadcast.
            String title = youtubeBroadcast.getTitle();
            System.out.println("You chose " + title + " for broadcast title.");

            // Create a snippet with the title and scheduled start and end
            // times for the broadcast. Currently, those times are hard-coded.
            LiveBroadcastSnippet broadcastSnippet = new LiveBroadcastSnippet();
            broadcastSnippet.setTitle(title);
            broadcastSnippet.setScheduledStartTime(new DateTime("2024-01-30T00:00:00.000Z"));
            broadcastSnippet.setScheduledEndTime(new DateTime("2024-01-31T00:00:00.000Z"));

            // Set the broadcast's privacy status to "private". See:
            // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts#status.privacyStatus
            LiveBroadcastStatus status = new LiveBroadcastStatus();
            status.setPrivacyStatus("private");

            LiveBroadcast broadcast = new LiveBroadcast();
            broadcast.setKind("youtube#liveBroadcast");
            broadcast.setSnippet(broadcastSnippet);
            broadcast.setStatus(status);

            // Construct and execute the API request to insert the broadcast.
            YouTube.LiveBroadcasts.Insert liveBroadcastInsert =
                    youtube.liveBroadcasts().insert("snippet,status", broadcast);
            LiveBroadcast returnedBroadcast = liveBroadcastInsert.execute();

            // Print information from the API response.
            System.out.println("\n================== Returned Broadcast ==================\n");
            System.out.println("  - Id: " + returnedBroadcast.getId());
            System.out.println("  - Title: " + returnedBroadcast.getSnippet().getTitle());
            System.out.println("  - Description: " + returnedBroadcast.getSnippet().getDescription());
            System.out.println("  - Published At: " + returnedBroadcast.getSnippet().getPublishedAt());
            System.out.println(
                    "  - Scheduled Start Time: " + returnedBroadcast.getSnippet().getScheduledStartTime());
            System.out.println(
                    "  - Scheduled End Time: " + returnedBroadcast.getSnippet().getScheduledEndTime());

            // Prompt the user to enter a title for the video stream.
            //title = getStreamTitle();
            title = youtubeBroadcast.getTitle();
            System.out.println("You chose " + title + " for stream title.");

            // Create a snippet with the video stream's title.
            LiveStreamSnippet streamSnippet = new LiveStreamSnippet();
            streamSnippet.setTitle(title);

            // Define the content distribution network settings for the
            // video stream. The settings specify the stream's format and
            // ingestion type. See:
            // https://developers.google.com/youtube/v3/live/docs/liveStreams#cdn
            CdnSettings cdnSettings = new CdnSettings();
            cdnSettings.setFormat("1080p");
            cdnSettings.setIngestionType("rtmp");

            LiveStream stream = new LiveStream();
            stream.setKind("youtube#liveStream");
            stream.setSnippet(streamSnippet);
            stream.setCdn(cdnSettings);

            // Construct and execute the API request to insert the stream.
            YouTube.LiveStreams.Insert liveStreamInsert =
                    youtube.liveStreams().insert("snippet,cdn", stream);
            LiveStream returnedStream = liveStreamInsert.execute();

            // Print information from the API response.
            System.out.println("\n================== Returned Stream ==================\n");
            System.out.println("  - Id: " + returnedStream.getId());
            System.out.println("  - Title: " + returnedStream.getSnippet().getTitle());
            System.out.println("  - Description: " + returnedStream.getSnippet().getDescription());
            System.out.println("  - Published At: " + returnedStream.getSnippet().getPublishedAt());

            // Construct and execute a request to bind the new broadcast
            // and stream.
            YouTube.LiveBroadcasts.Bind liveBroadcastBind =
                    youtube.liveBroadcasts().bind(returnedBroadcast.getId(), "id,contentDetails");
            liveBroadcastBind.setStreamId(returnedStream.getId());
            returnedBroadcast = liveBroadcastBind.execute();

            // Print information from the API response.
            System.out.println("\n================== Returned Bound Broadcast ==================\n");
            System.out.println("  - Broadcast Id: " + returnedBroadcast.getId());
            System.out.println(
                    "  - Bound Stream Id: " + returnedBroadcast.getContentDetails().getBoundStreamId());

        } catch (GoogleJsonResponseException e) {
            System.err.println("GoogleJsonResponseException code: " + e.getDetails().getCode() + " : "
                    + e.getDetails().getMessage());
            e.printStackTrace();

        } catch (IOException e) {
            System.err.println("IOException: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable t) {
            System.err.println("Throwable: " + t.getMessage());
            t.printStackTrace();
        }
        
        youtubeBroadcastSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the youtubeBroadcasts.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<YoutubeBroadcast> findAll(Pageable pageable) {
        log.debug("Request to get all YoutubeBroadcasts");
        Page<YoutubeBroadcast> result = youtubeBroadcastRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one youtubeBroadcast by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public YoutubeBroadcast findOne(Long id) {
        log.debug("Request to get YoutubeBroadcast : {}", id);
        YoutubeBroadcast youtubeBroadcast = youtubeBroadcastRepository.findOne(id);
        return youtubeBroadcast;
    }

    /**
     *  delete the  youtubeBroadcast by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete YoutubeBroadcast : {}", id);
        youtubeBroadcastRepository.delete(id);
        youtubeBroadcastSearchRepository.delete(id);
    }

    /**
     * search for the youtubeBroadcast corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<YoutubeBroadcast> search(String query) {
        
        log.debug("REST request to search YoutubeBroadcasts for query {}", query);
        return StreamSupport
            .stream(youtubeBroadcastSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
