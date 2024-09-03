package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@AttributeOverride(name = "id", column = @Column(name = "payrollTypeId"))
public class PayrollAreaType extends BaseEntity {
    private String payrollType;
    private String payrollName;

    @OneToMany(mappedBy = "payrollAreaType", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
