package com.cms.employeeservice.beans;

import java.time.LocalDate;
import java.util.List;

import com.cms.employeeservice.model.Address;
import com.cms.employeeservice.model.MainDepartment;
import com.cms.employeeservice.model.Location;
import com.cms.employeeservice.model.Skills;

import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmployeeRegistrationBean {

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

	private long sapEntryId;

	private boolean status;

	private Location location;

	private MainDepartment department;

	private List<Address> addressList;

	private List<Skills> skillsList;
}
