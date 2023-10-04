package kraheja.sales.infra.service;

import kraheja.sales.bean.request.InchequeRequest;

public interface AuxiliaryPersistanceService {
	String saveIncheqe(String bldgCode, String wing, String flatNumber, String chargeCode,InchequeRequest inchequeRequest);
}
