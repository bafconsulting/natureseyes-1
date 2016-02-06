package com.bafconsulting.natureseyes.repository;

import com.bafconsulting.natureseyes.domain.Broadcast;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Broadcast entity.
 */
public interface BroadcastRepository extends JpaRepository<Broadcast,Long> {

}
