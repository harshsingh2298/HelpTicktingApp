package com.cms.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.cms.employeeservice.model.Skills;
@Repository
public interface SkillsRepository extends JpaRepository<Skills, Long>{

}
