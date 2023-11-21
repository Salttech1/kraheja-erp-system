package kraheja.adminexp.billing.dataentry.intercompany.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.adminexp.billing.dataentry.intercompany.bean.request.InterCompanyRequest;
import kraheja.adminexp.billing.dataentry.intercompany.bean.response.AddInterCompanyResponse;
import kraheja.adminexp.billing.dataentry.intercompany.service.InterCompanyService;
import kraheja.payload.GenericResponse;
import lombok.extern.log4j.Log4j2;

@Log4j2
@RestController
@RequestMapping("/inter-company")
public class InterCompanyApi {
	
	@Autowired private InterCompanyService interCompanyService;
	
	@PostMapping("/add")
	public ResponseEntity<AddInterCompanyResponse> addInterCompany(@RequestHeader String siteName, @RequestHeader String userId, @RequestBody InterCompanyRequest request) {
		log.debug("/post/request/inter-company/add siteName: {} userId: {} InterCompanyRequest: {}", siteName, userId, request);

		AddInterCompanyResponse interCompanyAddResponse = interCompanyService.addInterCompany(request);
		log.debug("/post/request/interCompanyAddResponse: {}", interCompanyAddResponse);

		return ResponseEntity.ok(interCompanyAddResponse);
	}
}
