package com.space.app.service;

import static org.assertj.core.api.Assertions.assertThat;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Transport;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.UserIdOtpMapEntity;
import com.space.app.exception.SpaceException;
import com.space.app.mockedRequest.MockedRequest;
import com.space.app.mockedResponse.RepoResponse;
import com.space.app.repo.AvailableTicketsRepo;
import com.space.app.repo.BookingDetailsRepo;
import com.space.app.repo.ClassDetailsRepo;
import com.space.app.repo.RouteDetailsRepo;
import com.space.app.repo.ShipClassMapRepo;
import com.space.app.repo.ShipDetailsRepo;
import com.space.app.repo.ShipRoutesRepo;
import com.space.app.repo.UserDetailsRepo;
import com.space.app.repo.UserIdOtpMapRepo;

@SpringBootTest
class SpaceServiceTest {

	@InjectMocks
	@Spy
	SpaceService spaceService;

	@Mock
	UserDetailsRepo userDetailsRepo;

	@Mock
	ClassDetailsRepo classDetailsRepo;

	@Mock
	ShipClassMapRepo shipClassMapRepo;

	@Mock
	RouteDetailsRepo routeDetailsRepo;

	@Mock
	ShipRoutesRepo shipRoutesRepo;

	@Mock
	ShipDetailsRepo shipDetailsRepo;

	@Mock
	UserIdOtpMapRepo userIdOtpMapRepo;

	@Mock
	BookingDetailsRepo bookingDetailsRepo;

	@Mock
	AvailableTicketsRepo availableTicketsRepo;

	@Mock
	Transport transport;

	@Mock
	Message message;

