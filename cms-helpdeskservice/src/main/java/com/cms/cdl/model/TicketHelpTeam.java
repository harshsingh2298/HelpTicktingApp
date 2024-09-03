package com.cms.cdl.model;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "helpTeamId"))
public class TicketHelpTeam extends BaseEntity
{
    
	private static final long serialVersionUID = 1L;

    private Long employeeId;

	private String employeeName;
	
    private String employeeOrganisation;
	
	private String teamMemberType;
	
	private String mainDepartment;
	
	private String subDepartment;
	
	private String emailId;
	
    private String country;
	
	private String state;
	
	private String district;
	
	private String city;
	
	private String location;
	
	
	
}
