package com.cms.cdl.service.service_impl;

import com.cms.cdl.dto.request_dto.SalaryAccDetailsReqDTO;
import com.cms.cdl.dto.response_dto.SalaryAccDetailsResDTO;
import com.cms.cdl.model.SalaryAccountDetails;
import com.cms.cdl.repo.SalaryAccDetailsRepo;
import com.cms.cdl.service.service_interface.SalaryAccDetailsService;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.util.Optional;

@Service
public class SalaryAccDetailsServiceImpl implements SalaryAccDetailsService {
    @Autowired
    SalaryAccDetailsRepo salaryAccDetailsRepo;
    @Override
    public SalaryAccDetailsResDTO getEmployeeSalaryDetails(long empId) throws AttributeNotFoundException {
        Optional<SalaryAccountDetails> salaryAccountDetails = salaryAccDetailsRepo.findByEmployee_Id(empId);

        if (salaryAccountDetails.isPresent()) {
            return ModelConverter.convertToSalAccResDTO(salaryAccountDetails.get());
        } else {
            throw new AttributeNotFoundException("Salary account details not found for employee ID: " + empId);
        }

    }

    @Override
    public SalaryAccDetailsResDTO saveEmployeeSalaryDetails(SalaryAccDetailsReqDTO salaryAccDetailsReqDTO) {

        SalaryAccountDetails salaryAccountDetails = ModelConverter.convertToSalaryAccDetails(salaryAccDetailsReqDTO);

        SalaryAccountDetails savedSalary = salaryAccDetailsRepo.save(salaryAccountDetails);

        return ModelConverter.convertToSalAccResDTO(savedSalary);

    }

    @Override
    public SalaryAccDetailsResDTO updateEmployeeSalaryDetails(long id, SalaryAccDetailsReqDTO salaryAccDetailsReqDTO) throws AttributeNotFoundException {
        Optional<SalaryAccountDetails> salaryAccountDetails = salaryAccDetailsRepo.findById(id);

        if (salaryAccountDetails.isEmpty()) {
            throw new AttributeNotFoundException("Salary account details not found for employee ID: " + id);
        }

        SalaryAccountDetails salaryDetails = salaryAccountDetails.get();

        // Update the details
        salaryDetails.setDescription(salaryAccDetailsReqDTO.getDescription());
        salaryDetails.setUpdatedBy(salaryAccDetailsReqDTO.getUpdatedBy());
        salaryDetails.setBankName(salaryAccDetailsReqDTO.getBankName());
        salaryDetails.setAccountNumber(salaryAccDetailsReqDTO.getAccountNumber());
        salaryDetails.setNameOnAccount(salaryAccDetailsReqDTO.getNameOnAccount());
        salaryDetails.setIfsc(salaryAccDetailsReqDTO.getIfsc());

        return ModelConverter.convertToSalAccResDTO(salaryAccDetailsRepo.save(salaryDetails));
    }

    @Override
    public boolean deleteEmployeeSalaryDetails(long id) throws AttributeNotFoundException {
        Optional<SalaryAccountDetails> salaryAccountDetailsOpt = salaryAccDetailsRepo.findById(id);

        if (salaryAccountDetailsOpt.isEmpty()) {
            throw new AttributeNotFoundException("Salary account details not found for ID: " + id);
        }

        salaryAccDetailsRepo.deleteById(id);

        return true;
    }

}
