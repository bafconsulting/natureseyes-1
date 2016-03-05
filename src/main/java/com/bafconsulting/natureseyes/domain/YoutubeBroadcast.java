package com.bafconsulting.natureseyes.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import java.time.ZonedDateTime;
import org.springframework.data.elasticsearch.annotations.Document;

import com.google.api.client.util.DateTime;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A YoutubeBroadcast.
 */
@Entity
@Table(name = "youtube_broadcast")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "youtubebroadcast")
public class YoutubeBroadcast implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "title")
    private String title;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "privacy_status")
    private String privacyStatus;
    
    @Column(name = "scheduled_start_time")
    private DateTime scheduledStartTime;
    
    @Column(name = "scheduled_end_time")
    private DateTime scheduledEndTime;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrivacyStatus() {
        return privacyStatus;
    }
    
    public void setPrivacyStatus(String privacyStatus) {
        this.privacyStatus = privacyStatus;
    }

    public DateTime getScheduledStartTime() {
        return scheduledStartTime;
    }
    
    public void setScheduledStartTime(DateTime scheduledStartTime) {
        this.scheduledStartTime = scheduledStartTime;
    }

    public DateTime getScheduledEndTime() {
        return scheduledEndTime;
    }
    
    public void setScheduledEndTime(DateTime scheduledEndTime) {
        this.scheduledEndTime = scheduledEndTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        YoutubeBroadcast youtubeBroadcast = (YoutubeBroadcast) o;
        if(youtubeBroadcast.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, youtubeBroadcast.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "YoutubeBroadcast{" +
            "id=" + id +
            ", title='" + title + "'" +
            ", description='" + description + "'" +
            ", privacyStatus='" + privacyStatus + "'" +
            ", scheduledStartTime='" + scheduledStartTime + "'" +
            ", scheduledEndTime='" + scheduledEndTime + "'" +
            '}';
    }
}
