package com.space.app.web;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.space.app.controller.SpaceController;
import com.space.app.dto.BookingDetailsDTO;
import com.space.app.dto.CommonResponseDTO;
import com.space.app.dto.ProfileResponseDTO;
import com.space.app.dto.TrainsResponse;
import com.space.app.exception.SpaceException;
import com.space.app.service.SpaceService;
import com.space.app.mockedRequest.MockedRequest;

@SpringBootTest
class SpaceControllerTest {

	@InjectMocks
	@Spy
	SpaceController spaceController;

	@Mock
	SpaceService spaceService;

	@Test
	void loginAuthTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.loginAuth(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.loginAuth(MockedRequest.getLoginRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void signUpAuthTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.signUpAuth(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.loginAuth(MockedRequest.getSignUpRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void loginAuthNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).loginAuth(Mockito.any());
		CommonResponseDTO response = spaceController.loginAuth(MockedRequest.getLoginRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void searchTrainsTest() throws SpaceException, JsonProcessingException {

		List<TrainsResponse> trainResponse = new ArrayList<>();
		Mockito.when(spaceService.searchTrains(Mockito.any())).thenReturn(trainResponse);
		trainResponse = spaceController.searchTrains(MockedRequest.getTrainsRequest());

		assertThat(trainResponse).isEmpty();
	}

	@Test
	void searchTrainsNegTest() throws SpaceException, JsonProcessingException {

		List<TrainsResponse> trainResponse = new ArrayList<>();
		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).searchTrains(Mockito.any());
		trainResponse = spaceController.searchTrains(MockedRequest.getTrainsRequest());

		assertThat(trainResponse).isEmpty();
	}

	@Test
	void fetchProfileDetailsTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.fetchProfileDetails(Mockito.any())).thenReturn(new ProfileResponseDTO());
		ProfileResponseDTO response = spaceController.fetchProfileDetails(MockedRequest.getProfileRequest());

		assertThat(response.getResponseCode()).isZero();
	}

	@Test
	void fetchProfileDetailsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).fetchProfileDetails(Mockito.any());
		ProfileResponseDTO response = spaceController.fetchProfileDetails(MockedRequest.getProfileRequest());

		assertThat(response.getResponseCode()).isEqualTo(1);
	}

	@Test
	void saveUserDetailsTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.saveUserDetails(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.saveUserDetails(MockedRequest.getSaveUserDetailsSuccessRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void saveUserDetailsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).saveUserDetails(Mockito.any());
		CommonResponseDTO response = spaceController.saveUserDetails(MockedRequest.getSaveUserDetailsSuccessRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void verifyUserTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.verifyUser(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.verifyUser(MockedRequest.getVerifyRequest());

		assertThat(response.getResponseCode()).isZero();
	}

	@Test
	void verifyUserNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).verifyUser(Mockito.any());
		CommonResponseDTO response = spaceController.verifyUser(MockedRequest.getVerifyRequest());

		assertThat(response.getResponseCode()).isEqualTo(1);
	}

	@Test
	void verifyOtpTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.verifyOtp(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.verifyOtp(MockedRequest.getVerifyRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void DeleteOtpTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.deleteOTP(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.verifyOtp(MockedRequest.getDeleteRequest());

		assertThat(response.getResponseCode()).isEqualTo(2);
	}

	@Test
	void verifyOtpNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).verifyOtp(Mockito.any());
		CommonResponseDTO response = spaceController.verifyOtp(MockedRequest.getVerifyRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void generateEticketTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.generateEticket(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.generateEticket(MockedRequest.getGenerateETicket());

		assertThat(response.getResponseCode()).isZero();
	}

	@Test
	void generateEticketNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).generateEticket(Mockito.any());
		CommonResponseDTO response = spaceController.generateEticket(MockedRequest.getGenerateETicket());

		assertThat(response.getResponseCode()).isEqualTo(1);
	}

	@Test
	void fetchBookingDetailsTest() throws SpaceException, JsonProcessingException {

		List<BookingDetailsDTO> bookingResponse = new ArrayList<>();
		Mockito.when(spaceService.fetchBookingDetails(Mockito.any())).thenReturn(bookingResponse);
		bookingResponse = spaceController.fetchBookingDetails(MockedRequest.getFetchRequest());

		assertThat(bookingResponse).isEmpty();
	}

	@Test
	void fetchBookingDetailsNegTest() throws SpaceException, JsonProcessingException {

		List<BookingDetailsDTO> bookingResponse = new ArrayList<>();
		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).fetchBookingDetails(Mockito.any());
		bookingResponse = spaceController.fetchBookingDetails(MockedRequest.getFetchRequest());

		assertThat(bookingResponse).isEmpty();
	}

	@Test
	void changePasswordTest() throws SpaceException, JsonProcessingException {

		Mockito.when(spaceService.changePassword(Mockito.any())).thenReturn(new CommonResponseDTO());
		CommonResponseDTO response = spaceController.changePassword(MockedRequest.getChangePasswordRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void changePasswordNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).changePassword(Mockito.any());
		CommonResponseDTO response = spaceController.changePassword(MockedRequest.getChangePasswordRequest());

		assertThat(response.getResponseCode()).isNull();
	}

	@Test
	void cancelTicketTest() throws SpaceException, JsonProcessingException {

		Mockito.doNothing().when(spaceService).cancelTicket(Mockito.any());
		CommonResponseDTO response = spaceController.cancelTicket(MockedRequest.getCancelTicketRequest());

		assertThat(response.getResponseCode()).isZero();
	}

	@Test
	void cancelTicketNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new SpaceException("FAILURE_CASE")).when(spaceService).cancelTicket(Mockito.any());
		CommonResponseDTO response = spaceController.cancelTicket(MockedRequest.getCancelTicketRequest());

		assertThat(response.getResponseCode()).isEqualTo(1);
	}

}
