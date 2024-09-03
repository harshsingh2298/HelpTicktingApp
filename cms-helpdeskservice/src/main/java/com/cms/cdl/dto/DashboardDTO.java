package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DashboardDTO {

	
	private String empId;
	private String ticketStatus;
	private Integer numberOfDays;
	
	
	@Override
	public String toString() {
		return "DashboardBean [userId=" + empId + ", ticketStatus=" + ticketStatus + ", numberOfDays=" + numberOfDays
				+ "]";
	}
	
	
	
}
