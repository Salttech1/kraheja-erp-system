package kraheja.adminexp.billing.dataentry.bean.request;

import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import java.time.LocalDateTime;

import kraheja.commons.bean.ActrandBean;
import kraheja.commons.bean.ActrandxBean;
import kraheja.commons.bean.ActranhBean;
import kraheja.commons.bean.ActranhxBean;
import kraheja.commons.bean.GSTValdiationBean;
import kraheja.commons.bean.request.PartyRequestBean;
import kraheja.commons.bean.request.AddressRequestBean;
import kraheja.commons.bean.request.MinorsRequestBean;
import lombok.Builder.Default;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor

public class AdmbillhRequestBean {

	private String acmajor ;
	private String acminor ;
	private String actranser ;
	private Double advnadjust ;
	private Double billamount ;
	private String billtype ;
	private String bldgcode ;
	private String clearacdate ;
	private String clearacperson ;
	private String coy ;
	private Double debitamt ;
	private String exptype ;
	private Double fotoamount ;
	private String fromdate ;
	private String gstrevchgyn ;
	private String mintype ;
	private String narration ;
	private String orderedby ;
	private String origsite ;
	private String origsys ;
	private Double paidamount ;
	private String paiddate ;
	private String paidref ;
	private String partycode ;
	private String partytype ;
	private String passedon ;
	private String project ;
	private String rundate ;
	private String ser ;
	private String site ;
	private String status ;
	private String sunclass ;
	private String sunid ;
	private String suppbilldt ;
	private String suppbillno ;
	private String tdsacmajor ;
	private Double tdsamount ;
	private Double tdsperc ;
	private String todate ;
	private LocalDateTime today ;
	private String userid ;
	//add or update flag
	@Default
	private Boolean isUpdate = Boolean.FALSE;
//	@Valid
//	private GSTValdiationBean gstValdiationBean;
	public String gstNumber;
	public String state;
	@Valid
	private PartyRequestBean partyRequestBean;
	@Valid
	private MinorsRequestBean minorsRequestBean;
	
	List <AdmbilldRequestBean> AdmbilldRequestBeanList;
	List <AdmbillhRequestBean> AdmbillhRequestBeanList;
	List <AdmadvanceRequestBean> AdmadvanceRequestBeanList;
	
	@Valid
	private ActrandBean ActrandBean;
	private ActranhBean ActranhBean;
	private ActrandxBean ActrandxBean;	
	private ActranhxBean ActranhxBean;
	
 	private AddressRequestBean addressRequestBean;
}       