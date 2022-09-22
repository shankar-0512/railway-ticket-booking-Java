package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.TrainRoutesEntity;

@Repository
public interface TrainRoutesRepo extends JpaRepository<TrainRoutesEntity, Integer> {
	
	@Query(value="select train_id from public.train_routes where route_id=:routeId and train_id in (:trainIds)", nativeQuery=true)
	List<Integer> fetchTrainIdsWithRouteId(@Param("routeId") Integer routeId, @Param("trainIds") List<Integer> trainIds);

}
