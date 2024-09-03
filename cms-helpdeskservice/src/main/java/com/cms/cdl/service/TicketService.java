package com.cms.cdl.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cms.cdl.dto.DepartmentDTO;
import com.cms.cdl.dto.EmployeeDTO;
import com.cms.cdl.dto.TicketCommentDTO;
import com.cms.cdl.dto.TicketHelpteamDTO;
import com.cms.cdl.model.TicketComment;
import com.cms.cdl.model.TicketHistory;
import com.cms.cdl.model.TicketInfo;

public interface TicketService {

	Long saveTicket(List<MultipartFile> file, String ticketData, String employeeData);
	
	List<DepartmentDTO> getAllDepartments();

	

	List<TicketInfo> getRaisedTickets(String role,Long empId);

	List<TicketInfo> getAssignedTickets(String role,Long empId);

	boolean deleteTicket(Long ticketId, String role,Long empId);

    TicketInfo updateTicket(List<MultipartFile> filesList, String ticketData,Long empId);

	

	

    List<TicketHelpteamDTO> getAvailableEmployeesFromDepartment(EmployeeDTO employeeBean);

	List<TicketHelpteamDTO> getAvailableEmployees(EmployeeDTO employeeBean);

    

	String insertComment(TicketCommentDTO commentBean,String comment);

	List<TicketComment> displayComments(Long employeeId, Long ticketID);

	List<TicketHistory> displayHistory(Long employeeId, Long ticketID);

	String cancelOrCloseTicketService(Long ticketId, Long employeeId);

	List<Map<String, Integer>> getCountOfTicketsToDisplayInDashboard(Long employeeId,String  role);

	
	
	List<Long> uploadDocument(EmployeeDTO employeeBean,List<MultipartFile> file, String locationData);

	boolean deletingDocument(Long ticketID, Long documentID,Long employeeId);

	Map<String, Map<String, Map<String, Integer>>> getCountOfTicketsToDisplayInCharts(Long empId);

	Map<String, Map<String, Map<String, Integer>>> getCountOfAssignedTicketsToDisplayInCharts(Long empId, String role);
   
	String durationCalculation(LocalDateTime  date1,LocalDateTime  date2);
	  
	
}
