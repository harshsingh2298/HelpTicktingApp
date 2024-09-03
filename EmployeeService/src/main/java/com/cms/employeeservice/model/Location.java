package com.cms.employeeservice.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity

public class Location implements Serializable {
	
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long locationId;


    private String country;
	
	private String state;
	
	private String district;
	 
	private String city;
	
	private String locationName;

    private Integer pincode;
    
    private Double latitude;
    
    private Double longitude;
    
    @OneToMany(mappedBy = "locationId")
    private List<Employee> employees;
    
    @CreationTimestamp()
	@jakarta.persistence.Column(updatable=false)
    private LocalDate createdDate;
    
    @UpdateTimestamp()
	@jakarta.persistence.Column(insertable=false)
    private LocalDate updatedDate;
    
    private String createdby;
    
    private String updatedby;
    
    
    
   

 }
