package com.space.app.mockedResponse;

import java.util.ArrayList;
import java.util.List;

import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.ClassDetailsEntity;
import com.space.app.entity.RouteDetailsEntity;
import com.space.app.entity.ShipDetailsEntity;
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
		entity.setBasePlanet("Earth");

		return entity;
	}

	public static UserDetailsEntity fetchUserDetailsInvalidPsw() {
		UserDetailsEntity entity = new UserDetailsEntity();
		entity.setUserEmail("email@gmail.com");
		entity.setPassword("54321");
		entity.setUserId(1);
		entity.setUserName("Shankar");
		entity.setDob("05/12/1998");
		entity.setBasePlanet("Earth");

		return entity;
	}

	public static ClassDetailsEntity getClassDetails() {
		ClassDetailsEntity entity = new ClassDetailsEntity();
		entity.setClassId(1);
		entity.setClassName("Economy");
		entity.setPriceMultiply(1);

		return entity;
	}

	public static List<Integer> getShipIdsWithClassId() {
		List<Integer> shipIds = new ArrayList<>();
		shipIds.add(1);

		return shipIds;
	}

	public static RouteDetailsEntity getRouteDetails() {
		RouteDetailsEntity entity = new RouteDetailsEntity();
		entity.setDestination("Mars");
		entity.setOrigin("Earth");
		entity.setRouteId(1);
		entity.setDistance(500);

		return entity;
	}

	public static List<Integer> getShipIdsWithRouteId() {
		List<Integer> shipIds = new ArrayList<>();
		shipIds.add(1);

		return shipIds;
	}

	public static List<ShipDetailsEntity> getShipDetails() {
		List<ShipDetailsEntity> entityList = new ArrayList<>();
		ShipDetailsEntity entity = new ShipDetailsEntity();
		entity.setShipId(1);
		entity.setShipName("Europa");
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

	public static ShipDetailsEntity getShipDetail() {
		ShipDetailsEntity entity = new ShipDetailsEntity();
		entity.setShipId(1);
		entity.setShipName("Europa");
		entity.setSpeed(50);
		entity.setBasePrice(50.0);

		return entity;
	}

	public static List<BookingDetailsEntity> getBookingDetails() {
		List<BookingDetailsEntity> entityList = new ArrayList<>();
		BookingDetailsEntity entity = new BookingDetailsEntity();
		entity.setArrival("Mars");
		entity.setBoarding("Earth");
		entity.setBookingId(1);
		entity.setDuration(50);
		entity.setJourneyDate("21/06/2022");
		entity.setPrice(5.0);
		entity.setShipClass("Economy");
		entity.setShipId(1);
		entity.setShipName("Europa");
		entity.setTraveller1Age(23);
		entity.setTraveller1Name("Shankar");
		entity.setTraveller2Age(17);
		entity.setTraveller2Name("Sohil");
		entity.setTravellerEmail("email@gmail.com");
		entity.setUserId("email@gmail.com");
		entityList.add(entity);

		return entityList;
	}

}
