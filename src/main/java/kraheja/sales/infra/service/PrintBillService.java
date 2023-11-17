package kraheja.sales.infra.service;

public interface PrintBillService {
	void printBill(String chargeCode, String billType, double sessionId);
	void deleteBill(double sessionId, String ownerId);
}
