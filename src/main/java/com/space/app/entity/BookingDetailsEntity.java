package com.space.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="booking_details", schema="public")
public class BookingDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="booking_id")
	private Integer bookingId;
	
	@Column(name="user_id")
	private String userId;
	
	@Column(name="traveller_email")
	private String travellerEmail;
	
	@Column(name="price")
	private Double price;
	
	@Column(name="traveller1_name")
	private String traveller1Name;
	
	@Column(name="traveller1_age")
	private Integer traveller1Age;
	
	@Column(name="traveller2_name")
	private String traveller2Name;
	
	@Column(name="traveller2_age")
	private Integer traveller2Age;
	
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="ship_name")
	private String shipName;
	
	@Column(name="ship_class")
	private String shipClass;
	
	@Column(name="journey_date")
	private Date journeyDate;
	
	@Column(name="boarding")
	private String boarding;
	
	@Column(name="arrival")
	private String arrival;
	
	@Column(name="duration")
	private Integer duration;
	
	@Column(name="booking_status")
	private String bookingStatus;

}
