package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "subDeptId"))
//@AttributeOverride(name = "description", column = @Column(name = "subDeptDesc"))
public class SubDepartment extends BaseEntity {

    private String subDeptName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mainDeptId", referencedColumnName = "mainDeptId")
//    @JsonBackReference
    private MainDepartment mainDepartment;

    @OneToMany(mappedBy = "subDepartment", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;

}
