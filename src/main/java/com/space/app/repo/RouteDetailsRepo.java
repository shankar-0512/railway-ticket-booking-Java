package com.space.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.RouteDetailsEntity;

@Repository
public interface RouteDetailsRepo extends JpaRepository<RouteDetailsEntity, Integer> {
	
	@Query(value="select * from public.route_details where origin=:origin and destination=:destination", nativeQuery=true)
	RouteDetailsEntity fetchRouteDetails(@Param("origin") String origin, @Param("destination") String destination);

}
