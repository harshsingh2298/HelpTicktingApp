package com.cms.cdl.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.cms.cdl.dto.TicketDocumentsResponseDTO;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Tickets")
@AttributeOverride(name = "id", column = @Column(name = "ticketId"))
public class TicketInfo extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String ticketClassification;

	private String title;

	private String ticketDescription;

	private String severity;

	private String priority;

	private String remarks;

	private String mainDepartment;

	private String subDepartment;

	private String status;

	private long raisedByEmployeeId;

	private String country;

	private String state;

	private String district;

	private String city;

	private String location;
	
	private String raisedByEmployeeOrg;

	private String raisedByEmployeeRole;
	
	private String SLACompliance;
	
	private LocalDateTime SLATimeLine;

	private Long assignedToEmployeeId;

	private Long assignedToAdminId;

	@OneToMany(mappedBy = "ticketId", cascade = jakarta.persistence.CascadeType.DETACH, fetch = jakarta.persistence.FetchType.LAZY)

	private List<TicketHistory> ticketHistoryList;

	@OneToMany(mappedBy = "ticketId", cascade = jakarta.persistence.CascadeType.DETACH, fetch = jakarta.persistence.FetchType.LAZY)

	private List<TicketComment> ticketCommentsList;

	

	private LocalDateTime closedDate;
	
	private LocalDateTime resolvedDate;
	
	private LocalDate    inProgressDate;
	
	private LocalDate    openDate;
	
	private LocalDate    cancelledDate;
	
	private String timeLine;
	
	
	private String timeTakenForClosureOfTicket;
	
	@JsonInclude()
	@Transient
	private String createdDateFormat;
	
	
	@JsonInclude()
	@Transient
	private String pendingTime;
	
    @Transient
    @JsonInclude
	private String assignedToEmpIdData;
	@Transient
    @JsonInclude
	private List<TicketDocumentsResponseDTO> ticketDocumentsResponse;

}
