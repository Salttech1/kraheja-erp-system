package kraheja.sales.bean.request;

import javax.validation.constraints.NotEmpty;

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
public class AuxilaryRequest {
	
	@NotEmpty
	private String chargeCode;
	private String narration;
	@NotEmpty private String billType;
	@NotEmpty private String buildingCode;
	@NotEmpty private String wing;
	private String flatNum;
	@NotEmpty private String date;
	@NotEmpty private String receiptAmt;
	@NotEmpty private String receiptAmtTds;
	@NotEmpty private String totalAmt;
}
