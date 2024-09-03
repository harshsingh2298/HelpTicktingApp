package com.cms.cdl.dto;

import java.util.List;

import com.cms.cdl.model.TicketDocuments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketBeanResponseDTO {
	
	private String title;
	private String description;
	private String severity;
	private String remarks;
	private String mainDepartment;
	private String subDepartment;
    private Long   id;
    private String status;
    private String assignedToEmpIdData;
    
    private String createdDateFormat;

	private String closedDate;
    
	private List<TicketDocuments> ticketDocuments;	
	
	
}
