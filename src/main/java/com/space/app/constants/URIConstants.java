package com.space.app.constants;

public class URIConstants {
	
	private URIConstants() {
		//To hide the implicit public one.
	}
	
	public static final String LOGIN_AUTH = "/api/protected/loginAuth";
	public static final String SEARCH_SHIPS = "/api/protected/searchShips";
	public static final String FETCH_PROFILE_DETAILS = "/api/protected/fetchProfileDetails";
	public static final String SAVE_USER_DETAILS = "/api/protected/saveUserDetails";
	public static final String VERIFY_USER = "/api/protected/verifyUser";
	public static final String VERIFY_OTP = "/api/protected/verifyOtp";
	public static final String GENERATE_ETICKET = "/api/protected/generateEticket";
	public static final String FETCH_BOOKING_DETAILS = "/api/protected/fetchBookingDetails";
	public static final String CHANGE_PASSWORD = "/api/protected/changePassword";
	public static final String CANCEL_TICKET = "/api/protected/cancelTicket";

}
