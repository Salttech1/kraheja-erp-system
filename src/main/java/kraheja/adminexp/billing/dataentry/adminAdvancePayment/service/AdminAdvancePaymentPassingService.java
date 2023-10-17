package kraheja.adminexp.billing.dataentry.adminAdvancePayment.service;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.request.AdmadvanceRequestBean;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.AdmAdvancePaymentFetchResponse;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.GenericResponse;

public interface AdminAdvancePaymentPassingService {
	GenericResponse<AdmAdvancePaymentFetchResponse> fetchAdmadvanceByPinvAndSer(String ser, String pinv);
GenericResponse<Void> updateAdmadvance(AdmadvanceRequestBean admadvanceRequestBean);
}
