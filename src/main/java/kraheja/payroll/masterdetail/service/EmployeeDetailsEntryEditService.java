package kraheja.payroll.masterdetail.service;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;


public interface EmployeeDetailsEntryEditService {
	
	ResponseEntity<?> fetchEmplDetails(String empcode) throws Exception;

	ResponseEntity<?> fetchAllSalaryPackage(String empcode,Character CurrentAll);

	ResponseEntity<?> fetchCompanySalPackage(String coycode,String closeDate);
	
	ResponseEntity<?> fetchCompanySalDedPackage(String coycode,String closeDate);

}
