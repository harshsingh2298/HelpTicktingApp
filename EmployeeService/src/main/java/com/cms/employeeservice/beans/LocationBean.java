package com.cms.employeeservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LocationBean {

	
	
	private long locationId;


    private String country;
	
	private String state;
	
	private String district;
	 
	private String city;
	
	private String locationName;

    private Integer pincode;
    
    private Double latitude;
    
    private Double longitude;
}
