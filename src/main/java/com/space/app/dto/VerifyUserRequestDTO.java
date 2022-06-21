package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VerifyUserRequestDTO {
	
	String userId;
	String userEmail;
	String otp;
	String deleteFlag;

}
