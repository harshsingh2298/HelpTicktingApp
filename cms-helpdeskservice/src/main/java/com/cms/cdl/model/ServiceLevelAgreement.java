package com.cms.cdl.model;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ServiceLevelAgreement implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	private Long serviceLevelId;
	
	private String severity;
	
	private String timeLine;

	
}
