package com.space.app.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDetailsDTO {

	Integer bookingId;
	String trainName;
	String boarding;
	String arrival;
	Date journeyDate;
	Integer duration;
	Double price;
	String bookingStatus;
}
