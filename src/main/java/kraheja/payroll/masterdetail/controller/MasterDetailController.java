package kraheja.payroll.masterdetail.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.payroll.bean.ComputationDetailRequestBean;
import kraheja.payroll.masterdetail.service.EmployeeDetailsEntryEditService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/payroll")
public class MasterDetailController {
	
	@Autowired
	private EmployeeDetailsEntryEditService employeeDetailsEntryEditService;
	
	
	@GetMapping("/masterDetail-EntryEdit")
	public ResponseEntity<?> fetchEmplDetails(String empcode) throws Exception{
		return this.employeeDetailsEntryEditService.fetchEmplDetails(empcode) ;
	}
	
	
}	
