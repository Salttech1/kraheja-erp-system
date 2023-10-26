package kraheja.adminexp.billing.dataentry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.adminexp.billing.dataentry.service.InvoiceCreationService;

@RestController
@RequestMapping("/invoice-creation")
public class InvoiceCreationController {
	
	@Autowired
	private InvoiceCreationService invoiceCreationService;

	@GetMapping("/fetch-invPartyMaster-list")
	public ResponseEntity<?> fetchInvPartyMaster(){
		return invoiceCreationService.fetchInvPartMasterDetails();
	}
	
	
}
