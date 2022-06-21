package com.space.app.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangePasswordRequestDTO {
	
	String userEmail;
	String oldPassword;
	String newPassword;
	String confirmNewPassword;
}
