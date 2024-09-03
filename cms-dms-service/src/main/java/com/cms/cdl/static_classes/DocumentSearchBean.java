package com.cms.cdl.static_classes;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.LocalDate;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DocumentSearchBean {

    private LocalDate fromDate ;
    private LocalDate toDate ;
    private String andOrOR;
    private  String[] keyValues;

}
