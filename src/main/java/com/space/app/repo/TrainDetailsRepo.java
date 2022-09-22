package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.TrainDetailsEntity;

@Repository
public interface TrainDetailsRepo extends JpaRepository<TrainDetailsEntity, Integer> {
	
	@Query(value="select * from public.train_details where train_id in (:trainIds)", nativeQuery=true)
	List<TrainDetailsEntity> fetchTrainDetails(@Param("trainIds") List<Integer> trainIds);
	
	@Query(value="select * from public.train_details where train_id=:trainId", nativeQuery=true)
	TrainDetailsEntity fetchTrainDetail(@Param("trainId") Integer trainId);

}
