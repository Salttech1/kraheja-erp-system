package kraheja.sales.bean.entitiesresponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Builder
public class PreviousDBBalance {
	private String prevOutMonth;
	private String prevAdmin;
	private String prevCGSTTax;
	private String prevSGSTTax;
	private String dblprevIGSTTax;
	private String prevAmtint;
	private String prevAmtPaid;
	private String prevInfraOutRate;
	private String prevAdmincharges;
	private String prevTds;
}
