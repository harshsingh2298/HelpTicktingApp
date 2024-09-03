package com.cms.employeeservice.controller;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cms.employeeservice.beans.ChangePasswordDataBean;
import com.cms.employeeservice.beans.ConfirmPasswordDataBean;
import com.cms.employeeservice.beans.DepartmentBean;
import com.cms.employeeservice.beans.EmployeeBean;
import com.cms.employeeservice.beans.EmployeeRegistrationBean;
import com.cms.employeeservice.beans.LocationBean;
import com.cms.employeeservice.beans.LoginRequestBean;
import com.cms.employeeservice.beans.MailIdBean;
import com.cms.employeeservice.beans.MainDepartmentBean;
import com.cms.employeeservice.beans.OTPBean;
import com.cms.employeeservice.model.MainDepartment;
import com.cms.employeeservice.model.Employee;
import com.cms.employeeservice.model.Location;
import com.cms.employeeservice.service.EmployeeService;


@RestController
@CrossOrigin()
@RequestMapping("/employee")
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

	@Autowired
	private AuthenticationManager authenticationManager;
	


	private Employee employee;

	/**
	 * This function will collect user credentials when user logged into Application
	 * and checks valid user or not
	 * 
	 * @param credentials - It represents credentials like user name and password
	 *                    entered by user
	 * @return - returns confirmation message, whether login success or not
	 */
	@PostMapping(value = "/signin")

	public ResponseEntity<?> loginRequest(@RequestBody LoginRequestBean credentials) {

		System.out.println("Entered into  login controller");

		System.out.println(credentials.getEmailId());

		employee = employeeService.getEmployeeByemailId(credentials.getEmailId());

		Authentication authenticate = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(credentials.getEmailId(), credentials.getPassword()));

		if (authenticate.isAuthenticated() && credentials.getRoles().equalsIgnoreCase(employee.getRoles())) {

			System.out.println("successfully login operation authenticated");

			System.out.println(employee.getEmpFirstName());
			
Location location = employeeService.getEmployeeLocation(employee.getLocationId().getLocationId());
			
			//MainDepartment department = employeeService.getEmployeeDepartment(employee.getMaindepartmentId().getMainDepartmentId());
			
          LocationBean     locationBean=new LocationBean();

          BeanUtils.copyProperties(location, locationBean);
			EmployeeBean employeeBean = new EmployeeBean();
			
			
			employeeBean.setEmpId(employee.getEmpId());
			employeeBean.setRoles(employee.getRoles());
			
			employeeBean.setUserId(employee.getUserId());
			
			employeeBean.setEmpFirstName(employee.getEmpFirstName());

			employeeBean.setEmpLastName(employee.getEmpLastName());

			employeeBean.setEmailId(employee.getEmailId());    
			employeeBean.setContactPrimary(employee.getContactPrimary());
			employeeBean.setContactSecondary(employee.getContactSecondary());  
			
			employeeBean.setLocation(locationBean);
			employeeBean.setEmployeeOrganisation(employee.getEmployeeOrganisation());
		
			
			MainDepartment mainDepartment = employee.getMainDepartmentId();
			
			MainDepartmentBean mainDepartmentBean = new MainDepartmentBean();
			
			BeanUtils.copyProperties(mainDepartment, mainDepartmentBean);
			
			
			employeeBean.setMainDepartment(mainDepartmentBean);
			
			

			return new ResponseEntity<EmployeeBean>(employeeBean, HttpStatus.OK);

		}

		else

		{
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}

		
		
	}

	
	@GetMapping("/loggedInEmployee")
	public ResponseEntity<?> getEmployee()
	{
		if (employee == null)

		{
			return new ResponseEntity<String>("No employee found", HttpStatus.BAD_REQUEST);
		}

		else {
			
			Location location = employeeService.getEmployeeLocation(employee.getLocationId().getLocationId());
			
			//MainDepartment department = employeeService.getEmployeeDepartment(employee.getMaindepartmentId().getMainDepartmentId());
			
			EmployeeBean employeeBean = new EmployeeBean();
			
			
			employeeBean.setEmpId(employee.getEmpId());
			
			employeeBean.setUserId(employee.getUserId());
			
			employeeBean.setEmpFirstName(employee.getEmpFirstName());

			employeeBean.setEmpLastName(employee.getEmpLastName());

			employeeBean.setEmailId(employee.getEmailId());    
			employeeBean.setContactPrimary(employee.getContactPrimary());
			employeeBean.setContactSecondary(employee.getContactSecondary());  
			
		
			employeeBean.setEmployeeOrganisation(employee.getEmployeeOrganisation());

			

	      return new ResponseEntity<EmployeeBean>(employeeBean, HttpStatus.OK);
		}
			
		
		
	}

	@PostMapping(value = "/changePassword")
	public ResponseEntity<?> changePasswordRequest(@RequestBody ChangePasswordDataBean changePasswordInfo) {

		boolean changePwddata = employeeService.changePasswordData(changePasswordInfo);

		if (changePwddata) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		else {
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping(value = "/confirmPassword")
	public ResponseEntity<?> confirmPasswordRequest(@RequestBody ConfirmPasswordDataBean confirmPasswordInfo) {
		System.out.println("confirm password data :" + confirmPasswordInfo.getConfirmPassword());

		boolean confirmPasswordData = employeeService.confirmPasswordData(confirmPasswordInfo);

		System.out.println(confirmPasswordData);

		if (confirmPasswordData) {
			return new ResponseEntity<String>("success", HttpStatus.OK);
		}

		else {
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}

	}

	@PostMapping(value = "/validateMailId")
	public ResponseEntity<?> mailIdValidationRequest(@RequestBody MailIdBean mail) {

		System.out.println("Entered into mailid validation in controller: " + mail.getMailId());

		Employee employee = employeeService.mailIdCheck(mail);

		if (employee == null)

		{
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
		}

		else {
			String emailId = employee.getEmailId();

			return new ResponseEntity<String>(emailId, HttpStatus.OK);
		}

	}

	@PostMapping(value = "/validateOTP")
	public ResponseEntity<?> otpValidationRequest(@RequestBody OTPBean otpinfo) {

		boolean otpCheck = employeeService.otpCheck(otpinfo);

		if (otpCheck)
			return new ResponseEntity<String>("success", HttpStatus.OK);
		else
			return new ResponseEntity<String>("failure", HttpStatus.BAD_REQUEST);
	}
	
	@GetMapping("/all-departments")
	public ResponseEntity<?> getAllDepartmentsData() {

		 List<DepartmentBean> departmentsList = employeeService.getAllDepartments();
		return new ResponseEntity<List<DepartmentBean>>( departmentsList, HttpStatus.OK);
	}
	
	

	@PostMapping("/saveEmployee")
	public ResponseEntity<?> saveEmployeeData(@RequestBody EmployeeRegistrationBean employeeBean) {

		boolean employeeRegistrationStatus = employeeService.saveEmployee(employeeBean);

		if (employeeRegistrationStatus)

			return new ResponseEntity<String>("Successfully Registered", HttpStatus.OK);

		else
			return new ResponseEntity<String>("Registration Failed", HttpStatus.BAD_REQUEST);

	}

	@GetMapping("/allEmployees")
	public ResponseEntity<?> getAllEmployeesData() {

		List<Employee> empList = employeeService.getAllEmployees();
		return new ResponseEntity<List<Employee>>(empList, HttpStatus.OK);
	}

	@GetMapping("/getEmployee/{empId}")
	public ResponseEntity<?> getEmployeeDataById(@PathVariable("empId") long empId) {

		Employee emp = employeeService.getEmployeeById(empId);
		
		EmployeeBean employeeBean = new EmployeeBean();
		
		
		employeeBean.setEmpId(emp.getEmpId());
		
		employeeBean.setEmpFirstName(emp.getEmpFirstName());

		employeeBean.setEmpLastName(emp.getEmpLastName());

		employeeBean.setEmployeeOrganisation(emp.getEmployeeOrganisation());
		
		employeeBean.setRoles(emp.getRoles());

		
		return new ResponseEntity<EmployeeBean>(employeeBean, HttpStatus.OK);
	}

	@PutMapping("/updateEmployee/{empId}")
	public ResponseEntity<?> updateEmployeeData(@PathVariable("empId") long empId, @RequestBody Employee employee) {

		Employee emp = employeeService.updateEmployee(empId, employee);
		return new ResponseEntity<Employee>(emp, HttpStatus.OK);
	}

	@DeleteMapping("/deleteEmployee/{empId}")
	public ResponseEntity<?> deleteEmployeeData(@PathVariable("empId") long empId) {

		boolean result = employeeService.deleteEmployee(empId);
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}

}
