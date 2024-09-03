package com.cms.cdl.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailDataDTO  {
	
	private String to;
	
	private String subject;
	
	private String body;
}
