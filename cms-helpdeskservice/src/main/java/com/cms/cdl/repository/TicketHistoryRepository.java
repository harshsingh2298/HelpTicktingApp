package com.cms.cdl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.cdl.model.TicketHistory;
import com.cms.cdl.model.TicketInfo;
@Repository
public interface TicketHistoryRepository extends JpaRepository<TicketHistory , Long>{

	List<TicketHistory> findByticketId(TicketInfo ticket);

}
