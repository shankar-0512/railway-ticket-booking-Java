package com.space.app.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.space.app.constants.AppConstants;
import com.space.app.constants.ErrorConstants;
import com.space.app.constants.SuccessConstants;
import com.space.app.constants.URIConstants;
import com.space.app.dto.BookingDetailsDTO;
import com.space.app.dto.BookingsRequestDTO;
import com.space.app.dto.ChangePasswordRequestDTO;
import com.space.app.dto.CommonResponseDTO;
import com.space.app.dto.GenerateEticketRequestDTO;
import com.space.app.dto.LoginDTO;
import com.space.app.dto.ProfileRequestDTO;
import com.space.app.dto.ProfileResponseDTO;
import com.space.app.dto.ShipsRequest;
import com.space.app.dto.ShipsResponse;
import com.space.app.dto.VerifyUserRequestDTO;
import com.space.app.exception.SpaceException;
import com.space.app.service.SpaceService;

@RestController
public class SpaceController {

	public static final Logger logger = LoggerFactory.getLogger(SpaceController.class);

	@Autowired
	SpaceService spaceService;

	@PostMapping(URIConstants.LOGIN_AUTH)
	public CommonResponseDTO loginAuth(@RequestBody LoginDTO loginRequest) {

		CommonResponseDTO loginResponse = new CommonResponseDTO();
		try {
			if (loginRequest.getSignUpF().equals(AppConstants.N_STR)) {
				loginResponse = spaceService.loginAuth(loginRequest);
			} else if (loginRequest.getSignUpF().equals(AppConstants.Y_STR)) {
				loginResponse = spaceService.signUpAuth(loginRequest);
			}
		} catch (SpaceException ex) {
			logger.error(ErrorConstants.LOGIN_AUTH_ERROR);
		}

		return loginResponse;
	}

	@PostMapping(URIConstants.SEARCH_SHIPS)
	public List<ShipsResponse> searchShips(@RequestBody ShipsRequest shipsRequest) {

		List<ShipsResponse> shipsResponse = new ArrayList<>();
		try {
			shipsResponse = spaceService.searchShips(shipsRequest);
		} catch (SpaceException ex) {
			logger.error(ErrorConstants.SEARCH_SHIPS_ERROR);
		}

		return shipsResponse;
	}

	@PostMapping(URIConstants.FETCH_PROFILE_DETAILS)
	public ProfileResponseDTO fetchProfileDetails(@RequestBody ProfileRequestDTO profileRequest) {

		ProfileResponseDTO profileResponse = new ProfileResponseDTO();
		try {
			profileResponse = spaceService.fetchProfileDetails(profileRequest);
			profileResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
			profileResponse.setResponseMessage(SuccessConstants.FETCH_PROFILE_SUCCESS);
		} catch (SpaceException ex) {
			profileResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
			profileResponse.setResponseMessage(ErrorConstants.FETCH_PROFILE_ERROR);
			logger.error(ErrorConstants.FETCH_PROFILE_ERROR);
		}

		return profileResponse;
	}

	@PostMapping(URIConstants.SAVE_USER_DETAILS)
	public CommonResponseDTO saveUserDetails(@RequestBody LoginDTO saveRequest) {

		CommonResponseDTO saveResponse = new CommonResponseDTO();
		try {
			saveResponse = spaceService.saveUserDetails(saveRequest);
		} catch (SpaceException ex) {
			logger.error(ErrorConstants.SAVE_DETAILS_ERROR);
		}

		return saveResponse;
	}

	@PostMapping(URIConstants.VERIFY_USER)
	public CommonResponseDTO verifyUser(@RequestBody VerifyUserRequestDTO verifyRequest) {

		CommonResponseDTO verifyResponse = new CommonResponseDTO();
		try {
			verifyResponse = spaceService.verifyUser(verifyRequest);
			verifyResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
			verifyResponse.setResponseMessage(SuccessConstants.EMAIL_SENT);
		} catch (SpaceException ex) {
			logger.error(ErrorConstants.EMAIL_EXCEPTION);
			verifyResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
			verifyResponse.setResponseMessage(ErrorConstants.EMAIL_EXCEPTION);
		}

		return verifyResponse;
	}

	@PostMapping(URIConstants.VERIFY_OTP)
	public CommonResponseDTO verifyOtp(@RequestBody VerifyUserRequestDTO verifyRequest) {

		CommonResponseDTO verifyResponse = new CommonResponseDTO();
		try {
			if (verifyRequest.getDeleteFlag() != null && verifyRequest.getDeleteFlag().equals(AppConstants.Y_STR)) {
				spaceService.deleteOTP(verifyRequest);
				verifyResponse.setResponseCode(AppConstants.SUCCESS_CODE_2);
				verifyResponse.setResponseMessage(SuccessConstants.OTP_DELETION_SUCCESS);
			} else {
				verifyResponse = spaceService.verifyOtp(verifyRequest);
			}
		} catch (SpaceException ex) {
			logger.error(ErrorConstants.OTP_VERIFICATION_FAILED);
		}

		return verifyResponse;
	}

	@PostMapping(URIConstants.GENERATE_ETICKET)
	public CommonResponseDTO generateEticket(@RequestBody GenerateEticketRequestDTO generateRequest) {

		CommonResponseDTO ticketResponse = new CommonResponseDTO();
		try {
			ticketResponse = spaceService.generateEticket(generateRequest);
			ticketResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
			ticketResponse.setResponseMessage(SuccessConstants.TICKET_GENERATION_SUCCESS);

		} catch (SpaceException ex) {
			logger.error(ErrorConstants.TICKET_GENERATION_FAILED);
			ticketResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
			ticketResponse.setResponseMessage(ErrorConstants.TICKET_GENERATION_FAILED);
		}

		return ticketResponse;
	}

	@PostMapping(URIConstants.FETCH_BOOKING_DETAILS)
	public List<BookingDetailsDTO> fetchBookingDetails(@RequestBody BookingsRequestDTO bookingRequest) {

		List<BookingDetailsDTO> bookingResponse = new ArrayList<>();
		try {
			bookingResponse = spaceService.fetchBookingDetails(bookingRequest);

		} catch (SpaceException ex) {
			logger.error(ErrorConstants.FETCH_BOOKING_DETAILS_ERROR);
		}

		return bookingResponse;
	}

	@PostMapping(URIConstants.CHANGE_PASSWORD)
	public CommonResponseDTO changePassword(@RequestBody ChangePasswordRequestDTO changePswRequest) {

		CommonResponseDTO changePasswordResponse = new CommonResponseDTO();
		try {
			changePasswordResponse = spaceService.changePassword(changePswRequest);

		} catch (SpaceException ex) {
			logger.error(ErrorConstants.PASSWORD_SAVE_FAILED);
		}

		return changePasswordResponse;
	}

	@PostMapping(URIConstants.CANCEL_TICKET)
	public CommonResponseDTO cancelTicket(@RequestBody BookingsRequestDTO cancelRequest) {

		CommonResponseDTO cancelTicketResponse = new CommonResponseDTO();
		try {
			spaceService.cancelTicket(cancelRequest);
			cancelTicketResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
			cancelTicketResponse.setResponseMessage(SuccessConstants.CANCELLATION_SUCCESSFUL);

		} catch (SpaceException ex) {
			logger.error(ErrorConstants.CANCELLATION_ERROR);
			cancelTicketResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
			cancelTicketResponse.setResponseMessage(ErrorConstants.CANCELLATION_ERROR);
		}

		return cancelTicketResponse;
	}

}
