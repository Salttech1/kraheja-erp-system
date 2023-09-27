package kraheja.sales.infra.service;

import kraheja.sales.bean.request.GenericRequest;
import kraheja.sales.bean.response.AuxilaryResponse;

public interface AuxilaryService {
	AuxilaryResponse getGridData(GenericRequest auxilaryRequest);
}
