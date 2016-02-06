package com.bafconsulting.natureseyes.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.elasticsearch.annotations.Document;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Broadcasts.
 */
@Entity
@Table(name = "broadcasts")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@Document(indexName = "broadcasts")
public class Broadcasts implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "broadcast_name")
    private String broadcastName;
    
    @Column(name = "broadcast_description")
    private String broadcastDescription;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBroadcastName() {
        return broadcastName;
    }
    
    public void setBroadcastName(String broadcastName) {
        this.broadcastName = broadcastName;
    }

    public String getBroadcastDescription() {
        return broadcastDescription;
    }
    
    public void setBroadcastDescription(String broadcastDescription) {
        this.broadcastDescription = broadcastDescription;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Broadcasts broadcasts = (Broadcasts) o;
        if(broadcasts.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, broadcasts.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Broadcasts{" +
            "id=" + id +
            ", broadcastName='" + broadcastName + "'" +
            ", broadcastDescription='" + broadcastDescription + "'" +
            '}';
    }
}
