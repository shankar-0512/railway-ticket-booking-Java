package com.space.app.util;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.space.app.dto.GenerateEticketRequestDTO;
import com.space.app.entity.BookingDetailsEntity;
import com.space.app.entity.TrainDetailsEntity;

public class SpaceUtil {
	
	private SpaceUtil() {
		//To hide the implicit public one.
	}
	
	public static Timestamp getCurrentTs() {
		
		LocalDateTime currentTs = LocalDateTime.now();
		return Timestamp.valueOf(currentTs);
	}
	
	public static String generateTicketEmailContent(GenerateEticketRequestDTO generateRequest, TrainDetailsEntity shipDetails,
			BookingDetailsEntity bookingDetails, Integer duration, Double price,
			Double serviceCharge, Double total, String status) {
		String emailContent;
		emailContent = "<!DOCTYPE html>\r\n"
				+ "<html>\r\n"
				+ "  <head>\r\n"
				+ "    <style>\r\n"
				+ "      th,\r\n"
				+ "      td {\r\n"
				+ "        text-align: left;\r\n"
				+ "        padding: 8px;\r\n"
				+ "      }\r\n"
				+ "\r\n"
				+ "      tr:nth-child(even) {\r\n"
				+ "        background-color: #f2f2f2;\r\n"
				+ "      }\r\n"
				+ "    </style>\r\n"
				+ "  </head>\r\n"
				+ "  <body>\r\n"
				+ "    <p>Hi Shankar,</p>\r\n"
				+ "    <p>\r\n"
				+ "      Your Continental Line E-ticket between ("+ generateRequest.getBoardingStation() +") and ("+ generateRequest.getArrivalStation() +") on "+ generateRequest.getJourneyDate() +"\r\n"
				+ "      has been booked. Please find the details below :\r\n"
				+ "    </p>\r\n"
				+ "    <div style=\"overflow-x: auto\">\r\n"
				+ "      <table\r\n"
				+ "        style=\"border: 1px solid black; border-style: collapse; width: 90%\"\r\n"
				+ "      >\r\n"
				+ "        <tr style=\"background-color: #ff8000\">\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">PNR No</th>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr style=\"background-color: #ffc837\">\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ bookingDetails.getBookingId() +"</td>\r\n"
				+ "        </tr>\r\n"
				+ "      </table>\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "    <h3>Trip Details</h3>\r\n"
				+ "    <div style=\"overflow-x: auto\">\r\n"
				+ "      <table\r\n"
				+ "        style=\"border: 1px solid black; border-style: collapse; width: 90%\"\r\n"
				+ "      >\r\n"
				+ "        <tr style=\"background-color: #ff8000\">\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Train Name</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Class</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Boarding Station</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Arrival Station</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Journey Date</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Duration</th>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr style=\"background-color: #ffc837\">\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">" + shipDetails.getTrainName() + "</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getTrainClass() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getBoardingStation() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getArrivalStation() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getJourneyDate() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ duration +" hrs</td>\r\n"
				+ "        </tr>\r\n"
				+ "      </table>\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "    <h3>Passenger Details</h3>\r\n"
				+ "    <div style=\"overflow-x: auto\">\r\n"
				+ "      <table\r\n"
				+ "        style=\"border: 1px solid black; border-style: collapse; width: 90%\"\r\n"
				+ "      >\r\n"
				+ "        <tr style=\"background-color: #ff8000\">\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Name</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Age</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Status</th>\r\n"
				//+ "          <th style=\"text-align: left; padding: 8px\">Seat No</th>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr style=\"background-color: #ffc837\">\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getUserName() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ generateRequest.getUserAge() +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">"+ status +"</td>\r\n"
				//+ "          <td style=\"text-align: left; padding: 8px\">25A</td>\r\n"
				+ "        </tr>\r\n"
				+ "      </table>\r\n"
				+ "    </div>\r\n"
				+ "\r\n"
				+ "    <h3>Fee Details</h3>\r\n"
				+ "    <div style=\"overflow-x: auto\">\r\n"
				+ "      <table\r\n"
				+ "        style=\"border: 1px solid black; border-style: collapse; width: 90%\"\r\n"
				+ "      >\r\n"
				+ "        <tr style=\"background-color: #ff8000\">\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Ticket Fare</th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">\r\n"
				+ "            Continental Line Service Charge\r\n"
				+ "          </th>\r\n"
				+ "          <th style=\"text-align: left; color: #000000; padding: 8px\">Total</th>\r\n"
				+ "        </tr>\r\n"
				+ "        <tr style=\"background-color: #ffc837\">\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">$"+ price +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">$"+ serviceCharge +"</td>\r\n"
				+ "          <td style=\"text-align: left; color: #000000; padding: 8px\">$"+ total +"</td>\r\n"
				+ "        </tr>\r\n"
				+ "      </table>\r\n"
				+ "    </div>\r\n"
				+ "    <p>\r\n"
				+ "      For any queries contact Continental Line customer care at 05411002453.\r\n"
				+ "    </p>\r\n"
				+ "  </body>\r\n"
				+ "</html>\r\n"
				+ "";
		return emailContent;
	}

}
