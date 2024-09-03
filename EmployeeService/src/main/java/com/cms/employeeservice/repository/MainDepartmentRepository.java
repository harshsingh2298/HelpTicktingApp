package com.cms.employeeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cms.employeeservice.model.MainDepartment;

public interface MainDepartmentRepository extends JpaRepository<MainDepartment, Long>{

	@Query(value = "select * from main_department", nativeQuery = true)
	List<MainDepartment> getAllMainDepartments();
}
