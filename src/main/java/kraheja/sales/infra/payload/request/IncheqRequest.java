package kraheja.sales.infra.payload.request;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class IncheqRequest {
	private String inchqNum;
	private Double inchqAmount;
	private String inchqSite;
	private String inchqUserid;
	private String inchqToday;
	private String inchqDate;
	private String inchqBank;
	private String inchqSlipnum;
	private String inchqTranser;
	private String inchqOutstat;
	private String inchqProprietor;
	private String inchqCoy;
	private String inchqPartycode;
	private String inchqFund;
	private String inchqActype;
	

}