	private String MapToJson(Object object) throws SpaceException, JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}

	@Test
	void loginAuthSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.loginAuth(MockedRequest.getLoginRequest()));
		String expectedJson = "{\"responseCode\":0,\"responseMessage\":\"LOGIN SUCCESSFUL\",\"userId\":\"email@gmail.com\"}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void loginAuthInvalidPswTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any()))
				.thenReturn(RepoResponse.fetchUserDetailsInvalidPsw());

		String actualJson = MapToJson(spaceService.loginAuth(MockedRequest.getLoginRequest()));
		String expectedJson = "{\"responseCode\":2,\"responseMessage\":\"INVALID PASSWORD\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void loginAuthNullTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.loginAuth(MockedRequest.getLoginRequest()));
		String expectedJson = "{\"responseCode\":1,\"responseMessage\":\"USER NOT AVAILABLE\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void loginAuthNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userDetailsRepo).findByUserEmail(Mockito.any());
		Assertions.assertThrows(SpaceException.class, () -> spaceService.loginAuth(MockedRequest.getLoginRequest()));
	}

	@Test
	void signUpAuthSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.signUpAuth(MockedRequest.getSignUpRequest()));
		String expectedJson = "{\"responseCode\":3,\"responseMessage\":\"SIGNUP SUCCESSFUL\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void signUpAuthUserExistsTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.signUpAuth(MockedRequest.getSignUpRequest()));
		String expectedJson = "{\"responseCode\":4,\"responseMessage\":\"USER ALREADY EXISTS\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void signUpAuthPswMisMatchTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.signUpAuth(MockedRequest.getSignUpPswMisMatchRequest()));
		String expectedJson = "{\"responseCode\":5,\"responseMessage\":\"PASSWORDS DO NOT MATCH\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void signUpAuthNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userDetailsRepo).findByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.signUpAuth(MockedRequest.getSignUpRequest()));
	}

	@Test
	void searchShipsSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(shipClassMapRepo.fetchShipIdsWithClassId(Mockito.any()))
				.thenReturn(RepoResponse.getShipIdsWithClassId());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getRouteDetails());
		Mockito.when(shipRoutesRepo.fetchShipIdsWithRouteId(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getShipIdsWithRouteId());
		Mockito.when(shipDetailsRepo.fetchShipDetails(Mockito.any())).thenReturn(RepoResponse.getShipDetails());

		String actualJson = MapToJson(spaceService.searchShips(MockedRequest.getShipsRequest()));
		String expectedJson = "[{\"responseCode\":null,\"responseMessage\":null,\"userId\":null,\"shipId\":1,\"shipName\":\"Europa\",\"duration\":10,\"price\":50.0}]";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void searchShipsNullRoutesTest() throws SpaceException, JsonProcessingException {

		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(shipClassMapRepo.fetchShipIdsWithClassId(Mockito.any()))
				.thenReturn(RepoResponse.getShipIdsWithClassId());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any())).thenReturn(null);
		Mockito.when(shipRoutesRepo.fetchShipIdsWithRouteId(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getShipIdsWithRouteId());
		Mockito.when(shipDetailsRepo.fetchShipDetails(Mockito.any())).thenReturn(RepoResponse.getShipDetails());

		String actualJson = MapToJson(spaceService.searchShips(MockedRequest.getShipsRequest()));
		String expectedJson = "[]";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void searchShipsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(classDetailsRepo).fetchClassIdWithClassName(Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.searchShips(MockedRequest.getShipsRequest()));
	}

	@Test
	void fetchProfileDetailsSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.fetchProfileDetails(MockedRequest.getProfileRequest()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null,\"userName\":\"Shankar\",\"userEmail\":\"email@gmail.com\",\"userPassword\":\"**3**6\",\"basePlanet\":\"Earth\",\"dob\":\"05/12/1998\"}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void fetchProfileDetailsNullTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.fetchProfileDetails(MockedRequest.getProfileRequest()));
		String expectedJson = "{\"responseCode\":1,\"responseMessage\":\"DETAILS NOT AVAILABLE, PLEASE TRY AGAIN LATER.\",\"userId\":null,\"userName\":null,\"userEmail\":null,\"userPassword\":null,\"basePlanet\":null,\"dob\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void fetchProfileDetailsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userDetailsRepo).findByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.fetchProfileDetails(MockedRequest.getProfileRequest()));
	}

	@Test
	void saveUserDetailsSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.saveUserDetails(MockedRequest.getSaveUserDetailsSuccessRequest()));
		String expectedJson = "{\"responseCode\":0,\"responseMessage\":\"YOUR PROFILE HAS BEEN UPDATED.\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void saveUserDetailsNoUpdatesTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.saveUserDetails(MockedRequest.getSaveUserDetailsNoUpdatesRequest()));
		String expectedJson = "{\"responseCode\":3,\"responseMessage\":\"NO UPDATES HAS BEEN MADE!\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void saveUserDetailsEmptyNameTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.saveUserDetails(MockedRequest.getSaveUserDetailsEmptyNameRequest()));
		String expectedJson = "{\"responseCode\":1,\"responseMessage\":\"NAME CANNOT BE EMPTY!\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void saveUserDetailsEmptyPlanetTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(
				spaceService.saveUserDetails(MockedRequest.getSaveUserDetailsEmptyPlanetRequest()));
		String expectedJson = "{\"responseCode\":2,\"responseMessage\":\"PLEASE SELECT A VALID PLANET.\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void saveUserDetailsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userDetailsRepo).findByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.saveUserDetails(MockedRequest.getSaveUserDetailsSuccessRequest()));
	}

	@Test
	void verifyUserSuccessTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.doReturn("123456").when(spaceService).generateOTP(Mockito.any());
		transport.sendMessage(null, null);
		Mockito.when(userIdOtpMapRepo.save(Mockito.any())).thenReturn(new UserIdOtpMapEntity());

		String actualJson = MapToJson(spaceService.verifyUser(MockedRequest.getVerifyRequest()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void verifyUserNegTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.doReturn("123456").when(spaceService).generateOTP(Mockito.any());
		transport.sendMessage(null, null);
		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userIdOtpMapRepo).save(Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.verifyUser(MockedRequest.getVerifyRequest()));
	}

	@SuppressWarnings("serial")
	@Test
	void prepareMessageNegTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(message).setFrom();

		Assertions.assertThrows(SpaceException.class, () -> spaceService.prepareMessage(null, "", "", "", ""));
	}

	@Test
	void generateOTPTest() throws SpaceException, JsonProcessingException {

		String actualJson = MapToJson(spaceService.generateOTP(6));
		String expectedJson = actualJson;
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void verifyOtpSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userIdOtpMapRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchSuccessOTP());

		String actualJson = MapToJson(spaceService.verifyOtp(MockedRequest.getVerifyRequest()));
		String expectedJson = "{\"responseCode\":0,\"responseMessage\":\"OTP VERIFIED\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void verifyOtpFailureTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userIdOtpMapRepo.findByUserEmail(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.verifyOtp(MockedRequest.getVerifyRequest()));
		String expectedJson = "{\"responseCode\":1,\"responseMessage\":\"OTP VERIFICATION FAILED!\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void verifyOtpNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userIdOtpMapRepo).findByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.verifyOtp(MockedRequest.getVerifyRequest()));
	}

	@Test
	void generateEticketSuccessTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.when(shipDetailsRepo.fetchShipDetail(Mockito.any())).thenReturn(RepoResponse.getShipDetail());
		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getRouteDetails());
		Mockito.when(bookingDetailsRepo.save(Mockito.any())).thenReturn(new BookingDetailsEntity());
		transport.sendMessage(null, null);

		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus());

		String actualJson = MapToJson(spaceService.generateEticket(MockedRequest.getGenerateETicket()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void generateWaitListEticketSuccessTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.when(shipDetailsRepo.fetchShipDetail(Mockito.any())).thenReturn(RepoResponse.getShipDetail());
		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getRouteDetails());
		Mockito.when(bookingDetailsRepo.save(Mockito.any())).thenReturn(new BookingDetailsEntity());
		transport.sendMessage(null, null);

		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus2());

		String actualJson = MapToJson(spaceService.generateEticket(MockedRequest.getGenerateETicket()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void generateEticketEmptyUser2Test() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.when(shipDetailsRepo.fetchShipDetail(Mockito.any())).thenReturn(RepoResponse.getShipDetail());
		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getRouteDetails());
		Mockito.when(bookingDetailsRepo.save(Mockito.any())).thenReturn(new BookingDetailsEntity());
		transport.sendMessage(null, null);

		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus2());

		String actualJson = MapToJson(spaceService.generateEticket(MockedRequest.getGenerateETicketEmptyUser2()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void generateEticketEmptyUser2AvailableTicketNullTest()
			throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.when(shipDetailsRepo.fetchShipDetail(Mockito.any())).thenReturn(RepoResponse.getShipDetail());
		Mockito.when(classDetailsRepo.fetchClassIdWithClassName(Mockito.any()))
				.thenReturn(RepoResponse.getClassDetails());
		Mockito.when(routeDetailsRepo.fetchRouteDetails(Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getRouteDetails());
		Mockito.when(bookingDetailsRepo.save(Mockito.any())).thenReturn(new BookingDetailsEntity());
		transport.sendMessage(null, null);

		String actualJson = MapToJson(spaceService.generateEticket(MockedRequest.getGenerateETicketEmptyUser2()));
		String expectedJson = "{\"responseCode\":null,\"responseMessage\":null,\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void generateEticketNegTest() throws SpaceException, JsonProcessingException, MessagingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(shipDetailsRepo).fetchShipDetail(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.generateEticket(MockedRequest.getGenerateETicket()));
	}

	@Test
	void fetchBookingDetailsSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.findBookingDetailsByUserId(Mockito.any()))
				.thenReturn(RepoResponse.getBookingDetails());

		String actualJson = MapToJson(spaceService.fetchBookingDetails(MockedRequest.getFetchRequest()));
		String expectedJson = "[{\"bookingId\":1,\"shipName\":\"Europa\",\"boarding\":\"Earth\",\"arrival\":\"Mars\",\"journeyDate\":0,\"duration\":50,\"price\":5.0,\"bookingStatus\":null}]";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void fetchBookingDetailsNullTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.findBookingDetailsByUserId(Mockito.any())).thenReturn(null);

		String actualJson = MapToJson(spaceService.fetchBookingDetails(MockedRequest.getFetchRequest()));
		String expectedJson = "[]";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void fetchBookingDetailsNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(bookingDetailsRepo).findBookingDetailsByUserId(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.fetchBookingDetails(MockedRequest.getFetchRequest()));
	}

	@Test
	void changePasswordSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.changePassword(MockedRequest.getChangePasswordRequest()));
		String expectedJson = "{\"responseCode\":0,\"responseMessage\":\"YOUR PASSWORD HAS BEEN CHANGED SUCCESSFULLY.\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void changePasswordEmptyFieldsTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(spaceService.changePassword(MockedRequest.getChangePasswordRequestEmptyFields()));
		String expectedJson = "{\"responseCode\":1,\"responseMessage\":\"PLEASE FILL ALL THE BLANKS!\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void changePasswordOldPswIncorrectTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(
				spaceService.changePassword(MockedRequest.getChangePasswordRequestOldPswIncorrect()));
		String expectedJson = "{\"responseCode\":2,\"responseMessage\":\"OLD PASSWORD IS INCORRECT!\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void changePasswordIncorrectPswLengthTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(
				spaceService.changePassword(MockedRequest.getChangePasswordRequestIncorrectPswLength()));
		String expectedJson = "{\"responseCode\":3,\"responseMessage\":\"YOUR PASSWORD SHOULD BE OF MINIMUM 6 CHARACTERS LONG.\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@Test
	void changePasswordPswDoesNotMatchTest() throws SpaceException, JsonProcessingException {

		Mockito.when(userDetailsRepo.findByUserEmail(Mockito.any())).thenReturn(RepoResponse.fetchUserDetailsSuccess());

		String actualJson = MapToJson(
				spaceService.changePassword(MockedRequest.getChangePasswordRequestPswDoesNotMatch()));
		String expectedJson = "{\"responseCode\":4,\"responseMessage\":\"PASSWORDS DO NOT MATCH\",\"userId\":null}";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void changePasswordNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userDetailsRepo).findByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.changePassword(MockedRequest.getChangePasswordRequest()));
	}

	@Test
	void deleteOTPSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.doNothing().when(userIdOtpMapRepo).deleteByUserEmail(Mockito.any());

		String actualJson = MapToJson(spaceService.deleteOTP(MockedRequest.getDeleteRequest()));
		String expectedJson = "null";
		assertThat(actualJson).isEqualTo(expectedJson);
	}

	@SuppressWarnings("serial")
	@Test
	void deleteOTPNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(userIdOtpMapRepo).deleteByUserEmail(Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.deleteOTP(MockedRequest.getDeleteRequest()));
	}

	@Test
	void cancelTicketSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.findByBookingId(Mockito.any())).thenReturn(RepoResponse.getBookingDetail());
		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus());
		Mockito.doNothing().when(bookingDetailsRepo).deleteByBookingId(Mockito.any());
		spaceService.cancelTicket(MockedRequest.getCancelTicketRequest());
	}

	@Test
	void cancelTicketDeleteSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.findByBookingId(Mockito.any())).thenReturn(RepoResponse.getBookingDetail());
		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus3());
		Mockito.doNothing().when(availableTicketsRepo).deleteByJourneyDateAndShipIdAndClassId(Mockito.any(),
				Mockito.any(), Mockito.any());
		Mockito.doNothing().when(bookingDetailsRepo).deleteByBookingId(Mockito.any());
		spaceService.cancelTicket(MockedRequest.getCancelTicketRequest());
	}

	@SuppressWarnings("serial")
	@Test
	void cancelTicketNegTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.findByBookingId(Mockito.any())).thenReturn(RepoResponse.getBookingDetail());
		Mockito.when(
				availableTicketsRepo.findBySelectionDetails(Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getTicketStatus());
		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(bookingDetailsRepo).deleteByBookingId(Mockito.any());

		Assertions.assertThrows(SpaceException.class,
				() -> spaceService.cancelTicket(MockedRequest.getCancelTicketRequest()));
	}

	@Test
	void updateTicketStatusOnCancellationSuccessTest() throws SpaceException, JsonProcessingException {

		Mockito.when(bookingDetailsRepo.fetchOldestWaitListedTicket(Mockito.any(), Mockito.any(), Mockito.any()))
				.thenReturn(RepoResponse.getBookingDetail());

		spaceService.updateTicketStatusOnCancellation(1, 1);
	}

	@SuppressWarnings("serial")
	@Test
	void updateTicketStatusOnCancellationNegTest() throws SpaceException, JsonProcessingException {

		Mockito.doThrow(new PersistenceException("FAILURE_CASE") {
		}).when(bookingDetailsRepo).fetchOldestWaitListedTicket(Mockito.any(), Mockito.any(), Mockito.any());

		Assertions.assertThrows(SpaceException.class, () -> spaceService.updateTicketStatusOnCancellation(1, 1));
	}

}
