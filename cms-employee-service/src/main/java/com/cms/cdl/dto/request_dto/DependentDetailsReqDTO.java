package com.cms.cdl.dto.request_dto;

import jakarta.persistence.Column;
import lombok.*;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class DependentDetailsReqDTO extends BaseEntityReqDTO{

    private String dependentName;

    private String dependentRelationship;

    private String dependentDateOfBirth;

}
