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
@AttributeOverride(name = "id", column = @Column(name = "gradeId"))
//@AttributeOverride(name = "description", column = @Column(name = "gradeDesc"))
public class Grade extends BaseEntity {
    private String grade;

    @OneToMany(mappedBy = "grade", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
