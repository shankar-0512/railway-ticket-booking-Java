package com.space.app.entity;

import java.io.Serializable;
import java.sql.Timestamp;

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
@Table(name="user_id_otp_map", schema="public")
public class UserIdOtpMapEntity implements Serializable {
	
private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mapId")
	private Integer mapId;
	
	@Column(name="user_email")
	private String userEmail;
	
	@Column(name="otp")
	private String otp;
	
	@Column(name="timestamp")
	private Timestamp timestamp;
	
}
