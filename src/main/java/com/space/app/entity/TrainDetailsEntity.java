package com.space.app.entity;

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
@Table(name="train_details", schema="public")
public class TrainDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="train_id")
	private Integer trainId;
	
	@Column(name="train_name")
	private String trainName;
	
	@Column(name="base_price")
	private Double basePrice;
	
	@Column(name="speed_per_hour")
	private Integer speed;

}
