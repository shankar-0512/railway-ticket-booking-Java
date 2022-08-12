package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipsResponse extends CommonResponseDTO {
	
	Integer shipId;
	String shipName;
	Integer duration;
	Double price;
	Integer ticketsAvailable;

}
