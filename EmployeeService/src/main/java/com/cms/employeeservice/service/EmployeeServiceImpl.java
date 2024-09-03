package com.cms.employeeservice.service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.cms.employeeservice.beans.ChangePasswordDataBean;
import com.cms.employeeservice.beans.ConfirmPasswordDataBean;
import com.cms.employeeservice.beans.DepartmentBean;
import com.cms.employeeservice.beans.EmployeeRegistrationBean;
import com.cms.employeeservice.beans.MailIdBean;
import com.cms.employeeservice.beans.OTPBean;
import com.cms.employeeservice.model.Employee;
import com.cms.employeeservice.model.Location;
import com.cms.employeeservice.model.MainDepartment;
import com.cms.employeeservice.repository.EmployeeRepository;
import com.cms.employeeservice.repository.LocationRepository;
import com.cms.employeeservice.repository.MainDepartmentRepository;
import com.cms.employeeservice.repository.SubDepartmentRepository;
import com.cms.employeeservice.utils.EmailUtils;


@Service
public class EmployeeServiceImpl implements EmployeeService, UserDetailsService {

	@Autowired
	private EmployeeRepository employeeRepo;
	
	@Autowired
	private MainDepartmentRepository mainDepartmentRepo;
	
	@Autowired
	private SubDepartmentRepository subDepartmentRepo;
	
	@Autowired
	private LocationRepository locationRepo;
	

	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private EmailUtils emailUtils;

	private Integer otp;

	private Employee employee;

	

