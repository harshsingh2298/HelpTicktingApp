package com.cms.employeeservice.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfirmPasswordDataBean {

	private String newPassword;
	private String confirmPassword;
	
	
		
	
}
