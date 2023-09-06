package kraheja.sales.bookings.dataentry.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;

import kraheja.sales.bean.request.BookingRequestBean;

public interface BookingService {

//	ResponseEntity<?> fetchBookingByBldgCodeAndWingAndFlatNum(Sting brokercode) throws ParseException;

	ResponseEntity<?> addBooking(BookingRequestBean bookingRequestBean) throws ParseException;

	ResponseEntity<?> updateBooking(BookingRequestBean bookingRequestBean) throws ParseException;

	ResponseEntity<?> checkBldgCodeAndWingAndFlatnumExists(String bldgCode,String Wing,String flatNum);

}
