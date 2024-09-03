package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "historyId"))
public class TicketHistory extends BaseEntity {

	
	private static final long serialVersionUID = 1L;

	
	
	@JsonIgnore
	@ManyToOne(cascade = CascadeType.DETACH,fetch=jakarta.persistence.FetchType.LAZY)
	@JoinColumn(name="ticketId")
    private TicketInfo ticketId;
	
	private Long initialAssignedToEmployeeId;
	
	private String historyInfo;
	
	private String ticketTitle;
	
	private String ticketDescription;
	
	private String ticketDocID;
	
	private String mainDepartment;
	
	private String subDepartment;
	
	private String severity;
	
	private String priority;
	
	private String status;
	
	private String assignedToEmployeeId;
	

	
	@Transient
	@JsonInclude
	private long ticketIdNum;
	
}
