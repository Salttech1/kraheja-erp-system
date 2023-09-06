// Developed By  - 	kalpana.m
// Developed on - 25-07-23
// Mode  - Data Entry
// Purpose - Empsalarypackage Entry / Edit
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
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.utils.CommonConstraints;
import kraheja.payroll.bean.EmpsalarypackageEarnDedBean;
import kraheja.payroll.bean.EmpsalarypackageEarnDedBean.EmpsalarypackageEarnDedBeanBuilder;
import kraheja.payroll.bean.request.EmpsalarypackageRequestBean;
import kraheja.payroll.bean.response.EmpsalarypackageResponseBean;
import java.util.stream.Collectors;
import kraheja.payroll.entity.Empsalarypackage;
import kraheja.payroll.entity.EmpsalarypackageCK;

public interface EmpsalarypackageEntityPojoMapper {
	@SuppressWarnings("unchecked")
public static Function	<List<Empsalarypackage>, List<EmpsalarypackageResponseBean>> fetchEmpsalarypackageEntityPojoMapper = empsalarypackageEntityList -> {
return empsalarypackageEntityList.stream().map(empsalarypackageEntity -> {
return EmpsalarypackageResponseBean.builder()
.empcode(empsalarypackageEntity.getEmpsalarypackageCK().getEspkEmpcode())
					.earndedcode(empsalarypackageEntity.getEmpsalarypackageCK().getEspkEarndedcode())
					.effectivefrom(Objects.nonNull(empsalarypackageEntity.getEmpsalarypackageCK().getEspkEffectivefrom()) ? empsalarypackageEntity.getEmpsalarypackageCK().getEspkEffectivefrom().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.earndedrate(empsalarypackageEntity.getEspkEarndedrate())
					.effectiveupto(Objects.nonNull(empsalarypackageEntity.getEspkEffectiveupto()) ? empsalarypackageEntity.getEspkEffectiveupto().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.ipaddress(empsalarypackageEntity.getEspkIpaddress())
					.machinename(empsalarypackageEntity.getEspkMachinename())
					.modifiedon(empsalarypackageEntity.getEspkModifiedon())
					.module(empsalarypackageEntity.getEspkModule())
					.paycycle(empsalarypackageEntity.getEspkPaycycle())
					.ratecycle(empsalarypackageEntity.getEspkRatecycle())
					.site(empsalarypackageEntity.getEspkSite())
					.userid(empsalarypackageEntity.getEspkUserid())
.build(); 
}).collect(Collectors.toList());

};


	public static Function<List<EmpsalarypackageRequestBean>, List <Empsalarypackage>> addEmpsalarypackagePojoEntityMapper = (empsalarypackageRequestBeanList) -> { 
return empsalarypackageRequestBeanList.stream().map(empsalarypackageRequestBean -> {
return Empsalarypackage.builder().empsalarypackageCK(EmpsalarypackageCK.builder()
					.espkEmpcode(empsalarypackageRequestBean.getEmpcode())
					.espkEarndedcode(empsalarypackageRequestBean.getEarndedcode())
					.espkEffectivefrom(Objects.nonNull(empsalarypackageRequestBean.getEffectivefrom()) ? LocalDate.parse(empsalarypackageRequestBean.getEffectivefrom(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.build())
					.espkEarndedrate(Objects.nonNull(empsalarypackageRequestBean.getEarndedrate()) ? empsalarypackageRequestBean.getEarndedrate() : BigInteger.ZERO.doubleValue())
					.espkEffectiveupto(Objects.nonNull(empsalarypackageRequestBean.getEffectiveupto()) ? LocalDate.parse(empsalarypackageRequestBean.getEffectiveupto(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.espkIpaddress(empsalarypackageRequestBean.getIpaddress())
					.espkMachinename(empsalarypackageRequestBean.getMachinename())
					.espkModifiedon(LocalDateTime.now())
					.espkModule(empsalarypackageRequestBean.getModule())
					.espkPaycycle(empsalarypackageRequestBean.getPaycycle())
					.espkRatecycle(empsalarypackageRequestBean.getRatecycle())
					.espkSite(GenericAuditContextHolder.getContext().getSite())
					.espkUserid(GenericAuditContextHolder.getContext().getUserid())
		
.build();
}).collect(Collectors.toList());
} ;
	public static BiFunction<Empsalarypackage, EmpsalarypackageRequestBean, Empsalarypackage> updateEmpsalarypackageEntityPojoMapper = (empsalarypackageEntity, empsalarypackageRequestBean) -> {
		empsalarypackageEntity.getEmpsalarypackageCK().setEspkEmpcode(Objects.nonNull(empsalarypackageRequestBean.getEmpcode()) ? empsalarypackageRequestBean.getEmpcode().trim() : empsalarypackageEntity.getEmpsalarypackageCK().getEspkEmpcode());
		empsalarypackageEntity.getEmpsalarypackageCK().setEspkEarndedcode(Objects.nonNull(empsalarypackageRequestBean.getEarndedcode()) ? empsalarypackageRequestBean.getEarndedcode().trim() : empsalarypackageEntity.getEmpsalarypackageCK().getEspkEarndedcode());
		empsalarypackageEntity.getEmpsalarypackageCK().setEspkEffectivefrom(Objects.nonNull(empsalarypackageRequestBean.getEffectivefrom()) ? LocalDate.parse(empsalarypackageRequestBean.getEffectivefrom(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empsalarypackageEntity.getEmpsalarypackageCK().getEspkEffectivefrom());
		empsalarypackageEntity.setEspkEarndedrate(Objects.nonNull(empsalarypackageRequestBean.getEarndedrate()) ? empsalarypackageRequestBean.getEarndedrate() : empsalarypackageEntity.getEspkEarndedrate());
		empsalarypackageEntity.setEspkEffectiveupto(Objects.nonNull(empsalarypackageRequestBean.getEffectiveupto()) ? LocalDate.parse(empsalarypackageRequestBean.getEffectiveupto(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empsalarypackageEntity.getEspkEffectiveupto());
		empsalarypackageEntity.setEspkIpaddress(Objects.nonNull(empsalarypackageRequestBean.getIpaddress()) ? empsalarypackageRequestBean.getIpaddress().trim() : empsalarypackageEntity.getEspkIpaddress());
		empsalarypackageEntity.setEspkMachinename(Objects.nonNull(empsalarypackageRequestBean.getMachinename()) ? empsalarypackageRequestBean.getMachinename().trim() : empsalarypackageEntity.getEspkMachinename());
		empsalarypackageEntity.setEspkModifiedon(LocalDateTime.now()) ; 
		empsalarypackageEntity.setEspkModule(Objects.nonNull(empsalarypackageRequestBean.getModule()) ? empsalarypackageRequestBean.getModule().trim() : empsalarypackageEntity.getEspkModule());
		empsalarypackageEntity.setEspkPaycycle(Objects.nonNull(empsalarypackageRequestBean.getPaycycle()) ? empsalarypackageRequestBean.getPaycycle().trim() : empsalarypackageEntity.getEspkPaycycle());
		empsalarypackageEntity.setEspkRatecycle(Objects.nonNull(empsalarypackageRequestBean.getRatecycle()) ? empsalarypackageRequestBean.getRatecycle().trim() : empsalarypackageEntity.getEspkRatecycle());
		empsalarypackageEntity.setEspkSite(GenericAuditContextHolder.getContext().getSite()) ; 
		empsalarypackageEntity.setEspkUserid(GenericAuditContextHolder.getContext().getUserid()) ; 


		return empsalarypackageEntity;
	};


}
