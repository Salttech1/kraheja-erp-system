package kraheja.adminexp.billing.dataentry.invoiceCreation.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.GenericResponse;
import kraheja.adminexp.billing.dataentry.invoiceCreation.bean.request.CombinedEntity;
import kraheja.adminexp.billing.dataentry.invoiceCreation.service.InvoiceCreationService;

@RestController
@RequestMapping("/invoice-creation")
public class InvoiceCreationController {
	
	@Autowired
	private InvoiceCreationService invoiceCreationService;

	@GetMapping("/fetch-invPartyMaster-list")
	public GenericResponse<List<Map<String, Object>>> fetchInvPartyMaster(){
		return invoiceCreationService.fetchInvPartMasterDetails();
	}
	
	@PostMapping("/save-invoice-bill")
	public GenericResponse<Void> saveInvoiceDetails(@RequestBody CombinedEntity combinedEntity){
		return invoiceCreationService.postInvoiceDetails(combinedEntity);
	}
	
	@PutMapping("/modify-invoice-bill")
	public GenericResponse<Void> modifyInvoiceDetails(@RequestBody CombinedEntity combinedEntity){
		return invoiceCreationService.postInvoiceDetails(combinedEntity);
	}
	
}
