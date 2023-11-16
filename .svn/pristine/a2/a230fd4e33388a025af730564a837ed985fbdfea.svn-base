package kraheja.adminexp.billing.dataentry.bean.request;

import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;
import kraheja.commons.bean.GSTValdiationBean;
import kraheja.commons.bean.request.PartyRequestBean;
import lombok.Builder.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import kraheja.adminexp.billing.dataentry.controller.AdmadvanceController;
//import kraheja.adminexp.billing.dataentry.service.AdmadvanceService;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor

public class AdmadvanceRequestBean {

	private String actranser ;
	private Double advanceamt ;
	private Double basicamt ;
	private String bldgcode ;
	private String coy ;
	private String date ;
	private Double fotoamount ;
	private Double gstamt ;
	private Double gstperc ;
	private String narration ;
	private String orderby ;
	private String origsite ;
	private Double paidamount ;
	private String paiddate ;
	private String paidref ;
	private String partycode ;
	private String partytype ;
	private String passedon ;
	private String pinvdate ;
	private String pinvno ;
	private String project ;
	private String ser ;
	private String site ;
	private String status ;
	private String tdsacmajor ;
	private Double tdsamount ;
	private Double tdsperc ;
	private LocalDateTime today ;
	private String userid ;
	//add or update flag
	@Default
	private Boolean isUpdate = Boolean.FALSE;
	@Valid
	private GSTValdiationBean gstValdiationBean;
	@Valid
	private PartyRequestBean partyRequestBean;
}