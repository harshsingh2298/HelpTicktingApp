package com.cms.employeeservice.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Employee implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long empId;

	private Long userId;
	
	private Long sapEntryId;
	
	private String empFirstName;

	private String empLastName;

	private String designation;

	private String emailId;

	private String password;

	private String roles;

	private String employeeOrganisation;
	
	private String permissions;

	private String reportingManager;

	private LocalDate dateOfJoining;

	private LocalDate dateOfBirth;

	private String contactPrimary;

	private String contactSecondary;

	private String aboutMe;

	private String status;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "locationId")
	private Location locationId;

	@ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "mainDepartmentId")
	private MainDepartment mainDepartmentId;
	
//	@ManyToOne
//    @JoinColumn(name = "subDepartmentId")
//    private SubDepartment subDepartment;

	@OneToMany(mappedBy = "employee")
	private List<Address> addressList;

	@OneToMany(mappedBy = "employee")
	private List<Skills> skillsList;

	@CreationTimestamp()
	@Column(updatable = false)
	private LocalDate createdDate;

	@UpdateTimestamp()
	@Column(insertable = false)
	private LocalDate updatedDate;

	private String createdby;

	private String updatedby;

	private String description;

}
