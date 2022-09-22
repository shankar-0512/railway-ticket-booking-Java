package com.space.app.mockedResponse;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.space.app.entity.AvailableTicketsEntity;
import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.ClassDetailsEntity;
import com.space.app.entity.RouteDetailsEntity;
import com.space.app.entity.TrainDetailsEntity;
import com.space.app.entity.UserDetailsEntity;
import com.space.app.entity.UserIdOtpMapEntity;
import com.space.app.util.SpaceUtil;

public class RepoResponse {

	public static UserDetailsEntity fetchUserDetailsSuccess() {
		UserDetailsEntity entity = new UserDetailsEntity();
		entity.setUserEmail("email@gmail.com");
		entity.setPassword("123456");
		entity.setUserId(1);
		entity.setUserName("Shankar");
		entity.setDob("05/12/1998");

		return entity;
	}

	public static UserDetailsEntity fetchUserDetailsInvalidPsw() {
		UserDetailsEntity entity = new UserDetailsEntity();
		entity.setUserEmail("email@gmail.com");
		entity.setPassword("54321");
		entity.setUserId(1);
		entity.setUserName("Shankar");
		entity.setDob("05/12/1998");

		return entity;
	}

	public static ClassDetailsEntity getClassDetails() {
		ClassDetailsEntity entity = new ClassDetailsEntity();
		entity.setClassId(1);
		entity.setClassName("Economy");
		entity.setPriceMultiply(1);

		return entity;
	}

	public static List<Integer> getTrainIdsWithClassId() {
		List<Integer> trainIds = new ArrayList<>();
		trainIds.add(1);

		return trainIds;
	}

	public static RouteDetailsEntity getRouteDetails() {
		RouteDetailsEntity entity = new RouteDetailsEntity();
		entity.setDestination("Mars");
		entity.setOrigin("Earth");
		entity.setRouteId(1);
		entity.setDistance(500);

		return entity;
	}

	public static List<Integer> getTrainIdsWithRouteId() {
		List<Integer> trainIds = new ArrayList<>();
		trainIds.add(1);

		return trainIds;
	}

	public static List<TrainDetailsEntity> getTrainDetails() {
		List<TrainDetailsEntity> entityList = new ArrayList<>();
		TrainDetailsEntity entity = new TrainDetailsEntity();
		entity.setTrainId(1);
		entity.setTrainName("Europa");
		entity.setSpeed(50);
		entity.setBasePrice(50.0);
		entityList.add(entity);

		return entityList;
	}

	public static UserIdOtpMapEntity fetchSuccessOTP() {
		UserIdOtpMapEntity entity = new UserIdOtpMapEntity();
		entity.setMapId(1);
		entity.setOtp("12345");
		entity.setTimestamp(SpaceUtil.getCurrentTs());
		entity.setUserEmail("email@gmail.com");

		return entity;
	}

	public static TrainDetailsEntity getTrainDetail() {
		TrainDetailsEntity entity = new TrainDetailsEntity();
		entity.setTrainId(1);
		entity.setTrainName("Europa");
		entity.setSpeed(50);
		entity.setBasePrice(50.0);

		return entity;
	}

	public static List<BookingDetailsEntity> getBookingDetails() {
		List<BookingDetailsEntity> entityList = new ArrayList<>();
		BookingDetailsEntity entity = new BookingDetailsEntity();
		Date date = new Date(19/06/2022);
		entity.setArrival("Mars");
		entity.setBoarding("Earth");
		entity.setBookingId(1);
		entity.setDuration(50);
		entity.setJourneyDate(date);
		entity.setPrice(5.0);
		entity.setTrainClass("Economy");
		entity.setTrainId(1);
		entity.setTrainName("Europa");
		entity.setTravellerEmail("email@gmail.com");
		entity.setUserId("email@gmail.com");
		entityList.add(entity);

		return entityList;
	}
	
	public static BookingDetailsEntity getBookingDetail() {
		BookingDetailsEntity entity = new BookingDetailsEntity();
		Date date = new Date(19/06/2022);
		entity.setArrival("Mars");
		entity.setBoarding("Earth");
		entity.setBookingId(1);
		entity.setDuration(50);
		entity.setJourneyDate(date);
		entity.setPrice(5.0);
		entity.setTrainClass("Economy");
		entity.setTrainId(1);
		entity.setTrainName("Europa");
		entity.setTravellerEmail("email@gmail.com");
		entity.setUserId("email@gmail.com");

		return entity;
	}

	public static AvailableTicketsEntity getTicketStatus() {
		AvailableTicketsEntity entity = new AvailableTicketsEntity();
		entity.setTicketsBooked(5);
		entity.setTrainId(1);
		return entity;
	}
	
	public static AvailableTicketsEntity getTicketStatus2() {
		AvailableTicketsEntity entity = new AvailableTicketsEntity();
		entity.setTicketsBooked(150);
		entity.setTrainId(1);
		return entity;
	}
	
	public static AvailableTicketsEntity getTicketStatus3() {
		AvailableTicketsEntity entity = new AvailableTicketsEntity();
		entity.setTicketsBooked(0);
		entity.setTrainId(1);
		return entity;
	}
	
	public static List<AvailableTicketsEntity> getAllTicketStatus() {
		List<AvailableTicketsEntity> entityList = new ArrayList<>();
		AvailableTicketsEntity entity = new AvailableTicketsEntity();
		entity.setTicketsBooked(5);
		entity.setTrainId(1);
		entityList.add(entity);
		
		return entityList;
	}

}
