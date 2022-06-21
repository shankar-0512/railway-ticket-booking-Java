package com.space.app.repo;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.UserIdOtpMapEntity;

@Repository
public interface UserIdOtpMapRepo extends JpaRepository<UserIdOtpMapEntity, Integer> {

	@Query(value = "select * from public.user_id_otp_map where user_email=:userEmail", nativeQuery = true)
	UserIdOtpMapEntity findByUserEmail(@Param("userEmail") String userEmail);
	
	@Transactional
	@Modifying
	@Query(value = "delete from public.user_id_otp_map where user_email=:userEmail", nativeQuery = true)
	void deleteByUserEmail(@Param("userEmail") String userEmail);

}
