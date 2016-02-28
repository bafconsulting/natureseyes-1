package com.bafconsulting.natureseyes.repository;

import com.bafconsulting.natureseyes.domain.YoutubeBroadcast;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the YoutubeBroadcast entity.
 */
public interface YoutubeBroadcastRepository extends JpaRepository<YoutubeBroadcast,Long> {

}
