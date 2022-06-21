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
@Table(name="ship_class_map", schema="public")
public class ShipClassMapEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="map_id")
	private Integer mapId;
	
	@Column(name="ship_id")
	private Integer shipId;
	
	@Column(name="class_id")
	private Integer classId;

}
