package kraheja.sales.outgoing.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.sales.bean.request.OutgoingReportsRequestBean;
//import kraheja.sales.outgoing.GstInfoDto;
import kraheja.sales.outgoing.service.OutgoingReportsService;

@RestController
@RequestMapping("/outgoingreports")
public class OutgoingReportsController {

	@Autowired
	private OutgoingReportsService outgoingReportsService;

//	@PostMapping("/add-into-infra-defaulters-list-temp-table")
//	public ResponseEntity<?> addIntoInfraDefaultersListTempTable(@RequestBody InfraDefaultersListRequestBean infraDefaultersListRequestBean){
//		return this.infraService.addIntoInfraDefaultersListTempTable(infraDefaultersListRequestBean); 
//	}

//    @GetMapping("/get-gst-data")
//    public ResponseEntity<?> getGstData(@RequestBody GstInfoDto gstInfoDto )
//    {
//        return this.outgoingReportsService.getGstData(gstInfoDto);
//    }

//  @GetMapping("/get-first-og-date")
//  public ResponseEntity<?> getFirstOgDate(String flatOwner)
//  {
//      return this.outgoingReportsService.getFirstOgDate(flatOwner);
//  }

	@PostMapping("/generate-billdata")
	public ResponseEntity<?> inserttempowner(@RequestBody OutgoingReportsRequestBean outgoingReportsRequestBean) {
		return this.outgoingReportsService.processOgBills(outgoingReportsRequestBean);
	}

}
