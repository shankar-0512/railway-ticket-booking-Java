package com.space.app.dto;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookingsResponseDTO extends CommonResponseDTO {
	
	List<BookingDetailsDTO> bookingDetails;

}
