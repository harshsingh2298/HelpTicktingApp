package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBeanRequestDTO {
	
	private String title;
	private String description;
	private String severity;
	private String remarks;
	private String mainDepartment;
	private String subDepartment;
    private String ticketClassification;
    
		
	
	
}
