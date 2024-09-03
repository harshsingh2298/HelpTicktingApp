package com.cms.cdl.utils;

import com.cms.cdl.dto.response_dto.*;
import com.cms.cdl.model.*;
import com.cms.cdl.repo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Component
public class EmployeeCommonFunctions {
    @Autowired
    MainDepartmentRepo mainDepartmentRepo;
    @Autowired
    SubDepartmentRepo subDepartmentRepo;
    @Autowired
    DependentDetailsRepo dependentDetailsRepo;
    @Autowired
    SalaryAccDetailsRepo salaryAccDetailsRepo;
    @Autowired
    DesignationRepo designationRepo;
    @Autowired
    OrganizationRepo organizationRepo;
    @Autowired
    ProjectRepo projectRepo;

    public EmpResDTO returningEmployeeResponse(Employee employee){
        EmpResDTO empResDTO;

        // fetching data from database

        MainDepartment mainDepartment = mainDepartmentRepo.findById(employee.getMainDepartment().getId()).orElseThrow();
        SubDepartment subDepartment = subDepartmentRepo.findById(employee.getSubDepartment().getId()).orElseThrow();
        SalaryAccountDetails salaryAccountDetails = salaryAccDetailsRepo.findByEmployee_Id(employee.getId()).orElseThrow();
        List<DependentDetails> dependentDetails = dependentDetailsRepo.findByEmployee_Id(employee.getId()).orElseThrow();
        Designation designation = designationRepo.findById(employee.getDesignation().getId()).orElseThrow();
        Organization organization = organizationRepo.findById(employee.getOrganization().getId()).orElseThrow();
        Project project = projectRepo.findById(employee.getProject().getId()).orElseThrow();

        // converting to response DTO

        MainDeptResDTO mainDeptResDTO = ModelConverter.convertToMainDeptResDTO(mainDepartment);
        SubDeptResDTO subDeptResDTO = ModelConverter.convertToSubDeptResDTO(subDepartment);
        SalaryAccDetailsResDTO salaryAccDetailsResDTO = ModelConverter.convertToSalAccResDTO(salaryAccountDetails);
        List<DependentDetailsResDTO> dependentDetailsResDTOS = ModelConverter.convertToDependentDetailsResDTOList(dependentDetails);
        DesignationResDTO designationResDTO = ModelConverter.convertToDegResDTO(designation);
        OrganizationResDTO organizationResDTO = ModelConverter.convertToOrgResDTO(organization);
        ProjectResDTO projectResDTO = ModelConverter.convertToProjectResDTO(project);


        // fetching current company experience

        String currCompanyExp = calculateCurrCompanyExp(employee);

        // set all data in employee response DTO

        empResDTO = ModelConverter.convertToEmpResDTO(employee);
        empResDTO.setMainDeptResDTO(mainDeptResDTO);
        empResDTO.setSubDeptResDTO(subDeptResDTO);
        empResDTO.setSalaryAccDetailsResDTO(salaryAccDetailsResDTO);
        empResDTO.setDependentDetailsResDTOS(dependentDetailsResDTOS);
        empResDTO.setDesignationResDTO(designationResDTO);
        empResDTO.setOrganizationResDTO(organizationResDTO);
        empResDTO.setProjectResDTO(projectResDTO);
        empResDTO.setExpWithCurrentCompany(currCompanyExp);

        return empResDTO;

    }


    public String calculateCurrCompanyExp(Employee employee){
        LocalDate currentDate = LocalDate.now();

        int currCompanyYears = 0;
        int currCompanyMonths = 0;
        String currentCompanyExp = "";

        Period per = Period.between(employee.getDateOfJoining(), currentDate);
        currCompanyYears = per.getYears() + per.getMonths() / 12;
        currCompanyMonths = per.getMonths() % 12;

        currentCompanyExp = currCompanyYears + " Years " + currCompanyMonths + " Months";

        return currentCompanyExp;
    }


}
