package com.cms.cdl.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cms.cdl.model.TicketComment;
import com.cms.cdl.model.TicketInfo;
@Repository
public interface TicketCommentRepository extends JpaRepository<TicketComment, Long>{

	List<TicketComment> findByticketId(TicketInfo ticket);

}