	public List<DepartmentBean> getAllDepartments()
	{
		
		List<DepartmentBean> departmentsBeansList=new ArrayList<DepartmentBean>();
				
		//departmentsList = departmentRepo.findBymainDepartmentAndsubDepartment();
		
		
		 List<MainDepartment> mainDepartmentsList = mainDepartmentRepo.getAllMainDepartments();
		
		
		 System.out.println(mainDepartmentsList.size());
		 
		 
		 for( MainDepartment mainDeptmnt: mainDepartmentsList)
			 
		 {
			 
			 
			 
			 DepartmentBean departmentBean = new DepartmentBean();
			 
			 departmentBean.setMainDepartment( mainDeptmnt.getMainDepartment());
			 
			 List<String> subDepartmentsList = subDepartmentRepo.getAllSubDepartments( mainDeptmnt.getMainDepartmentId());
			
			 String subDept="";
			 
			 System.out.println(subDepartmentsList);
			 
			 for(String subdeptmnt:subDepartmentsList)
			 {
				 
				
				 subDept+=subdeptmnt+",";
				
			     
			 }
			 
			 
			 
			 departmentBean.setSubDepartment(subDept.substring(0, subDept.length()-1));
			 
			 
			 departmentsBeansList.add(departmentBean);
			 
			 
			 
		 }
	return departmentsBeansList;
	}

	
	
	
	@Override
	public UserDetails loadUserByUsername(String mailId) throws UsernameNotFoundException {

		System.out.println("Entered into Load user by mailId: " + mailId);

		Optional<Employee> empOptional = employeeRepo.findByemailId(mailId);

		Employee employee = empOptional.get();
		System.out.println("hiiiiiii");

		System.out.println("------------------------");
		Employee employeeInfo = employee;
		System.out.println(employeeInfo.getEmailId());
		System.out.println("------------------------");
		return new org.springframework.security.core.userdetails.User(employeeInfo.getEmailId(),
				employeeInfo.getPassword(), Arrays.stream(employeeInfo.getRoles().split(","))
						.map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
	}

	/**
	 * This function will perform changing the existing password
	 * 
	 * @param changePasswordInfo - It contains information about updated password
	 * @return - returns confirmation value with respect to changing password
	 *         operation
	 */
	public boolean changePasswordData(ChangePasswordDataBean changePasswordInfo) {

		System.out.println("Entered into change password service");

		String oldPassword = changePasswordInfo.getOldPassword();
		String newPassword = changePasswordInfo.getNewPassword();

		Employee employeeInfo = employeeRepo.findByemailId(employee.getEmailId()).get();

		String existingPassword = employeeInfo.getPassword();

		boolean result = encoder.matches(oldPassword, existingPassword);

		if (result) {
			employeeInfo.setPassword(encoder.encode(newPassword));
			employeeRepo.save(employeeInfo);
			return true;
		}

		return false;

	}

	/**
	 * This function will perform double check while changing your password
	 * 
	 * @param confirmPasswordInfo - It contains confirmed password
	 * @return - It returns confirmation value regarding your password confirmed or
	 *         not
	 */

	public boolean confirmPasswordData(ConfirmPasswordDataBean confirmPasswordInfo) {

		System.out.println("Entered into confirm password service");

		String confirmPassword = confirmPasswordInfo.getConfirmPassword();
		String newPassword = confirmPasswordInfo.getNewPassword();

		if (confirmPassword.equalsIgnoreCase(newPassword)) {
			employee.setPassword(encoder.encode(newPassword));
			Employee emp = employeeRepo.save(employee);

			return true;
		}

		else {
			return false;
		}

	}

	/**
	 * This function will perform validation of given mailId or userId
	 * 
	 * @param data - It contains mailId or userId as input data
	 * @return - It returns confirmation value, whether your mailId or userId is
	 *         valid or not
	 */

	public Employee mailIdCheck(MailIdBean mail) {

		System.out.println("Entered into mailid check service");

		String input = mail.getMailId();

		if (input.endsWith("@gmail.com") || input.endsWith("@cms.co.in"))

		{
			employee = employeeRepo.findByemailId(input).get();
		}

		if (employee != null) {
			String to = employee.getEmailId();

			String subject = "OTP  For Reset  Password";

			String body = "OTP ---> " + generateOTP();

			emailUtils.sendEmail(employee.getEmailId(), subject, body);

			return employee;

		}
		return null;

	}

	/**
	 * @return - returns random OTP number
	 */

	public Integer generateOTP() {

		System.out.println("Entered into OTP generation service");

		Random random = new Random();

		otp = random.nextInt(10000);
		return otp;

	}

	/**
	 * This function will perform validation of OTP number
	 * 
	 * @param otpinfo - It contains OTP number
	 * @return - returns confirmation value, whether your OTP number is valid or not
	 */

	public boolean otpCheck(OTPBean otpInfo) {

		System.out.println("Entered into OTP validate service");

		int otpnum = otpInfo.getOtp();

		if (otpnum == otp) {

			return true;

		}

		else {
			return false;

		}

	}

	@Override
	public boolean saveEmployee(EmployeeRegistrationBean employeeRegistrationBean) {
		
		
		
		Employee employee=new Employee();
		
	    BeanUtils.copyProperties(employeeRegistrationBean, employee);
	    
	    String password = encoder.encode(employee.getPassword());
	    
	    employee.setPassword(password);
	

	           employee = employeeRepo.save(employee);
	           
	           
	           if(employee==null)
	           
	           return false;
	           
	           else
	        	   
	        	   return true;
	        	   
	}

	@Override
	public List<Employee> getAllEmployees() {

		return null;
	}

	@Override
	public Employee getEmployeeById(long empId) {
		return employeeRepo.findByempId(empId);

	}

	@Override
	public Employee updateEmployee(long empId, Employee employee) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean deleteEmployee(long empId) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Employee getEmployeeByemailId(String emailId) {
		return employeeRepo.findByemailId(emailId).get();
	}




	@Override
	public Location getEmployeeLocation(long locationId) {
		
		System.out.println(locationId);
		
		Location location = locationRepo.findBylocationId(locationId);
		return location;
	}




	@Override
	public MainDepartment getEmployeeDepartment(long departmentId) {
		
		//MainDepartment department = departmentRepo.findBydepartmentId(departmentId);

		//return department;
		return null;
	}

}
