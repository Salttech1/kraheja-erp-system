package kraheja.sales.infra.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kraheja.sales.bean.request.AuxilaryRequest;
import kraheja.sales.bean.request.InchequeRequest;
import kraheja.sales.bean.request.OutinfraRequestBean;
import kraheja.sales.bean.response.AuxilaryResponse;
import kraheja.sales.bean.response.InchequeResponse;
import kraheja.sales.infra.service.AuxilaryService;
import kraheja.sales.infra.service.AuxiliaryPersistanceService;
import kraheja.sales.infra.service.OutinfraService;
import lombok.extern.log4j.Log4j2;
 
@Log4j2
@RestController
@RequestMapping("/outinfra")
public class OutinfraController {

	@Autowired
	private OutinfraService outinfraService;
	@Autowired 
	private AuxilaryService auxilaryService;
	
	@Autowired private AuxiliaryPersistanceService auxiPersistanceService;
	
	//following function will fetch data of flatowner. 
	
	//-----Start--NS-08-06-2023---------
			@GetMapping("/fetch-Previous-Outgoing-Record-by-Ownerid-and-Month-and-Chargecode-and-Rectype")
			public ResponseEntity<?> fetchPreviousOgRecords(@RequestParam(value="ownerid") String owenerid, @RequestParam(value="month") String month, @RequestParam(value="chargecode") String chargecode, @RequestParam(value="rectype") String rectype){
				return this.outinfraService.fetchPreviousOgRecords(owenerid, month, chargecode, rectype);
			}
		//-----End--NS--08.06.2023----------
			
	//following function will fetch the maintainance rate
	//-----Start--NS-14-08-2023---------
	@GetMapping("/fetch-maintainance-rate-for-auxi")
	public ResponseEntity<?> fetchMaintainanceRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchMaintainanceRate(bldgcode, wing, flatno, billType);
	}		
	//-----End--NS--14-08-2023---------
			
	//following function will fetch the admin rate
	//-----Start--NS-17-08-2023---------
	@GetMapping("/fetch-admin-rate-for-auxi")
	public ResponseEntity<?> fetchAdminRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchAdminRate(bldgcode, wing, flatno, billType);
	}		
	//-----End--NS--17-08-2023---------
					
	//following function will fetch the TDS rate
	//-----Start--NS-17-08-2023---------
	@GetMapping("/fetch-TDS-rate-for-auxi")
	public ResponseEntity<?> fetchTDSRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchTDSRate(bldgcode, wing, flatno, billType);
	}		
	//-----End--NS--17-08-2023---------
						
	//-----Start---NS 15.05.2023-------
	@GetMapping("/fetch-flatowner-data-by-Bldgcode-and-Flatnum-and-Wing")
	public ResponseEntity<?> fetchFlatOwnerByBldgcodeAndFlatnumAndWing(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="flatnum") String flatnum,@RequestParam(value="wing") String wing){
		return this.outinfraService.fetchFlatOwnerByBldgcodeAndFlatnumAndWing(bldgcode, flatnum, wing);
	}
	//-----End---NS 15.05.2023---------
	
	//-----Start---NS 15.05.2023------
	@GetMapping("/fetch-start-date")
	public ResponseEntity<?> fetchStartDateByBldgcodeAndWingAndFlatnoAndBilltype(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="billtype") String billtype)
	{
		return this.outinfraService.fetchStartDateByBldgcodeAndWingAndFlatnoAndBilltype(bldgcode, wing, flatno, billtype);
	}
	//----End--NS 19.05.2023---------
	
	//-----Start NS 29-05-2023---------
	@GetMapping("/fetch-gst-rates")
	public ResponseEntity<?> fetchGstRates(){
		return this.outinfraService.fetchGstRates();
	}
	//-----End NS 29-05-2023-----------

	@GetMapping("/fetch-outinfra-by-Bldgcode-and-Ownerid-and-Recnum-and-Month-and-Narrcode")
	public ResponseEntity<?> fetchOutinfraByBldgcodeAndOwneridAndRecnumAndMonthAndNarrcode(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "ownerid") String  ownerid, @RequestParam(value = "recnum") String  recnum, @RequestParam(value = "month") String  month, @RequestParam(value = "narrcode") String  narrcode) throws ParseException {
		return this.outinfraService.fetchOutinfraByBldgcodeAndOwneridAndRecnumAndMonthAndNarrcode(bldgcode, ownerid, recnum, month, narrcode) ; 
	}

	@PostMapping("/add-outinfra")
	public ResponseEntity<?> addOutinfra(@Valid @RequestBody OutinfraRequestBean outinfraRequestBean) throws ParseException {
		return this.outinfraService.addOutinfra(outinfraRequestBean);
	}

	@PutMapping("/update-outinfra")
	public ResponseEntity<?> updateOutinfra(@Valid @RequestBody OutinfraRequestBean outinfraRequestBean) throws ParseException {
		return this.outinfraService.updateOutinfra(outinfraRequestBean);
	}
	
	@GetMapping("/fetch-startdate-and-enddate-by-Bldgcode-and-wing-and-flatnum-and-bill-type") //NS 04.09.2023
	public ResponseEntity<?> fetchStartdateAndEnddateByBldgcodeAndWingAndFlatnumAndbilltype(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "wing") String  wing, @RequestParam(value = "flatno") String  flatnum, @RequestParam(value = "billType") String  billtype) throws ParseException {
		return this.outinfraService.fetchStartdateAndEnddateByBldgcodeAndWingAndFlatnumAndbilltype(bldgcode, wing, flatnum, billtype) ; 
	}

