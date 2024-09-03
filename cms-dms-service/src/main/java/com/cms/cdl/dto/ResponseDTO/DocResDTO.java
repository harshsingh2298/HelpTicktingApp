package com.cms.cdl.dto.ResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DocResDTO {
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
