package com.cms.employeeservice.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cms.employeeservice.model.SubDepartment;

public interface SubDepartmentRepository extends JpaRepository<SubDepartment, Long> {

	
	@Query(value = "select sub_department from sub_department where main_deptmt_id = :mainDeptmtId", nativeQuery = true)
	List<String> getAllSubDepartments(long mainDeptmtId);
}
