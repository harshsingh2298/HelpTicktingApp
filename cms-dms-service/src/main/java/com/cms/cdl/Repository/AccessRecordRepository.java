package com.cms.cdl.Repository;

import com.cms.cdl.Entity.AccessRecords;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRecordRepository extends JpaRepository<AccessRecords, Integer> {
}
