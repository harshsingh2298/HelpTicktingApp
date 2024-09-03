package com.cms.cdl.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Entity
@AttributeOverride(name = "id", column = @Column(name = "salaryAccDetailsId"))
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
//@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SalaryAccountDetails extends BaseEntity {

    private String bankName;

    private long accountNumber;

    private String nameOnAccount;

    private String ifsc;

    @OneToOne(mappedBy = "salaryAccountDetails", fetch = FetchType.LAZY)
//    @JsonIgnore
    private Employee employee;

}
