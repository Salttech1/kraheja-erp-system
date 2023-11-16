package kraheja.sales.bean.entitiesresponse;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//kraheja.sales.bean.entitiesresponse.InfrBillDBResponse(infrBillnum,infrInvoiceNo,infrIrnNo,infrOwnerId,infrBldgCode,infrWing,infrFlatnum)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfrBillDBResponse {
	private String infrBillnum;
	private String infrInvoiceNo;
	private String infrIrnNo;
	private String infrOwnerId;
	private String infrBldgCode;
	private String infrWing;
	private String infrFlatnum;
	private String infrMonth;
	
	private double infrBillamt;
	private LocalDate infrBilldate;
	private LocalDate infrFromdate;
	private LocalDate infrTodate;
	private double infrAmtos;
	private double infrArrears;
	private double infrIntarrears;
	private double infrInterest;
	private double infrAdmincharges;
	private double infrCgst;
	private double infrSgst;
	private double infrIgst;
	private double infrCgstperc;
	private double infrSgstperc;
	private double infrIgstperc;
}
