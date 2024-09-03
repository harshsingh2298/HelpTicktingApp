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
@AttributeOverride(name = "id" , column = @Column(name = "eeSubgroupId"))
public class EESubGroup extends BaseEntity {
    private String eeSubGroupName;

    @OneToMany(mappedBy = "eeSubGroup", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
