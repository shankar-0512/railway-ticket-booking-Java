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
@Table(name="train_class_map", schema="public")
public class TrainClassMapEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="map_id")
	private Integer mapId;
	
	@Column(name="train_id")
	private Integer shipId;
	
	@Column(name="class_id")
	private Integer classId;

}
