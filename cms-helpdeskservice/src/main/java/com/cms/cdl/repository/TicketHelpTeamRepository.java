package com.cms.cdl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.cdl.model.TicketHelpTeam;

@Repository
public interface TicketHelpTeamRepository extends JpaRepository<TicketHelpTeam, Long>{
	
	
	public TicketHelpTeam findByemployeeOrganisation(String userOrg);
	
	public TicketHelpTeam findByemployeeOrganisationAndLocation(String userOrg,String location);
	
	public List<TicketHelpTeam> findBycountryAndStateAndDistrictAndCityAndLocation(String country,String state,String district,String city,String location);

	public TicketHelpTeam findByteamMemberType(String teamMemberType);

	public TicketHelpTeam findByemployeeId(long empId);

	public List<TicketHelpTeam> findBylocation(String location);

	public List<TicketHelpTeam> findByemployeeOrganisationAndTeamMemberTypeAndLocation(String employeeOrganisation, String role,String location);

	public List<TicketHelpTeam> findBymainDepartmentAndTeamMemberTypeAndLocation(String mainDepartment, String role, String location);

	public TicketHelpTeam findBylocationAndMainDepartmentAndTeamMemberType(String location, String mainDepartment, String teamMemberType);

	public  TicketHelpTeam findBylocationAndSubDepartmentAndTeamMemberType(String location, String subDepartment, String teamMemberType);

	public TicketHelpTeam findBymainDepartmentAndTeamMemberTypeAndLocationAndSubDepartment(String mainDepartment, String role,
			String location, String subDepartment);
	

	
	

}
