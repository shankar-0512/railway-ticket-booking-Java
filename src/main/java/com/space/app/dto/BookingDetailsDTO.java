package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingDetailsDTO {

	Integer bookingId;
	String shipName;
	String boarding;
	String arrival;
	String journeyDate;
	Integer duration;
	Double price;
}
