package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.ShipClassMapEntity;

@Repository
public interface ShipClassMapRepo extends JpaRepository<ShipClassMapEntity, Integer> {
	
	@Query(value="select ship_id from public.ship_class_map where class_id=:classId", nativeQuery = true)
	List<Integer> fetchShipIdsWithClassId(@Param("classId") Integer classId);

}
