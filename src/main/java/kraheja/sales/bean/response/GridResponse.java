package kraheja.sales.bean.response;

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
public class GridResponse {
	private String monthName;
	private String narrationCode;
	private String narration;
	private String auxiPaid;
	private String intPaid;
	private String admin;
	private String cgst;
	private String sgst;
	private String igst;
	private String cgstPercent;
	private String sgstPercent;
	private String igstPercent;
	private String tds;
}
