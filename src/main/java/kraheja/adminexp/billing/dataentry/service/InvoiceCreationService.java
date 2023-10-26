package kraheja.adminexp.billing.dataentry.service;

import org.springframework.http.ResponseEntity;

public interface InvoiceCreationService {
	
ResponseEntity<?> fetchInvPartMasterDetails();
}
