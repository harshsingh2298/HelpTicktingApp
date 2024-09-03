package com.cms.cdl.repo;

import com.cms.cdl.model.SubDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubDepartmentRepo extends JpaRepository<SubDepartment, Long> {
}
