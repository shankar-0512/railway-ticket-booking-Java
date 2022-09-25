package com.space.app.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import com.space.app.dto.TrainsRequest;
import com.space.app.dto.TrainsResponse;
import com.space.app.dto.VerifyUserRequestDTO;
import com.space.app.entity.AvailableTicketsEntity;
import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.ClassDetailsEntity;
import com.space.app.entity.RouteDetailsEntity;
import com.space.app.entity.TrainDetailsEntity;
import com.space.app.entity.TrainDetailsEntity;
import com.space.app.entity.UserDetailsEntity;
import com.space.app.entity.UserIdOtpMapEntity;
import com.space.app.exception.SpaceException;
import com.space.app.repo.AvailableTicketsRepo;
import com.space.app.repo.BookingDetailsRepo;
import com.space.app.repo.ClassDetailsRepo;
import com.space.app.repo.RouteDetailsRepo;
import com.space.app.repo.TrainClassMapRepo;
import com.space.app.repo.TrainDetailsRepo;
import com.space.app.repo.TrainRoutesRepo;
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
	TrainClassMapRepo trainClassMapRepo;

	@Autowired
	TrainDetailsRepo trainDetailsRepo;

	@Autowired
	RouteDetailsRepo routeDetailsRepo;

	@Autowired
	TrainRoutesRepo trainRoutesRepo;

	@Autowired
	UserIdOtpMapRepo userIdOtpMapRepo;

	@Autowired
	BookingDetailsRepo bookingDetailsRepo;

	@Autowired
	AvailableTicketsRepo availableTicketsRepo;

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

	public List<TrainsResponse> searchTrains(TrainsRequest trainsRequest) throws SpaceException {
		List<TrainsResponse> trainsResponseList = new ArrayList<>();

		Integer classId = null;
		List<Integer> trainIds = null;
		List<Integer> filteredTrainIds = null;
		RouteDetailsEntity routeDetails = new RouteDetailsEntity();
		ClassDetailsEntity classEntity = new ClassDetailsEntity();
		List<TrainDetailsEntity> trainEntity = new ArrayList<>();
		List<AvailableTicketsEntity> availableTicketsFetch = new ArrayList<>();
		Map<Integer, AvailableTicketsEntity> trainIdTicketMap = new HashMap<>();

		try {

			// fetching class_id with class_name
			classEntity = classDetailsRepo.fetchClassIdWithClassName(trainsRequest.getTrainClass());
			classId = classEntity.getClassId();

			// fetching train-class map with classId
			trainIds = trainClassMapRepo.fetchTrainIdsWithClassId(classId);

			routeDetails = routeDetailsRepo.fetchRouteDetails(trainsRequest.getFrom(), trainsRequest.getTo());

			if (routeDetails == null) {
				return trainsResponseList;
			}

			filteredTrainIds = trainRoutesRepo.fetchTrainIdsWithRouteId(routeDetails.getRouteId(), trainIds);

			// fetching train details.
			trainEntity = trainDetailsRepo.fetchTrainDetails(filteredTrainIds);

			availableTicketsFetch = availableTicketsRepo.findAllBySelectionDetails(trainsRequest.getJourneyDate(),
					classEntity.getClassId(), trainsRequest.getFrom(), trainsRequest.getTo());
			
			for(AvailableTicketsEntity list : availableTicketsFetch) {
				trainIdTicketMap.put(list.getTrainId(), list);
			}

			for (TrainDetailsEntity train : trainEntity) {
				
				Integer ticketsBooked = 0;
				if(trainIdTicketMap.get(train.getTrainId()) != null) {
					ticketsBooked = trainIdTicketMap.get(train.getTrainId()).getTicketsBooked();
				}
				
				TrainsResponse response = new TrainsResponse();
				response.setTrainId(train.getTrainId());
				response.setTrainName(train.getTrainName());
				response.setDuration(routeDetails.getDistance() / train.getSpeed());
				response.setPrice(train.getBasePrice() * classEntity.getPriceMultiply());
				response.setTicketsAvailable(AppConstants.TOTAL_TICKET_COUNT - ticketsBooked);
				trainsResponseList.add(response);
			}

		} catch (DataAccessException | PersistenceException ex) {
			logger.error(ErrorConstants.SEARCH_TRAINS_ERROR);
			throw new SpaceException(ErrorConstants.SEARCH_TRAINS_ERROR);
		}

		return trainsResponseList;
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
					&& userEntityFetch.getDob().equals(saveRequest.getDob())) {
				saveResponse.setResponseCode(AppConstants.FAILURE_CODE_3);
				saveResponse.setResponseMessage(ErrorConstants.NO_UPDATES);
				return saveResponse;
			}
			// End of Validations

			userEntitySave.setUserEmail(userEntityFetch.getUserEmail());
			userEntitySave.setUserName(saveRequest.getName());
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
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			properties.put("mail.smtp.port", "587");

			String myAccountEmail = "noreply.continentalline@gmail.com";
			String password = "linhttwxkxcgnaek";
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
		TrainDetailsEntity trainDetails = new TrainDetailsEntity();
		ClassDetailsEntity classEntity = new ClassDetailsEntity();
		RouteDetailsEntity routeDetails = new RouteDetailsEntity();
		BookingDetailsEntity bookingDetails = new BookingDetailsEntity();
		AvailableTicketsEntity availableTicketsFetch = new AvailableTicketsEntity();
		AvailableTicketsEntity availableTickets = new AvailableTicketsEntity();
		String status = AppConstants.CONFIRMED_TICKET;
		String emailSubject = AppConstants.EMPTY_STR;
		String emailContent = AppConstants.EMPTY_STR;
		try {

			// ###### FETCHING AND CALCULATING REQUIRED VALUES FOR TICKET ######
			trainDetails = trainDetailsRepo.fetchTrainDetail(generateRequest.getTrainId());

			classEntity = classDetailsRepo.fetchClassIdWithClassName(generateRequest.getTrainClass());

			routeDetails = routeDetailsRepo.fetchRouteDetails(generateRequest.getBoardingStation(),
					generateRequest.getArrivalStation());

			Integer duration = routeDetails.getDistance() / trainDetails.getSpeed();

			Double price = trainDetails.getBasePrice() * classEntity.getPriceMultiply();
			Double serviceCharge = 10.5;
			Double total = price + serviceCharge;

			// ###### ADDING TICKET COUNT ######

			availableTicketsFetch = availableTicketsRepo.findBySelectionDetails(generateRequest.getJourneyDate(),
					generateRequest.getTrainId(), classEntity.getClassId(), generateRequest.getBoardingStation(),
					generateRequest.getArrivalStation());

			// NO BOOKING HAS BEEN MADE ON THIS DATE
			if (availableTicketsFetch == null) {
				availableTickets.setJourneyDate(generateRequest.getJourneyDate());
				availableTickets.setTicketsBooked(AppConstants.ONE);
				availableTickets.setTrainId(generateRequest.getTrainId());
				availableTickets.setClassId(classEntity.getClassId());
				availableTickets.setBoarding(generateRequest.getBoardingStation());
				availableTickets.setDestination(generateRequest.getArrivalStation());

				availableTicketsRepo.save(availableTickets);

				// BOOKINGS HAS BEEN MADE ON THIS DATE
			} else {

				if (availableTicketsFetch.getTicketsBooked() >= AppConstants.TOTAL_TICKET_COUNT) {
					status = AppConstants.WAITLISTED_TICKET;
				}

				Integer tickets = availableTicketsFetch.getTicketsBooked() + 1;
				availableTicketsFetch.setTicketsBooked(tickets);

				availableTicketsRepo.save(availableTicketsFetch);
			}

			// ###### SAVING BOOKING DETAILS ######
			bookingDetails.setJourneyDate(generateRequest.getJourneyDate());
			bookingDetails.setPrice(total);
			bookingDetails.setTrainClass(generateRequest.getTrainClass());
			bookingDetails.setClassId(classEntity.getClassId());
			bookingDetails.setTrainId(generateRequest.getTrainId());
			bookingDetails.setTrainName(trainDetails.getTrainName());
			bookingDetails.setBoarding(generateRequest.getBoardingStation());
			bookingDetails.setArrival(generateRequest.getArrivalStation());
			bookingDetails.setDuration(duration);
			bookingDetails.setTravellerName(generateRequest.getUserName());
			bookingDetails.setTravellerAge(generateRequest.getUserAge());
			bookingDetails.setTravellerEmail(generateRequest.getUserEmail());
			bookingDetails.setUserId(generateRequest.getUserId());
			bookingDetails.setBookingStatus(status);

			bookingDetails = bookingDetailsRepo.save(bookingDetails);

			// ###### GENERATING TICKET AND CONFIGURING FOR EMAIL ######
			Properties properties = new Properties();

			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.starttls.enable", "true");
			properties.put("mail.smtp.host", "smtp.gmail.com");
			properties.put("mail.smtp.ssl.protocols", "TLSv1.2");
			properties.put("mail.smtp.port", "587");

			String myAccountEmail = "noreply.continentalline@gmail.com";
			String password = "linhttwxkxcgnaek";
			String recipient = generateRequest.getUserEmail();

			Session session = Session.getInstance(properties, new Authenticator() {
				@Override
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(myAccountEmail, password);
				}
			});

			emailSubject = "Your E-Ticket";
			emailContent = SpaceUtil.generateTicketEmailContent(generateRequest, trainDetails, bookingDetails, duration,
					price, serviceCharge, total, status);

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
				booking.setTrainName(entity.getTrainName());
				booking.setBoarding(entity.getBoarding());
				booking.setArrival(entity.getArrival());
				booking.setDuration(entity.getDuration());
				booking.setJourneyDate(entity.getJourneyDate());
				booking.setPrice(entity.getPrice());
				booking.setBookingStatus(entity.getBookingStatus());

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

		BookingDetailsEntity bookingDetails = new BookingDetailsEntity();
		AvailableTicketsEntity availableTicketsFetch = new AvailableTicketsEntity();
		try {

			bookingDetails = bookingDetailsRepo.findByBookingId(cancelRequest.getBookingId());

			availableTicketsFetch = availableTicketsRepo.findBySelectionDetails(bookingDetails.getJourneyDate(),
					bookingDetails.getTrainId(), bookingDetails.getClassId(), bookingDetails.getBoarding(),
					bookingDetails.getArrival());

			Integer tickets = availableTicketsFetch.getTicketsBooked();
			tickets = tickets - 1;
			if (tickets <= 0) {
				availableTicketsRepo.deleteByJourneyDateAndTrainIdAndClassId(bookingDetails.getJourneyDate(),
						bookingDetails.getTrainId(), bookingDetails.getClassId());
			} else {
				availableTicketsFetch.setTicketsBooked(tickets);
				availableTicketsRepo.save(availableTicketsFetch);
				updateTicketStatusOnCancellation(bookingDetails.getTrainId(), bookingDetails.getClassId());
			}

			bookingDetailsRepo.deleteByBookingId(cancelRequest.getBookingId());

		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.CANCELLATION_ERROR);
			throw new SpaceException(ErrorConstants.CANCELLATION_ERROR);
		}
	}

	public void updateTicketStatusOnCancellation(Integer trainId, Integer classId) throws SpaceException {

		BookingDetailsEntity oldestWaitListedTicket = new BookingDetailsEntity();
		String status = AppConstants.WAITLISTED_TICKET;
		try {

			oldestWaitListedTicket = bookingDetailsRepo.fetchOldestWaitListedTicket(status, trainId, classId);

			if (oldestWaitListedTicket == null) {
				return;
			}

			oldestWaitListedTicket.setBookingStatus(AppConstants.CONFIRMED_TICKET);
			bookingDetailsRepo.save(oldestWaitListedTicket);

		} catch (DataAccessException | PersistenceException e) {
			logger.error(ErrorConstants.CANCELLATION_ERROR);
			throw new SpaceException(ErrorConstants.CANCELLATION_ERROR);
		}

	}

}
