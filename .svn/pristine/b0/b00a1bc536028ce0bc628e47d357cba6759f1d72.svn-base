package kraheja.adminexp.billing.dataentry.controller;

import java.text.ParseException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kraheja.adminexp.billing.dataentry.bean.request.AdmbillhRequestBean;
import kraheja.adminexp.billing.dataentry.service.AdmbillhService;

@RestController
@RequestMapping("/admbillh")
public class AdmbillhController {

	@Autowired
	private AdmbillhService admbillhService;

	@GetMapping("/fetch-admbillh-by-Ser")
	public ResponseEntity<?> fetchAdmbillhBySer(@RequestParam(value = "ser") String  ser) {
		return this.admbillhService.fetchAdmbillhBySer(ser) ; 
	}

	@PostMapping("/add-admbillh")
	public ResponseEntity<?> addAdmbillh(@Valid @RequestBody AdmbillhRequestBean admbillhRequestBean) throws ParseException {
		return this.admbillhService.addAdmbillh(admbillhRequestBean);
	}

	@PutMapping("/update-admbillh")
	public ResponseEntity<?> updateAdmbillh(@Valid @RequestBody AdmbillhRequestBean admbillhRequestBean) throws ParseException {
		return this.admbillhService.updateAdmbillh(admbillhRequestBean);
	}
//
//	@GetMapping("/check-Ser-exists")
//	public ResponseEntity<?> checkSerExists(@RequestParam(value = "ser") String  ser)  {
//		return this.admbillhService.checkSerExists(ser);
//	}

}