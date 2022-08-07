package com.space.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GenerateEticketRequestDTO {
	
	String userName;
	String user2Name;
	String userEmail;
	Integer userAge;
	Integer user2Age;
	Integer shipId;
	String shipClass;
	String boardingStation;
	String arrivalStation;
	Date journeyDate;
	Integer duration;
	Integer ticketFare;
	String userId;

}
