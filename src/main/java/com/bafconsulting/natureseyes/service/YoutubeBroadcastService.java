package com.bafconsulting.natureseyes.service;

import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;
import com.bafconsulting.natureseyes.repository.YoutubeBroadcastRepository;
import com.bafconsulting.natureseyes.repository.search.YoutubeBroadcastSearchRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

/*
 * Authentication
 */
import com.bafconsulting.natureseyes.security.SecurityUtils;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.google.api.Google;

/**
 * Youtube API imports
 */
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.*;

import java.io.IOException;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing YoutubeBroadcasts. We can insert a youtube broadcast to schedule a broadcast.
 * We can update a broadcasts date, and we can delete scheduled broadcasts\. Whena broadcast is created it will trigger a script on
 * the rasbnerry pi to feed the cameras RTSP or RTMP video feed to youtubes RTMP video feed with the users unique broadcast ID.
 */
@Service
@Transactional
public class YoutubeBroadcastService {

    private final Logger log = LoggerFactory.getLogger(YoutubeBroadcastService.class);
    
    @Inject
    private YoutubeBroadcastRepository youtubeBroadcastRepository;
    
    @Inject
    private YoutubeBroadcastSearchRepository youtubeBroadcastSearchRepository;

	private DateTime startDateTime;
    
	private DateTime endDateTime;
	
    /**
     * Define a global instance of a Youtube object, which will be used
     * to make YouTube Data API requests.
     */
    
    @Inject
    private static YouTube youtube;
    
    /**
     * Saves a youtubeBroadcast.
     * @param endTime 
     * @return the persisted entity
     */
    public YoutubeBroadcast save(YoutubeBroadcast youtubeBroadcast) {
        log.debug("Request to save YoutubeBroadcast : {}", youtubeBroadcast);
        YoutubeBroadcast result = youtubeBroadcast;
        
           DateTime startDateTime = result.getScheduledStartTime();
           Date startDate = new Date(startDateTime.getValue());
           log.debug("the date" + startDate.toString());
           
           DateTime endDateTime = result.getScheduledEndTime();
           Date endDate = new Date(endDateTime.getValue());
           log.debug("the date" + endDate.toString());
              
           java.util.Calendar cal = Calendar.getInstance();
           //java.util.Date utilDate = startDate; // your util date
           cal.setTime(startDate);
           cal.set(Calendar.HOUR_OF_DAY, 0);
           cal.set(Calendar.MINUTE, 0);
           cal.set(Calendar.SECOND, 0);
           cal.set(Calendar.MILLISECOND, 0);   
           
           java.util.Calendar cal1 = Calendar.getInstance();
           //java.util.Date utilDate = startDate; // your util date
           cal1.setTime(endDate);
           cal1.set(Calendar.HOUR_OF_DAY, 0);
           cal1.set(Calendar.MINUTE, 0);
           cal1.set(Calendar.SECOND, 0);
           cal1.set(Calendar.MILLISECOND, 0);  
           
           //java.sql.Date sqlDate = new java.sql.Date(cal.getTime().getTime()); // your sql date
           java.sql.Date sqlDate = new java.sql.Date(startDateTime.getValue());
           log.debug("start date " + sqlDate.toString());
          
           java.sql.Date sqlEndDate = new java.sql.Date(endDateTime.getValue());
           log.debug("end date " + sqlEndDate.toString());   
           
	       // This OAuth 2.0 access scope allows for full read/write access to the
	       // authenticated user's account. https://www.googleapis.com/auth/youtube It must be added to the users permissions url on signin
           //This can be done by modifying the angularjs file for webapps=>scripts=>account=>social=>social.service
	       //List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");

        try {
        	String userid = SecurityUtils.getCurrentUserLogin();
        	// locator for factories needed to construct Connections when restoring from persistent form
            
            // This object is used to make YouTube Data API requests.
        	Connection<Google> connection = SecurityUtils.connection;
        	ConnectionData connectionData = connection.createData();
        	String accessToken = connectionData.getAccessToken();
        	GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
        	Collection<String> currentScopes = credential.getServiceAccountScopes();
        	
        	log.debug("currentScopes: " + currentScopes);
        	
        	//credential.
        	//List<String> scopes = Lists.newArrayList("https://www.googleapis.com/auth/youtube");
        	//List<String> scopes = Lists.newArrayList("createbroadcast");
        	log.debug("accessToken {"+ accessToken + "}");
        	//scopes.add("https://www.googleapis.com/auth/plus.login");
        	// int i =0;
        	//while ( i < scopes.size()) {
        	//	System.out.println("Scope " + i + " " + scopes.get(i));
        	//	i++;
        	// }
        	
        	log.debug("Scopes" + credential.getServiceAccountScopes());
        	//credential.set
        	//new NetHttpTransport(), JacksonFactory.getDefaultInstance()
            youtube = new YouTube.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
                   .setApplicationName("Test111").build();
            
            // Plus plus = new Plus.builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance(), credential)
            //     .setApplicationName("Google-PlusSample/1.0")
            //   .build();
            
            log.debug("Appname" + youtube.getApplicationName());
            // Prompt the user to enter a title for the broadcast.
            String title = youtubeBroadcast.getTitle();
            String description = youtubeBroadcast.getDescription();	
           // (System.out.println"You chose " + title + " for broadcast title.");
            youtube.getApplicationName();
            // Create a snippet with the title and scheduled start and end
            // times for the broadcast. Currently, those times are hard-coded.
            LiveBroadcastSnippet broadcastSnippet = new LiveBroadcastSnippet();
            broadcastSnippet.setTitle(title);
            broadcastSnippet.setDescription(description);
           
            /**
             * The date and time that the broadcast is scheduled to start. The value is specified in ISO 8601
             * (YYYY-MM-DDThh:mm:ss.sZ) format.
             * 2016-03-04T00:00:00.000Z
             * The value may be {@code null}.
             */
             
            log.debug("start time: " + result.getScheduledStartTime());
            log.debug("end time: " + result.getScheduledEndTime());
            broadcastSnippet.setScheduledStartTime(result.getScheduledStartTime());
            broadcastSnippet.setScheduledEndTime(result.getScheduledEndTime());
            
            // Set the broadcast's privacy status to "private". See:
            // https://developers.google.com/youtube/v3/live/docs/liveBroadcasts#status.privacyStatus
            LiveBroadcastStatus status = new LiveBroadcastStatus();
            status.setPrivacyStatus("public");

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
            //System.out.println("You chose " + title + " for stream title.");
            description = youtubeBroadcast.getDescription();
            // Create a snippet with the video stream's title.
            LiveStreamSnippet streamSnippet = new LiveStreamSnippet();
            streamSnippet.setTitle(title);
            streamSnippet.setTitle(description);
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
        
        //embedHtml
        youtubeBroadcastSearchRepository.save(result);
        //youtubeBroadcastRepository.save(result);
        //youtubeBroadcastSearchRepository.save(result);
        
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
