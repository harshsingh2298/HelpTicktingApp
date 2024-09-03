package com.cms.cdl.controller;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.cms.cdl.dto.DepartmentDTO;
import com.cms.cdl.dto.EmployeeDTO;
import com.cms.cdl.dto.TicketCommentDTO;
import com.cms.cdl.dto.TicketHelpteamDTO;
import com.cms.cdl.model.TicketComment;
import com.cms.cdl.model.TicketHelpTeam;
import com.cms.cdl.model.TicketHistory;
import com.cms.cdl.model.TicketInfo;
import com.cms.cdl.repository.TicketHelpTeamRepository;
import com.cms.cdl.service.TicketService;
import com.cms.cdl.utils.DocumentOperations;
import com.cms.cdl.utils.FileAndContentTypeBean;
import com.google.gson.Gson;

import io.netty.handler.timeout.WriteTimeoutException;
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/tickets")
public class TicketController {

	@Autowired
	private TicketService ticketService;
	
	@Autowired
	private TicketHelpTeamRepository ticketHelpTeamRepository;
	

	
	 @Value("${employee.fetchAPI}")
     private String fetchAPI;
	 
	 @Value("${employee.downloadMultipleFiles}")
	 private String downloadMultipleFilesAPI;
	
	
	@PostMapping(value = "/save-ticket")
	public ResponseEntity<?> saveTicketData(@RequestPart(required = false)List<MultipartFile> files,@RequestPart("ticketData") String ticketData,@RequestPart("employeeData") String employeeData) {
		
		
	
		
		System.out.println("----------------Entered into ticket controller---save ticket--------------");
		
		
		for(MultipartFile m:files)
		{
			System.out.println(m.getOriginalFilename());
			
			System.out.println(m.getContentType());
			
			
		}
		
		
		
		
		
		
		
		System.out.println("-------------------ENTERED SAVE TICKET-----------------");

	    Long ticketId = ticketService.saveTicket(files,ticketData,employeeData);

		System.out.println("TICKETID: " + ticketId);
		
		  if (ticketId !=0) {
			return new ResponseEntity<Long>(ticketId, HttpStatus.OK);
		}

		return new ResponseEntity<String>("Ticket creation failed", HttpStatus.BAD_REQUEST);

	}

	
	
	
	

	@GetMapping("/all-departments")
	public ResponseEntity<?> getDepartmentsData() {
		
		//Below write code using web client to communicate with employee service, to get list of departments available


		List<DepartmentDTO> departmentsList = ticketService.getAllDepartments();
		
		        
		
		if (departmentsList != null) {
			return new ResponseEntity<List<DepartmentDTO>>(departmentsList, HttpStatus.OK);
		}

		else {
			return new ResponseEntity<String>("fetching departments failed", HttpStatus.BAD_REQUEST);
		}

	}

	
	
	@GetMapping(value = "/raised-tickets/{roles}/{empId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> showRaisedTickets(@PathVariable("roles") String roles,@PathVariable("empId") Long empId) throws ParseException {

		System.out.println("Entered into raised tickets "+roles+"-------"+empId);
		
		List<TicketInfo> ticketsListData = ticketService.getRaisedTickets(roles,empId);
		
		List<TicketInfo> ticketsList=new ArrayList<TicketInfo>();
		
		if (ticketsListData != null) {
			
			
            
			// 12-hour format 
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");

			for (TicketInfo ticket : ticketsListData) {
				
				LocalDateTime localDateTime = ticket.getCreatedDate();
				
		       
		        
		        // convert the LocalDateTime object into the desired 12-hour format string
		        String formattedDateTime12 = localDateTime.format(formatter);

			          ticket.setCreatedDateFormat(formattedDateTime12);
			          
			          
			          if(ticket.getStatus().equalsIgnoreCase("CLOSED")|| ticket.getStatus().equalsIgnoreCase("CANCELLED") || ticket.getStatus().equalsIgnoreCase("RESOLVED"))
			        		  {}
			          
			          else
			          {
			        	  
			        	  
			        	  
			        	  String durationCalculation = ticketService.durationCalculation(ticket.getCreatedDate(), LocalDateTime.now());
			        	  
			        	  ticket.setPendingTime(durationCalculation);
			          }
			          
			          ticketsList.add(ticket);
				
				
				
				
				

			}

			return new ResponseEntity<Object>(ticketsList, HttpStatus.OK);

		}

		else

			return new ResponseEntity<Object>("NoTicketsAvailable", HttpStatus.BAD_REQUEST);

	}
	
	
	
	

