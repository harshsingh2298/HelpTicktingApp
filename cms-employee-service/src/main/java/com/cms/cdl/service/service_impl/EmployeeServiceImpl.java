package com.cms.cdl.service.service_impl;

import com.cms.cdl.beans.EmpAndUserResponse;
import com.cms.cdl.beans.FileAndContentTypeBean;
import com.cms.cdl.beans.FileAndObjectTypeBean;
import com.cms.cdl.dto.document_dto.DocumentDTO;
import com.cms.cdl.dto.request_dto.EmpReqDTO;
import com.cms.cdl.dto.response_dto.EmpResDTO;
import com.cms.cdl.dto.user_dto.UserDTO;
import com.cms.cdl.model.*;
import com.cms.cdl.repo.*;
import com.cms.cdl.service.service_interface.EmployeeService;
import com.cms.cdl.utils.DocumentOperations;
import com.cms.cdl.utils.EmployeeCommonFunctions;
import com.cms.cdl.utils.ModelConverter;
import com.cms.cdl.utils.UserOperations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    EmployeeRepo employeeRepo;
    @Autowired
    MainDepartmentRepo mainDepartmentRepo;
    @Autowired
    SubDepartmentRepo subDepartmentRepo;
    @Autowired
    DependentDetailsRepo dependentDetailsRepo;
    @Autowired
    SalaryAccDetailsRepo salaryAccDetailsRepo;
    @Autowired
    DocumentOperations documentOperations;
    @Autowired
    EmployeeCommonFunctions employeeCommonFunctions;
    @Autowired
    ProjectRepo projectRepo;
    @Autowired
    OrganizationRepo organizationRepo;
    @Autowired
    DesignationRepo designationRepo;
    @Autowired
    BuHeadsRepo buHeadsRepo;
    @Autowired
    CategoryRepo categoryRepo;
    @Autowired
    ClassificationRepo classificationRepo;
    @Autowired
    EESubGroupRepo eeSubGroupRepo;
    @Autowired
    EmploymentStatusRepo employmentStatusRepo;
    @Autowired
    GenerationTypeRepo generationTypeRepo;
    @Autowired
    GradeRepo gradeRepo;
    @Autowired
    PayrollAreaTypeRepo payrollAreaTypeRepo;
    @Autowired
    UserOperations userOperations;
    @Value("${myDocuments.API}")
    private String myDocsAPI;


    @Override
    public EmpResDTO addEmployees(EmpReqDTO empReqDTO, MultipartFile empProfileImg) throws IOException {

        Employee employee = ModelConverter.convertToEmployee(empReqDTO);

        // Fetch and set related entities
        mainDepartmentRepo.findById(empReqDTO.getMainDeptId()).ifPresent(employee::setMainDepartment);
        subDepartmentRepo.findById(empReqDTO.getSubDeptId()).ifPresent(employee::setSubDepartment);
        projectRepo.findById(empReqDTO.getProjectId()).ifPresent(employee::setProject);
        organizationRepo.findById(empReqDTO.getOrgCode()).ifPresent(employee::setOrganization);
        designationRepo.findById(empReqDTO.getDesignationId()).ifPresent(employee::setDesignation);
        buHeadsRepo.findById(empReqDTO.getBuHeadId()).ifPresent(employee::setBuHeads);
        categoryRepo.findById(empReqDTO.getCategoryId()).ifPresent(employee::setCategory);
        classificationRepo.findById(empReqDTO.getClassificationId()).ifPresent(employee::setClassification);
        eeSubGroupRepo.findById(empReqDTO.getEeSubgroupId()).ifPresent(employee::setEeSubGroup);
        employmentStatusRepo.findById(empReqDTO.getEmploymentStatusId()).ifPresent(employee::setEmploymentStatus);
        generationTypeRepo.findById(empReqDTO.getGenerationTypeId()).ifPresent(employee::setGenerationType);
        gradeRepo.findById(empReqDTO.getGradeId()).ifPresent(employee::setGrade);
        payrollAreaTypeRepo.findById(empReqDTO.getPayrollAreaTypeId()).ifPresent(employee::setPayrollAreaType);


        // save employee in database without documentId
        Employee savedEmployee = employeeRepo.save(employee);

        DocumentDTO documentData = new DocumentDTO();
        documentData.setEmpId(savedEmployee.getId());
        documentData.setEmpOrg(savedEmployee.getOrganization().getOrganizationHierarchy());
        documentData.setLocation("INDIA.KARNATAKA.BENGALURU.VASANTH NAGAR");

        List<DocumentDTO> documentDTO = documentOperations.uploadDocument(documentData, empProfileImg);

        savedEmployee.setProfileImgDocId(documentDTO.get(0).getDocId());


        // save employee in database with documentId
        Employee updatedEmployee = employeeRepo.save(savedEmployee);

        return employeeCommonFunctions.returningEmployeeResponse(updatedEmployee);
    }

    @Override
    public EmpResDTO updateEmployee(long empId, EmpReqDTO empReqDTO, MultipartFile empProfileImg) throws IOException {

        // fetch the existing employee data by using employee ID
        Optional<Employee> existingEmpOpt = employeeRepo.findById(empId);


        // convert the employee request DTO to model employee object
        Employee updatedEmp = ModelConverter.convertToEmployee(empReqDTO);


        // Fetch and set related entities
        mainDepartmentRepo.findById(empReqDTO.getMainDeptId()).ifPresent(updatedEmp::setMainDepartment);
        subDepartmentRepo.findById(empReqDTO.getSubDeptId()).ifPresent(updatedEmp::setSubDepartment);
        projectRepo.findById(empReqDTO.getProjectId()).ifPresent(updatedEmp::setProject);
        organizationRepo.findById(empReqDTO.getOrgCode()).ifPresent(updatedEmp::setOrganization);
        designationRepo.findById(empReqDTO.getDesignationId()).ifPresent(updatedEmp::setDesignation);
        buHeadsRepo.findById(empReqDTO.getBuHeadId()).ifPresent(updatedEmp::setBuHeads);
        categoryRepo.findById(empReqDTO.getCategoryId()).ifPresent(updatedEmp::setCategory);
        classificationRepo.findById(empReqDTO.getClassificationId()).ifPresent(updatedEmp::setClassification);
        eeSubGroupRepo.findById(empReqDTO.getEeSubgroupId()).ifPresent(updatedEmp::setEeSubGroup);
        employmentStatusRepo.findById(empReqDTO.getEmploymentStatusId()).ifPresent(updatedEmp::setEmploymentStatus);
        generationTypeRepo.findById(empReqDTO.getGenerationTypeId()).ifPresent(updatedEmp::setGenerationType);
        gradeRepo.findById(empReqDTO.getGradeId()).ifPresent(updatedEmp::setGrade);
        payrollAreaTypeRepo.findById(empReqDTO.getPayrollAreaTypeId()).ifPresent(updatedEmp::setPayrollAreaType);


        if (existingEmpOpt.isPresent()) {
            Employee existingEmpData = existingEmpOpt.get();

            updatedEmp.setId(existingEmpData.getId());

            // set the document data
            DocumentDTO documentData = new DocumentDTO();
            documentData.setEmpId(updatedEmp.getId());
            documentData.setEmpOrg(updatedEmp.getOrganization().getOrganizationHierarchy());
            documentData.setLocation("INDIA.KARNATAKA.BENGALURU.VASANTH NAGAR");

            DocumentDTO documentDTO = documentOperations.updateDocument(documentData, existingEmpData.getProfileImgDocId(), empProfileImg,  "Employee");

            updatedEmp.setProfileImgDocId(documentDTO.getDocId());

            // Save the updated employee in the database
            Employee savedUpdatedEmployee = employeeRepo.save(updatedEmp);

            return employeeCommonFunctions.returningEmployeeResponse(savedUpdatedEmployee);
        } else {
            return null;
        }
    }

    @Override
    public boolean updateProfileImage(long empId, MultipartFile empProfileImg) throws IOException {
        Optional<Employee> optionalEmp = employeeRepo.findById(empId);

        if(optionalEmp.isPresent()){
            Employee employee = optionalEmp.get();

            // fetch the user details
            UserDTO userDTO = userOperations.getUserByUserId(employee.getUserId());

            // get the location
            String country = userDTO.getLocationDTO().getCountry();
            String state = userDTO.getLocationDTO().getState();
            String city = userDTO.getLocationDTO().getCity();
            String locationName = userDTO.getLocationDTO().getLocationName();

            String location = country+"."+state+"."+city+"."+locationName;

            // set the document data
            DocumentDTO docData = new DocumentDTO();
            docData.setEmpId(empId);
            docData.setEmpOrg(employee.getOrganization().getOrganizationHierarchy());
            docData.setLocation(location);

            // fetch the existing profile image docID if available
            Long docId = employee.getProfileImgDocId();

            // create the document object for creating or updating the profile image and receive data in that object
            DocumentDTO documentDTO = new DocumentDTO();

            if(docId != null){
                System.out.println("1");
                documentDTO = documentOperations.updateDocument(docData, docId, empProfileImg,  "Employee");
            }
            else{
                System.out.println("2");
                documentDTO = documentOperations.uploadDocument(docData, empProfileImg).get(0);
            }

            // set the profile image docId in the employee
            employee.setProfileImgDocId(documentDTO.getDocId());

            // save the docId in the employee table
            employeeRepo.save(employee);

            return true;
        }
        else {
            return false;
        }

    }


    @Override
    public boolean deleteEmployee(long empId) {
        Optional<Employee> employee = employeeRepo.findById(empId);

        // delete Document from DMS and employee
        if(employee.isPresent()){
            Employee emp = employee.get();

            if (emp.getProfileImgDocId() != null) {
                boolean docDeleted = documentOperations.deleteDocument(emp.getProfileImgDocId());
                if (!docDeleted) {
                    return false;
                }
            }

            // Delete the employee
            employeeRepo.deleteById(empId);
            return true;
        }

        // if employee not found
        return false;

    }

    @Override
    public EmpAndUserResponse getEmployeeByEmployeeId(long empId) throws ExecutionException, InterruptedException {
        Optional<Employee> employee = employeeRepo.findById(empId);

        EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
        FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        EmpResDTO empResDTO;
        UserDTO userDTO;

        if (employee.isPresent()) {
            Employee emp = employee.get();
            System.out.println(emp.getId());

            empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);

            Long profileImgDocId = emp.getProfileImgDocId();

            if(profileImgDocId != null){
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(emp.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }

            // fetch the userDTO
            userDTO = userOperations.getUserByUserId(emp.getUserId());


            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            empAndUserResponse.setUserDTO(userDTO);


            return empAndUserResponse;
        } else {
            return null;
        }
    }

    @Override
    public EmpAndUserResponse getEmployeeByEmailId(String emailId) throws ExecutionException, InterruptedException {
        Optional<Employee> employee = employeeRepo.findByEmailId(emailId);

        EmpAndUserResponse empAndUserResponse = new EmpAndUserResponse();
        FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        EmpResDTO empResDTO;
        UserDTO userDTO;

        if(employee.isPresent()){
            Employee emp = employee.get();

            empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);

            Long profileImgDocId = emp.getProfileImgDocId();

            if(profileImgDocId != null){
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(emp.getProfileImgDocId());
                fileAndContentTypeBean = futureResult.get();
            }

            // fetch the userDTO
            userDTO = userOperations.getUserByUserId(emp.getUserId());

            // set the values of file and emp response
            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            empAndUserResponse.setFileAndObjectTypeBean(fileAndObjectTypeBean);
            empAndUserResponse.setUserDTO(userDTO);

            return empAndUserResponse;

        }
        else{
            return null;
        }
    }

    @Override
    public List<FileAndObjectTypeBean> getAllEmployees() throws ExecutionException, InterruptedException {
        List<Employee> employeeList = employeeRepo.findAll();

        EmpResDTO empResDTO;

        List<FileAndObjectTypeBean> allEmployees = new ArrayList<>();
        FileAndContentTypeBean fileAndContentTypeBean = null;


        for(Employee employee : employeeList){

            empResDTO = employeeCommonFunctions.returningEmployeeResponse(employee);

            Long profileImgDocId = employee.getProfileImgDocId();

            if (profileImgDocId != null) {
                CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                fileAndContentTypeBean = futureResult.get();
            }

            FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

            fileAndObjectTypeBean.setEmpResDTO(empResDTO);
            fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

            allEmployees.add(fileAndObjectTypeBean);

        }

        return allEmployees;
    }


    @Override
    public List<FileAndObjectTypeBean> getBirthdayWishesData() throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepo.findBirthdays();

        List<FileAndObjectTypeBean> birthdayData = new ArrayList<>();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        EmpResDTO empResDTO;

        if(employees!=null){
            for(Employee emp : employees){

                empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);

                Long profileImgDocId = emp.getProfileImgDocId();

                if (profileImgDocId != null) {
                    CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                    fileAndContentTypeBean = futureResult.get();
                }
                FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

                fileAndObjectTypeBean.setEmpResDTO(empResDTO);
                fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

                birthdayData.add(fileAndObjectTypeBean);
            }

            return birthdayData;
        }
        else {
            return null;
        }
    }

    @Override
    public List<FileAndObjectTypeBean> getWorkAnniversaryData() throws ExecutionException, InterruptedException {
        List<Employee> employees = employeeRepo.findWorkAnniversary();

        List<FileAndObjectTypeBean> workAnniversary = new ArrayList<>();
        FileAndContentTypeBean fileAndContentTypeBean = null;

        LocalDate currentDate = LocalDate.now();

        EmpResDTO empResDTO;

        if(employees!=null){
            for(Employee emp : employees){

                int yearsCompleted = Period.between(emp.getDateOfJoining(), currentDate).getYears();

                empResDTO = employeeCommonFunctions.returningEmployeeResponse(emp);
                empResDTO.setYearsCompleted(yearsCompleted);

                Long profileImgDocId = emp.getProfileImgDocId();

                if (profileImgDocId != null) {
                    CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(profileImgDocId);
                    fileAndContentTypeBean = futureResult.get();
                }

                FileAndObjectTypeBean fileAndObjectTypeBean = new FileAndObjectTypeBean();

                fileAndObjectTypeBean.setEmpResDTO(empResDTO);
                fileAndObjectTypeBean.setFileAndContentTypeBean(fileAndContentTypeBean);

                workAnniversary.add(fileAndObjectTypeBean);
            }
            return workAnniversary;
        }
        return null;
    }

    @Override
    public List<DocumentDTO> getMyDocuments(long empId) {
        return documentOperations.getMyDocuments(empId);
    }

    @Override
    public FileAndContentTypeBean openAndDownloadMyDocuments(long docId) throws ExecutionException, InterruptedException {
        CompletableFuture<FileAndContentTypeBean> futureResult = documentOperations.openAndDownloadDocument(docId);

        return futureResult.get();
    }

    @Override
    public List<FileAndObjectTypeBean> getEmployeesLocationBased(long empId) {
        return null;
    }
}
