package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AddressDTO {

	
	
	private long addressId;

	private String houseNo;

	private String street;

	private String landmark;

	private String country;

	private String state;

	private String district;

	private String city;

	private Integer pincode;
	
	
	

	
}
