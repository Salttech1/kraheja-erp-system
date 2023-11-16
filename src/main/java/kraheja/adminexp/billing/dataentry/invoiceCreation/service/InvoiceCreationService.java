package kraheja.adminexp.billing.dataentry.invoiceCreation.service;

import java.util.List;
import java.util.Map;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.GenericResponse;
import kraheja.adminexp.billing.dataentry.invoiceCreation.bean.request.CombinedEntity;

public interface InvoiceCreationService {
	
GenericResponse<List<Map<String, Object>>> fetchInvPartMasterDetails();
GenericResponse<Void> postInvoiceDetails(CombinedEntity combinedEntity);
}
