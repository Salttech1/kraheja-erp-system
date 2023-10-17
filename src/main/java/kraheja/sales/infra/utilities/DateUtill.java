package kraheja.sales.infra.utilities;


import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
/**
 * 
 * <p>this class is use for convert the month and increase counting month sequence</p>
 * 
 * @author sazzad.alom
 * @since 27 SEP 2023
 * @version 1.0.0
 * */
public class DateUtill {

	// 202306+5
	public static String endMonth(int countMonth) {

		int year = Integer.valueOf((String.valueOf(countMonth)).substring(0, 4));
		int month = Integer.valueOf((String.valueOf(countMonth)).substring(4, 6));
		
		 if (countMonth > 12) {
	            int yearsToAdd = (month - 1) / 12;
	            year += yearsToAdd;
	            month = (month - 1) % 12 + 1;
	            YearMonth yearMonth = YearMonth.of(year, month);
	            return yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
	        }
		return String.valueOf(countMonth); // 202311
	}
	
	 public static String dateFormatter(LocalDateTime date) {

	        // Define a DateTimeFormatter for the desired format
	        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	        // Format the date and time
	        String formattedDate = date.format(formatter);

	        System.out.println(formattedDate);
			return formattedDate;
	    }
	
}