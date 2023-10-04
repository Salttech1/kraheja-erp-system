
package kraheja.adminexp.billing.dataentry.controller;

import java.util.Map;
import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import kraheja.adminexp.billing.dataentry.bean.request.AdmadvanceRequestBean;
import kraheja.adminexp.billing.dataentry.service.AdmadvanceService;

@RestController
@RequestMapping("/admadvance")
public class AdmadvanceController {

	@Autowired(required=false)
	private AdmadvanceService admadvanceService;

	@GetMapping("/fetch-admadvance-by-Ser")
	public ResponseEntity<?> fetchAdmadvanceBySer(@RequestParam(value = "ser") String ser) throws ParseException{
		return this.admadvanceService.fetchAdmadvanceBySer(ser); 
	}

	@PostMapping("/add-admadvance")
	public ResponseEntity<?> addAdmadvance(@Valid @RequestBody AdmadvanceRequestBean admadvanceRequestBean) throws ParseException  {
		return this.admadvanceService.addAdmadvance(admadvanceRequestBean);
	}

	@PutMapping("/update-admadvance")
	public ResponseEntity<?> updateAdmadvance(@Valid @RequestBody AdmadvanceRequestBean admadvanceRequestBean) throws ParseException  {
		return this.admadvanceService.updateAdmadvance(admadvanceRequestBean);
	}

	@GetMapping("/check-Ser-exists")
	public ResponseEntity<?> checkSerExists(@RequestParam(value = "ser") String ser)  {
		return this.admadvanceService.checkserExists(ser);
	}
}                                 
