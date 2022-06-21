package com.space.app.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.UserDetailsEntity;

@Repository
public interface UserDetailsRepo extends JpaRepository<UserDetailsEntity, Integer> {

	@Query(value = "select * from public.user_details where user_email=:userEmail", nativeQuery = true)
	UserDetailsEntity findByUserEmail(@Param("userEmail") String userEmail);

}
