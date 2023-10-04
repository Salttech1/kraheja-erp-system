// Developed By  - 	kalpana.m
// Developed on - 25-07-23
// Mode  - Data Entry
// Purpose - Empexperience Entry / Edit
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
import kraheja.payroll.bean.request.EmpexperienceRequestBean;
import kraheja.payroll.bean.response.EmpexperienceResponseBean;
import kraheja.payroll.bean.response.EmpexperienceResponseBean.EmpexperienceResponseBeanBuilder;
import java.util.stream.Collectors;
import kraheja.payroll.entity.Empexperience;
import kraheja.payroll.entity.EmpexperienceCK;

public interface EmpexperienceEntityPojoMapper {
	@SuppressWarnings("unchecked")
public static Function	<List<Empexperience>, List<EmpexperienceResponseBean>> fetchEmpexperienceEntityPojoMapper = empexperienceEntityList -> {
return empexperienceEntityList.stream().map(empexperienceEntity -> {
return EmpexperienceResponseBean.builder()
.empcode(empexperienceEntity.getEmpexperienceCK().getEexpEmpcode())
					.jobsrno(empexperienceEntity.getEmpexperienceCK().getEexpJobsrno())
					.companyname(empexperienceEntity.getEexpCompanyname())
					.endgrade(empexperienceEntity.getEexpEndgrade())
					.endgrosspermth(empexperienceEntity.getEexpEndgrosspermth())
					.endlevel(empexperienceEntity.getEexpEndlevel())
					.endpackage(empexperienceEntity.getEexpEndpackage())
					.endpost(empexperienceEntity.getEexpEndpost())
					.endreportingto(empexperienceEntity.getEexpEndreportingto())
					.ipaddress(empexperienceEntity.getEexpIpaddress())
					.jobprofile(empexperienceEntity.getEexpJobprofile())
					.machinename(empexperienceEntity.getEexpMachinename())
					.modifiedon(empexperienceEntity.getEexpModifiedon())
					.module(empexperienceEntity.getEexpModule())
					.reasonforleaving(empexperienceEntity.getEexpReasonforleaving())
					.site(empexperienceEntity.getEexpSite())
					.startgrade(empexperienceEntity.getEexpStartgrade())
					.startgrosspermth(empexperienceEntity.getEexpStartgrosspermth())
					.startlevel(empexperienceEntity.getEexpStartlevel())
					.startpackage(empexperienceEntity.getEexpStartpackage())
					.startpost(empexperienceEntity.getEexpStartpost())
					.startreportingto(empexperienceEntity.getEexpStartreportingto())
					.totalservice(empexperienceEntity.getEexpTotalservice())
					.userid(empexperienceEntity.getEexpUserid())
					.workedfrom(Objects.nonNull(empexperienceEntity.getEexpWorkedfrom()) ? empexperienceEntity.getEexpWorkedfrom().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.workedupto(Objects.nonNull(empexperienceEntity.getEexpWorkedupto()) ? empexperienceEntity.getEexpWorkedupto().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
.build(); 
}).collect(Collectors.toList());

};


	public static Function<List<EmpexperienceRequestBean>, List <Empexperience>> addEmpexperiencePojoEntityMapper = (empexperienceRequestBeanList) -> { 
return empexperienceRequestBeanList.stream().map(empexperienceRequestBean -> {
return Empexperience.builder().empexperienceCK(EmpexperienceCK.builder()
					.eexpEmpcode(empexperienceRequestBean.getEmpcode())
					.eexpJobsrno(Objects.nonNull(empexperienceRequestBean.getJobsrno()) ? empexperienceRequestBean.getJobsrno() : BigInteger.ZERO.intValue())
		.build())
					.eexpCompanyname(empexperienceRequestBean.getCompanyname())
					.eexpEndgrade(empexperienceRequestBean.getEndgrade())
					.eexpEndgrosspermth(Objects.nonNull(empexperienceRequestBean.getEndgrosspermth()) ? empexperienceRequestBean.getEndgrosspermth() : BigInteger.ZERO.doubleValue())
					.eexpEndlevel(empexperienceRequestBean.getEndlevel())
					.eexpEndpackage(Objects.nonNull(empexperienceRequestBean.getEndpackage()) ? empexperienceRequestBean.getEndpackage() : BigInteger.ZERO.doubleValue())
					.eexpEndpost(empexperienceRequestBean.getEndpost())
					.eexpEndreportingto(empexperienceRequestBean.getEndreportingto())
					.eexpIpaddress(empexperienceRequestBean.getIpaddress())
					.eexpJobprofile(empexperienceRequestBean.getJobprofile())
					.eexpMachinename(empexperienceRequestBean.getMachinename())
					.eexpModifiedon(LocalDateTime.now())
					.eexpModule(empexperienceRequestBean.getModule())
					.eexpReasonforleaving(empexperienceRequestBean.getReasonforleaving())
					.eexpSite(GenericAuditContextHolder.getContext().getSite())
					.eexpStartgrade(empexperienceRequestBean.getStartgrade())
					.eexpStartgrosspermth(Objects.nonNull(empexperienceRequestBean.getStartgrosspermth()) ? empexperienceRequestBean.getStartgrosspermth() : BigInteger.ZERO.doubleValue())
					.eexpStartlevel(empexperienceRequestBean.getStartlevel())
					.eexpStartpackage(Objects.nonNull(empexperienceRequestBean.getStartpackage()) ? empexperienceRequestBean.getStartpackage() : BigInteger.ZERO.doubleValue())
					.eexpStartpost(empexperienceRequestBean.getStartpost())
					.eexpStartreportingto(empexperienceRequestBean.getStartreportingto())
					.eexpTotalservice(Objects.nonNull(empexperienceRequestBean.getTotalservice()) ? empexperienceRequestBean.getTotalservice() : BigInteger.ZERO.intValue())
					.eexpUserid(GenericAuditContextHolder.getContext().getUserid())
					.eexpWorkedfrom(Objects.nonNull(empexperienceRequestBean.getWorkedfrom()) ? LocalDate.parse(empexperienceRequestBean.getWorkedfrom(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.eexpWorkedupto(Objects.nonNull(empexperienceRequestBean.getWorkedupto()) ? LocalDate.parse(empexperienceRequestBean.getWorkedupto(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		
.build();
}).collect(Collectors.toList());
} ;
	public static BiFunction<Empexperience, EmpexperienceRequestBean, Empexperience> updateEmpexperienceEntityPojoMapper = (empexperienceEntity, empexperienceRequestBean) -> {
		empexperienceEntity.getEmpexperienceCK().setEexpEmpcode(Objects.nonNull(empexperienceRequestBean.getEmpcode()) ? empexperienceRequestBean.getEmpcode().trim() : empexperienceEntity.getEmpexperienceCK().getEexpEmpcode());
		empexperienceEntity.getEmpexperienceCK().setEexpJobsrno(Objects.nonNull(empexperienceRequestBean.getJobsrno()) ? empexperienceRequestBean.getJobsrno() : empexperienceEntity.getEmpexperienceCK().getEexpJobsrno());
		empexperienceEntity.setEexpCompanyname(Objects.nonNull(empexperienceRequestBean.getCompanyname()) ? empexperienceRequestBean.getCompanyname().trim() : empexperienceEntity.getEexpCompanyname());
		empexperienceEntity.setEexpEndgrade(Objects.nonNull(empexperienceRequestBean.getEndgrade()) ? empexperienceRequestBean.getEndgrade().trim() : empexperienceEntity.getEexpEndgrade());
		empexperienceEntity.setEexpEndgrosspermth(Objects.nonNull(empexperienceRequestBean.getEndgrosspermth()) ? empexperienceRequestBean.getEndgrosspermth() : empexperienceEntity.getEexpEndgrosspermth());
		empexperienceEntity.setEexpEndlevel(Objects.nonNull(empexperienceRequestBean.getEndlevel()) ? empexperienceRequestBean.getEndlevel().trim() : empexperienceEntity.getEexpEndlevel());
		empexperienceEntity.setEexpEndpackage(Objects.nonNull(empexperienceRequestBean.getEndpackage()) ? empexperienceRequestBean.getEndpackage() : empexperienceEntity.getEexpEndpackage());
		empexperienceEntity.setEexpEndpost(Objects.nonNull(empexperienceRequestBean.getEndpost()) ? empexperienceRequestBean.getEndpost().trim() : empexperienceEntity.getEexpEndpost());
		empexperienceEntity.setEexpEndreportingto(Objects.nonNull(empexperienceRequestBean.getEndreportingto()) ? empexperienceRequestBean.getEndreportingto().trim() : empexperienceEntity.getEexpEndreportingto());
		empexperienceEntity.setEexpIpaddress(Objects.nonNull(empexperienceRequestBean.getIpaddress()) ? empexperienceRequestBean.getIpaddress().trim() : empexperienceEntity.getEexpIpaddress());
		empexperienceEntity.setEexpJobprofile(Objects.nonNull(empexperienceRequestBean.getJobprofile()) ? empexperienceRequestBean.getJobprofile().trim() : empexperienceEntity.getEexpJobprofile());
		empexperienceEntity.setEexpMachinename(Objects.nonNull(empexperienceRequestBean.getMachinename()) ? empexperienceRequestBean.getMachinename().trim() : empexperienceEntity.getEexpMachinename());
		empexperienceEntity.setEexpModifiedon(LocalDateTime.now()) ; 
		empexperienceEntity.setEexpModule(Objects.nonNull(empexperienceRequestBean.getModule()) ? empexperienceRequestBean.getModule().trim() : empexperienceEntity.getEexpModule());
		empexperienceEntity.setEexpReasonforleaving(Objects.nonNull(empexperienceRequestBean.getReasonforleaving()) ? empexperienceRequestBean.getReasonforleaving().trim() : empexperienceEntity.getEexpReasonforleaving());
		empexperienceEntity.setEexpSite(GenericAuditContextHolder.getContext().getSite()) ; 
		empexperienceEntity.setEexpStartgrade(Objects.nonNull(empexperienceRequestBean.getStartgrade()) ? empexperienceRequestBean.getStartgrade().trim() : empexperienceEntity.getEexpStartgrade());
		empexperienceEntity.setEexpStartgrosspermth(Objects.nonNull(empexperienceRequestBean.getStartgrosspermth()) ? empexperienceRequestBean.getStartgrosspermth() : empexperienceEntity.getEexpStartgrosspermth());
		empexperienceEntity.setEexpStartlevel(Objects.nonNull(empexperienceRequestBean.getStartlevel()) ? empexperienceRequestBean.getStartlevel().trim() : empexperienceEntity.getEexpStartlevel());
		empexperienceEntity.setEexpStartpackage(Objects.nonNull(empexperienceRequestBean.getStartpackage()) ? empexperienceRequestBean.getStartpackage() : empexperienceEntity.getEexpStartpackage());
		empexperienceEntity.setEexpStartpost(Objects.nonNull(empexperienceRequestBean.getStartpost()) ? empexperienceRequestBean.getStartpost().trim() : empexperienceEntity.getEexpStartpost());
		empexperienceEntity.setEexpStartreportingto(Objects.nonNull(empexperienceRequestBean.getStartreportingto()) ? empexperienceRequestBean.getStartreportingto().trim() : empexperienceEntity.getEexpStartreportingto());
		empexperienceEntity.setEexpTotalservice(Objects.nonNull(empexperienceRequestBean.getTotalservice()) ? empexperienceRequestBean.getTotalservice() : empexperienceEntity.getEexpTotalservice());
		empexperienceEntity.setEexpUserid(GenericAuditContextHolder.getContext().getUserid()) ; 
		empexperienceEntity.setEexpWorkedfrom(Objects.nonNull(empexperienceRequestBean.getWorkedfrom()) ? LocalDate.parse(empexperienceRequestBean.getWorkedfrom(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empexperienceEntity.getEexpWorkedfrom());
		empexperienceEntity.setEexpWorkedupto(Objects.nonNull(empexperienceRequestBean.getWorkedupto()) ? LocalDate.parse(empexperienceRequestBean.getWorkedupto(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empexperienceEntity.getEexpWorkedupto());


		return empexperienceEntity;
	};

}
