package com.cms.employeeservice.service;

import java.util.List;

import com.cms.employeeservice.beans.ChangePasswordDataBean;
import com.cms.employeeservice.beans.ConfirmPasswordDataBean;
import com.cms.employeeservice.beans.DepartmentBean;
import com.cms.employeeservice.beans.EmployeeRegistrationBean;
import com.cms.employeeservice.beans.MailIdBean;
import com.cms.employeeservice.beans.OTPBean;
import com.cms.employeeservice.model.MainDepartment;
import com.cms.employeeservice.model.Employee;
import com.cms.employeeservice.model.Location;



public interface EmployeeService {
	
	

	boolean saveEmployee(EmployeeRegistrationBean employee);

	List<DepartmentBean> getAllDepartments();
	
	List<Employee> getAllEmployees();

	Employee getEmployeeById(long empId);
	
	Employee getEmployeeByemailId(String emailId);
	
	Employee updateEmployee(long empId,Employee employee);

	boolean deleteEmployee(long empId);
	
    boolean changePasswordData(ChangePasswordDataBean changePasswordInfo);
    
	boolean confirmPasswordData(ConfirmPasswordDataBean confirmPasswordInfo);
	
	Employee mailIdCheck(MailIdBean mail);
	
	Integer generateOTP();
	
	boolean otpCheck(OTPBean otpinfo);
	
	
    Location getEmployeeLocation(long locationId);
	
	MainDepartment getEmployeeDepartment(long departmentId);

}
