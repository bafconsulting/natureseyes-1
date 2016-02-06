package com.bafconsulting.natureseyes.repository;

import com.bafconsulting.natureseyes.domain.Broadcasts;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Broadcasts entity.
 */
public interface BroadcastsRepository extends JpaRepository<Broadcasts,Long> {

}
