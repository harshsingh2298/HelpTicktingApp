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
@AttributeOverride(name = "id", column = @Column(name = "employmentStatusId"))
public class EmploymentStatus extends BaseEntity {
    private String employmentStatus;

    @OneToMany(mappedBy = "employmentStatus", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
