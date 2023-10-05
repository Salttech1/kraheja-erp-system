package kraheja.sales.infra.utilities;

import java.util.Random;

public class ReceiptNumberGenerator {
	  public static String getReceiptNumber() {
	    	Random random = new Random();
	        int number = random.nextInt(9000) + 1000;
	        String receiptNumber = String.format("%06d", number);
			return receiptNumber;
	    }
}
