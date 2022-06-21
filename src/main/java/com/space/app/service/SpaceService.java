package com.space.app.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Random;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.PersistenceException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.space.app.constants.AppConstants;
import com.space.app.constants.ErrorConstants;
import com.space.app.constants.SuccessConstants;
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
import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.ClassDetailsEntity;
import com.space.app.entity.RouteDetailsEntity;
import com.space.app.entity.ShipDetailsEntity;
import com.space.app.entity.UserDetailsEntity;
import com.space.app.entity.UserIdOtpMapEntity;
import com.space.app.exception.SpaceException;
import com.space.app.repo.BookingDetailsRepo;
import com.space.app.repo.ClassDetailsRepo;
import com.space.app.repo.RouteDetailsRepo;
import com.space.app.repo.ShipClassMapRepo;
import com.space.app.repo.ShipDetailsRepo;
import com.space.app.repo.ShipRoutesRepo;
import com.space.app.repo.UserDetailsRepo;
import com.space.app.repo.UserIdOtpMapRepo;
import com.space.app.util.SpaceUtil;

@Service
public class SpaceService {

	public static final String ACCOUNT_SID = "AYajjmpDLunawN9mRtBUbWAMSNG9on1NRL";
	public static final String AUTH_TOKEN = "5f612b501d3aad8b834d8fae731074c4";

	@Autowired
	UserDetailsRepo userDetailsRepo;

	@Autowired
	ClassDetailsRepo classDetailsRepo;

	@Autowired
	ShipClassMapRepo shipClassMapRepo;

	@Autowired
	ShipDetailsRepo shipDetailsRepo;

	@Autowired
	RouteDetailsRepo routeDetailsRepo;

	@Autowired
	ShipRoutesRepo shipRoutesRepo;

	@Autowired
	UserIdOtpMapRepo userIdOtpMapRepo;

	@Autowired
	BookingDetailsRepo bookingDetailsRepo;

	public static final Logger logger = LoggerFactory.getLogger(SpaceService.class);

