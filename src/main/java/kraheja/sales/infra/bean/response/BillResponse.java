package kraheja.sales.infra.bean.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import kraheja.payload.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@JsonInclude(Include.NON_NULL)
public class BillResponse extends GenericResponse{
	private String billNumber;
	private String ownerId;
	private String month;
	private String billDate;
	private String billFromDate;
	private String billToDate;
	private String billAmount;
	private String billArrears;
	private String interest;
	private String interestArrears;
	private String admin;
	private String cgst;
	private String sgst;
	private String igst;
	private String cgstPerc;
	private String sgstPerc;
	private String igstPerc;
	private String invoiceNumber;
	private String irnno;
}
