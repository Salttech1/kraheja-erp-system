package kraheja.sales.bookings.dataentry.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kraheja.sales.bookings.dataentry.service.FlatsService;

@RestController
@RequestMapping("/flats")
public class FlatsController {

	@Autowired
	private FlatsService flatsService;
	
	@DeleteMapping("delete-flat")
	public ResponseEntity<?> deleteFlatByBldgCodeAndWingAndFlatnum(String bldgCode,String wing,String flatnum){
		return this.flatsService.deleteFlatByBldgCodeAndWingAndFlatnum(bldgCode, wing, flatnum);
	}
}
