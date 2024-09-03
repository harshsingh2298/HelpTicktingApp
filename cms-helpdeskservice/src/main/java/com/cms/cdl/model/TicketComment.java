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
@AttributeOverride(name = "id", column = @Column(name = "commentId"))
public class TicketComment extends BaseEntity {

	private static final long serialVersionUID = 1L;

	

	@JsonIgnore
	@ManyToOne(cascade = CascadeType.DETACH, fetch = jakarta.persistence.FetchType.LAZY)
	@JoinColumn(name = "ticketId")
	private TicketInfo ticketId;

	
	private Long empId;

	private String comment;

	@Transient
	@JsonInclude
	private long ticketIdNum;

	

}
