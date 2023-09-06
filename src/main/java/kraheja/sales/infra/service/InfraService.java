package kraheja.sales.infra.service;


import org.springframework.http.ResponseEntity;

import kraheja.sales.bean.request.InfraDefaultersListRequestBean;

public interface InfraService {

	ResponseEntity<?> addIntoInfraDefaultersListTempTable(InfraDefaultersListRequestBean infraDefaultersListRequestBean);

	ResponseEntity<?> deleteInfraDefaultersListFromSessionId(Integer sessionId);
}