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
@AttributeOverride(name = "id", column = @Column(name = "orgCode"))
public class Organization extends BaseEntity{

    private String organizationName;

    private String organizationHierarchy;

    @OneToMany(mappedBy = "organization", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
