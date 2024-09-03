package com.cms.cdl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.cdl.model.TicketDocuments;

@Repository
public interface TicketDocumentsRepository extends JpaRepository<TicketDocuments, Long>  {

	List<TicketDocuments> findByticketId(Long ticketId);

	
}
