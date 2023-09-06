package kraheja.sales.bean.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import kraheja.commons.bean.response.AddressResponseBean;
import kraheja.commons.bean.response.PartyResponseBean;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class BookingResponseBean {
	
	private String bldgCode;
	private String wing;
	private String flatNum;
	private Double agPrice;
	private String bookedBy;
	private String ownerId;

//	PartyResponseBean partyResponseBean;

//	AddressResponseBean addressResponseBean;
}
