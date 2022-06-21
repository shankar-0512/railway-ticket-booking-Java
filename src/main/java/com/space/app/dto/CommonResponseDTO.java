package com.space.app.dto;

import javax.validation.Valid;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonResponseDTO {
	
	@Valid
	Integer responseCode;
	@Valid
	String responseMessage;
	String userId;

}
