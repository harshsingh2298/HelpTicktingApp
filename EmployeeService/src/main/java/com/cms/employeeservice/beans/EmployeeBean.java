package com.cms.employeeservice.beans;

import java.time.LocalDate;
import java.util.List;

import com.cms.employeeservice.model.Address;
import com.cms.employeeservice.model.Location;
import com.cms.employeeservice.model.MainDepartment;
import com.cms.employeeservice.model.Skills;
import com.cms.employeeservice.model.SubDepartment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeBean {

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

	private LocalDate dateOfJoining;

	private LocalDate dateOfBirth;

	private String contactPrimary;

	private String contactSecondary;

	private String aboutMe;
	
    private MainDepartmentBean mainDepartment;
	
	private SubDepartmentBean subDepartment;

	private Long sapEntryId;

	private String status;

	private LocationBean location;

	private String employeeOrganisation;
	
	private List<Address> addressList;

	private List<Skills> skillsList;
}
