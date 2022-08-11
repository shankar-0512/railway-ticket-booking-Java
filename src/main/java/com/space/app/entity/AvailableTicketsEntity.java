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
@Table(name="available_tickets", schema="public")
public class AvailableTicketsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="selection_id")
	private Integer selectionId;
	
	@Column(name="journey_date")
	private Date journeyDate;
	
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="class_id")
	private Integer classId;
	
	@Column(name="tickets_booked")
	private Integer ticketsBooked;
	
	@Column(name="boarding")
	private String boarding;
	
	@Column(name="destination")
	private String destination;

}
