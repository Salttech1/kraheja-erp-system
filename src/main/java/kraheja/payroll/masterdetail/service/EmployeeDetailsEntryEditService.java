package kraheja.payroll.masterdetail.service;

import org.springframework.http.ResponseEntity;


public interface EmployeeDetailsEntryEditService {
	
	ResponseEntity<?> fetchEmplDetails(String empcode) throws Exception;

	ResponseEntity<?> fetchAllSalaryPackage(String empcode,Character CurrentAll);

}
