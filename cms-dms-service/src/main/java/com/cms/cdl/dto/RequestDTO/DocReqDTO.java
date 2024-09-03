package com.cms.cdl.dto.RequestDTO;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"docId","createdDate","lastUpdatedDate"})
public class DocReqDTO {

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
    private String docCaption;
    private String docDescription;
}
