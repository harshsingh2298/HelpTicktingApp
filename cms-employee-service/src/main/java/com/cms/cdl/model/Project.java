package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "projectId"))
public class Project extends BaseEntity {

    private String projectName;
    private String projectShortName;
    private String projectCode;
    private String wbsName;

    @OneToMany(mappedBy = "project", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;

}
