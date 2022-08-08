package com.space.app.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	@Column(name="journey_date")
	private Date journeyDate;
	
	@Column(name="tickets")
	private Integer tickets;
	
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="class_id")
	private Integer classId;

}
