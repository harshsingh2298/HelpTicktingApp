package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketHelpteamDTO {
	
	
	private long employeeId;
	
	private String employeeName;
	
	private String city;
	
	private String state;
	
	private String district;
	
	private String location;
	
	private String employeeOrganisation;
	
	private String role;
}
