package com.space.app.repo;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.AvailableTicketsEntity;

@Repository
public interface AvailableTicketsRepo extends JpaRepository<AvailableTicketsEntity, Integer> {

	@Query(value = "select * from public.available_tickets where journey_date=:journeyDate and ship_id=:shipId and class_id=:classId and boarding=:boarding and destination=:destination", nativeQuery = true)
	AvailableTicketsEntity findBySelectionDetails(@Param("journeyDate") Date journeyDate,
			@Param("shipId") Integer shipId, @Param("classId") Integer classId, @Param("boarding") String boarding,
			@Param("destination") String destination);

	@Query(value = "select * from public.available_tickets where journey_date=:journeyDate and class_id=:classId and boarding=:boarding and destination=:destination", nativeQuery = true)
	List<AvailableTicketsEntity> findAllBySelectionDetails(@Param("journeyDate") Date journeyDate,
			@Param("classId") Integer classId, @Param("boarding") String boarding,
			@Param("destination") String destination);

	@Transactional
	@Modifying
	@Query(value = "delete from public.available_tickets where journey_date=:journeyDate and ship_id=:shipId and class_id=:classId", nativeQuery = true)
	void deleteByJourneyDateAndShipIdAndClassId(@Param("journeyDate") Date journeyDate, @Param("shipId") Integer shipId,
			@Param("classId") Integer classId);

}
