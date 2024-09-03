package com.cms.employeeservice.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

	
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long addressId;

	private String houseNo;

	private String street;

	private String landmark;

	private String country;

	private String state;

	private String district;

	private String city;

	private Integer pincode;
	
	
	@ManyToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinColumn(name = "employeeId", referencedColumnName = "empId")
	private Employee employee;

	@CreationTimestamp()
	@jakarta.persistence.Column(updatable = false)
	private LocalDate createdDate;

	@UpdateTimestamp()
	@jakarta.persistence.Column(insertable = false)
	private LocalDate updatedDate;

	private String createdby;

	private String updatedby;

	
}
