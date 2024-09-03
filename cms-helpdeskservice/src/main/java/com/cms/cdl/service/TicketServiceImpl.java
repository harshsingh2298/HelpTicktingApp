package com.cms.cdl.service;


import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.JSONObject;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;

import com.cms.cdl.constants.ChartsValues;
import com.cms.cdl.dto.DepartmentDTO;
import com.cms.cdl.dto.DocumentDTO;
import com.cms.cdl.dto.EmailDataDTO;
import com.cms.cdl.dto.EmployeeDTO;
import com.cms.cdl.dto.LocationDTO;
import com.cms.cdl.dto.TicketBeanRequestDTO;
import com.cms.cdl.dto.TicketBeanResponseDTO;
import com.cms.cdl.dto.TicketCommentDTO;
import com.cms.cdl.dto.TicketDocumentsResponseDTO;
import com.cms.cdl.dto.TicketHelpteamDTO;
import com.cms.cdl.model.ServiceLevelAgreement;
import com.cms.cdl.model.TicketComment;
import com.cms.cdl.model.TicketDocuments;
import com.cms.cdl.model.TicketHelpTeam;
import com.cms.cdl.model.TicketHistory;
import com.cms.cdl.model.TicketInfo;
import com.cms.cdl.repository.ServiceLevelAgreementRepository;
import com.cms.cdl.repository.TicketCommentRepository;
import com.cms.cdl.repository.TicketDocumentsRepository;
import com.cms.cdl.repository.TicketHelpTeamRepository;
import com.cms.cdl.repository.TicketHistoryRepository;
import com.cms.cdl.repository.TicketInfoRepository;
import com.cms.cdl.utils.DocumentOperations;
import com.google.gson.Gson;

import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

@Service
public class TicketServiceImpl implements TicketService {

	@Autowired
	private TicketInfoRepository ticketRepository;
	
	@Autowired
	private TicketDocumentsRepository ticketDocumentsRepository;

	@Autowired
	private TicketHelpTeamRepository ticketHelpTeamRepository;

	@Autowired
	TicketCommentRepository ticketCommentRepository;
	
	@Autowired
	ServiceLevelAgreementRepository ticketServiceLevelAgreementRepository;

	@Autowired
	TicketHistoryRepository ticketHistoryRepository;
	
	@Autowired
	private RabbitTemplate rabbitTemplate;

	private List<TicketHelpTeam> ticketHelpTeamBranchList1;
   

	@Value("${spring.rabbitmq.exchange}")
	private String emailExchange;

	@Value("${spring.rabbitmq.routingkey}")
	private String emailRoutingKey;
	
	
	private EmployeeDTO empBean;


	private boolean result;

	private final String errorMessage = "Self Assignment Not Possible";
	
	
	 
	 
	 @Value("${employee.postAPI}")
     private String postAPI;
	 

	 @Value("${employee.uploaddirectory}")
     private String uploaddirectory;
	 
	 
	 

	/**
	 * This function will perform storing of newly raised ticket information into
	 * database
	 * 
	 * @param ticketData - It contains raised ticket information
	 * @param employeeData - It contains raised employee information
	 * @return - returns ticketId, once if raised ticket is successfully stored in
	 *         database
	 */