	@GetMapping(value = "/assigned-tickets/{roles}/{empId}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> showAssignedTickets(@PathVariable("roles") String roles,@PathVariable("empId") Long empId) {

		List<TicketInfo>    ticketsList= ticketService.getAssignedTickets(roles,empId);
		
		List<TicketInfo> assignedTicketsList=new ArrayList<TicketInfo>();
		
		WebClient wc = WebClient.create();
		
		if (ticketsList != null) {
			
			// 12-hour format 
	        DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
	        
			for (TicketInfo ticket : ticketsList) {
				
				
               LocalDateTime localDateTime = ticket.getCreatedDate();
				
		       
		        
		        // Format the LocalDateTime object into the desired 12-hour format string
		        String formattedDateTime = localDateTime.format(formatter12);

			          ticket.setCreatedDateFormat(formattedDateTime);
			          
			   if(ticket.getStatus().equalsIgnoreCase("CLOSED") || ticket.getStatus().equalsIgnoreCase("RESOLVED"))
	        		  {}
	          
	           else
	           {
	        	  
	        	  
	        	  
	        	   String durationCalculation = ticketService.durationCalculation(ticket.getCreatedDate(), LocalDateTime.now());
	        	  
	        	   ticket.setPendingTime(durationCalculation);
	            }
			          
			          
			          
			          
			          
			          
			          
			         assignedTicketsList.add(ticket);
				

                   TicketHelpTeam helpTeamMember =null;
                 
				if(ticket.getAssignedToAdminId()==ticket.getAssignedToEmployeeId())
				
				
				{  
					
					helpTeamMember= ticketHelpTeamRepository.findByemployeeId(ticket.getAssignedToAdminId());
				
				}
				
				if(ticket.getAssignedToAdminId()!=ticket.getAssignedToEmployeeId())
					
					
				{  
					helpTeamMember= ticketHelpTeamRepository.findByemployeeId(ticket.getAssignedToEmployeeId());
				
				}
				      
				
				
				      String empData=helpTeamMember.getEmployeeId()+"-"+helpTeamMember.getTeamMemberType()+"-"+helpTeamMember.getEmployeeName()+"-"+helpTeamMember.getEmployeeOrganisation();

			           
				  
				         ticket.setAssignedToEmpIdData(empData);
			}

			return new ResponseEntity<Object>(assignedTicketsList, HttpStatus.OK);
		}

		else

			return new ResponseEntity<Object>("NoTicketsAvailable", HttpStatus.BAD_REQUEST);

	}
	
	
	@DeleteMapping(value = "/delete-ticket/{ticketId}/{roles}/{empId}")
	public ResponseEntity<String> deleteTicket(@PathVariable("ticketId") Long ticketId,@PathVariable("roles") String roles,@PathVariable("empId") Long empId) {

		System.out.println("*******************");
		System.out.println("From Delete Ticket controller: " + ticketId);
		System.out.println("*******************");

		boolean result = ticketService.deleteTicket(ticketId,roles,empId);

		if (result) {
			System.out.println("*******************");
			System.out.println("From Delete Ticket controller: " + result);
			System.out.println("*******************");
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		else {
			System.out.println("*******************");
			System.out.println("From Delete Ticket controller: " + result);
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}
	}

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PutMapping(value = "/update-ticket/{empId}")
	public ResponseEntity<String> updateTicketData(@RequestPart(required = false) List<MultipartFile> files,@RequestPart("ticketData") String ticketData,@PathVariable("empId") Long empId)

	{
		System.out.println("Entered into updation of ticket in controller");
		
		System.out.println(ticketData);
		
		

		TicketInfo ticketInfo = ticketService.updateTicket(files,ticketData,empId);

		if (ticketInfo != null)

		{
			return new ResponseEntity<String>("successfully updation done", HttpStatus.OK);

		}

		else

			return new ResponseEntity<String>("failed to update", HttpStatus.BAD_REQUEST);
	}
	
	
	

	
	
	
	
	@PostMapping(value = "/available-help-team-members")
	public ResponseEntity<Object> availableHelpTeamPeople(@RequestPart(required = false) MultipartFile file,@RequestPart("employeeData") String employeeData)

	{
	
		System.out.println("Entered into show available help team members in controller: " );
		
		List<TicketHelpteamDTO> helpTeam = null;
		
		Gson gson = new Gson();
		
		JSONObject jsonObj = new JSONObject(employeeData);
		
	    EmployeeDTO employeeBean= gson.fromJson(jsonObj.toString(), EmployeeDTO.class);
	    
	    System.out.println("Employee Bean: " + employeeBean.getEmpId() + ": " );
		
		String role=employeeBean.getRoles();
		
		
		if (role.equalsIgnoreCase("Admin"))

		{
			//If login person is department Admin means, then he can choose some available team members
			helpTeam = ticketService.getAvailableEmployeesFromDepartment(employeeBean);
		}

		if (role.equalsIgnoreCase("Employee"))

		{
			//If login person is just employee means, then he can choose some available team members
			helpTeam = ticketService.getAvailableEmployees(employeeBean);
		}

		if (helpTeam.size() != 0) {
			
			List<String> availableMembers=new ArrayList<String>();
			for(TicketHelpteamDTO team:helpTeam)
			
			{
				
				availableMembers.add(team.getEmployeeId()+"-"+team.getRole()+"-"+team.getEmployeeName()+"-"+team.getEmployeeOrganisation());
			}
			
			return new ResponseEntity<Object>(availableMembers, HttpStatus.OK);
		}

		else

			return new ResponseEntity<Object>("No records found", HttpStatus.BAD_REQUEST);
	}

	
	
	
	

	@PostMapping(value = "/create-comment/{comment}")

	public ResponseEntity<String> saveComment(@RequestBody TicketCommentDTO commentBean,@PathVariable("comment") String comment) {
		
		System.out.println("Entered into comment controller");
		System.out.println(commentBean);
		System.out.println(comment);

		String response = ticketService.insertComment(commentBean,comment);

		System.out.println(response);

		if (response.equalsIgnoreCase("success"))

		{

			return new ResponseEntity<String>("Successfully Commented", HttpStatus.OK);

		}

		else

			return new ResponseEntity<String>("Failed To Comment", HttpStatus.BAD_REQUEST);

	}
	
	

	@GetMapping(value = "/view-comments/{empId}/{ticketId}")
    public ResponseEntity<?> showComments(@PathVariable("empId") Long empId,
			@PathVariable("ticketId") Long ticketId) {
		System.out.println("Entered into view comments sesction");
		


		List<TicketComment> commentsList = ticketService.displayComments(empId, ticketId);
		
		System.out.println(commentsList.size());

		if (commentsList.size() != 0)

		{
			for (TicketComment ticketComment : commentsList) {
				ticketComment.setTicketIdNum(ticketComment.getTicketId().getId());
				

			}

			return new ResponseEntity<Object>(commentsList, HttpStatus.OK);

		}

		else

			return new ResponseEntity<Object>("No records found", HttpStatus.BAD_REQUEST);

	}

	
	@GetMapping(value = "/view-ticket-history/{empId}/{ticketId}")

	public ResponseEntity<?> showTicketHistory(@PathVariable("empId") Long empId,
			@PathVariable("ticketId") Long ticketId) {
		System.out.println("Entered into Ticket History Section");

		List<TicketHistory> displayHistoryList = ticketService.displayHistory(empId, ticketId);

		if (displayHistoryList.size() != 0)

		{

			    for (TicketHistory ticketHistory : displayHistoryList) {
				ticketHistory.setTicketIdNum(ticketHistory.getTicketId().getId());

			}

			return new ResponseEntity<Object>(displayHistoryList, HttpStatus.OK);

		}

		else

			return new ResponseEntity<Object>("No records found", HttpStatus.BAD_REQUEST);

	}
	
	

	@PostMapping(value = "/cancel-or-close/{ticketId}/{empId}")

	public ResponseEntity<String> cancelOrCloseTicket(@PathVariable("ticketId") Long ticketId,
			@PathVariable("empId") Long empId) {
		
		System.out.println("Entered into Ticket Cancel/Close Section");

		String msg = ticketService.cancelOrCloseTicketService(ticketId, empId);

		if (msg.equalsIgnoreCase("success"))

		{

			return new ResponseEntity<String>("Successfully Done", HttpStatus.OK);

		}

		else

			return new ResponseEntity<String>("Operation Failed", HttpStatus.BAD_REQUEST);
	}
	
	
	
	@GetMapping(value = "/dashboard/{empId}/{role}")
	public ResponseEntity<?> dashboardOfTickets(@PathVariable("empId") Long empId,@PathVariable("role") String role)
	{
	
		System.out.println("Entered in to ticket dash board of controller");
		
		
       List<Map<String, Integer>> ticketsDashboardCountList = ticketService.getCountOfTicketsToDisplayInDashboard(empId,role);
		
	 return new ResponseEntity<List<Map<String, Integer>>>(ticketsDashboardCountList, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	@DeleteMapping(value = "/delete-document/{ticketId}/{docId}/{empId}")
	public ResponseEntity<String> deleteDoc(@PathVariable("ticketId") Long ticketId,@PathVariable("docId") Long docId,@PathVariable("empId") Long empId) {

		
		
		System.out.println("Entered into  Delete Document controller: " + ticketId+" "+docId+" "+empId);
		

		boolean result = ticketService.deletingDocument(ticketId,docId,empId);

		if (result) {
			System.out.println("*******************");
			System.out.println("From Delete Document controller: " + result);
			System.out.println("*******************");
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		else {
			System.out.println("*******************");
			System.out.println("From Delete  Document controller: " + result);
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	@GetMapping(value="/get-document/{docId}")
	public ResponseEntity<?>getDocument(@PathVariable("docId") Long docId)
	{
		
		System.out.println(fetchAPI);
		System.out.println("Entered into getDocument function in ticket controller");
		System.out.println(docId);
		
		
		FileAndContentTypeBean fileBean = new DocumentOperations().openAndDownloadDocument(docId, fetchAPI);
		
		byte[] file = fileBean.getFile();
		
		String contentType = fileBean.getContentType();
		
		 return  ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(contentType)).body(file);
		
		
	}
	
	
	
	@PostMapping(value="/get-all-documents/{docIds}")
	public ResponseEntity<?> getAllDocuments(@PathVariable("docIds") List<Long> docsList) throws IOException
	{
		FileAndContentTypeBean fileBean = new DocumentOperations().downloadMultipleFiles(docsList,downloadMultipleFilesAPI);
		
	
		System.out.println(docsList.size());
		
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=documents.zip");
        return ResponseEntity.status(HttpStatus.OK)
                .headers(headers)
                .contentType(MediaType.valueOf(fileBean.getContentType()))
                .body(fileBean.getFile());      	    
	
}

	


	
	
	 



@GetMapping(value = "/charts/{empId}")
public ResponseEntity<?> displayTicketsInCharts(@PathVariable("empId") Long empId)
{
	
	System.out.println("Entered into raised tickets charts");

	Map<String, Map<String, Map<String, Integer>>> ticketsCountList = ticketService.getCountOfTicketsToDisplayInCharts(empId);
	
 return new ResponseEntity< Map<String, Map<String, Map<String, Integer>>>>(ticketsCountList, HttpStatus.OK);
}




@GetMapping(value = "/charts/{empId}/{role}")
public ResponseEntity<?> displayAssignedTicketsInCharts(@PathVariable("empId") Long empId,@PathVariable("role") String role)
{

	
	System.out.println("Entered into assigned tickets charts");
	Map<String, Map<String, Map<String, Integer>>> ticketsCountList = ticketService.getCountOfAssignedTicketsToDisplayInCharts(empId,role);
	
 return new ResponseEntity< Map<String, Map<String, Map<String, Integer>>>>(ticketsCountList, HttpStatus.OK);
}
	
}








