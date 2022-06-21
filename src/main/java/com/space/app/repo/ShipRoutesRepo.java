package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.ShipDetailsEntity;

@Repository
public interface ShipRoutesRepo extends JpaRepository<ShipDetailsEntity, Integer> {
	
	@Query(value="select ship_id from public.ship_routes where route_id=:routeId and ship_id in (:shipIds)", nativeQuery=true)
	List<Integer> fetchShipIdsWithRouteId(@Param("routeId") Integer routeId, @Param("shipIds") List<Integer> shipIds);

}