	public Long saveTicket(List<MultipartFile> file, String ticketData,String employeeData) {
		
		
		
		DocumentDTO documentBean = new DocumentDTO();

		String data = "";

		Gson gson = new Gson();

		System.out.println("Entered into  ticket save method");

//		System.out.println(file.getOriginalFilename());
		
		JSONObject jsonObject = new JSONObject(ticketData);

		TicketBeanRequestDTO ticketBean = gson.fromJson(jsonObject.toString(), TicketBeanRequestDTO.class);
		
		
		JSONObject jsonObj = new JSONObject(employeeData);
		
	     empBean= gson.fromJson(jsonObj.toString(), EmployeeDTO.class);
		
	     
		
		System.out.println(empBean.getEmailId());
		
		System.out.println(empBean.getLocation().getCountry());
		
		
		
		
		LocationDTO location = empBean.getLocation();
		
		String employeeOrganisation = empBean.getEmployeeOrganisation();
		
		
		String locationData=location.getCountry()+"."+location.getState()+"."+location.getDistrict()+"."+location.getCity()+"."+location.getLocationName();
		
		
		String to = empBean.getEmailId();

		

        TicketInfo ticket = new TicketInfo();

		BeanUtils.copyProperties(ticketBean, ticket);

		

	
        String mainDepartment = "cms." + ticketBean.getMainDepartment().toLowerCase();

		String employeeOrg = "cms." + ticketBean.getMainDepartment().toLowerCase() + "." + ticketBean.getSubDepartment().toLowerCase();

		
        ticket.setRaisedByEmployeeRole(empBean.getRoles());

		ticket.setRaisedByEmployeeId(empBean.getEmpId());

		ticket.setCountry(location.getCountry());

		ticket.setState(location.getState());

		ticket.setRaisedByEmployeeOrg(employeeOrganisation);

		ticket.setDistrict(location.getDistrict());

		ticket.setCity(location.getCity());

		ticket.setLocation(location.getLocationName());
		
		
       List<ServiceLevelAgreement> slaList = ticketServiceLevelAgreementRepository.findAll();
		
       
      
		
		
		if(ticket.getSeverity().equalsIgnoreCase("Critical"))
		{
			
			ticket.setTimeLine(slaList.get(0).getTimeLine());
			
		}
		else if(ticket.getSeverity().equalsIgnoreCase("High"))
		{
			ticket.setTimeLine(slaList.get(1).getTimeLine());
		}
		
		else if(ticket.getSeverity().equalsIgnoreCase("Medium"))
		{
			ticket.setTimeLine(slaList.get(2).getTimeLine());
		}
		
		else if(ticket.getSeverity().equalsIgnoreCase("Low"))
		{
			ticket.setTimeLine(slaList.get(3).getTimeLine());
		}
		
		
		

		

		TicketInfo ticketdata = null;



		
		ticketHelpTeamBranchList1 = ticketHelpTeamRepository.findBycountryAndStateAndDistrictAndCityAndLocation(
				location.getCountry(), location.getState(), location.getDistrict(), location.getCity(),location.getLocationName());

		

		if (ticketHelpTeamBranchList1 != null) {

			for (TicketHelpTeam thmember : ticketHelpTeamBranchList1) {

				if (employeeOrg.equalsIgnoreCase(thmember.getEmployeeOrganisation())) {

					if (thmember.getTeamMemberType().equalsIgnoreCase("Admin")) {

						ticket.setAssignedToAdminId(thmember.getEmployeeId());

						ticket.setAssignedToEmployeeId(thmember.getEmployeeId());

						

						System.out.println("Entered into  ticket assignment");
						
						
						if(file!=null)
						{
							System.out.println(file.size());
							if(file.size()>0 && file.size()<=3)
						{      
							System.out.println(file.size());
							
							ticket.setCreatedBy(empBean.getEmpId());
							
							ticket.setStatus("CREATED");
							
							ticketdata = ticketRepository.save(ticket);
						
						
						       Long ticketId = ticketdata.getId();
						
						       List<Long> docIDSList = uploadDocument(empBean, file, locationData);
						
						
						           if(docIDSList.size()>0)
						       {
							
							        for(Long docId:docIDSList)
							       {
								
								       TicketDocuments ticketDocuments = new TicketDocuments();
								
								       ticketDocuments.setDocId(docId);
								       
								       ticketDocuments.setTicketId(ticketId);
								       
								       ticketDocumentsRepository.save(ticketDocuments);
								
							       }
						        }
						}  
						
						}
						           
				        else
						        	   
						{
				        	ticket.setCreatedBy(empBean.getEmpId());
				        	
				        	ticket.setStatus("CREATED");
				        	
				        	ticketdata = ticketRepository.save(ticket);
				        	
						}
						
					    TicketHistory history = new TicketHistory();

						history.setTicketId(ticketdata);
						history.setInitialAssignedToEmployeeId(thmember.getEmployeeId());
						history.setHistoryInfo("Ticket created with TicketId: " + ticketdata.getId());

						ticketHistoryRepository.save(history);
						
						result = true;

						System.out.println("Boolean1:  " + result);

						String toEmail = thmember.getEmailId();

						System.out.println("sending mail notification");

						String subject = "Ticket Rised by " + empBean.getEmpFirstName()+"."+ empBean.getEmpLastName()+ " ---> EmployeeId: " + empBean.getEmpId();

						String body = "Generated TICKETID ---> " + ticketdata.getId();

					    sendMail(toEmail, subject, body);

						System.out.println("mail notification sent successfully");

						return ticketdata.getId();

					}

				}

			}

		}

		if (result == false) {
			List<TicketHelpTeam> ticketHelpTeamMemberCityList = ticketHelpTeamRepository
					.findBycountryAndStateAndDistrictAndCityAndLocation(location.getCountry(), location.getState(),
							location.getDistrict(), location.getCity(), null);

			if (ticketHelpTeamMemberCityList.size() > 0) {
				TicketHelpTeam ticketHelpTeamMemberCity = ticketHelpTeamMemberCityList.get(0);

				if (ticketHelpTeamMemberCity.getTeamMemberType().equalsIgnoreCase("Admin")
						&& mainDepartment.equalsIgnoreCase(ticketHelpTeamMemberCity.getEmployeeOrganisation())) {
					ticket.setAssignedToAdminId(ticketHelpTeamMemberCity.getEmployeeId());

					ticket.setAssignedToEmployeeId(ticketHelpTeamMemberCity.getEmployeeId());      

					

					System.out.println("Entered into  ticket assignment");
					
					if(file!=null)
					{

						System.out.println(file.size());
					if(file.size()>0 && file.size()<=3)
					{      
						System.out.println(file.size());
						
						ticket.setCreatedBy(empBean.getEmpId());
						
						ticket.setStatus("CREATED");
						
						ticketdata = ticketRepository.save(ticket);
					
					
					       Long ticketId = ticketdata.getId();
					
					       List<Long> docIDSList = uploadDocument(empBean, file, locationData);
					
					
					           if(docIDSList.size()>0)
					       {
						
						        for(Long docId:docIDSList)
						       {
							
							       TicketDocuments ticketDocuments = new TicketDocuments();
							
							       ticketDocuments.setDocId(docId);
							       
							       ticketDocuments.setTicketId(ticketId);
							       
							       ticketDocumentsRepository.save(ticketDocuments);
							
						       }
					        }
					}           
					     
					}
			        else
					        	   
					{
			        	ticket.setCreatedBy(empBean.getEmpId());
			        	
			        	ticket.setStatus("CREATED");
			        	
			        	ticketdata = ticketRepository.save(ticket);
			        	
					}

					TicketHistory history = new TicketHistory();

					history.setTicketId(ticketdata);
					history.setInitialAssignedToEmployeeId(ticketHelpTeamMemberCity.getEmployeeId());
					history.setHistoryInfo("Ticket created with TicketId: " + ticketdata.getId());

					ticketHistoryRepository.save(history);

					result = true;

					System.out.println("Boolean2:  " + result);

					String toEmail = ticketHelpTeamMemberCity.getEmailId();

					System.out.println("sending mail notification");

					String subject = "Ticket Rised by " + empBean.getEmpFirstName()+"."+ empBean.getEmpLastName()+ " ---> EmployeeId: " + empBean.getEmpId();

					String body = "Generated TICKETID ---> " + ticketdata.getId();

					sendMail(toEmail, subject, body);
					
					System.out.println("mail notification sent successfully");

					return ticketdata.getId();

				}
			}

		}

		if (result == false) {
			List<TicketHelpTeam> ticketHelpTeamMemberDistrictList = ticketHelpTeamRepository
					.findBycountryAndStateAndDistrictAndCityAndLocation(location.getCountry(), location.getState(),
							location.getDistrict(), null, null);

			TicketHelpTeam ticketHelpTeamMemberDistrict = ticketHelpTeamMemberDistrictList.get(0);

			if (ticketHelpTeamMemberDistrict.getTeamMemberType().equalsIgnoreCase("Admin")
					&& mainDepartment.equalsIgnoreCase(ticketHelpTeamMemberDistrict.getEmployeeOrganisation())) {
				ticket.setAssignedToAdminId(ticketHelpTeamMemberDistrict.getEmployeeId());

				ticket.setAssignedToEmployeeId(ticketHelpTeamMemberDistrict.getEmployeeId());

			

				System.out.println("Entered into  ticket assignment");
				if(file!=null)
				{

					System.out.println(file.size());
				if(file.size()>0 && file.size()<=3)
				{      
					
					System.out.println(file.size());
					ticket.setCreatedBy(empBean.getEmpId());
					
					ticket.setStatus("CREATED");
					
					ticketdata = ticketRepository.save(ticket);
				
				
				       Long ticketId = ticketdata.getId();
				
				       List<Long> docIDSList = uploadDocument(empBean, file, locationData);
				
				
				           if(docIDSList.size()>0)
				       {
					
					        for(Long docId:docIDSList)
					       {
						
						       TicketDocuments ticketDocuments = new TicketDocuments();
						
						       ticketDocuments.setDocId(docId);
						       
						       ticketDocuments.setTicketId(ticketId);
						       
						       ticketDocumentsRepository.save(ticketDocuments);
						
					       }
				        }
				}     
				}
				           
		        else
				        	   
				{
		        	ticket.setCreatedBy(empBean.getEmpId());
		        	
		        	ticket.setStatus("CREATED");
		        	
		        	ticketdata = ticketRepository.save(ticket);
		        	
				}
				TicketHistory history = new TicketHistory();

				history.setTicketId(ticketdata);

				history.setInitialAssignedToEmployeeId(ticketHelpTeamMemberDistrict.getEmployeeId());

				history.setHistoryInfo("Ticket created with TicketId: " + ticketdata.getId());

				ticketHistoryRepository.save(history);

				result = true;

				System.out.println("Boolean3:  " + result);

				String toEmail = ticketHelpTeamMemberDistrict.getEmailId();

				System.out.println("sending mail notification");

				String subject = "Ticket Rised by " + empBean.getEmpFirstName()+"."+ empBean.getEmpLastName()+ " ---> EmployeeId: " + empBean.getEmpId();

				String body = "Generated TICKETID ---> " + ticketdata.getId();

			    sendMail(toEmail, subject, body);

				System.out.println("mail notification sent successfully");

				return ticketdata.getId();

			}

		}

		if (result == false) {
			List<TicketHelpTeam> ticketHelpTeamMemberStateList = ticketHelpTeamRepository
					.findBycountryAndStateAndDistrictAndCityAndLocation(location.getCountry(), location.getState(), null, null,
							null);

			TicketHelpTeam ticketHelpTeamMemberState = ticketHelpTeamMemberStateList.get(0);

			if (ticketHelpTeamMemberState.getTeamMemberType().equalsIgnoreCase("Admin")
					&& mainDepartment.equalsIgnoreCase(ticketHelpTeamMemberState.getEmployeeOrganisation())) {
				ticket.setAssignedToAdminId(ticketHelpTeamMemberState.getEmployeeId());

				ticket.setAssignedToEmployeeId(ticketHelpTeamMemberState.getEmployeeId());

				

				System.out.println("Entered into  ticket assignment");
				if(file!=null)
				{

					System.out.println(file.size());
				if(file.size()>0 && file.size()<=3)
					
					
				{      
					
					System.out.println(file.size());
					ticket.setCreatedBy(empBean.getEmpId());
					
					ticket.setStatus("CREATED");
					
					ticketdata = ticketRepository.save(ticket);
				
				
				       Long ticketId = ticketdata.getId();
				
				       List<Long> docIDSList = uploadDocument(empBean, file, locationData);
				
				
				           if(docIDSList.size()>0)
				       {
					
					        for(Long docId:docIDSList)
					       {
						
						       TicketDocuments ticketDocuments = new TicketDocuments();
						
						       ticketDocuments.setDocId(docId);
						       
						       ticketDocuments.setTicketId(ticketId);
						       
						       ticketDocumentsRepository.save(ticketDocuments);
						
					       }
				        }
				}           
				  
				}
		        else
				        	   
				{
		        	ticket.setCreatedBy(empBean.getEmpId());
		        	
		        	ticket.setStatus("CREATED");
		        	
		        	ticketdata = ticketRepository.save(ticket);
		        	
				}

				TicketHistory history = new TicketHistory();

				history.setTicketId(ticketdata);
				history.setInitialAssignedToEmployeeId(ticketHelpTeamMemberState.getEmployeeId());
				history.setHistoryInfo("Ticket created with TicketId: " + ticketdata.getId());

				ticketHistoryRepository.save(history);

				result = true;

				System.out.println("Boolean4:  " + result);

				String toEmail = ticketHelpTeamMemberState.getEmailId();

				System.out.println("sending mail notification");

				String subject = "Ticket Rised by " + empBean.getEmpFirstName()+"."+ empBean.getEmpLastName()+ " ---> EmployeeId: " + empBean.getEmpId();

				String body = "Generated TICKETID ---> " + ticketdata.getId();

				sendMail(toEmail, subject, body);
				
				System.out.println("mail notification sent successfully");

				return ticketdata.getId();

			}

		}

		if (result == false) {

			List<TicketHelpTeam> ticketHelpTeamMemberCountryList = ticketHelpTeamRepository
					.findBycountryAndStateAndDistrictAndCityAndLocation(location.getCountry(), null, null, null, null);

			TicketHelpTeam ticketHelpTeamMemberCountry = ticketHelpTeamMemberCountryList.get(0);

			if (ticketHelpTeamMemberCountry.getTeamMemberType().equalsIgnoreCase("Admin")
					&& mainDepartment.equalsIgnoreCase(ticketHelpTeamMemberCountry.getEmployeeOrganisation())) 
			{
				ticket.setAssignedToAdminId(ticketHelpTeamMemberCountry.getEmployeeId());

				ticket.setAssignedToEmployeeId(ticketHelpTeamMemberCountry.getEmployeeId());

				

				System.out.println("Entered into  ticket assignment");
				if(file!=null)
				{

				if(file.size()>0 && file.size()<=3)
				{      
					ticket.setCreatedBy(empBean.getEmpId());
					
					ticket.setStatus("CREATED");
					
					ticketdata = ticketRepository.save(ticket);
				
				
				       Long ticketId = ticketdata.getId();
				
				       List<Long> docIDSList = uploadDocument(empBean, file, locationData);
				
				
				           if(docIDSList.size()>0)
				       {
					
					        for(Long docId:docIDSList)
					       {
						
						       TicketDocuments ticketDocuments = new TicketDocuments();
						
						       ticketDocuments.setDocId(docId);
						       
						       ticketDocuments.setTicketId(ticketId);
						       
						       ticketDocumentsRepository.save(ticketDocuments);
						
					       }
				        }
				}     
				}
				           
		        else
				        	   
				{
		        	ticket.setCreatedBy(empBean.getEmpId());
		        	
		        	ticket.setStatus("CREATED");
		        	
		        	ticketdata = ticketRepository.save(ticket);
		        	
				}
				TicketHistory history = new TicketHistory();

				history.setTicketId(ticketdata);
				history.setInitialAssignedToEmployeeId(ticketHelpTeamMemberCountry.getEmployeeId());
				history.setHistoryInfo("Ticket created with TicketId: " + ticketdata.getId());

				ticketHistoryRepository.save(history);

				result = true;

				System.out.println("Boolean5:  " + result);

				String toEmail = ticketHelpTeamMemberCountry.getEmailId();

				System.out.println("sending mail notification");

				String subject = "Ticket Rised by " + empBean.getEmpFirstName()+"."+ empBean.getEmpLastName()+ " ---> EmployeeId: " + empBean.getEmpId();

				String body = "Generated TICKETID ---> " + ticketdata.getId();
				
				//Here we send following mail data as input to RabbitMQ

				sendMail(toEmail, subject, body);

				System.out.println("mail notification sent successfully");

				return ticketdata.getId();

			}

		}

		  		return 0l;

	}
	
	

	
	
	public boolean sendMail(String toEmail, String subject, String body) {

		
		
		
		EmailDataDTO emailDataBean=new EmailDataDTO();
				
				emailDataBean.setTo(toEmail);
		        emailDataBean.setSubject(subject);
		        emailDataBean.setBody(body);
		        
		      
		
		rabbitTemplate.convertAndSend(emailExchange, emailRoutingKey, emailDataBean);

		return true;
	}
	
	

