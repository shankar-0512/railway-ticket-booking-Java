package com.space.app.repo;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.space.app.entity.BookingDetailsEntity;

@Repository
public interface BookingDetailsRepo extends JpaRepository<BookingDetailsEntity, Integer> {
	
	@Query(value = "select*from public.booking_details where user_id=:userId", nativeQuery = true)
	List<BookingDetailsEntity> findBookingDetailsByUserId(@Param("userId") String userId);
	
	@Transactional
	@Modifying
	@Query(value = "delete from public.booking_details where booking_id=:bookingId", nativeQuery = true)
	void deleteByBookingId(@Param("bookingId") Integer bookingId);

}
