package com.space.app.mockedRequest;

import java.util.Date;

import com.space.app.dto.BookingsRequestDTO;
import com.space.app.dto.ChangePasswordRequestDTO;
import com.space.app.dto.GenerateEticketRequestDTO;
import com.space.app.dto.LoginDTO;
import com.space.app.dto.ProfileRequestDTO;
import com.space.app.dto.TrainsRequest;
import com.space.app.dto.VerifyUserRequestDTO;

public class MockedRequest {

	public static LoginDTO getLoginRequest() {
		LoginDTO login = new LoginDTO();
		login.setEmail("email@gmail.com");
		login.setPassword("123456");
		login.setSignUpF("N");

		return login;
	}

	public static LoginDTO getSignUpRequest() {
		LoginDTO login = new LoginDTO();
		login.setEmail("email@gmail.com");
		login.setPassword("12345");
		login.setRePassword("12345");
		login.setDob("05/12/1998");
		login.setBasePlanet("Earth");
		login.setSignUpF("Y");

		return login;
	}

	public static LoginDTO getSignUpPswMisMatchRequest() {
		LoginDTO login = new LoginDTO();
		login.setEmail("email@gmail.com");
		login.setPassword("12345");
		login.setRePassword("123456");
		login.setDob("05/12/1998");
		login.setBasePlanet("Earth");
		login.setSignUpF("Y");

		return login;
	}

	public static TrainsRequest getTrainsRequest() {
		TrainsRequest request = new TrainsRequest();
		request.setFrom("Earth");
		request.setTo("Mars");
		request.setTrainClass("Economy");

		return request;
	}

	public static ProfileRequestDTO getProfileRequest() {
		ProfileRequestDTO request = new ProfileRequestDTO();
		request.setEmail("email@gmail.com");

		return request;
	}

	public static LoginDTO getSaveUserDetailsNoUpdatesRequest() {
		LoginDTO saveRequest = new LoginDTO();
		saveRequest.setEmail("email@gmail.com");
		saveRequest.setName("Shankar");
		saveRequest.setPassword("12345");
		saveRequest.setDob("05/12/1998");
		saveRequest.setBasePlanet("Earth");

		return saveRequest;
	}

	public static LoginDTO getSaveUserDetailsEmptyNameRequest() {
		LoginDTO saveRequest = new LoginDTO();
		saveRequest.setEmail("email@gmail.com");
		saveRequest.setName("");
		saveRequest.setPassword("12345");
		saveRequest.setDob("05/12/1998");
		saveRequest.setBasePlanet("Earth");

		return saveRequest;
	}

	public static LoginDTO getSaveUserDetailsEmptyPlanetRequest() {
		LoginDTO saveRequest = new LoginDTO();
		saveRequest.setEmail("email@gmail.com");
		saveRequest.setName("Shankar");
		saveRequest.setPassword("12345");
		saveRequest.setDob("05/12/1998");
		saveRequest.setBasePlanet("");

		return saveRequest;
	}

	public static LoginDTO getSaveUserDetailsSuccessRequest() {
		LoginDTO saveRequest = new LoginDTO();
		saveRequest.setEmail("email@gmail.com");
		saveRequest.setName("ShankarV");
		saveRequest.setPassword("12345");
		saveRequest.setDob("05/12/1998");
		saveRequest.setBasePlanet("Earth");

		return saveRequest;
	}

	public static VerifyUserRequestDTO getVerifyRequest() {
		VerifyUserRequestDTO request = new VerifyUserRequestDTO();
		request.setUserEmail("email@gmail.com");
		request.setUserId("email@gmail.com");
		request.setOtp("12345");

		return request;
	}

	public static VerifyUserRequestDTO getDeleteRequest() {
		VerifyUserRequestDTO request = new VerifyUserRequestDTO();
		request.setUserEmail("email@gmail.com");
		request.setUserId("email@gmail.com");
		request.setOtp("12345");
		request.setDeleteFlag("Y");

		return request;
	}

	public static GenerateEticketRequestDTO getGenerateETicket() {
		GenerateEticketRequestDTO request = new GenerateEticketRequestDTO();
		Date date = new Date(19/06/2022);
		request.setArrivalStation("Mars");
		request.setBoardingStation("Earth");
		request.setDuration(50);
		request.setJourneyDate(date);
		request.setTrainClass("Economy");
		request.setTrainId(1);
		request.setTicketFare(50);
		request.setUserAge(17);
		request.setUserName("Sohil");
		request.setUserEmail("email@gmail.com");
		request.setUserId("email@gmail.com");

		return request;
	}

	public static GenerateEticketRequestDTO getGenerateETicketEmptyUser2() {
		GenerateEticketRequestDTO request = new GenerateEticketRequestDTO();
		Date date = new Date(19/06/2022);
		request.setArrivalStation("Mars");
		request.setBoardingStation("Earth");
		request.setDuration(50);
		request.setJourneyDate(date);
		request.setTrainClass("Economy");
		request.setTrainId(1);
		request.setTicketFare(50);
		request.setUserAge(17);
		request.setUserName("Sohil");
		request.setUserEmail("email@gmail.com");
		request.setUserId("email@gmail.com");

		return request;
	}

	public static BookingsRequestDTO getFetchRequest() {
		BookingsRequestDTO request = new BookingsRequestDTO();
		request.setUserEmail("email@gmail.com");
		request.setBookingId(1);

		return request;
	}

	public static ChangePasswordRequestDTO getChangePasswordRequest() {
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("123456");
		request.setNewPassword("543210");
		request.setConfirmNewPassword("543210");
		request.setUserEmail("email@gmail.com");

		return request;
	}

	public static ChangePasswordRequestDTO getChangePasswordRequestEmptyFields() {
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("");
		request.setNewPassword("");
		request.setConfirmNewPassword("");
		request.setUserEmail("email@gmail.com");

		return request;
	}

	public static ChangePasswordRequestDTO getChangePasswordRequestOldPswIncorrect() {
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("1234567");
		request.setNewPassword("543210");
		request.setConfirmNewPassword("543210");
		request.setUserEmail("email@gmail.com");

		return request;
	}

	public static ChangePasswordRequestDTO getChangePasswordRequestPswDoesNotMatch() {
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("123456");
		request.setNewPassword("5432101");
		request.setConfirmNewPassword("5432102");
		request.setUserEmail("email@gmail.com");

		return request;
	}

	public static ChangePasswordRequestDTO getChangePasswordRequestIncorrectPswLength() {
		ChangePasswordRequestDTO request = new ChangePasswordRequestDTO();
		request.setOldPassword("123456");
		request.setNewPassword("54321");
		request.setConfirmNewPassword("54321");
		request.setUserEmail("email@gmail.com");

		return request;
	}

	public static BookingsRequestDTO getCancelTicketRequest() {
		BookingsRequestDTO request = new BookingsRequestDTO();
		request.setBookingId(1);
		request.setUserEmail("email@gmail.com");

		return request;
	}

}