	/*
	 * This function will perform uploading of documents to DMS service
	 */
	public List<Long> uploadDocument(EmployeeDTO employeeBean,List<MultipartFile> file, String locationData) {
		
		List<DocumentDTO> docDTOList=new ArrayList<>();
		
		List<Long> docIDSList=new ArrayList<>();
		
		DocumentDTO documentData = new DocumentDTO();
        documentData.setEmpId(empBean.getEmpId());
        documentData.setEmpOrg(empBean.getEmployeeOrganisation());
        documentData.setLocation(locationData);
        
        
        DocumentOperations docOperations = new DocumentOperations();
        
        try {
        	 docDTOList = docOperations.uploadDocument(documentData, file,postAPI,uploaddirectory);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		
        
        
        if(docDTOList.size()>0)
		{
        	for(DocumentDTO docDTO:docDTOList)
        	
        	{
        		
        		docIDSList.add(docDTO.getDocId());
        	}
        	
        	System.out.println(docIDSList.size());
        	return docIDSList;
		}
		
        else
              return null;	

	}



	

	/**
	 * This function will perform displaying all the available departments
	 * 
	 * @return - returns all the ticket categories available, like main departments
	 *         and their sub departments
	 */

	public List<DepartmentDTO> getAllDepartments() {
		
		
		
		WebClient webClient = WebClient.create();
		
		Mono<List<DepartmentDTO>> response = webClient.get()
				
			      .uri("http://192.168.249.17:9090/employee/allDepartments")
				  .accept(MediaType.APPLICATION_JSON)
				  .retrieve()
				  .bodyToMono(new ParameterizedTypeReference<List<DepartmentDTO>>() {});
				List<DepartmentDTO>  departmentsList = response.block();
                return departmentsList;

	}



	/**
	 * This function will perform displaying of tickets raised by particular employee
	 * 
	 * @return - returns all the tickets raised by particular employee
	 */
	public List<TicketInfo> getRaisedTickets(String role,Long empId) {



		List<TicketDocumentsResponseDTO> ticketDocumentsResponseBeanList=new ArrayList<TicketDocumentsResponseDTO>();
		
		
		System.out.println("----Entered into Raised Tickets----");

		List<TicketInfo> ticketList = null;

		if (role.equalsIgnoreCase("Admin")  || role.equalsIgnoreCase("Employee")) {

			ticketList = ticketRepository.findByraisedByEmployeeId(empId);

			System.out.println(ticketList.size());
		}
		
		
		if(ticketList.size()>0)
		{
			
			
			
			for(int i=0;i<ticketList.size();i++)
			{
				List<TicketDocuments> ticketDocsList = ticketDocumentsRepository.findByticketId(ticketList.get(i).getId());
				
				if(ticketDocsList.size()>0)
				{
				    for(TicketDocuments ticketDocs:ticketDocsList)
				    {
				          TicketDocumentsResponseDTO ticketDocumentsResponseBean = new TicketDocumentsResponseDTO();
				    
				    
				          BeanUtils.copyProperties(ticketDocs, ticketDocumentsResponseBean);
				    
				          ticketDocumentsResponseBeanList.add(ticketDocumentsResponseBean);
					
					}
				    
				    ticketList.get(i).setTicketDocumentsResponse(ticketDocumentsResponseBeanList);
				}
			}
			
			
		}
		
		return ticketList;
	}

	/**
	 * This function will perform displaying of tickets assigned to particular employee
	 * 
	 * @return - returns all the tickets assigned to particular employee
	 */
	public List<TicketInfo> getAssignedTickets(String role,Long empId) {
		
		List<TicketDocumentsResponseDTO> ticketDocumentsResponseBeanList=new ArrayList<TicketDocumentsResponseDTO>();

		EmployeeDTO employeeBean=new EmployeeDTO();

		List<TicketInfo> ticketList = null;

		
		System.out.println("-----Entered into Assigned Tickets-----");
		
		if (role.equalsIgnoreCase("Admin") ) {

			ticketList = ticketRepository.findByassignedToAdminId(empId);

			System.out.println(ticketList.size());
		}

		if (role.equalsIgnoreCase("Employee")) {

			ticketList = ticketRepository.findByassignedToEmployeeId(empId);
		}
		
		
		
		
		if(ticketList.size()>0)
		{
			
			
			
			for(int i=0;i<ticketList.size();i++)
			{
				List<TicketDocuments> ticketDocsList = ticketDocumentsRepository.findByticketId(ticketList.get(i).getId());
				
				if(ticketDocsList.size()>0)
				{
				    for(TicketDocuments ticketDocs:ticketDocsList)
				    {
				          TicketDocumentsResponseDTO ticketDocumentsResponseBean = new TicketDocumentsResponseDTO();
				    
				    
				          BeanUtils.copyProperties(ticketDocs, ticketDocumentsResponseBean);
				    
				          ticketDocumentsResponseBeanList.add(ticketDocumentsResponseBean);
					
					
				          ticketList.get(i).setTicketDocumentsResponse(ticketDocumentsResponseBeanList);
				          
				         
				          
				          
				     }
				}
			}
			
			
		}
		
		
		
		
		
		
		
		

		return ticketList;
	}

	/**
	 * This function will perform deletion of a ticket based on ticketId sent
	 * 
	 * @param id - It indicates Id number of a ticket
	 * @return - returns true or false whether successfully ticket record deleted or not
	 */

	@Transactional
	public boolean deleteTicket(Long ticketId, String role, Long empId) {

		EmployeeDTO employeeBean=new EmployeeDTO();

		TicketInfo ticket = ticketRepository.findById(ticketId).get();

		List<TicketHistory> historyList = ticketHistoryRepository.findByticketId(ticket);
		
		List<TicketComment> commentsList = ticketCommentRepository.findByticketId(ticket);

		System.out.println(historyList.size());
		System.out.println(commentsList.size());

		if ((role.equalsIgnoreCase("Admin"))
				&& ticket.getAssignedToAdminId()==empId)

		{

			ticketHistoryRepository.deleteAll(historyList);
			ticketCommentRepository.deleteAll(commentsList);

			boolean result = true;

			
			
			
		
			
			List<TicketDocuments> ticketDocsList = ticketDocumentsRepository.findByticketId(ticketId);
			
			   if(ticketDocsList.size()>0)        
			{ticketDocumentsRepository.deleteAll(ticketDocsList);}
			
			


			   
			   Optional<TicketInfo> ticketInfo = ticketRepository.findById(ticketId);
			   
			   
			   
		        if (ticketInfo.isPresent()) {
		        	
		        
		         
		            ticketRepository.deleteById(ticketId);
		        } else {
		            throw new NoSuchElementException("Ticket with id " + ticketId + " not found");
		        }

			
			
			System.out.println("Hiiiiiiiiiii");
			
			
			Optional<TicketInfo> ticketInfo2 = ticketRepository.findById(ticketId);

			if (!(ticketInfo2.isPresent())) {

				System.out.println("Entered");

				
				TicketHistory ticketHistory =new TicketHistory();
				ticketHistory.setHistoryInfo("Deleted Ticket: " + ticketId);
				ticketHistoryRepository.save(ticketHistory);
				return result;
			} 
			else
				return false;
		}

		else
			return false;

	}

	/**
	 * This function will perform updating of a ticket information based on ticketId
	 * and ticketInfo sent
	 * 
	 * @param ticketId   - It indicates Id of a ticket
	 * @param ticketData - It contains updated ticket information
	 * @return - returns updated ticket information 
	 */

	
	public TicketInfo updateTicket(List<MultipartFile> filesList, String ticketData,Long empId) {

		
		
		
		System.out.println("Entered into update ticket service");
		
		Gson gson = new Gson();

		//System.out.println("---------->"+filesList.get(0).getOriginalFilename()+"<--------------");

		JSONObject jsonObject = new JSONObject(ticketData);

		TicketBeanResponseDTO ticketResponseBean = gson.fromJson(jsonObject.toString(), TicketBeanResponseDTO.class);
		
		// write code for get the raised Employee details like location(country,state,...), raised employeeId,RaisedByEmployeeOrg
		// based on ticketId
		
	    TicketInfo ticket = ticketRepository.findById(ticketResponseBean.getId()).get();
	    
	    
	   String locationData= ticket.getCountry()+"."+ticket.getState()+"."+ticket.getDistrict()+"."+ticket.getCity()+"."+ticket.getLocation();
		
		EmployeeDTO empBean = new EmployeeDTO();
		
		empBean.setEmpId(ticket.getRaisedByEmployeeId());
		
		empBean.setEmployeeOrganisation(ticket.getRaisedByEmployeeOrg());
	   
		//Here upload new document or delete existing document
		
//	   if(filesList.size()>0)
//		{      
//		       Long ticketId = ticketResponseBean.getTicketId();
//		       List<Long> docIDSList = uploadDocument(empBean, filesList, locationData);
//				if(docIDSList.size()>0)
//							        {
//								
//								        for(Long docId:docIDSList)
//								       {
//									
//									       TicketDocuments ticketDocuments = new TicketDocuments();
//									
//									       ticketDocuments.setDocId(docId);
//									       
//									       ticketDocuments.setTicketId(ticketId);
//									       
//									       ticketDocumentsRepository.save(ticketDocuments);
//									
//								       }
//							        }
//	        }
//
		
		
		

		Long ticketId = ticketResponseBean.getId();


        //Existing Ticket information
		TicketInfo existingTicket = ticketRepository.findById(ticketId).get();

		
		if(empId==existingTicket.getRaisedByEmployeeId())
		{
			boolean b1=false;boolean b2=false;boolean b3=false;boolean b4=false;boolean b5=false;
			
			String data1="Ticket data changed : ";
			
			TicketHistory ticketHistory = new TicketHistory();
			
			    if(ticketResponseBean.getTitle().equalsIgnoreCase(existingTicket.getTitle()))
				{
					
				}
			    else
			    {
			    	b1=true;
			    	existingTicket.setTitle(ticketResponseBean.getTitle());
			    	
			    	existingTicket.setUpdatedBy(empId);
			    	
			    	ticketHistory.setTicketTitle("Modified");
			    }
				
				
				if(ticketResponseBean.getDescription().equalsIgnoreCase(existingTicket.getDescription()))
				{
					
				}
				
				else
			    {
					b2=true;
			    	existingTicket.setDescription(ticketResponseBean.getDescription());
			    	
			    	existingTicket.setUpdatedBy(empId);
			    	
			    	ticketHistory.setDescription("Modified");
			    }
				
				if(ticketResponseBean.getRemarks().equalsIgnoreCase(existingTicket.getRemarks()))
				{
					
				}
				else
			    {
					existingTicket.setUpdatedBy(empId);
			    	existingTicket.setRemarks(ticketResponseBean.getRemarks());
			    }
				
				if(ticketResponseBean.getMainDepartment().equalsIgnoreCase(existingTicket.getMainDepartment()))
				{
					
				}
				else
			    {
					b3=true;
					data1=data1+"MainDepartment=" + existingTicket.getMainDepartment();
					
					existingTicket.setMainDepartment(ticketResponseBean.getMainDepartment());
					
					data1=data1+ " To " + existingTicket.getMainDepartment();
					
					existingTicket.setUpdatedBy(empId);
			    	
			    	
			    }
				
				if(ticketResponseBean.getSubDepartment().equalsIgnoreCase(existingTicket.getSubDepartment()))
				{
					
				}
				else
			    {
					b4=true;
					data1=data1+" SubDepartment=" + existingTicket.getSubDepartment();
					
			    	existingTicket.setSubDepartment(ticketResponseBean.getSubDepartment());
			    	
			    	data1=data1+ " To " + existingTicket.getSubDepartment();
			    	
			    	existingTicket.setUpdatedBy(empId);
			    	
			    }
				
				if(ticketResponseBean.getSeverity().equalsIgnoreCase(existingTicket.getSeverity()))
				{
					
				}
				else
			    {
					b5=true;
					data1=data1+" Severity="+existingTicket.getSeverity();
					
			    	existingTicket.setSeverity(ticketResponseBean.getSeverity());
			    	
			    	data1=data1+ " To " + existingTicket.getSeverity();
			    	
			    	existingTicket.setUpdatedBy(empId);
			    }
				
				if(b1||b2||b3||b4||b5)
				{
					
					System.out.println("Employee Entrance");
					
					ticketHistory.setTicketId(existingTicket);
					ticketHistory.setHistoryInfo(data1);
					ticketHistoryRepository.save(ticketHistory);

					return ticketRepository.save(existingTicket);
					
					
				}
			
		}
			
			
		
		
		
		if(empId==existingTicket.getAssignedToAdminId() || empId==existingTicket.getAssignedToEmployeeId())
		{
			    boolean b1=false;boolean b2=false;boolean b3=false;boolean b4=false;boolean b5=false;boolean b6=false;
			
			    String data1="Ticket data changed : ";
			
			    TicketHistory ticketHistory = new TicketHistory();
			
			    if(ticketResponseBean.getStatus().equalsIgnoreCase(existingTicket.getStatus()))
				{
					
				}
			    else
			    {
			    	b1=true;
			    	
			    	
			    	String status = ticketResponseBean.getStatus();
			    	
			    	//in progress,close,resolve
			    	
			    	if(status.equalsIgnoreCase("RESOLVED"))
			    	{
			    		existingTicket.setResolvedDate(LocalDateTime.now());
			    		
			    		existingTicket.setPendingTime(null);
			    		
			    	}
			    	
			    	else if(status.equalsIgnoreCase("CLOSED"))
			    	{
			    		existingTicket.setClosedDate(LocalDateTime.now());
			    		existingTicket.setPendingTime(null);
			    		
			    	}
			    	
			    	else if(status.equalsIgnoreCase("IN PROGRESS"))
			    	{
			    		existingTicket.setInProgressDate(LocalDate.now());
			    		
			    	}
			    	
			    	else if(status.equalsIgnoreCase("OPEN"))
			    	{
			    		System.out.println("STATUS---->OPEN");
			    		existingTicket.setOpenDate(LocalDate.now());
			    		
			    	}
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	if(status.equalsIgnoreCase("RESOLVED") || status.equalsIgnoreCase("CLOSED") )
			    	{
			    		
			    		
			    		System.out.println("STATUS---->"+status);
			    		
			    		
			    		DateTimeFormatter formatter12 = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss a");
			    		//DateTimeFormatter formatter24 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			    		
			    		
			    		LocalDateTime createdDate =LocalDateTime.parse(ticketResponseBean.getCreatedDateFormat(),formatter12);
				    	
				    	LocalDateTime closedDate = existingTicket.getClosedDate();
				    	
				    	LocalDateTime resolvedDate = existingTicket.getResolvedDate();
				    	
				    	long hours=0;
				    	
				    	if(status.equalsIgnoreCase("CLOSED"))
				    	{ hours = createdDate.until(closedDate, ChronoUnit.HOURS);}
				    	
				    	else
				    		{hours = createdDate.until(resolvedDate, ChronoUnit.HOURS);}
				    	
				    	
				    	 System.out.println("Time Taken Hours:--->"+hours);
				    	
				    
				    	 
				    	 
				    	 String severity = ticketResponseBean.getSeverity();
				    	 
				    	 if((hours>4 && severity.equalsIgnoreCase("Critical")) || (hours>48 && severity.equalsIgnoreCase("High"))|| (hours>72 && severity.equalsIgnoreCase("Medium"))|| (hours>120 && severity.equalsIgnoreCase("Low")))
				    	 {
				    		 
				    		 existingTicket.setSLACompliance("Late");
				    		 
				    	 }
				    	
				    	 else if((hours==4 && severity.equalsIgnoreCase("Critical")) || (hours==48 && severity.equalsIgnoreCase("High"))|| (hours==72 && severity.equalsIgnoreCase("Medium"))|| (hours==120 && severity.equalsIgnoreCase("Low")))
				    	 {
				    		 
				    		 existingTicket.setSLACompliance("On-Time");
				    	 }
				    	 
				    	 else 
				    	 {
				    		 
				    		 existingTicket.setSLACompliance("Before-Time");
				    	 }
				    	 
				    	
			    		
			    	}
			    	
			    	data1=data1+"Status=" + existingTicket.getStatus();
			    	
			    	System.out.println("Existing status: "+existingTicket.getStatus());
			    	
			    	System.out.println("New Status: "+ticketResponseBean.getStatus());
			    	
			    	existingTicket.setStatus(ticketResponseBean.getStatus());
			    	data1=data1+ " To " + existingTicket.getStatus();
			    	existingTicket.setUpdatedBy(empId);
			    	
			    }
			    
			    
			    
			    String assignedToEmployeeId = ticketResponseBean.getAssignedToEmpIdData();
			    
			    String[] result = assignedToEmployeeId.split("-");
			    
			    long emplId = Long.parseLong(result[0]);
			    
			    String empRole=result[1];
			    
			   
			    
			   if(empRole.equalsIgnoreCase("Employee"))
				
			    {
				    if(emplId==existingTicket.getAssignedToEmployeeId())
				    	
				    {}
				    else
				    { 	
				    	b2=true;
				    data1=data1+"EmpId=" + existingTicket.getAssignedToEmployeeId();
			    	
				    existingTicket.setAssignedToEmployeeId(emplId);
				    
				    data1=data1+ " To " + existingTicket.getAssignedToEmployeeId();
			    	
			    	existingTicket.setUpdatedBy(empId);
			    	
			    	
			    	//Here get the above emplId related  email Id details from employee service by passing emplId as input 
				     
				    //in API using web client and send mail to him.
			    	
			    	String toEmail = "";

					System.out.println("sending mail notification");

					String subject = "Ticket Raised by " + " ---> EmployeeId: " + existingTicket.getRaisedByEmployeeId();

					String body = "Generated TICKETID ---> " + existingTicket.getId();
			    	
			    	
			    	sendMail(toEmail, subject, body);
			    	
			    	
			    	
			    	
			    	
				    }
			    }
			    
			    if(empRole.equalsIgnoreCase("Admin"))
					
			    {
                      if(emplId==existingTicket.getAssignedToAdminId())
				    	
				                {}
                      else
                      {
                    	  b6=true;
                    	  data1=data1+"EmpId=" + existingTicket.getAssignedToAdminId();
			    	         existingTicket.setAssignedToAdminId(emplId);
			    	         data1=data1+ " To " + existingTicket.getAssignedToAdminId();
			    	         existingTicket.setUpdatedBy(empId);
			    	         
						    	//Here get the above emplId related  email Id details from employee service by passing emplId as input 
						     
							    //in API using web client and send mail to him.
						    	String toEmail = "";

								System.out.println("sending mail notification");

								String subject = "Ticket Raised by " + " ---> EmployeeId: " + existingTicket.getRaisedByEmployeeId();

								String body = "Generated TICKETID ---> " + existingTicket.getId();
						    	
						    	
						    	sendMail(toEmail, subject, body);
			    	         
			    	         
			    	         
			    	         
                      }
                      
                      
                      existingTicket.setAssignedToEmployeeId(emplId);
                      
			    }
			    
			    
				
				if(ticketResponseBean.getRemarks().equalsIgnoreCase(existingTicket.getRemarks()))
				{
					
				}
				else
			    {
			        
			    	existingTicket.setRemarks(ticketResponseBean.getRemarks());
			    	
			    	existingTicket.setUpdatedBy(empId);
			    }
				
				if(ticketResponseBean.getMainDepartment().equalsIgnoreCase(existingTicket.getMainDepartment()))
				{
					
				}
				else
			    {
					b3=true;
					data1=data1+"MainDepartment=" + existingTicket.getMainDepartment();
					
					existingTicket.setMainDepartment(ticketResponseBean.getMainDepartment());
					
					data1=data1+ " To " + existingTicket.getMainDepartment();
					existingTicket.setUpdatedBy(empId);
			    	
			    	
			    }
				
				if(ticketResponseBean.getSubDepartment().equalsIgnoreCase(existingTicket.getSubDepartment()))
				{
					
					System.out.println("Entered into sub department same");
					
				}
				else
			    {
					System.out.println("Entered into sub department different");
					b4=true;
					data1=data1+" SubDepartment=" + existingTicket.getSubDepartment();
					
			    	existingTicket.setSubDepartment(ticketResponseBean.getSubDepartment());
			    	
			    	data1=data1+ " To " + existingTicket.getSubDepartment();
			    	existingTicket.setUpdatedBy(empId);
			    	
			    	
			    }
				
				if(ticketResponseBean.getSeverity().equalsIgnoreCase(existingTicket.getSeverity()))
				{
					
				}
				else
			    {
					b5=true;
					data1=data1+" Severity="+existingTicket.getSeverity();
					
			    	existingTicket.setSeverity(ticketResponseBean.getSeverity());
			    	
			    	data1=data1+ " To " + existingTicket.getSeverity();
			    	existingTicket.setUpdatedBy(empId);
			    }
				
				if(b1||b2||b3||b4||b5||b6)
				{
					
					
					System.out.println("Admin entrance");
					
					System.out.println(existingTicket.getMainDepartment());
					System.out.println(existingTicket.getSubDepartment());
					System.out.println(existingTicket.getAssignedToAdminId());
					
					
					ticketHistory.setTicketId(existingTicket);
					
					ticketHistory.setHistoryInfo(data1);
					
					ticketHistoryRepository.save(ticketHistory);

					return ticketRepository.save(existingTicket);
					
					
				}
			
		}

		
		
		
		
		

				return null;

	}

	

	
	/**
	 * This function will perform displaying of available help team members to handle a ticket,
	 * if logged in employee ROLE is EMPLOYEE
	 * @param employeeBean - It indicates currently logged in employee details
	 * @return - returns all the available help team members from ticketHelpTeam table
	 */

	public List<TicketHelpteamDTO> getAvailableEmployees(EmployeeDTO employeeBean) {
		
		//From logged in employee we require  organisation, location

		System.out.println("Entered into available employees service for help ticket ");

		

		String location = employeeBean.getLocation().getLocationName();
		
		String employeeOrganisation = employeeBean.getEmployeeOrganisation();


		List<TicketHelpTeam> empsList = ticketHelpTeamRepository
				.findByemployeeOrganisationAndTeamMemberTypeAndLocation(employeeOrganisation, "Employee", location);

		List<TicketHelpTeam> empsAdminList = ticketHelpTeamRepository
				.findByemployeeOrganisationAndTeamMemberTypeAndLocation(employeeOrganisation, "Admin", location);

		List<TicketHelpteamDTO> helpTeamList = new ArrayList<TicketHelpteamDTO>();

		

		if (empsAdminList.size() > 0) {

			TicketHelpTeam th = empsAdminList.get(0);

			TicketHelpteamDTO helpTeamBean = new TicketHelpteamDTO();

			helpTeamBean.setEmployeeId(th.getEmployeeId());

			helpTeamBean.setEmployeeName(th.getEmployeeName());

			helpTeamBean.setRole(th.getTeamMemberType());
			helpTeamBean.setEmployeeOrganisation(th.getEmployeeOrganisation());
			helpTeamList.add(helpTeamBean);

		}

		

		if (empsList.size() > 0)

		{

			System.out.println("------------> Employees AVAILABLE <--------------");
			for (TicketHelpTeam th : empsList) {
				TicketHelpteamDTO helpTeamBean = new TicketHelpteamDTO();

				if (th.getEmployeeId()==employeeBean.getEmpId()) {
				} else {
					helpTeamBean.setEmployeeId(th.getEmployeeId());

					helpTeamBean.setEmployeeName(th.getEmployeeName());

					helpTeamBean.setRole(th.getTeamMemberType());
					helpTeamBean.setEmployeeOrganisation(th.getEmployeeOrganisation());
					helpTeamList.add(helpTeamBean);
				}
			}

		}

		if (helpTeamList.size() != 0) {
			return helpTeamList;
		}

		else {
			return null;
		}

	}

	/**
	 * This function will perform displaying of available help team members from department
	 * regarding to handle a ticket, if logged in employee ROLE is ADMIN
	 * 
	 * @param employeeBean - It indicates currently logged in employee details
	 * @return - returns all the available help team members in ticketHelpTeam
	 */

	public List<TicketHelpteamDTO> getAvailableEmployeesFromDepartment(EmployeeDTO employeeBean) {

		System.out.println("Entered into available employees from department service for help ticket ");

	
        //From logged in employee we require  organisation,location,main department
		
		
		String employeeOrganisation =employeeBean.getEmployeeOrganisation();
		String location =employeeBean.getLocation().getLocationName();
		String mainDepartment = employeeBean.getMainDepartment().getMainDepartment();


		

		


		List<TicketHelpTeam> empOrgAndroleList = ticketHelpTeamRepository
				.findByemployeeOrganisationAndTeamMemberTypeAndLocation(employeeOrganisation, "Employee", location);

		List<TicketHelpTeam> mainDepartmentAndroleAndlocationList = ticketHelpTeamRepository
				.findBymainDepartmentAndTeamMemberTypeAndLocation(mainDepartment, "Admin", location);
		
		
		
		        
		List<TicketHelpteamDTO> helpTeamList = new ArrayList<TicketHelpteamDTO>();

		for (TicketHelpTeam th : empOrgAndroleList) {
			TicketHelpteamDTO helpTeamBean = new TicketHelpteamDTO();

			helpTeamBean.setEmployeeId(th.getEmployeeId());

			helpTeamBean.setEmployeeName(th.getEmployeeName());

			helpTeamBean.setRole(th.getTeamMemberType());
			
			helpTeamBean.setEmployeeOrganisation(th.getEmployeeOrganisation());
			helpTeamList.add(helpTeamBean);
		}

		for (TicketHelpTeam th : mainDepartmentAndroleAndlocationList) {
			TicketHelpteamDTO helpTeamBean = new TicketHelpteamDTO();

			if (th.getEmployeeId()==employeeBean.getEmpId()) {
			}

			else {
				helpTeamBean.setEmployeeId(th.getEmployeeId());

				helpTeamBean.setEmployeeName(th.getEmployeeName());

				helpTeamBean.setRole(th.getTeamMemberType());
				helpTeamBean.setEmployeeOrganisation(th.getEmployeeOrganisation());
				helpTeamList.add(helpTeamBean);
			}
		}

		
		
		String[] s= {"HR","SALES","FINANCE"};
		
        for(int i=0;i<s.length;i++) 
        {
        	
        	if(mainDepartment.equalsIgnoreCase(s[i]))
        	{ continue; }
        	
        	
        	
        	else
        	{
        		TicketHelpteamDTO helpTeamBean = new TicketHelpteamDTO();
        		
        		
        		TicketHelpTeam th = ticketHelpTeamRepository.findBymainDepartmentAndTeamMemberTypeAndLocationAndSubDepartment(s[i], "Admin", location,null);
        	
        		helpTeamBean.setEmployeeId(th.getEmployeeId());
        		helpTeamBean.setEmployeeOrganisation(th.getEmployeeOrganisation());
        		helpTeamBean.setEmployeeName(th.getEmployeeName());
        		helpTeamBean.setRole(th.getTeamMemberType());
        		
        		helpTeamList.add(helpTeamBean);
        		
        		
        	}
        	
        	
        	
        }
		
		
		if (helpTeamList.size() != 0) {
			return helpTeamList;
		}

		else {
			return null;
		}
	}

	


	/**
	 * This function will perform storing of a comment data into data base which is
	 * made on a ticket
	 * 
	 * @param commentBean - It contains information of a comment made on a ticket
	 * @return - returns confirmation message, once comment data stored successfully
	 *         in data base
	 */

	@Override
	public String insertComment(TicketCommentDTO commentBean,String comment) {

		System.out.println("Entered into Ticket comment Service class");

		TicketInfo ticket = ticketRepository.findById(commentBean.getTicketIdNum()).get();

		

		TicketComment ticketComment = new TicketComment();

		ticketComment.setTicketId(ticket);
		ticketComment.setEmpId(commentBean.getEmpId());

		String commentData = comment;
		System.out.println("Comment Data is: " + commentData);
		ticketComment.setComment(commentData);

		

		TicketComment ticketCommentInfo = ticketCommentRepository.save(ticketComment);

		if (ticketCommentInfo != null)
			return "success";
		else
			return "fail";

	}

	/**
	 * This function will perform displaying of comments of a particular ticket
	 * based on ticketId,empId sent
	 * 
	 * @param empId   - It indicates Id of a employee
	 * @param ticketId - It indicates Id of a ticket
	 * @return - returns list of comments made on a particular ticket
	 */
	@Override
	public List<TicketComment> displayComments(Long empId, Long ticketId) {

		TicketInfo ticket = ticketRepository.findById(ticketId).get();

		Long assignedToAdminId = ticket.getAssignedToAdminId();

		Long assignedToEmployeeId = ticket.getAssignedToEmployeeId();

		Long raisedByEmployeeId = ticket.getRaisedByEmployeeId();

		
		System.out.println("AdminId: "+assignedToAdminId);
		
		System.out.println("EmpId: "+assignedToEmployeeId);
		
		System.out.println("RaisedEmpId: "+raisedByEmployeeId);
		
		if (assignedToAdminId==empId || assignedToEmployeeId==empId
				|| raisedByEmployeeId==empId)

			return ticketCommentRepository.findByticketId(ticket);

		else
			return null;
	}

	/**
	 * This function will perform displaying of history of a particular ticket based
	 * on ticketId,empId sent
	 * 
	 * @param empId   - It indicates Id of a employee
	 * @param ticketId - It indicates Id of a ticket
	 * @return - returns history of a particular ticket
	 */
	@Override
	public List<TicketHistory> displayHistory(Long empId, Long ticketID) {
		



		System.out.println("-----Entered into Display History Service-------------");

		TicketInfo ticket = ticketRepository.findById(ticketID).get();

		Long assignedToAdminId = ticket.getAssignedToAdminId();

		Long assignedToEmployeeId = ticket.getAssignedToEmployeeId();

		Long raisedByEmployeeId = ticket.getRaisedByEmployeeId();

		

		

		if (assignedToAdminId==empId || assignedToEmployeeId==empId
				|| raisedByEmployeeId==empId)

		{
			System.out.println("Display History --------->");

			return ticketHistoryRepository.findByticketId(ticket);
		}

		else
			return null;
	}

	/**
	 * This function will perform cancel, close operations on a particular ticket
	 * based on ticketId,empId sent
	 * 
	 * @param empId   - It indicates Id of a employee
	 * @param ticketId - It indicates Id of a ticket
	 * @return - returns message whether successfully cancelled or closed a ticket.
	 */

	public String cancelOrCloseTicketService(Long ticketId, Long empId) {

		TicketInfo ticket = ticketRepository.findById(ticketId).get();

		String oldStatus = ticket.getStatus();

		long raisedEmployeeId = ticket.getRaisedByEmployeeId();

		long empId2 = ticket.getAssignedToAdminId();

		long empId3 = ticket.getAssignedToEmployeeId();

		TicketHistory history = new TicketHistory();

		if (empId==raisedEmployeeId) {
			
			ticket.setStatus("CANCELLED");
			
			
			
			
			if(ticket.getStatus().equalsIgnoreCase("CANCELLED"))
	    	{
	    		ticket.setCancelledDate(LocalDate.now());
	    		ticket.setPendingTime(null);
	    		ticketRepository.save(ticket);
	    		
	    	}
			
			
			
			history.setTicketId(ticket);

			history.setHistoryInfo("Ticket data changed : Status=" + oldStatus + " To " + ticket.getStatus());

			ticketHistoryRepository.save(history);
			
			return "success";
		}

		else if (empId==empId2 || empId==empId3) {

			ticket.setStatus("CLOSED");
			
			
			
			
			if(ticket.getStatus().equalsIgnoreCase("CLOSED"))
	    	{
				
				 ticket.setPendingTime(null);
	    		 ticket.setClosedDate(LocalDateTime.now());
	    		
	    	     LocalDateTime createdDate =ticket.getCreatedDate();
		    	
		    	 LocalDateTime closedDate = ticket.getClosedDate();
		    	
		    	 long hours = createdDate.until(closedDate, ChronoUnit.HOURS);
		    	
                 System.out.println("Time Taken Hours:--->"+hours);
                 
                 
                 String durationCalculation = durationCalculation(createdDate, closedDate);
                 
                                 
                 ticket.setTimeTakenForClosureOfTicket(durationCalculation);
		    	
		    	 
		    	 String severity = ticket.getSeverity();
		    	 
		    	 if((hours>4 && severity.equalsIgnoreCase("Critical")) || (hours>48 && severity.equalsIgnoreCase("High"))|| (hours>72 && severity.equalsIgnoreCase("Medium"))|| (hours>120 && severity.equalsIgnoreCase("Low")))
		    	 {
		    		 
		    		 ticket.setSLACompliance("Late");
		    		 
		    	 }
		    	
		    	 else if((hours==4 && severity.equalsIgnoreCase("Critical")) || (hours==48 && severity.equalsIgnoreCase("High"))|| (hours==72 && severity.equalsIgnoreCase("Medium"))|| (hours==120 && severity.equalsIgnoreCase("Low")))
		    	 {
		    		 
		    		 ticket.setSLACompliance("On-Time");
		    	 }
		    	 
		    	 else 
		    	 {
		    		 
		    		 ticket.setSLACompliance("Before-Time");
		    	 }
		    	 
		    	
		    	 
		    	 ticketRepository.save(ticket);
		    	 
		    	 
		    
	    	}

			history.setTicketId(ticket);

			history.setHistoryInfo("Ticket data changed : Status=" + oldStatus + " To " + ticket.getStatus());

			ticketHistoryRepository.save(history);

			return "success";
		}

		return null;
	}

	
	
	/*
	 * This function will give you statistics of all categories of tickets
	 * like tickets open,resolved,created in last 30,60,90 days
	 */
	@Override
	public List<Map<String, Integer>> getCountOfTicketsToDisplayInDashboard(Long empId,String role) {

		LocalDate now = LocalDate.now();
		
		LocalDate thirty = now.minusDays( 30 );
		LocalDate sixty  = now.minusDays( 60 );
		LocalDate ninety = now.minusDays( 90 );

		Map<String, Integer> allTicketCategoriesCountMapThirty = new HashMap<String, Integer>();
		Map<String, Integer> allTicketCategoriesCountMapSixty = new HashMap<String, Integer>();
		Map<String, Integer> allTicketCategoriesCountMapNinety = new HashMap<String, Integer>();
		
        //This gives  assigned tickets based on 30,60,90 days with respect to different categories for Employee
		Map<String, Integer> assignedTicketsCountMapForEmployeeThirty = new HashMap<String, Integer>();
		Map<String, Integer> assignedTicketsCountMapForEmployeeSixty = new HashMap<String, Integer>();
		Map<String, Integer> assignedTicketsCountMapForEmployeeNinety = new HashMap<String, Integer>();
		
		//This gives over all assigned tickets based on 30,60,90 days for  with respect to different categories Admin
		Map<String, Integer> assignedTicketsCountMapForAdminThirty = new HashMap<String, Integer>();
		Map<String, Integer> assignedTicketsCountMapForAdminSixty = new HashMap<String, Integer>();
		Map<String, Integer> assignedTicketsCountMapForAdminNinety = new HashMap<String, Integer>();

		//This gives over all assigned tickets for Employee, Admin
		Map<String, Integer> assignedTicketsCountMapForEmployee = new HashMap<String, Integer>();
		Map<String, Integer> assignedTicketsCountMapForAdmin = new HashMap<String, Integer>();
		
		
		//For Employee
		Integer assignedTicketsCountForEmployeeThirty =0;
		Integer assignedTicketsCountForEmployeeSixty =0;
		Integer assignedTicketsCountForEmployeeNinety =0;
		
		//For Admin
		Integer assignedTicketsCountForAdminThirty=0;
		Integer assignedTicketsCountForAdminSixty=0;
		Integer assignedTicketsCountForAdminNinety=0;
		

		if (role.equalsIgnoreCase("Employee"))
		{ 
			assignedTicketsCountForEmployeeThirty = ticketRepository.findByassignedToEmployeeAndCreatedDateAndSelectedDate(empId,
				thirty, now.minusDays(1));
			assignedTicketsCountForEmployeeSixty = ticketRepository.findByassignedToEmployeeAndCreatedDateAndSelectedDate(empId,
					sixty, now.minusDays(1));
			assignedTicketsCountForEmployeeNinety = ticketRepository.findByassignedToEmployeeAndCreatedDateAndSelectedDate(empId,
					ninety, now.minusDays(1));
		}

		if (role.equalsIgnoreCase("Admin"))
		{ 
			assignedTicketsCountForAdminThirty = ticketRepository
				.findByassignedToAdminAndCreatedDateAndSelectedDate(empId, thirty, now.minusDays(1));
		
			
			assignedTicketsCountForAdminSixty = ticketRepository
					.findByassignedToAdminAndCreatedDateAndSelectedDate(empId, sixty, now.minusDays(1));

			assignedTicketsCountForAdminNinety = ticketRepository
					.findByassignedToAdminAndCreatedDateAndSelectedDate(empId, ninety, now.minusDays(1));

		
		
		
		}
		
		
		String[] statusTypes = { "OPEN", "RESOLVED","CREATED" };

		

		

		//For Employee or Admin, we are collecting all ticket categories count
		//Raised Tickets
		for (int i = 0; i < statusTypes.length; i++)

		{
			
			if(i==0)
			{
				Integer countThirty = ticketRepository.findByraisedByEmployeeIdAndOpenDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				
				Integer countSixty = ticketRepository.findByraisedByEmployeeIdAndOpenDateAndSelectedDate(empId, sixty,
						now.minusDays(1));
				
				Integer countNinety = ticketRepository.findByraisedByEmployeeIdAndOpenDateAndSelectedDate(empId, ninety,
						now.minusDays(1));
				
				allTicketCategoriesCountMapThirty.put(statusTypes[i], countThirty);
				allTicketCategoriesCountMapSixty.put(statusTypes[i], countSixty);
				allTicketCategoriesCountMapNinety.put(statusTypes[i], countNinety);
			}

			
			else if(i==1)
			{
				
				
				Integer countThirty =ticketRepository. findByraisedByEmployeeIdAndResolvedDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				
				Integer countSixty = ticketRepository.findByraisedByEmployeeIdAndResolvedDateAndSelectedDate(empId, sixty,
						now.minusDays(1));
				Integer countNinety = ticketRepository.findByraisedByEmployeeIdAndResolvedDateAndSelectedDate(empId, ninety,
						now.minusDays(1));
				
				allTicketCategoriesCountMapThirty.put(statusTypes[i], countThirty);
				allTicketCategoriesCountMapSixty.put(statusTypes[i], countSixty);
				allTicketCategoriesCountMapNinety.put(statusTypes[i], countNinety);
			}
			else if(i==2)
			
			{
				
				
				Integer countThirty = ticketRepository.findByraisedByEmployeeIdAndCreatedDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
				Integer countSixty = ticketRepository.findByraisedByEmployeeIdAndCreatedDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				Integer countNinety = ticketRepository.findByraisedByEmployeeIdAndCreatedDateAndSelectedDate(empId, thirty,
						now.minusDays(1));

				allTicketCategoriesCountMapThirty.put(statusTypes[i], countThirty);
				allTicketCategoriesCountMapSixty.put(statusTypes[i], countSixty);
				allTicketCategoriesCountMapNinety.put(statusTypes[i], countNinety);
			
			
			}
			
			
			
			
			
			
			
		}

				
		
		
		
		String[] statusTypes2 = { "OPEN", "IN PROGRESS", "CANCELLED", "CLOSED", "RESOLVED"};
		
		
		
		//For Employee or Admin, we are collecting tickets count based on status of ticket
		//Assigned Tickets
		if (role.equalsIgnoreCase("Employee"))
		{ 
		
		    for (int i = 0; i < statusTypes2.length; i++)
           {
		    	
		    	if(i==0)
		    	{
			       Integer countThirtyDays = ticketRepository.findByassignedToEmployeeAndOpenDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
			       Integer countSixtyDays = ticketRepository.findByassignedToEmployeeAndOpenDateAndSelectedDate(empId, sixty,
							now.minusDays(1));
			       Integer countNinetyDays = ticketRepository.findByassignedToEmployeeAndOpenDateAndSelectedDate(empId, ninety,
							now.minusDays(1));

			       assignedTicketsCountMapForEmployeeThirty.put(statusTypes2[i], countThirtyDays);
			       assignedTicketsCountMapForEmployeeSixty.put(statusTypes2[i], countSixtyDays);
			       assignedTicketsCountMapForEmployeeNinety.put(statusTypes2[i], countNinetyDays);
		    	}   
		    	
		    	
		    	
		    	
		    	
		    	
		    	
		    	else if(i==1)
		    	{
			       Integer countThirtyDays = ticketRepository.findByassignedToEmployeeAndInProgressDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
			       Integer countSixtyDays = ticketRepository.findByassignedToEmployeeAndInProgressDateAndSelectedDate(empId, sixty,
							now.minusDays(1));
			       Integer countNinetyDays = ticketRepository.findByassignedToEmployeeAndInProgressDateAndSelectedDate(empId, ninety,
							now.minusDays(1));

			       assignedTicketsCountMapForEmployeeThirty.put(statusTypes2[i], countThirtyDays);
			       assignedTicketsCountMapForEmployeeSixty.put(statusTypes2[i], countSixtyDays);
			       assignedTicketsCountMapForEmployeeNinety.put(statusTypes2[i], countNinetyDays);
		    	} 
		    	
		    	else if(i==2)
		    	{
			       Integer countThirtyDays = ticketRepository.findByassignedToEmployeeAndCancelledDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
			       Integer countSixtyDays = ticketRepository.findByassignedToEmployeeAndCancelledDateAndSelectedDate(empId, sixty,
							now.minusDays(1));
			       Integer countNinetyDays = ticketRepository.findByassignedToEmployeeAndCancelledDateAndSelectedDate(empId, ninety,
							now.minusDays(1));

			       assignedTicketsCountMapForEmployeeThirty.put(statusTypes2[i], countThirtyDays);
			       assignedTicketsCountMapForEmployeeSixty.put(statusTypes2[i], countSixtyDays);
			       assignedTicketsCountMapForEmployeeNinety.put(statusTypes2[i], countNinetyDays);
		    	} 
		    	
		    	else if(i==3)
		    	{
			       Integer countThirtyDays = ticketRepository.findByassignedToEmployeeAndClosedDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
			       Integer countSixtyDays = ticketRepository.findByassignedToEmployeeAndClosedDateAndSelectedDate(empId, sixty,
							now.minusDays(1));
			       Integer countNinetyDays = ticketRepository.findByassignedToEmployeeAndClosedDateAndSelectedDate(empId, ninety,
							now.minusDays(1));

			       assignedTicketsCountMapForEmployeeThirty.put(statusTypes2[i], countThirtyDays);
			       assignedTicketsCountMapForEmployeeSixty.put(statusTypes2[i], countSixtyDays);
			       assignedTicketsCountMapForEmployeeNinety.put(statusTypes2[i], countNinetyDays);
		    	} 
		    	
		    	else if(i==4)
		    	{
			       Integer countThirtyDays = ticketRepository.findByassignedToEmployeeAndResolvedDateAndSelectedDate(empId, thirty,
					now.minusDays(1));
			       Integer countSixtyDays = ticketRepository.findByassignedToEmployeeAndResolvedDateAndSelectedDate(empId, sixty,
							now.minusDays(1));
			       Integer countNinetyDays = ticketRepository.findByassignedToEmployeeAndResolvedDateAndSelectedDate(empId, ninety,
							now.minusDays(1));

			       assignedTicketsCountMapForEmployeeThirty.put(statusTypes2[i], countThirtyDays);
			       assignedTicketsCountMapForEmployeeSixty.put(statusTypes2[i], countSixtyDays);
			       assignedTicketsCountMapForEmployeeNinety.put(statusTypes2[i], countNinetyDays);
		    	} 
		    	
		    	
		   }
		    
		    
		    

		    
		    
		}

		
		
		if (role.equalsIgnoreCase("Admin"))
		{ 
			  for (int i = 0; i < statusTypes2.length; i++)
	           {
			    	
			    	if(i==0)
			    	{
				       Integer countThirtyDays = ticketRepository.findByassignedToAdminAndOpenDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				       Integer countSixtyDays = ticketRepository.findByassignedToAdminAndOpenDateAndSelectedDate(empId, sixty,
								now.minusDays(1));
				       Integer countNinetyDays = ticketRepository.findByassignedToAdminAndOpenDateAndSelectedDate(empId, ninety,
								now.minusDays(1));

				       assignedTicketsCountMapForAdminThirty.put(statusTypes2[i], countThirtyDays);
				       assignedTicketsCountMapForAdminSixty.put(statusTypes2[i], countSixtyDays);
				       assignedTicketsCountMapForAdminNinety.put(statusTypes2[i], countNinetyDays);
			    	}   
			    	
			    	
			    	
			    	
			    	
			    	
			    	
			    	else if(i==1)
			    	{
				       Integer countThirtyDays = ticketRepository.findByassignedToAdminAndInProgressDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				       Integer countSixtyDays = ticketRepository.findByassignedToAdminAndInProgressDateAndSelectedDate(empId, sixty,
								now.minusDays(1));
				       Integer countNinetyDays = ticketRepository.findByassignedToAdminAndInProgressDateAndSelectedDate(empId, ninety,
								now.minusDays(1));

				       assignedTicketsCountMapForAdminThirty.put(statusTypes2[i], countThirtyDays);
				       assignedTicketsCountMapForAdminSixty.put(statusTypes2[i], countSixtyDays);
				       assignedTicketsCountMapForAdminNinety.put(statusTypes2[i], countNinetyDays);
			    	} 
			    	
			    	else if(i==2)
			    	{
				       Integer countThirtyDays = ticketRepository.findByassignedToAdminAndCancelledDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				       Integer countSixtyDays = ticketRepository.findByassignedToAdminAndCancelledDateAndSelectedDate(empId, sixty,
								now.minusDays(1));
				       Integer countNinetyDays = ticketRepository.findByassignedToAdminAndCancelledDateAndSelectedDate(empId, ninety,
								now.minusDays(1));

				       assignedTicketsCountMapForAdminThirty.put(statusTypes2[i], countThirtyDays);
				       assignedTicketsCountMapForAdminSixty.put(statusTypes2[i], countSixtyDays);
				       assignedTicketsCountMapForAdminNinety.put(statusTypes2[i], countNinetyDays);
			    	} 
			    	
			    	else if(i==3)
			    	{
				       Integer countThirtyDays = ticketRepository.findByassignedToAdminAndClosedDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				       Integer countSixtyDays = ticketRepository.findByassignedToAdminAndClosedDateAndSelectedDate(empId, sixty,
								now.minusDays(1));
				       Integer countNinetyDays = ticketRepository.findByassignedToAdminAndClosedDateAndSelectedDate(empId, ninety,
								now.minusDays(1));

				       assignedTicketsCountMapForAdminThirty.put(statusTypes2[i], countThirtyDays);
				       assignedTicketsCountMapForAdminSixty.put(statusTypes2[i], countSixtyDays);
				       assignedTicketsCountMapForAdminNinety.put(statusTypes2[i], countNinetyDays);
			    	} 
			    	
			    	else if(i==4)
			    	{
				       Integer countThirtyDays = ticketRepository.findByassignedToAdminAndResolvedDateAndSelectedDate(empId, thirty,
						now.minusDays(1));
				       Integer countSixtyDays = ticketRepository.findByassignedToAdminAndResolvedDateAndSelectedDate(empId, sixty,
								now.minusDays(1));
				       Integer countNinetyDays = ticketRepository.findByassignedToAdminAndResolvedDateAndSelectedDate(empId, ninety,
								now.minusDays(1));

				       assignedTicketsCountMapForAdminThirty.put(statusTypes2[i], countThirtyDays);
				       assignedTicketsCountMapForAdminSixty.put(statusTypes2[i], countSixtyDays);
				       assignedTicketsCountMapForAdminNinety.put(statusTypes2[i], countNinetyDays);
			    	} 
			    	
		  
		}
			  
		}			  
		
		
		
		
		
		
		
        List<Map<String, Integer>> ticketsCountList = new ArrayList<Map<String, Integer>>();
		
		
		
		if (role.equalsIgnoreCase("Employee"))
		{ 
			
		   assignedTicketsCountMapForEmployee.put("AssignedTicketsThirty", assignedTicketsCountForEmployeeThirty);
		   assignedTicketsCountMapForEmployee.put("AssignedTicketsSixty", assignedTicketsCountForEmployeeSixty);
		   assignedTicketsCountMapForEmployee.put("AssignedTicketsNinety", assignedTicketsCountForEmployeeNinety);
		   
		   
		   ticketsCountList.add(assignedTicketsCountMapForEmployee);
		   ticketsCountList.add(assignedTicketsCountMapForEmployeeThirty);
		   ticketsCountList.add(assignedTicketsCountMapForEmployeeSixty);
		   ticketsCountList.add(assignedTicketsCountMapForEmployeeNinety);
		   
		   
		}
		
		if (role.equalsIgnoreCase("Admin"))
		{ 
		   assignedTicketsCountMapForAdmin.put("AssignedTicketsThirty", assignedTicketsCountForAdminThirty);
		   assignedTicketsCountMapForAdmin.put("AssignedTicketsSixty", assignedTicketsCountForAdminSixty);
		   assignedTicketsCountMapForAdmin.put("AssignedTicketsNinety", assignedTicketsCountForAdminNinety);
		   ticketsCountList.add(assignedTicketsCountMapForAdmin);
		   ticketsCountList.add(assignedTicketsCountMapForAdminThirty);
		   ticketsCountList.add(assignedTicketsCountMapForAdminSixty);
		   ticketsCountList.add(assignedTicketsCountMapForAdminNinety);
		}  

		ticketsCountList.add(allTicketCategoriesCountMapThirty);
		ticketsCountList.add(allTicketCategoriesCountMapSixty);
		ticketsCountList.add(allTicketCategoriesCountMapNinety);

		return ticketsCountList;
	
	}
	
	
	
/*
 * 
 * This function will give perform deletion of document record from TicketDoc Table
 * 
 * by taking confirmation from DMS service
 *
 */
	
public boolean deletingDocument(Long ticketId, Long docId, Long empId) 
{
			
			
			
			
			
			System.out.println("Entered into Document Deletion service");
			
			
			//Here write logic to communicate with DMS service to delete document using web client.
			//After successfully deleted. write logic to dlete that docid records in TICKET DOCUMENTS Table
			
			
			
			WebClient wc = WebClient.create();
			
			Mono<Boolean> bodyToMono = wc.delete()
            .uri("http://192.168.249.45:9091/delete/file/api/{docId}",docId)
            .retrieve()
            .bodyToMono(Boolean.class)
            .doOnError(error -> {
                System.err.println("Error occurred: " + error.getMessage());
            });

            
			bodyToMono.subscribe(
		            response -> {
		                System.out.println("Delete successful: " + response);
		            },
		            error -> {
		                System.err.println("Delete failed: " + error.getMessage());
		            }
		        );
			
			System.out.println(bodyToMono);
			
			
			
			
			
			
			TicketInfo ticket = ticketRepository.findById(ticketId).get();
			
			if(empId==ticket.getAssignedToAdminId() ||  empId==ticket.getRaisedByEmployeeId() || empId==ticket.getAssignedToEmployeeId())
			{ 
				
				ticketDocumentsRepository.deleteById(docId);
				
			
			  
			    Optional<TicketDocuments> docIdOptional = ticketDocumentsRepository.findById(docId);
			
			   if(docIdOptional.isPresent())
			     return false;
			   else
				return true;
			
		     }
			
			return false;
		  }







/*
 * This function will give you total raised tickets
 */
@Override
public Map<String, Map<String, Map<String, Integer>>> getCountOfTicketsToDisplayInCharts(Long empId) {
	
	System.out.println("Entered into charts service");
	
	
	
	
	
	String[] status = {"created_date","closed_date","cancelled_date", "open_date", "resolved_date", "in_progress_date" };
	

	
	
	Map<String, Integer> countMap= new HashMap<String, Integer>();
	
	
	
	
	       Object[] quarters= new Object[6];
	       
	       quarters[0]= ChartsValues.QUARTER1;
	       quarters[1]= ChartsValues.QUARTER2;
	       quarters[2]= ChartsValues.QUARTER3;
	       quarters[3]= ChartsValues.QUARTER4;
	       
	       
	       
	       
	       
           Object[] quarter1Keys= new Object[6];
	       
           quarter1Keys[0]= ChartsValues.CREATED_KEYS_QUARTER1;
           quarter1Keys[1]= ChartsValues.CLOSED_KEYS_QUARTER1;
           quarter1Keys[2]= ChartsValues.CANCELLED_KEYS_QUARTER1;
           quarter1Keys[3]=  ChartsValues.OPEN_KEYS_QUARTER1;
           quarter1Keys[4]= ChartsValues.RESOLVED_KEYS_QUARTER1;
           quarter1Keys[5]= ChartsValues.IN_PROGRESS_KEYS_QUARTER1;
           
           
           
           
           Object[] quarter2Keys= new Object[6];
	       
           quarter2Keys[0]= ChartsValues.CREATED_KEYS_QUARTER2;
           quarter2Keys[1]= ChartsValues.CLOSED_KEYS_QUARTER2;
           quarter2Keys[2]= ChartsValues.CANCELLED_KEYS_QUARTER2;
           quarter2Keys[3]=  ChartsValues.OPEN_KEYS_QUARTER2;
           quarter2Keys[4]= ChartsValues.RESOLVED_KEYS_QUARTER2;
           quarter2Keys[5]= ChartsValues.IN_PROGRESS_KEYS_QUARTER2;
           
           
           
           
           
          Object[] quarter3Keys= new Object[6];
	       
           quarter3Keys[0]= ChartsValues.CREATED_KEYS_QUARTER3;
           quarter3Keys[1]= ChartsValues.CLOSED_KEYS_QUARTER3;
           quarter3Keys[2]= ChartsValues.CANCELLED_KEYS_QUARTER3;
           quarter3Keys[3]=  ChartsValues.OPEN_KEYS_QUARTER3;
           quarter3Keys[4]= ChartsValues.RESOLVED_KEYS_QUARTER3;
           quarter3Keys[5]= ChartsValues.IN_PROGRESS_KEYS_QUARTER3;
           
           
           Object[] quarter4Keys= new Object[6];
	       
           quarter4Keys[0]= ChartsValues.CREATED_KEYS_QUARTER4;
           quarter4Keys[1]= ChartsValues.CLOSED_KEYS_QUARTER4;
           quarter4Keys[2]= ChartsValues.CANCELLED_KEYS_QUARTER4;
           quarter4Keys[3]=  ChartsValues.OPEN_KEYS_QUARTER4;
           quarter4Keys[4]= ChartsValues.RESOLVED_KEYS_QUARTER4;
           quarter4Keys[5]= ChartsValues.IN_PROGRESS_KEYS_QUARTER4;
           
           
           
           Object[] quarterKeys= new Object[4];
           
           
           quarterKeys[0]=quarter1Keys;
           quarterKeys[1]=quarter2Keys;
           quarterKeys[2]=quarter3Keys;
           quarterKeys[3]=quarter4Keys;
           
           
           
	       
	       
	       for(int i=0;i<4;i++)
	       {
	    	   
	    	  
	    	   
	    	   String[] a=(String[]) quarters[i];
	    	   
	    	   Object[] qk= (Object[]) quarterKeys[i];
	    	   
	    	   
	           for(int s=0;s<status.length;s++)
	    	   {	   
	    	   
	    	         int k=0; int l=1;
	    	         
	    	         
	    	         
	    	         String[] b=(String[]) qk[s];
	    		   
	    		   
	    	          for(int j=0;j<3;j++)
	    	        {
	    		   
	    		   
	    	        	  
	    	        	  if(s==0)
	    	        	  {		  
	    		             Integer createdCount=  ticketRepository.findByCreatedDateData(a[k],a[l],empId);
	    		             
	    		             countMap.put(b[j], createdCount);
	    	        	  }
	    	        	  
	    	        	  
	    	        	  if(s==1)
	    	        	  {		  
	    		             Integer closedCount=  ticketRepository.findByClosedDateData(a[k],a[l],empId);
	    		             
	    		             countMap.put(b[j], closedCount);
	    	        	  }
	    	        	  else if(s==2)
	    	        	  {		  
	    		             Integer cancelledCount=  ticketRepository.findByCancelledDateData(a[k],a[l],empId);
	    		             countMap.put(b[j], cancelledCount);
	    	        	  }
	    	        	  else if(s==3)
	    	        	  {		  
	    		             Integer openCount=  ticketRepository.findByOpenDateData(a[k],a[l],empId);
	    		             countMap.put(b[j], openCount);
	    	        	  }
	    	        	  else if(s==4)
	    	        	  {		  
	    		             Integer resolvedCount=  ticketRepository.findByResolvedDateData(a[k],a[l],empId);
	    		             countMap.put(b[j], resolvedCount);
	    	        	  }
	    	        	  else if(s==5)
	    	        	  {		  
	    		             Integer inProgressCount=  ticketRepository.findByInProgressDateData(a[k],a[l],empId);
	    		             
	    		             countMap.put(b[j], inProgressCount);
	    	        	  }
	    	        	  
	    	        	 
	    		         
	    		         System.out.println(status[s]);
	    		   
	    		                   k=k+2;
	    		                   l=l+2;
	    		                   
	    		              
	    		                	   
	    		                    
	    		   
	    	          }
	    	   
	    	   }
	    	   
	    	   
	    	   
	    	   
	       }
	       

	       
	       

	        @SuppressWarnings("unchecked")
			Map.Entry<String, Integer>[] entries = countMap.entrySet()
	                .stream()
	                .toArray(Map.Entry[]::new);
	        

	        
	        Map<String, Integer> rawData= Map.ofEntries(entries);
	        
	        Map<String, Map<String, Map<String, Integer>>> structuredData = parseData(rawData);

	        return structuredData;
	  }


/*
 * This function will give you total assigned tickets
 */

@Override
public Map<String, Map<String, Map<String, Integer>>> getCountOfAssignedTicketsToDisplayInCharts(Long empId,String role) {
	
	System.out.println("Entered into charts service");
	
	
String[] status = {"closed_date","cancelled_date", "open_date", "resolved_date", "in_progress_date" };
	

	
	
	Map<String, Integer> countMap= new HashMap<String, Integer>();
	
	
	
	
	       Object[] quarters= new Object[4];
	       
	       quarters[0]= ChartsValues.QUARTER1;
	       quarters[1]= ChartsValues.QUARTER2;
	       quarters[2]= ChartsValues.QUARTER3;
	       quarters[3]= ChartsValues.QUARTER4;
	       
	       
	       
	       
	       
           Object[] quarter1Keys= new Object[5];
	       
           
           quarter1Keys[0]= ChartsValues.CLOSED_KEYS_QUARTER1;
           quarter1Keys[1]= ChartsValues.CANCELLED_KEYS_QUARTER1;
           quarter1Keys[2]= ChartsValues.OPEN_KEYS_QUARTER1;
           quarter1Keys[3]= ChartsValues.RESOLVED_KEYS_QUARTER1;
           quarter1Keys[4]= ChartsValues.IN_PROGRESS_KEYS_QUARTER1;
           
           
           
           
           Object[] quarter2Keys= new Object[5];
	       
           
           quarter2Keys[0]= ChartsValues.CLOSED_KEYS_QUARTER2;
           quarter2Keys[1]= ChartsValues.CANCELLED_KEYS_QUARTER2;
           quarter2Keys[2]=  ChartsValues.OPEN_KEYS_QUARTER2;
           quarter2Keys[3]= ChartsValues.RESOLVED_KEYS_QUARTER2;
           quarter2Keys[4]= ChartsValues.IN_PROGRESS_KEYS_QUARTER2;
           
           
           
           
           
          Object[] quarter3Keys= new Object[5];
	       
          
           quarter3Keys[0]= ChartsValues.CLOSED_KEYS_QUARTER3;
           quarter3Keys[1]= ChartsValues.CANCELLED_KEYS_QUARTER3;
           quarter3Keys[2]=  ChartsValues.OPEN_KEYS_QUARTER3;
           quarter3Keys[3]= ChartsValues.RESOLVED_KEYS_QUARTER3;
           quarter3Keys[4]= ChartsValues.IN_PROGRESS_KEYS_QUARTER3;
           
           
           Object[] quarter4Keys= new Object[5];
	       
           
           quarter4Keys[0]= ChartsValues.CLOSED_KEYS_QUARTER4;
           quarter4Keys[1]= ChartsValues.CANCELLED_KEYS_QUARTER4;
           quarter4Keys[2]=  ChartsValues.OPEN_KEYS_QUARTER4;
           quarter4Keys[3]= ChartsValues.RESOLVED_KEYS_QUARTER4;
           quarter4Keys[4]= ChartsValues.IN_PROGRESS_KEYS_QUARTER4;
           
           
           
           Object[] quarterKeys= new Object[4];
           
           
           quarterKeys[0]=quarter1Keys;
           quarterKeys[1]=quarter2Keys;
           quarterKeys[2]=quarter3Keys;
           quarterKeys[3]=quarter4Keys;
           
           
           
	       
	       
	       for(int i=0;i<4;i++)
	       {
	    	   
	    	  
	    	   
	    	   String[] a=(String[]) quarters[i];
	    	   
	    	   Object[] qk= (Object[]) quarterKeys[i];
	    	   
	    	   
	           for(int s=0;s<status.length;s++)
	    	   {	   
	    	   
	    	         int k=0; int l=1;
	    	         
	    	         
	    	         
	    	         String[] b=(String[]) qk[s];
	    		   
	    		   
	    	          for(int j=0;j<3;j++)
	    	        {
	    		   
	    		   
	    	        	  
	    	        	 
	    	        	  
	    	        	  
	    	        	  if(s==0)
	    	        	  {		
	    	        		  
	    	        		  
	    	        		  Integer closedCount=0;
	    	        		  
	    	        		  if(role.equalsIgnoreCase("Employee"))
	    	        		  
	    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  closedCount=  ticketRepository.findByClosedDateDataAndassignedToEmployee(a[k],a[l],empId);
	    	        			  
	    	        		  }	  
	    	        		  
	    	        		  
	    	        		  else if(role.equalsIgnoreCase("Admin"))
		    	        		  
		    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			   closedCount=  ticketRepository.findByClosedDateDataAndassignedToAdmin(a[k],a[l],empId);
	    	        			  
	    	        		  }
	    	        		  
	    		             
	    		             countMap.put(b[j], closedCount);
	    	        	  }
	    	        	  else if(s==1)
	    	        	  {		
	    	        		  
 		                       Integer cancelledCount=0;
	    	        		  
	    	        		  if(role.equalsIgnoreCase("Employee"))
	    	        		  
	    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  cancelledCount=  ticketRepository.findByCancelledDateDataAndassignedToEmployee(a[k],a[l],empId);
	    	        			  
	    	        		  }	  
	    	        		  
	    	        		  
	    	        		  else if(role.equalsIgnoreCase("Admin"))
		    	        		  
		    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  cancelledCount=  ticketRepository.findByCancelledDateDataAndassignedToAdmin(a[k],a[l],empId);
	    	        			  
	    	        		  }
	    	        		  
	    	        		  
	    	        		  
	    	        		  
	    	        		  
	    	        		  
	    		           
	    		             countMap.put(b[j], cancelledCount);
	    	        	  }
	    	        	  
	    	        	  
	    	        	  
	    	        	  
	    	        	  
	    	    else if(s==2)
	    	        	  {		
                              Integer openCount=0;
	    	        		  
	    	        		  if(role.equalsIgnoreCase("Employee"))
	    	        		  
	    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  openCount=  ticketRepository.findByOpenDateDataAndassignedToEmployee(a[k],a[l],empId);
	    	        			  
	    	        		  }	  
	    	        		  
	    	        		  
	    	        		  else if(role.equalsIgnoreCase("Admin"))
		    	        		  
		    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  openCount=  ticketRepository.findByOpenDateDataAndassignedToAdmin(a[k],a[l],empId);
	    	        			  
	    	        		  }
	    	        		  
	    	        		  
	    	                   countMap.put(b[j], openCount);
	    	        	  }
	    	        	  else if(s==3)
	    	        	  {	
	    	        		    Integer  resolvedCount=0;
		    	        		  
		    	        		  if(role.equalsIgnoreCase("Employee"))
		    	        		  
		    	        		  
		    	        		  { 
		    	        			  
		    	        			  
		    	        			  resolvedCount=  ticketRepository.findByResolvedDateDataAndassignedToEmployee(a[k],a[l],empId);
		    	        			  
		    	        		  }	  
		    	        		  
		    	        		  
		    	        		  else if(role.equalsIgnoreCase("Admin"))
			    	        		  
			    	        		  
		    	        		  { 
		    	        			  
		    	        			  
		    	        			  resolvedCount=  ticketRepository.findByResolvedDateDataAndassignedToAdmin(a[k],a[l],empId);
		    	        			  
		    	        		  }
	    	        		  
	    	        		  countMap.put(b[j], resolvedCount);
	    	        	  }
	    	        	  else if(s==4)
	    	        	  {		  
	    	        		  Integer  inProgressCount=0;
	    	        		  
	    	        		  if(role.equalsIgnoreCase("Employee"))
	    	        		  
	    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  inProgressCount=  ticketRepository.findByInProgressDateDataAndassignedToEmployee(a[k],a[l],empId);
	    	        			  
	    	        		  }	  
	    	        		  
	    	        		  
	    	        		  else if(role.equalsIgnoreCase("Admin"))
		    	        		  
		    	        		  
	    	        		  { 
	    	        			  
	    	        			  
	    	        			  inProgressCount=  ticketRepository.findByInProgressDateDataAndassignedToAdmin(a[k],a[l],empId);
	    	        			  
	    	        		  }
	    	        		  
	    	        		  
	    	        		 countMap.put(b[j], inProgressCount);
	    	        	  }
	    	        	  
	    	        	 
	    		         
	    		         System.out.println(status[s]);
	    		   
	    		                   k=k+2;
	    		                   l=l+2;
	    		                   
	    		              
	    		                	   
	    		                    
	    		   
	    	          }
	    	   
	    	   }
	    	   
	    	   
	    	   
	    	   
	       }
	       

	       
	       

	        @SuppressWarnings("unchecked")
			Map.Entry<String, Integer>[] entries = countMap.entrySet()
	                .stream()
	                .toArray(Map.Entry[]::new);
	        

	        // Create an immutable map using Map.ofEntries
	        Map<String, Integer> rawData= Map.ofEntries(entries);
	        
	        Map<String, Map<String, Map<String, Integer>>> structuredData = parseData(rawData);

	        return structuredData;


	


}





