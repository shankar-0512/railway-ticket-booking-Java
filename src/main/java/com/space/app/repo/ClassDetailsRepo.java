package com.space.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.ClassDetailsEntity;

@Repository
public interface ClassDetailsRepo extends JpaRepository<ClassDetailsEntity, Integer> {
	
	@Query(value="select * from public.class_details where class_name=:className", nativeQuery=true)
	ClassDetailsEntity fetchClassIdWithClassName(@Param("className") String className);

}
