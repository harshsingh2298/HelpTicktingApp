package com.cms.cdl.dto;

import java.util.List;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

	private Long empId;
	
	private Long userId;
	
	private String empFirstName;

	private String empLastName;

	private String designation;

	private String emailId;

	private String password;

	private String roles;

	private String permissions;

	private String reportingManager;

	private String dateOfJoining;

	private String dateOfBirth;

	private String contactPrimary;

	private String contactSecondary;

	private String aboutMe;
	
    private MainDepartmentDTO mainDepartment;
	
	private SubDepartmentDTO subDepartment;

	private Long sapEntryId;

	private boolean status;

	private LocationDTO location;

	private String employeeOrganisation;

	private List<AddressDTO> addressList;

	private List<SkillsDTO> skillsList;
}