//	@GetMapping("/check-Bldgcode-and-Ownerid-and-Recnum-and-Month-and-Narrcode-exists")
//	public ResponseEntity<?> checkBldgcodeAndOwneridAndRecnumAndMonthAndNarrcodeExists(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "ownerid") String  ownerid, @RequestParam(value = "recnum") String  recnum, @RequestParam(value = "month") String  month, @RequestParam(value = "narrcode") String  narrcode) throws ParseException {
//		return this.outinfraService.checkBldgcodeAndOwneridAndRecnumAndMonthAndNarrcodeExists(bldgcode, ownerid, recnum, month, narrcode);
//	}

	@DeleteMapping("/delete-outinfra")
	public ResponseEntity<?> deleteOutinfra(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "ownerid") String  ownerid, @RequestParam(value = "recnum") String  recnum, @RequestParam(value = "month") String  month, @RequestParam(value = "narrcode") String  narrcode) throws ParseException {
		return this.outinfraService.deleteOutinfra(bldgcode, ownerid, recnum, month, narrcode) ; 
	}
	
	@PostMapping("/auxilary-allocation-grid")
	public ResponseEntity<AuxilaryResponse> gridData(@RequestBody AuxilaryRequest request){
		log.debug("post/request/outinfra/auxilary-fill-grid request : {}", request);
		
		AuxilaryResponse response = auxilaryService.getGridData(request);
		log.debug("post/response/outinfra/auxilary-fill-grid incheq response : {}", response);
		
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/save-incheqe-details")
	public ResponseEntity<InchequeResponse> saveIncheqe(
			@RequestParam String buildingCode, 
			@RequestParam String wing,
			@RequestParam String flatNumber, 
			@RequestParam String chargeCode,
			@RequestBody InchequeRequest inchequeRequest){
		
		log.debug("post/request/outinfra/saveIncheqe buildingCode : {} wing:{} flatNumber: {} chargeCode: {} inchequeRequest: {}", buildingCode, wing, flatNumber, chargeCode, inchequeRequest);
		
		InchequeResponse response = auxiPersistanceService.saveIncheqe(buildingCode, wing, flatNumber, chargeCode, inchequeRequest);
		log.debug("post/response/saveIncheqe : {}",response);
		
		return ResponseEntity.ok(response);
	}
}