package com.space.app.repo;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.AvailableTicketsEntity;

@Repository
public interface AvailableTicketsRepo extends JpaRepository<AvailableTicketsEntity, Integer> {

	@Query(value = "select * from public.available_tickets where journey_date=:journeyDate and ship_id=:shipId and class_id=:classId and from=:from and to=:to", nativeQuery = true)
	AvailableTicketsEntity findBySelectionDetails(@Param("journeyDate") Date journeyDate,
			@Param("shipId") Integer shipId, @Param("classId") Integer classId, @Param("from") String from,
			@Param("to") String to);

	@Transactional
	@Modifying
	@Query(value = "delete from public.available_tickets where journey_date=:journeyDate and ship_id=:shipId and class_id=:classId", nativeQuery = true)
	void deleteByJourneyDateAndShipIdAndClassId(@Param("journeyDate") Date journeyDate, @Param("shipId") Integer shipId,
			@Param("classId") Integer classId);

}
