package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCommentDTO {

	private Long ticketIdNum;
	private Long empId;
	private String emailId;
	
}
