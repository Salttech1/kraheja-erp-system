package kraheja.sales.infra.service;

public interface PrintBillService {
	void printBill(String chargeCode, String billType, double sessionId);
}
