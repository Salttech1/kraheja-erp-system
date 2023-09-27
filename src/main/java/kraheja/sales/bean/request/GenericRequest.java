package kraheja.sales.bean.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class GenericRequest {
	private String chargeCode;
	private String narration;
	private String billType;
	private String buildingCode;
	private String wing;
	private String flatNum;
	private String date;
	private String receiptAmt;
	private String receiptAmtTds;
	private String totalAmt;
}
