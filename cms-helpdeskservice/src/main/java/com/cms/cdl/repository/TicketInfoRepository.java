package com.cms.cdl.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import com.cms.cdl.model.TicketInfo;



@Repository
public interface TicketInfoRepository extends JpaRepository<TicketInfo, Long> {

	
	
	
	
	List<TicketInfo>findByraisedByEmployeeId(long employeeId);
	
	
	
	List<TicketInfo> findByassignedToEmployeeId(long employeeId);



	List<TicketInfo> findByassignedToAdminId(long employeeId);


	
	//3 categories of raised tickets for Employee or Admin
	@Query(value = "SELECT count(*) FROM tickets WHERE date(created_date) between date(:start) and date(:end) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByraisedByEmployeeIdAndCreatedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	
	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:start) and date(:end) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByraisedByEmployeeIdAndResolvedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	
	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:start) and date(:end) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByraisedByEmployeeIdAndOpenDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	
	
	
	
	
	
	//Total Assigned Tickets To Employee
	@Query(value = "SELECT count(*) FROM tickets WHERE date(created_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndCreatedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	
	//Assigned To Employee of all categories
	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndOpenDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(cancelled_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndCancelledDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(closed_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndClosedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(in_progress_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndInProgressDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:start) and date(:end) and  assigned_to_employee_id =:empId", nativeQuery = true)
	Integer findByassignedToEmployeeAndResolvedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);

	
	
	
	
	
	//Total Assigned Tickets To Admin
	@Query(value = "SELECT count(*) FROM tickets WHERE date(created_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndCreatedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	
	//Assigned To Admin of all categories
	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndOpenDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(cancelled_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndCancelledDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(closed_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndClosedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(in_progress_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndInProgressDateAndSelectedDate(long empId,LocalDate start,LocalDate end);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:start) and date(:end) and  assigned_to_admin_id =:empId", nativeQuery = true)
	Integer findByassignedToAdminAndResolvedDateAndSelectedDate(long empId,LocalDate start,LocalDate end);

	
	
	
	
	
	
	
	@Query(value = "SELECT count(*) FROM tickets WHERE date(created_date) between date(:startDate ) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByCreatedDateData(String startDate, String endDate, Long empId);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(closed_date) between date(:startDate) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByClosedDateData(String startDate, String endDate, Long empId);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(cancelled_date) between date(:startDate) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByCancelledDateData(String startDate, String endDate, Long empId);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:startDate) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByOpenDateData(String startDate, String endDate, Long empId);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:startDate) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByResolvedDateData(String startDate, String endDate, Long empId);
	@Query(value = "SELECT count(*) FROM tickets WHERE date(in_progress_date) between date(:startDate) and date(:endDate) and raised_by_employee_id=:empId", nativeQuery = true)
	Integer findByInProgressDateData(String startDate, String endDate, Long empId);


	
	
	@Query(value = "SELECT count(*) FROM tickets WHERE date(closed_date) between date(:startDate) and date(:endDate) and assigned_to_employee_id=:empId", nativeQuery = true)
	Integer findByClosedDateDataAndassignedToEmployee(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(closed_date) between date(:startDate) and date(:endDate) and assigned_to_admin_id=:empId", nativeQuery = true)
	Integer findByClosedDateDataAndassignedToAdmin(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(cancelled_date) between date(:startDate) and date(:endDate) and assigned_to_employee_id=:empId", nativeQuery = true)
	Integer findByCancelledDateDataAndassignedToEmployee(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(cancelled_date) between date(:startDate) and date(:endDate) and assigned_to_admin_id=:empId", nativeQuery = true)
	Integer findByCancelledDateDataAndassignedToAdmin(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:startDate) and date(:endDate) and assigned_to_employee_id=:empId", nativeQuery = true)
	Integer findByOpenDateDataAndassignedToEmployee(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(open_date) between date(:startDate) and date(:endDate) and assigned_to_admin_id=:empId", nativeQuery = true)
	Integer findByOpenDateDataAndassignedToAdmin(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:startDate) and date(:endDate) and assigned_to_employee_id=:empId", nativeQuery = true)
	Integer findByResolvedDateDataAndassignedToEmployee(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(resolved_date) between date(:startDate) and date(:endDate) and assigned_to_admin_id=:empId", nativeQuery = true)
	Integer findByResolvedDateDataAndassignedToAdmin(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(in_progress_date) between date(:startDate) and date(:endDate) and assigned_to_employee_id=:empId", nativeQuery = true)
	Integer findByInProgressDateDataAndassignedToEmployee(String startDate, String endDate, Long empId);


	@Query(value = "SELECT count(*) FROM tickets WHERE date(in_progress_date) between date(:startDate) and date(:endDate) and assigned_to_admin_id=:empId", nativeQuery = true)
	Integer findByInProgressDateDataAndassignedToAdmin(String startDate, String endDate,Long empId);

	
	
	
	
	
	
	
	
}
