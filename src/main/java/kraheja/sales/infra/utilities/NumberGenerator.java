package kraheja.sales.infra.utilities;

import java.util.Random;

public class NumberGenerator {
	public static String getReceiptNumber() {
		Random random = new Random();
		int number = random.nextInt(9000) + 1000;
		String receiptNumber = String.format("%06d", number);
		return receiptNumber;
	}

	public static String getTranser() {
		Random random = new Random();
		int number = random.nextInt(90000) + 100000;
		String receiptNumber = String.format("Z110" + number);
		return receiptNumber;
	}
}
