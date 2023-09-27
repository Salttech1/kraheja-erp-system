package kraheja.sales.bean.request;

import java.util.Date;

import javax.validation.Valid;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import kraheja.commons.annotaions.PanNumberCheck;
import kraheja.commons.bean.request.AddressRequestBean;
import kraheja.commons.bean.request.PartyRequestBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BookingRequestBean {
	
	private String bldgCode;
	private String wing;
	private String flatNum;
	private Double agPrice;
	private String bookedBy;
	private String ownerId;
	
	private AddressRequestBean addressRequestBean;
	@Valid
	private PartyRequestBean partyRequestBean;
}
