// Developed By  - 	kalpana.m
// Developed on - 25-07-23
// Mode  - Data Entry
// Purpose - Empassetinfo Entry / Edit
// Modification Details
// =======================================================================================================================================================
// Date		Coder  Version User    Reason              
// =======================================================================================================================================================

package kraheja.payroll.masterdetail.mappers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.utils.CommonConstraints;
import kraheja.payroll.bean.request.EmpassetinfoRequestBean;
import kraheja.payroll.bean.response.EmpassetinfoResponseBean;
import kraheja.payroll.bean.response.EmpassetinfoResponseBean.EmpassetinfoResponseBeanBuilder;
import java.util.stream.Collectors;
import kraheja.payroll.entity.Empassetinfo;
import kraheja.payroll.entity.EmpassetinfoCK;

public interface EmpassetinfoEntityPojoMapper {
	@SuppressWarnings("unchecked")
public static Function	<List<Empassetinfo>, List<EmpassetinfoResponseBean>> fetchEmpassetinfoEntityPojoMapper = empassetinfoEntityList -> {
return empassetinfoEntityList.stream().map(empassetinfoEntity -> {
return EmpassetinfoResponseBean.builder()
.empcode(empassetinfoEntity.getEmpassetinfoCK().getEassEmpcode())
					.assetcode(empassetinfoEntity.getEmpassetinfoCK().getEassAssetcode())
					.assetdesc(empassetinfoEntity.getEassAssetdesc())
					.assetname(empassetinfoEntity.getEassAssetname())
					.authby(empassetinfoEntity.getEassAuthby())
					.ipaddress(empassetinfoEntity.getEassIpaddress())
					.issuedate(Objects.nonNull(empassetinfoEntity.getEassIssuedate()) ? empassetinfoEntity.getEassIssuedate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.issuedby(empassetinfoEntity.getEassIssuedby())
					.machinename(empassetinfoEntity.getEassMachinename())
					.modifiedon(empassetinfoEntity.getEassModifiedon())
					.module(empassetinfoEntity.getEassModule())
					.receivedby(empassetinfoEntity.getEassReceivedby())
					.remark(empassetinfoEntity.getEassRemark())
					.returndate(Objects.nonNull(empassetinfoEntity.getEassReturndate()) ? empassetinfoEntity.getEassReturndate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.site(empassetinfoEntity.getEassSite())
					.userid(empassetinfoEntity.getEassUserid())
.build(); 
}).collect(Collectors.toList());

};


	public static Function<List<EmpassetinfoRequestBean>, List <Empassetinfo>> addEmpassetinfoPojoEntityMapper = (empassetinfoRequestBeanList) -> { 
return empassetinfoRequestBeanList.stream().map(empassetinfoRequestBean -> {
return Empassetinfo.builder().empassetinfoCK(EmpassetinfoCK.builder()
					.eassEmpcode(empassetinfoRequestBean.getEmpcode())
					.eassAssetcode(empassetinfoRequestBean.getAssetcode())
		.build())
					.eassAssetdesc(empassetinfoRequestBean.getAssetdesc())
					.eassAssetname(empassetinfoRequestBean.getAssetname())
					.eassAuthby(empassetinfoRequestBean.getAuthby())
					.eassIpaddress(empassetinfoRequestBean.getIpaddress())
					.eassIssuedate(Objects.nonNull(empassetinfoRequestBean.getIssuedate()) ? LocalDate.parse(empassetinfoRequestBean.getIssuedate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.eassIssuedby(empassetinfoRequestBean.getIssuedby())
					.eassMachinename(empassetinfoRequestBean.getMachinename())
					.eassModifiedon(LocalDateTime.now())
					.eassModule(empassetinfoRequestBean.getModule())
					.eassReceivedby(empassetinfoRequestBean.getReceivedby())
					.eassRemark(empassetinfoRequestBean.getRemark())
					.eassReturndate(Objects.nonNull(empassetinfoRequestBean.getReturndate()) ? LocalDate.parse(empassetinfoRequestBean.getReturndate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.eassSite(GenericAuditContextHolder.getContext().getSite())
					.eassUserid(GenericAuditContextHolder.getContext().getUserid())
		
.build();
}).collect(Collectors.toList());
} ;
	public static BiFunction<Empassetinfo, EmpassetinfoRequestBean, Empassetinfo> updateEmpassetinfoEntityPojoMapper = (empassetinfoEntity, empassetinfoRequestBean) -> {
		empassetinfoEntity.getEmpassetinfoCK().setEassEmpcode(Objects.nonNull(empassetinfoRequestBean.getEmpcode()) ? empassetinfoRequestBean.getEmpcode().trim() : empassetinfoEntity.getEmpassetinfoCK().getEassEmpcode());
		empassetinfoEntity.getEmpassetinfoCK().setEassAssetcode(Objects.nonNull(empassetinfoRequestBean.getAssetcode()) ? empassetinfoRequestBean.getAssetcode().trim() : empassetinfoEntity.getEmpassetinfoCK().getEassAssetcode());
		empassetinfoEntity.setEassAssetdesc(Objects.nonNull(empassetinfoRequestBean.getAssetdesc()) ? empassetinfoRequestBean.getAssetdesc().trim() : empassetinfoEntity.getEassAssetdesc());
		empassetinfoEntity.setEassAssetname(Objects.nonNull(empassetinfoRequestBean.getAssetname()) ? empassetinfoRequestBean.getAssetname().trim() : empassetinfoEntity.getEassAssetname());
		empassetinfoEntity.setEassAuthby(Objects.nonNull(empassetinfoRequestBean.getAuthby()) ? empassetinfoRequestBean.getAuthby().trim() : empassetinfoEntity.getEassAuthby());
		empassetinfoEntity.setEassIpaddress(Objects.nonNull(empassetinfoRequestBean.getIpaddress()) ? empassetinfoRequestBean.getIpaddress().trim() : empassetinfoEntity.getEassIpaddress());
		empassetinfoEntity.setEassIssuedate(Objects.nonNull(empassetinfoRequestBean.getIssuedate()) ? LocalDate.parse(empassetinfoRequestBean.getIssuedate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empassetinfoEntity.getEassIssuedate());
		empassetinfoEntity.setEassIssuedby(Objects.nonNull(empassetinfoRequestBean.getIssuedby()) ? empassetinfoRequestBean.getIssuedby().trim() : empassetinfoEntity.getEassIssuedby());
		empassetinfoEntity.setEassMachinename(Objects.nonNull(empassetinfoRequestBean.getMachinename()) ? empassetinfoRequestBean.getMachinename().trim() : empassetinfoEntity.getEassMachinename());
		empassetinfoEntity.setEassModifiedon(LocalDateTime.now()) ; 
		empassetinfoEntity.setEassModule(Objects.nonNull(empassetinfoRequestBean.getModule()) ? empassetinfoRequestBean.getModule().trim() : empassetinfoEntity.getEassModule());
		empassetinfoEntity.setEassReceivedby(Objects.nonNull(empassetinfoRequestBean.getReceivedby()) ? empassetinfoRequestBean.getReceivedby().trim() : empassetinfoEntity.getEassReceivedby());
		empassetinfoEntity.setEassRemark(Objects.nonNull(empassetinfoRequestBean.getRemark()) ? empassetinfoRequestBean.getRemark().trim() : empassetinfoEntity.getEassRemark());
		empassetinfoEntity.setEassReturndate(Objects.nonNull(empassetinfoRequestBean.getReturndate()) ? LocalDate.parse(empassetinfoRequestBean.getReturndate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empassetinfoEntity.getEassReturndate());
		empassetinfoEntity.setEassSite(GenericAuditContextHolder.getContext().getSite()) ; 
		empassetinfoEntity.setEassUserid(GenericAuditContextHolder.getContext().getUserid()) ; 


		return empassetinfoEntity;
	};

}
