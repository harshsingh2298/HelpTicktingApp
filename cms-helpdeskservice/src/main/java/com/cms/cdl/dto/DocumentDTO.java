package com.cms.cdl.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.cms.cdl.model.TicketHistory;
import com.cms.cdl.model.TicketInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocumentDTO {

    private Long docId;
    private String itemName;
    private String docType;
    private String docPath;
    private String compressed;
    private long empId;
    private String empGroup;
    private String empOrg;
    private String docTags;
    private String location;
    private String itemType;
    private String createdByRole;
    private LocalDate createdDate;
    private String docCaptions;
    private String docDescription;
	
	
}
