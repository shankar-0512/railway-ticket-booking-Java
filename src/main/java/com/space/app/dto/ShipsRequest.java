package com.space.app.dto;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ShipsRequest {
	
	String from;
	String to;
	Date journeyDate;
	String shipClass;

}
