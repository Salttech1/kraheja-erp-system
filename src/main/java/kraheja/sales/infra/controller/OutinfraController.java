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

import kraheja.sales.bean.request.OutinfraRequestBean;
import kraheja.sales.infra.service.OutinfraService;

@RestController
@RequestMapping("/outinfra")
public class OutinfraController {

	@Autowired
	private OutinfraService outinfraService;
	
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
	public ResponseEntity<?> fetchMaintainanceRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="month") String month, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchMaintainanceRate(bldgcode, wing, flatno, month, billType);
	}		
	//-----End--NS--14-08-2023---------
			
	//following function will fetch the admin rate
	//-----Start--NS-17-08-2023---------
	@GetMapping("/fetch-admin-rate-for-auxi")
	public ResponseEntity<?> fetchAdminRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="month") String month, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchAdminRate(bldgcode, wing, flatno, month, billType);
	}		
	//-----End--NS--17-08-2023---------
					
	//following function will fetch the TDS rate
	//-----Start--NS-17-08-2023---------
	@GetMapping("/fetch-TDS-rate-for-auxi")
	public ResponseEntity<?> fetchTDSRate(@RequestParam(value="bldgcode") String bldgcode, @RequestParam(value="wing") String wing, @RequestParam(value="flatno") String flatno, @RequestParam(value="month") String month, @RequestParam(value="billType") String billType){
		return this.outinfraService.fetchTDSRate(bldgcode, wing, flatno, month, billType);
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

//	@GetMapping("/check-Bldgcode-and-Ownerid-and-Recnum-and-Month-and-Narrcode-exists")
//	public ResponseEntity<?> checkBldgcodeAndOwneridAndRecnumAndMonthAndNarrcodeExists(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "ownerid") String  ownerid, @RequestParam(value = "recnum") String  recnum, @RequestParam(value = "month") String  month, @RequestParam(value = "narrcode") String  narrcode) throws ParseException {
//		return this.outinfraService.checkBldgcodeAndOwneridAndRecnumAndMonthAndNarrcodeExists(bldgcode, ownerid, recnum, month, narrcode);
//	}

	@DeleteMapping("/delete-outinfra")
	public ResponseEntity<?> deleteOutinfra(@RequestParam(value = "bldgcode") String  bldgcode, @RequestParam(value = "ownerid") String  ownerid, @RequestParam(value = "recnum") String  recnum, @RequestParam(value = "month") String  month, @RequestParam(value = "narrcode") String  narrcode) throws ParseException {
		return this.outinfraService.deleteOutinfra(bldgcode, ownerid, recnum, month, narrcode) ; 
	}
}