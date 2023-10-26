package kraheja.sales.infra.utilities;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import lombok.extern.log4j.Log4j2;

/**
 * 
 * <p>
 * this class is use for convert the month and increase counting month sequence
 * </p>
 * 
 * @author sazzad.alom
 * @since 27 SEP 2023
 * @version 1.0.0
 */
@Log4j2
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

	public static LocalDate convertStringToDateFormat(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
		return LocalDate.parse(date, formatter);
	}

	public static String startYearMonthFromInput(String date) {
		String strYear = "";
		String strMonth = "";
		String strYearMonth = "";

		if (!date.isEmpty()) {
			strYear = date.substring(6, 10);
			strMonth = date.substring(3, 5);
			strYearMonth = strYear + strMonth;
		} else {
			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			date = dateFormat.format(new Date());
			strYear = date.substring(6, 10);
			strMonth = date.substring(3, 5);
			strYearMonth = strYear + strMonth;
		}

		return strYearMonth;
	}

	public static String increaseMonth(String month) {
		// Parse the input string to a YearMonth object
		YearMonth inputYearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyyMM"));

		// Add one month to the YearMonth
		YearMonth newYearMonth = inputYearMonth.plusMonths(1);

		// Format the new YearMonth back to the YYYYMM format
		return newYearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
	}

	public static int getDaysInMonth(int year, int month) {
		if (month < 1 || month > 12) {
			throw new IllegalArgumentException("Invalid month: " + month);
		}
		if (year < 1) {
			throw new IllegalArgumentException("Invalid year: " + year);
		}

		switch (month) {
		case 2:
			if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
				return 29; // February has 29 days in a leap year
			} else {
				return 28; // February has 28 days in a non-leap year
			}
		case 4:
		case 6:
		case 9:
		case 11:
			return 30; // April, June, September, and November have 30 days
		default:
			return 31; // All other months have 31 days
		}
	}

	public static int dateComparison(LocalDate billDate, LocalDate recpDate) {
		
		int comparison = billDate.compareTo(recpDate);

        if (comparison < 0) {
            log.debug("Date billDate is before Date recpDate");

        } else if (comparison > 0) {
        	log.debug("Date billDate is after Date recpDate");
        } else {
        	log.debug("Date billDate is equal to Date recpDate");
        }
		return comparison;
		
	}
}