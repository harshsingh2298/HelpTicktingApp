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
@AttributeOverride(name = "id", column = @Column(name = "genTypeId"))
public class GenerationType extends BaseEntity {
    private String generationTypeName;
    private String bornYear;

    @OneToMany(mappedBy = "generationType", fetch = FetchType.LAZY)
//    @JsonIgnore
    private List<Employee> employees;
}
