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
@AttributeOverride(name = "id", column = @Column(name = "designationId"))
public class Designation extends BaseEntity{

    private String designationName;

    @OneToMany(mappedBy = "designation", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;

}
