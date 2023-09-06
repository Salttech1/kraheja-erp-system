package kraheja.sales.bookings.dataentry.controller;

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

import kraheja.sales.bean.request.BookingRequestBean;
import kraheja.sales.bookings.dataentry.service.BookingService;


@RestController
@RequestMapping("/booking")
public class BookingController {

	@Autowired
	private BookingService  bookingService; 

	
//	@PostMapping("/fetch-booking-by-ownerid")
//	public ResponseEntity<?> fetchBookingByOwnerId(@RequestBody BookingRequestBean bookingRequestBean) throws ParseException{
//		return this.bookingService.fetchBookingByBldgCodeAndWingAndFlatNum(bookingRequestBean);
//	}

	@PostMapping("/add-booking")
	public ResponseEntity<?> addBooking(@Valid @RequestBody BookingRequestBean bookingRequestBean) throws ParseException {
		return this.bookingService.addBooking(bookingRequestBean);
	}

	@PutMapping("/add-booking")
	public ResponseEntity<?> updateBooking(@Valid @RequestBody BookingRequestBean bookingRequestBean) throws ParseException {
		return this.bookingService.updateBooking(bookingRequestBean);
	}
	
	@GetMapping("/check-ownerid-exists")
	public ResponseEntity<?> checkBldgCodeAndWingAndFlatnumExists(@RequestParam(value = "bldgCode") String bldgCode,@RequestParam(value = "Wing") String Wing,@RequestParam(value = "FlatNum") String flatNum) throws ParseException {
		return this.bookingService.checkBldgCodeAndWingAndFlatnumExists(bldgCode,Wing,flatNum);
	}

	
}
