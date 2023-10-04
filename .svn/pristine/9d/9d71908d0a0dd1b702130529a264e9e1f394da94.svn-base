package kraheja.payroll.Reports.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.payroll.Reports.service.ReportParametersService;
import kraheja.payroll.bean.response.ReportParametersRequestBean;

@RestController
@RequestMapping("/payrollreports")
public class ReportParametersController {
	
	@Autowired
	private ReportParametersService reportParametersService;
	
	
	@GetMapping("/reportparameters")
	public ResponseEntity<?> ReportParameters(){
		return this.reportParametersService.GetReportParameters();
	}
	
	
}