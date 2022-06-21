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
@Table(name="ship_details", schema="public")
public class ShipDetailsEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="ship_name")
	private String shipName;
	
	@Column(name="base_price")
	private Double basePrice;
	
	@Column(name="speed_per_second")
	private Integer speed;

}
