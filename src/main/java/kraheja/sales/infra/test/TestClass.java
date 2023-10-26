package kraheja.sales.infra.test;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;

import kraheja.sales.infra.utilities.DateUtill;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class TestClass {

	public static void main(String[] args) {
//		String txtFlatownerFrom = "ORRG F0103"; // Replace 'your_text_here' with the actual text
//
//		String result = txtFlatownerFrom.substring(0, 4);
//
//		System.out.println(result);
		
//		String yearMonth = getQuaterEndYearMonth("01/11/2023");
//		System.out.println(yearMonth);
		
//		String StrMonthYear = "202402"; 
//
//		int month = Integer.parseInt(StrMonthYear.substring(4, 6));
//
////		System.out.println("month: " + month);
//		
//		int year = Integer.parseInt(StrMonthYear.substring(0, 4));

//		System.out.println("year: " + year);
//		YearMonth yearMonth = YearMonth.of(year, month);
//		System.out.println("yearMonth: " + yearMonth);
//		
//		int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
//		System.out.println("last day of the month: " + lastDayOfMonth);
//		
	    // Create a LocalDate object
//        LocalDate localDate = DateUtill.convertStringToDateFormat("20/10/2023"); // You can also provide a specific date here.
//
//        // Define the desired date format
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
//
//        // Format the LocalDate to a String
//        String formattedDate = localDate.format(formatter);
//
//        // Print the formatted date
//		log.debug("Formatted Date: {}", formattedDate);
		
//		int month = LocalDate.now().getMonthValue();
//		int year = LocalDate.now().getYear();
//		int lastDate = DateUtill.getDaysInMonth(year, month);
//		String StrLastDate = lastDate + "/" + month + "/" + year;
//		log.debug("IntLastDate : {} month: {} year : {} StrLastDate: {}",lastDate, month, year, StrLastDate);

		 LocalDate date1 = LocalDate.now();
	        LocalDate date2 = LocalDate.of(2023, 10, 24);

	        int comparison = date1.compareTo(date2);

	        if (comparison < 0) {
	            System.out.println("Date 1 is before Date 2");
	        } else if (comparison > 0) {
	            System.out.println("Date 1 is after Date 2");
	        } else {
	            System.out.println("Date 1 is equal to Date 2");
	        }

	}

	
	public static String getQuaterEndYearMonth(String StrBillDate) {
	    String quaterMonth, quaterYear;
	    int intMonth, IntYear;

	    intMonth = Integer.parseInt(StrBillDate.substring(3, 5));
	    IntYear = Integer.parseInt(StrBillDate.substring(6));

	    switch (intMonth) {
	        case 1:
	        case 2:
	        case 3:
	        	quaterMonth = "03";
	        	quaterYear = IntYear + quaterMonth;
	            break;
	        case 4:
	        case 5:
	        case 6:
	        	quaterMonth = "06";
	        	quaterYear = IntYear + quaterMonth;
	            break;
	        case 7:
	        case 8:
	        case 9:
	        	quaterMonth = "09";
	        	quaterYear = IntYear + quaterMonth;
	            break;
	        case 10:
	        case 11:
	        case 12:
	        	quaterMonth = "12";
	        	quaterYear = IntYear + quaterMonth;
	            break;
	        default:
	            // Handle invalid month here, if needed
	        	quaterYear = ""; // or some other default value
	            break;
	    }

	    return quaterYear;
	}

}