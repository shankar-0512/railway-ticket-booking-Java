package com.space.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.TrainClassMapEntity;

@Repository
public interface TrainClassMapRepo extends JpaRepository<TrainClassMapEntity, Integer> {
	
	@Query(value="select train_id from public.train_class_map where class_id=:classId", nativeQuery = true)
	List<Integer> fetchTrainIdsWithClassId(@Param("classId") Integer classId);

}
