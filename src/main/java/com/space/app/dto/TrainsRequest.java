package com.space.app.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TrainsRequest {
	
	String from;
	String to;
	Date journeyDate;
	String trainClass;

}