/**
 * 
 * This function will give you quarter wise data of different ticket categories
 * @return
 */

private static Map<String, Map<String, Map<String, Integer>>> parseData(Map<String, Integer> rawData) 
{
    
	
	
	Map<String, Map<String, Map<String, Integer>>>  finalMap = rawData.entrySet().stream()
            
			   .collect(Collectors.groupingBy(
                    
					   entry -> extractQuarter(entry.getKey()),
                    
                    
                    Collectors.groupingBy(
                            entry -> extractMonth(entry.getKey()),
                            
                            
                            Collectors.toMap(
                                    entry -> extractCategory(entry.getKey()),
                                    Map.Entry::getValue
                            )
                    )
            ));
	
	
	
	return finalMap;
	
	
	
}         

private static String extractQuarter(String key) {
    return key.split("-")[4];
}

private static String extractMonth(String key) {
    return key.split("-")[0];
}

private static String extractCategory(String key) {
    return key.split("-")[3];
}



/*
 * This function will give a result of how long it is pending by ticket handling person
 * to resolve or close a ticket
 * 
 */
public String durationCalculation(LocalDateTime  date1,LocalDateTime  date2)
  {
	

	 // Calculate the duration between the two dates
    Duration duration = Duration.between(date1, date2);

    // Extract days, hours, and minutes
    long totalMinutes = duration.toMinutes();
    long days = totalMinutes / (24 * 60);
    long hours2 = (totalMinutes % (24 * 60)) / 60;
    long minutes = totalMinutes % 60;
    
    System.out.println("Display Time Taken");
    
    return days + " days, " + hours2 + " hours, " + minutes + " minutes";                  

   }



}