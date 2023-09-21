package kraheja.sales.infra.service;

import kraheja.payload.GenericResponse;
import kraheja.sales.bean.request.GenericRequest;
import kraheja.sales.bean.response.AuxilaryResponse;
import kraheja.sales.infra.payload.request.IncheqRequest;

public interface AuxilaryService {

	GenericResponse addInchq(IncheqRequest incheqRequest);
	AuxilaryResponse getGridData(GenericRequest auxilaryRequest);
}
