package kraheja.adminexp.billing.dataentry.intercompany.service;

import kraheja.adminexp.billing.dataentry.intercompany.bean.request.InterCompanyRequest;
import kraheja.adminexp.billing.dataentry.intercompany.bean.response.AddInterCompanyResponse;

public interface InterCompanyService {
	AddInterCompanyResponse addInterCompany(InterCompanyRequest request);
}
