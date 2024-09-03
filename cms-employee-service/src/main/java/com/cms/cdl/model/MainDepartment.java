package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@AttributeOverride(name = "id", column = @Column(name = "mainDeptId"))
//@AttributeOverride(name = "description", column = @Column(name = "mainDeptDesc"))
public class MainDepartment extends BaseEntity {

    private String mainDeptName;

    @OneToMany(mappedBy = "mainDepartment", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;

    @OneToMany(mappedBy = "mainDepartment", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<SubDepartment> subDepartmentsList;

}