	public CommonResponseDTO loginAuth(LoginDTO loginRequest) throws SpaceException {

		CommonResponseDTO loginResponse = new CommonResponseDTO();
		UserDetailsEntity userEntity = new UserDetailsEntity();
		try {

			userEntity = userDetailsRepo.findByUserEmail(loginRequest.getEmail().toLowerCase());
			if (userEntity == null) {
				loginResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
				loginResponse.setResponseMessage(ErrorConstants.USER_NOT_AVAILABLE);
				return loginResponse;
			} else if (!userEntity.getPassword().equals(loginRequest.getPassword())) {
				loginResponse.setResponseCode(AppConstants.FAILURE_CODE_2);
				loginResponse.setResponseMessage(ErrorConstants.INVALID_PASSWORD);
				return loginResponse;
			} else {
				loginResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
				loginResponse.setResponseMessage(SuccessConstants.LOGIN_SUCCESSFUL);
				loginResponse.setUserId(userEntity.getUserEmail());
				return loginResponse;
			}

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.LOGIN_AUTH_ERROR);
			throw new SpaceException(ErrorConstants.LOGIN_AUTH_ERROR);
		}
	}

	public CommonResponseDTO signUpAuth(LoginDTO signUpRequest) throws SpaceException {

		CommonResponseDTO signUpResponse = new CommonResponseDTO();
		UserDetailsEntity userEntity = new UserDetailsEntity();
		UserDetailsEntity userExistsCheck = new UserDetailsEntity();
		try {

			userExistsCheck = userDetailsRepo.findByUserEmail(signUpRequest.getEmail().toLowerCase());

			// Validations
			if (userExistsCheck != null) {
				signUpResponse.setResponseCode(AppConstants.FAILURE_CODE_4);
				signUpResponse.setResponseMessage(ErrorConstants.USER_ALREADY_EXISTS);
				return signUpResponse;
			}

			if (!signUpRequest.getPassword().equals(signUpRequest.getRePassword())) {
				signUpResponse.setResponseCode(AppConstants.FAILURE_CODE_5);
				signUpResponse.setResponseMessage(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
				return signUpResponse;
			}
			// End of Validations

			userEntity.setUserName(signUpRequest.getName());
			userEntity.setUserEmail(signUpRequest.getEmail().toLowerCase());
			userEntity.setPassword(signUpRequest.getPassword());
			userEntity.setBasePlanet(signUpRequest.getBasePlanet());
			userEntity.setDob(signUpRequest.getDob());

			userDetailsRepo.save(userEntity);
			signUpResponse.setResponseCode(AppConstants.FAILURE_CODE_3);
			signUpResponse.setResponseMessage(SuccessConstants.SIGNUP_SUCCESSFUL);

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.SIGNUP_ERROR);
			throw new SpaceException(ErrorConstants.SIGNUP_ERROR);
		}
		return signUpResponse;
	}

	public List<ShipsResponse> searchShips(ShipsRequest shipsRequest) throws SpaceException {
		List<ShipsResponse> shipsResponseList = new ArrayList<>();

		Integer classId = null;
		List<Integer> shipIds = null;
		List<Integer> filteredShipIds = null;
		RouteDetailsEntity routeDetails = new RouteDetailsEntity();
		ClassDetailsEntity classEntity = new ClassDetailsEntity();
		List<ShipDetailsEntity> shipEntity = new ArrayList<>();

		try {

			// fetching class_id with class_name
			classEntity = classDetailsRepo.fetchClassIdWithClassName(shipsRequest.getShipClass());
			classId = classEntity.getClassId();

			// fetching ship-class map with classId
			shipIds = shipClassMapRepo.fetchShipIdsWithClassId(classId);

			routeDetails = routeDetailsRepo.fetchRouteDetails(shipsRequest.getFrom(), shipsRequest.getTo());

			if (routeDetails == null) {
				return shipsResponseList;
			}

			filteredShipIds = shipRoutesRepo.fetchShipIdsWithRouteId(routeDetails.getRouteId(), shipIds);

			// fetching ship details.
			shipEntity = shipDetailsRepo.fetchShipDetails(filteredShipIds);

			for (ShipDetailsEntity ship : shipEntity) {
				ShipsResponse response = new ShipsResponse();
				response.setShipId(ship.getShipId());
				response.setShipName(ship.getShipName());
				response.setDuration(routeDetails.getDistance() / ship.getSpeed());
				response.setPrice(ship.getBasePrice() * classEntity.getPriceMultiply());
				shipsResponseList.add(response);
			}

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.SEARCH_SHIPS_ERROR);
			throw new SpaceException(ErrorConstants.SEARCH_SHIPS_ERROR);
		}

		return shipsResponseList;
	}

	public ProfileResponseDTO fetchProfileDetails(ProfileRequestDTO profileRequest) throws SpaceException {

		UserDetailsEntity userDetails = new UserDetailsEntity();
		ProfileResponseDTO profileResponse = new ProfileResponseDTO();
		try {

			userDetails = userDetailsRepo.findByUserEmail(profileRequest.getEmail().toLowerCase());

			if (userDetails == null) {
				profileResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
				profileResponse.setResponseMessage(ErrorConstants.PROFILE_DETAIL_NOT_AVAILABLE);
				return profileResponse;
			}

			char[] password = userDetails.getPassword().toCharArray();
			Integer pswLength = userDetails.getPassword().length();
			StringBuilder hiddenPassword = new StringBuilder();
			Integer starCount = AppConstants.ZERO;
			for (int i = 0; i < pswLength; i++) {
				if (starCount.equals(AppConstants.ZERO)) {
					hiddenPassword.append(AppConstants.STAR);
				} else if (starCount.equals(AppConstants.ONE)) {
					hiddenPassword.append(AppConstants.STAR);
				} else {
					hiddenPassword.append(password[i]);
					starCount = -1;
				}

				starCount++;
			}

			profileResponse.setUserEmail(userDetails.getUserEmail());
			profileResponse.setUserName(userDetails.getUserName());
			profileResponse.setUserPassword(hiddenPassword.toString());
			profileResponse.setBasePlanet(userDetails.getBasePlanet());
			profileResponse.setDob(userDetails.getDob());

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.FETCH_PROFILE_ERROR);
			throw new SpaceException(ErrorConstants.FETCH_PROFILE_ERROR);
		}
		return profileResponse;
	}

	public CommonResponseDTO saveUserDetails(LoginDTO saveRequest) throws SpaceException {

		CommonResponseDTO saveResponse = new CommonResponseDTO();
		UserDetailsEntity userEntityFetch = new UserDetailsEntity();
		UserDetailsEntity userEntitySave = new UserDetailsEntity();
		try {

			if (saveRequest.getName().equals(AppConstants.EMPTY_STR)) {
				saveResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
				saveResponse.setResponseMessage(ErrorConstants.NAME_CANNOT_BE_EMPTY);
				return saveResponse;
			}

			if (saveRequest.getBasePlanet().equals(AppConstants.EMPTY_STR)) {
				saveResponse.setResponseCode(AppConstants.FAILURE_CODE_2);
				saveResponse.setResponseMessage(ErrorConstants.INVALID_PLANET);
				return saveResponse;
			}

			userEntityFetch = userDetailsRepo.findByUserEmail(saveRequest.getEmail().toLowerCase());

			// Validations
			if (userEntityFetch.getUserName().equals(saveRequest.getName())
					&& userEntityFetch.getBasePlanet().equals(saveRequest.getBasePlanet())
					&& userEntityFetch.getDob().equals(saveRequest.getDob())) {
				saveResponse.setResponseCode(AppConstants.FAILURE_CODE_3);
				saveResponse.setResponseMessage(ErrorConstants.NO_UPDATES);
				return saveResponse;
			}
			// End of Validations

			userEntitySave.setUserEmail(userEntityFetch.getUserEmail());
			userEntitySave.setUserName(saveRequest.getName());
			userEntitySave.setBasePlanet(saveRequest.getBasePlanet());
			userEntitySave.setDob(saveRequest.getDob());
			userEntitySave.setPassword(userEntityFetch.getPassword());
			userEntitySave.setUserId(userEntityFetch.getUserId());

			userDetailsRepo.save(userEntitySave);
			saveResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
			saveResponse.setResponseMessage(SuccessConstants.PROFILE_UPDATED);

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.SAVE_DETAILS_ERROR);
			throw new SpaceException(ErrorConstants.SAVE_DETAILS_ERROR);
		}
		return saveResponse;
	}

	public CommonResponseDTO verifyUser(VerifyUserRequestDTO verifyRequest) throws SpaceException {

		CommonResponseDTO verifyResponse = new CommonResponseDTO();
		UserIdOtpMapEntity userOtpMap = new UserIdOtpMapEntity();
		try {

			Integer length = 6;
			String otp = generateOTP(length);

			Properties properties = new Properties();

			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");

			String myAccountEmail = "alien.propulsion2022@gmail.com";
			String password = "kmcyhprwwmhuctwa";
			String recipient = verifyRequest.getUserEmail();

			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(myAccountEmail, password);
				}
			});

			String emailSubject = "E-mail Verification";
			String emailContent = "Hi, \n\nPlease verify your E-mail.\n\nYour OTP is " + otp;
			Message message = prepareMessage(session, myAccountEmail, recipient, emailContent, emailSubject);

			Transport.send(message);

			userOtpMap.setUserEmail(verifyRequest.getUserEmail());
			userOtpMap.setOtp(otp);
			userOtpMap.setTimestamp(SpaceUtil.getCurrentTs());

			userIdOtpMapRepo.save(userOtpMap);

		} catch (DataAccessException | PersistenceException | MessagingException ex) {
			logger.error(ErrorConstants.VERIFICATION_EMAIL_ERROR);
			throw new SpaceException(ErrorConstants.VERIFICATION_EMAIL_ERROR);
		}
		return verifyResponse;
	}

	public Message prepareMessage(Session session, String myAccountEmail, String recipient, String emailContent,
			String emailSubject) throws SpaceException {

		Message message = new MimeMessage(session);
		try {
			message.setFrom(new InternetAddress(myAccountEmail));
			message.setRecipient(Message.RecipientType.TO, new InternetAddress(recipient));
			message.setSubject(emailSubject);
			message.setText(emailContent);
			message.setContent(emailContent, "text/html");

		} catch (MessagingException | PersistenceException ex) {
			logger.error(ErrorConstants.PREPARE_MESSAGE_ERROR);
			throw new SpaceException(ErrorConstants.PREPARE_MESSAGE_ERROR);
		}
		return message;
	}

	public String generateOTP(Integer length) {

		// Using numeric values
		String numbers = AppConstants.NUMBERS_0_9;

		// Using random method
		Random randomMethod = new Random();

		StringBuilder otp = new StringBuilder();

		for (int i = 0; i < length; i++) {
			// Use of charAt() method : to get character value
			// Use of nextInt() as it is scanning the value as int
			otp.append(numbers.charAt(randomMethod.nextInt(numbers.length())));
		}
		return otp.toString();
	}

	public CommonResponseDTO verifyOtp(VerifyUserRequestDTO verifyRequest) throws SpaceException {

		CommonResponseDTO verifyResponse = new CommonResponseDTO();
		UserIdOtpMapEntity userOtpMap = new UserIdOtpMapEntity();
		try {

			userOtpMap = userIdOtpMapRepo.findByUserEmail(verifyRequest.getUserEmail());

			if (userOtpMap == null || !userOtpMap.getOtp().equals(verifyRequest.getOtp())) {
				verifyResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
				verifyResponse.setResponseMessage(ErrorConstants.OTP_VERIFICATION_FAILED);
			} else {
				verifyResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
				verifyResponse.setResponseMessage(SuccessConstants.OTP_VERIFIED);
				userIdOtpMapRepo.deleteByUserEmail(verifyRequest.getUserEmail());
			}

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.OTP_VERIFICATION_FAILED);
			throw new SpaceException(ErrorConstants.OTP_VERIFICATION_FAILED);
		}
		return verifyResponse;
	}

	public CommonResponseDTO generateEticket(GenerateEticketRequestDTO generateRequest) throws SpaceException {

		CommonResponseDTO verifyResponse = new CommonResponseDTO();
		ShipDetailsEntity shipDetails = new ShipDetailsEntity();
		ClassDetailsEntity classEntity = new ClassDetailsEntity();
		RouteDetailsEntity routeDetails = new RouteDetailsEntity();
		BookingDetailsEntity bookingDetails = new BookingDetailsEntity();
		String user2Name = null;
		String user2Age = null;
		String emailSubject = AppConstants.EMPTY_STR;
		String emailContent = AppConstants.EMPTY_STR;
		try {

			// ###### FETCHING AND CALCULATING REQUIRED VALUES FOR TICKET ######
			shipDetails = shipDetailsRepo.fetchShipDetail(generateRequest.getShipId());

			classEntity = classDetailsRepo.fetchClassIdWithClassName(generateRequest.getShipClass());

			routeDetails = routeDetailsRepo.fetchRouteDetails(generateRequest.getBoardingStation(),
					generateRequest.getArrivalStation());

			Integer duration = routeDetails.getDistance() / shipDetails.getSpeed();

			Double price = shipDetails.getBasePrice() * classEntity.getPriceMultiply();
			Double serviceCharge = 10.5;
			Double total = price + serviceCharge;

			if (generateRequest.getUser2Name().equals(AppConstants.EMPTY_STR)) {
				user2Name = AppConstants.HYPHEN;
				user2Age = AppConstants.HYPHEN;
			} else {
				user2Name = generateRequest.getUser2Name();
				user2Age = generateRequest.getUser2Age().toString();
			}

			// ###### SAVING BOOKING DETAILS ######
			bookingDetails.setJourneyDate(generateRequest.getJourneyDate());
			bookingDetails.setPrice(total);
			bookingDetails.setShipClass(generateRequest.getShipClass());
			bookingDetails.setShipId(generateRequest.getShipId());
			bookingDetails.setShipName(shipDetails.getShipName());
			bookingDetails.setBoarding(generateRequest.getBoardingStation());
			bookingDetails.setArrival(generateRequest.getArrivalStation());
			bookingDetails.setDuration(duration);
			bookingDetails.setTraveller1Name(generateRequest.getUserName());
			bookingDetails.setTraveller1Age(generateRequest.getUserAge());
			bookingDetails.setTraveller2Name(generateRequest.getUser2Name());
			bookingDetails.setTraveller2Age(generateRequest.getUser2Age());
			bookingDetails.setTravellerEmail(generateRequest.getUserEmail());
			bookingDetails.setUserId(generateRequest.getUserId());

			bookingDetails = bookingDetailsRepo.save(bookingDetails);

			// ###### GENERATING TICKET AND CONFIGURING FOR EMAIL ######
			Properties properties = new Properties();

			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.port", "587");

			String myAccountEmail = "alien.propulsion2022@gmail.com";
			String password = "kmcyhprwwmhuctwa";
			String recipient = generateRequest.getUserEmail();

			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(myAccountEmail, password);
				}
			});

			emailSubject = "Your E-Ticket";
			emailContent = SpaceUtil.generateTicketEmailContent(generateRequest, shipDetails, bookingDetails, user2Name,
					user2Age, duration, price, serviceCharge, total);

			Message message = prepareMessage(session, myAccountEmail, recipient, emailContent, emailSubject);
			Transport.send(message);

		} catch (DataAccessException | PersistenceException | MessagingException ex) {
			logger.error(ErrorConstants.TICKET_GENERATION_FAILED);
			throw new SpaceException(ErrorConstants.TICKET_GENERATION_FAILED);
		}
		return verifyResponse;
	}

	public List<BookingDetailsDTO> fetchBookingDetails(BookingsRequestDTO bookingRequest) throws SpaceException {
		List<BookingDetailsEntity> bookingDetails = new ArrayList<>();
		List<BookingDetailsDTO> bookingList = new ArrayList<>();

		try {

			bookingDetails = bookingDetailsRepo.findBookingDetailsByUserId(bookingRequest.getUserEmail());

			if (bookingDetails == null) {
				return bookingList;
			}

			for (BookingDetailsEntity entity : bookingDetails) {
				BookingDetailsDTO booking = new BookingDetailsDTO();

				booking.setBookingId(entity.getBookingId());
				booking.setShipName(entity.getShipName());
				booking.setBoarding(entity.getBoarding());
				booking.setArrival(entity.getArrival());
				booking.setDuration(entity.getDuration());
				booking.setJourneyDate(entity.getJourneyDate());
				booking.setPrice(entity.getPrice());

				bookingList.add(booking);
			}

		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.FETCH_BOOKING_DETAILS_ERROR);
			throw new SpaceException(ErrorConstants.FETCH_BOOKING_DETAILS_ERROR);
		}

		return bookingList;
	}

	public CommonResponseDTO changePassword(ChangePasswordRequestDTO changePswRequest) throws SpaceException {

		CommonResponseDTO changePasswordResponse = new CommonResponseDTO();
		UserDetailsEntity userDetails = new UserDetailsEntity();
		try {

			userDetails = userDetailsRepo.findByUserEmail(changePswRequest.getUserEmail().toLowerCase());

			if (changePswRequest.getOldPassword().equals(AppConstants.EMPTY_STR)
					|| changePswRequest.getNewPassword().equals(AppConstants.EMPTY_STR)
					|| changePswRequest.getConfirmNewPassword().equals(AppConstants.EMPTY_STR)) {
				changePasswordResponse.setResponseCode(AppConstants.FAILURE_CODE_1);
				changePasswordResponse.setResponseMessage(ErrorConstants.EMPTY_FIELDS);
				return changePasswordResponse;
			} else if (!userDetails.getPassword().equals(changePswRequest.getOldPassword())) {
				changePasswordResponse.setResponseCode(AppConstants.FAILURE_CODE_2);
				changePasswordResponse.setResponseMessage(ErrorConstants.OLD_PASSWORD_INCORRECT);
				return changePasswordResponse;
			} else if (changePswRequest.getNewPassword().length() < 6) {
				changePasswordResponse.setResponseCode(AppConstants.FAILURE_CODE_3);
				changePasswordResponse.setResponseMessage(ErrorConstants.INVALID_PASSWORD_LENGTH);
				return changePasswordResponse;
			} else if (!changePswRequest.getNewPassword().equals(changePswRequest.getConfirmNewPassword())) {
				changePasswordResponse.setResponseCode(AppConstants.FAILURE_CODE_4);
				changePasswordResponse.setResponseMessage(ErrorConstants.PASSWORDS_DO_NOT_MATCH);
				return changePasswordResponse;
			} else {
				userDetails.setPassword(changePswRequest.getNewPassword());
				userDetailsRepo.save(userDetails);
				changePasswordResponse.setResponseCode(AppConstants.SUCCESS_CODE_0);
				changePasswordResponse.setResponseMessage(ErrorConstants.PASSWORD_CHANGED_SUCCESSFULLY);
			}

		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.PASSWORD_SAVE_FAILED);
			throw new SpaceException(ErrorConstants.PASSWORD_SAVE_FAILED);
		}
		return changePasswordResponse;
	}

	public CommonResponseDTO deleteOTP(VerifyUserRequestDTO verifyRequest) throws SpaceException {

		try {
			userIdOtpMapRepo.deleteByUserEmail(verifyRequest.getUserEmail());
		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.OTP_DELETION_ERROR);
			throw new SpaceException(ErrorConstants.OTP_DELETION_ERROR);
		}
		return null;
	}

	public void cancelTicket(BookingsRequestDTO cancelRequest) throws SpaceException {

		try {
			bookingDetailsRepo.deleteByBookingId(cancelRequest.getBookingId());

		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.CANCELLATION_ERROR);
			throw new SpaceException(ErrorConstants.CANCELLATION_ERROR);
		}
	}

}