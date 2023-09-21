package kraheja.sales.infra.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.payload.GenericResponse;
import kraheja.sales.bean.request.GenericRequest;
import kraheja.sales.bean.response.AuxilaryResponse;
import kraheja.sales.infra.payload.request.IncheqRequest;
import kraheja.sales.infra.service.AuxilaryService;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/outinfra")
public class AuxilaryController {
	
	private final AuxilaryService auxilaryService;
	public AuxilaryController(AuxilaryService auxilaryService) {
		this.auxilaryService = auxilaryService;
	}
	@PostMapping("/auxilary-add-incheqe")
	public ResponseEntity<GenericResponse> addInchqDetail(@RequestBody IncheqRequest request){
		log.debug("post/request/outinfra/auxilary-add-incheqe incheq request : {}", request);

		GenericResponse response = auxilaryService.addInchq(request);
		log.debug("post/response/outinfra/auxilary-add-incheqe incheq response : {}", response);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/auxilary-fill-grid")
	public ResponseEntity<AuxilaryResponse> gridData(@RequestBody GenericRequest request){
		log.debug("post/request/outinfra/auxilary-fill-grid request : {}", request);
		
		AuxilaryResponse response = auxilaryService.getGridData(request);
		log.debug("post/response/outinfra/auxilary-fill-grid incheq response : {}", response);
		
		return ResponseEntity.ok(response);
		
	}
	
}
