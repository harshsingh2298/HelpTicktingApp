package com.cms.cdl.service.service_impl;

import com.cms.cdl.dto.request_dto.DependentDetailsReqDTO;
import com.cms.cdl.dto.response_dto.DependentDetailsResDTO;
import com.cms.cdl.model.DependentDetails;
import com.cms.cdl.repo.DependentDetailsRepo;
import com.cms.cdl.service.service_interface.DependentDetailsService;
import com.cms.cdl.utils.ModelConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.AttributeNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class DependentDetailsServiceImpl implements DependentDetailsService {
    @Autowired
    DependentDetailsRepo dependentDetailsRepo;


    @Override
    public List<DependentDetailsResDTO> getDependentDetails(long empId) throws AttributeNotFoundException {
        Optional<List<DependentDetails>> dependentDetails = dependentDetailsRepo.findByEmployee_Id(empId);

        if(dependentDetails.isEmpty()){
            throw new AttributeNotFoundException("Salary account details not found for employee ID : " + empId);
        }
        return ModelConverter.convertToDependentDetailsResDTOList(dependentDetails.get());
    }

    @Override
    public DependentDetailsResDTO updateDependentDetails(long id, DependentDetailsReqDTO dependentDetailsReqDTO) throws AttributeNotFoundException {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate dob = LocalDate.parse(dependentDetailsReqDTO.getDependentDateOfBirth(), formatter);

        Optional<DependentDetails> optionalDependentDetails = dependentDetailsRepo.findById(id);

        if (optionalDependentDetails.isEmpty()) {
            throw new AttributeNotFoundException("Dependent details not found for ID: " + id);
        }

        DependentDetails dependentDetails = optionalDependentDetails.get();

        dependentDetails.setDescription(dependentDetailsReqDTO.getDescription());
        dependentDetails.setUpdatedBy(dependentDetailsReqDTO.getUpdatedBy());
        dependentDetails.setDependentName(dependentDetailsReqDTO.getDependentName());
        dependentDetails.setDependentRelationship(dependentDetailsReqDTO.getDependentRelationship());
        dependentDetails.setDependentDateOfBirth(dob);

        DependentDetails updatedDependentDetails = dependentDetailsRepo.save(dependentDetails);

        return ModelConverter.convertToDependentDetailsResDTO(updatedDependentDetails);
    }

    @Override
    public boolean deleteDependentDetails(long id) throws AttributeNotFoundException {
        Optional<DependentDetails> dependentDetailsOpt = dependentDetailsRepo.findById(id);

        if (dependentDetailsOpt.isEmpty()) {
            throw new AttributeNotFoundException("Salary account details not found for ID: " + id);
        }

        dependentDetailsRepo.deleteById(id);

        return true;
    }
}
