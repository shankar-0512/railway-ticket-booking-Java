package com.space.app.repo;

import java.util.Date;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.AvailableTicketsEntity;

@Repository
public interface AvailableTicketsRepo extends JpaRepository<AvailableTicketsEntity, Integer> {

	@Query(value = "select * from public.available_tickets where journey_date=:journeyDate and ship_id=:shipId", nativeQuery=true)
	AvailableTicketsEntity findByJourneyDateAndShipId(@Param("journeyDate") Date journeyDate, @Param("shipId") Integer shipId);

}
