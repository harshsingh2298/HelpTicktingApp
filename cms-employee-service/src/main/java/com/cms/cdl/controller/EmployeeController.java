package com.cms.cdl.controller;

import com.cms.cdl.beans.FileAndContentTypeBean;
import com.cms.cdl.dto.request_dto.EmpReqDTO;
import com.cms.cdl.service.service_interface.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class EmployeeController {

    @Autowired
    EmployeeService employeeService;

    // Add Employee

    // http://localhost:8086/employee
    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> addEmployee(@RequestParam("employeeDTO") String employeeReqDTO, @RequestPart("image")MultipartFile profileImage) throws IOException {
        EmpReqDTO empReqDTO = new ObjectMapper().readValue(employeeReqDTO, EmpReqDTO.class);
        return new ResponseEntity<>(employeeService.addEmployees(empReqDTO, profileImage), HttpStatus.OK);
    }

    // update employee

    // http://localhost:8086/employee/
    @PutMapping(value = "/{empId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> updateEmployee(@PathVariable("empId") long empId , @RequestParam("employeeDTO") String employeeReqDTO, @RequestPart("image")MultipartFile profileImage) throws IOException {
        EmpReqDTO empReqDTO = new ObjectMapper().readValue(employeeReqDTO, EmpReqDTO.class);
        return new ResponseEntity<>(employeeService.updateEmployee(empId, empReqDTO, profileImage), HttpStatus.OK);
    }

    // update employee profile image

    // http://localhost:8086/employee/update/img/
    @PutMapping(value = "/update/img/{empId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.ALL_VALUE})
    public ResponseEntity<?> updateEmployeeProfileImg(@PathVariable("empId") long empId, @RequestPart("image") MultipartFile profileImage) throws IOException {
        return new ResponseEntity<>(employeeService.updateProfileImage(empId, profileImage), HttpStatus.OK);
    }

    // delete employee

    // http://localhost:8086/employee/
    @DeleteMapping("/{empId}")
    public ResponseEntity<?> deleteEmployee(@PathVariable("empId") long empId){
        return new ResponseEntity<>(employeeService.deleteEmployee(empId), HttpStatus.OK);
    }

    // http://localhost:8086/employee/
    @GetMapping("/{empId}")
    public ResponseEntity<?> getEmployeeInfoByEmpId(@PathVariable long empId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeeByEmployeeId(empId), HttpStatus.OK);
    }

    // http://localhost:8086/employee/by/email/
    @GetMapping("/by/email/{emailId}")
    public ResponseEntity<?> getEmployeeByEmailId(@PathVariable String emailId) throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getEmployeeByEmailId(emailId), HttpStatus.OK);
    }

    // http://localhost:8086/employee/getAll
    @GetMapping("/getAll")
    public ResponseEntity<?> getAllEmployees() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getAllEmployees(), HttpStatus.OK);
    }

    // http://localhost:8086/employee/birthday/wishes/data
    @GetMapping("/birthday/wishes/data")
    public ResponseEntity<?> birthdayWishesData() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getBirthdayWishesData(), HttpStatus.OK);
    }

    // http://localhost:8086/employee/work/anniversary
    @GetMapping("/work/anniversary")
    public ResponseEntity<?> celebratingWorkAnniversary() throws ExecutionException, InterruptedException {
        return new ResponseEntity<>(employeeService.getWorkAnniversaryData(), HttpStatus.OK);
    }

    // http://localhost:8086/employee/get/myDocs/
    @GetMapping("/get/myDocs/{empId}")
    public ResponseEntity<?> getMyDocuments(@PathVariable("empId") long empId){
        System.out.println("1");
        return new ResponseEntity<>(employeeService.getMyDocuments(empId), HttpStatus.OK);
    }

    // http://localhost:8086/employee/open/docs/{docId}
    @GetMapping("/open/docs/{docId}")
    public ResponseEntity<?> openMyDocuments(@PathVariable("docId") Long docId) throws ExecutionException, InterruptedException {
        FileAndContentTypeBean file = employeeService.openAndDownloadMyDocuments(docId);
        return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf(file.getContentType()))
                .body(file.getFile());
    }


}
