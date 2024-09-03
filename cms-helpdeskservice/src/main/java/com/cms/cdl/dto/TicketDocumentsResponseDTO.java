package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDocumentsResponseDTO {

	private Long docId;
	private Long ticketId;

}
