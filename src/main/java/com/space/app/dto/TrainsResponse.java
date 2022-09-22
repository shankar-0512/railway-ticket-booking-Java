package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainsResponse extends CommonResponseDTO {
	
	Integer trainId;
	String trainName;
	Integer duration;
	Double price;
	Integer ticketsAvailable;

}
