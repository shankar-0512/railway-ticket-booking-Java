package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.ShipDetailsEntity;

@Repository
public interface ShipDetailsRepo extends JpaRepository<ShipDetailsEntity, Integer> {
	
	@Query(value="select * from public.ship_details where ship_id in (:shipIds)", nativeQuery=true)
	List<ShipDetailsEntity> fetchShipDetails(@Param("shipIds") List<Integer> shipIds);
	
	@Query(value="select * from public.ship_details where ship_id=:shipId", nativeQuery=true)
	ShipDetailsEntity fetchShipDetail(@Param("shipId") Integer shipId);

}
