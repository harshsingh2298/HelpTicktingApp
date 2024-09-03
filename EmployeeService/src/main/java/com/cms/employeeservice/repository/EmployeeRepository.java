package com.cms.employeeservice.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.employeeservice.model.Employee;


@Repository

public interface EmployeeRepository extends JpaRepository<Employee,Long> {
	
	
	Optional<Employee> findByemailId(String userEmail);

	Employee findByempId(long empId);

	
	
	

}
