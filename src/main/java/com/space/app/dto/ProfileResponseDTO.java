package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProfileResponseDTO extends CommonResponseDTO {
	
	String userName;
	String userEmail;
	String userPassword;
	String basePlanet;
	String dob;

}
