package com.cms.employeeservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.employeeservice.model.Location;
@Repository
public interface LocationRepository extends JpaRepository<Location, Long> {

	Location findBylocationId(long locationId);

}
