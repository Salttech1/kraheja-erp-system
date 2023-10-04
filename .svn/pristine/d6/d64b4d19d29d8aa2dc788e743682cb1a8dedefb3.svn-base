// Developed By  - 	kalpana.m
// Developed on - 25-07-23
// Mode  - Data Entry
// Purpose - Empeducation Entry / Edit
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
import kraheja.payroll.bean.request.EmpeducationRequestBean;
import kraheja.payroll.bean.response.EmpeducationResponseBean;
import kraheja.payroll.bean.response.EmpeducationResponseBean.EmpeducationResponseBeanBuilder;
import java.util.stream.Collectors;
import kraheja.payroll.entity.Empeducation;
import kraheja.payroll.entity.EmpeducationCK;

public interface EmpeducationEntityPojoMapper {
	@SuppressWarnings("unchecked")
public static Function	<List<Empeducation>, List<EmpeducationResponseBean>> fetchEmpeducationEntityPojoMapper = empeducationEntityList -> {
return empeducationEntityList.stream().map(empeducationEntity -> {
return EmpeducationResponseBean.builder()
.empcode(empeducationEntity.getEmpeducationCK().getEeduEmpcode())
					.educsrno(empeducationEntity.getEmpeducationCK().getEeduEducsrno())
					.attendedfromdate(Objects.nonNull(empeducationEntity.getEeduAttendedfromdate()) ? empeducationEntity.getEeduAttendedfromdate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.attendedtodate(Objects.nonNull(empeducationEntity.getEeduAttendedtodate()) ? empeducationEntity.getEeduAttendedtodate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.educlass(empeducationEntity.getEeduClass())
					.coursename(empeducationEntity.getEeduCoursename())
					.degree(empeducationEntity.getEeduDegree())
					.institute(empeducationEntity.getEeduInstitute())
					.ipaddress(empeducationEntity.getEeduIpaddress())
					.machinename(empeducationEntity.getEeduMachinename())
					.mainsubjetcs(empeducationEntity.getEeduMainsubjetcs())
					.modifiedon(empeducationEntity.getEeduModifiedon())
					.module(empeducationEntity.getEeduModule())
					.percofmarks(empeducationEntity.getEeduPercofmarks())
					.site(empeducationEntity.getEeduSite())
					.userid(empeducationEntity.getEeduUserid())
.build(); 
}).collect(Collectors.toList());

};


	public static Function<List<EmpeducationRequestBean>, List <Empeducation>> addEmpeducationPojoEntityMapper = (empeducationRequestBeanList) -> { 
return empeducationRequestBeanList.stream().map(empeducationRequestBean -> {
return Empeducation.builder().empeducationCK(EmpeducationCK.builder()
					.eeduEmpcode(empeducationRequestBean.getEmpcode())
					.eeduEducsrno(Objects.nonNull(empeducationRequestBean.getEducsrno()) ? empeducationRequestBean.getEducsrno() : BigInteger.ZERO.intValue())
		.build())
					.eeduAttendedfromdate(Objects.nonNull(empeducationRequestBean.getAttendedfromdate()) ? LocalDate.parse(empeducationRequestBean.getAttendedfromdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.eeduAttendedtodate(Objects.nonNull(empeducationRequestBean.getAttendedtodate()) ? LocalDate.parse(empeducationRequestBean.getAttendedtodate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.eeduClass(empeducationRequestBean.getEduclass())
					.eeduCoursename(empeducationRequestBean.getCoursename())
					.eeduDegree(empeducationRequestBean.getDegree())
					.eeduInstitute(empeducationRequestBean.getInstitute())
					.eeduIpaddress(empeducationRequestBean.getIpaddress())
					.eeduMachinename(empeducationRequestBean.getMachinename())
					.eeduMainsubjetcs(empeducationRequestBean.getMainsubjetcs())
					.eeduModifiedon(LocalDateTime.now())
					.eeduModule(empeducationRequestBean.getModule())
					.eeduPercofmarks(Objects.nonNull(empeducationRequestBean.getPercofmarks()) ? empeducationRequestBean.getPercofmarks() : BigInteger.ZERO.intValue())
					.eeduSite(GenericAuditContextHolder.getContext().getSite())
					.eeduUserid(GenericAuditContextHolder.getContext().getUserid())
		
.build();
}).collect(Collectors.toList());
} ;
	public static BiFunction<Empeducation, EmpeducationRequestBean, Empeducation> updateEmpeducationEntityPojoMapper = (empeducationEntity, empeducationRequestBean) -> {
		empeducationEntity.getEmpeducationCK().setEeduEmpcode(Objects.nonNull(empeducationRequestBean.getEmpcode()) ? empeducationRequestBean.getEmpcode().trim() : empeducationEntity.getEmpeducationCK().getEeduEmpcode());
		empeducationEntity.getEmpeducationCK().setEeduEducsrno(Objects.nonNull(empeducationRequestBean.getEducsrno()) ? empeducationRequestBean.getEducsrno() : empeducationEntity.getEmpeducationCK().getEeduEducsrno());
		empeducationEntity.setEeduAttendedfromdate(Objects.nonNull(empeducationRequestBean.getAttendedfromdate()) ? LocalDate.parse(empeducationRequestBean.getAttendedfromdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empeducationEntity.getEeduAttendedfromdate());
		empeducationEntity.setEeduAttendedtodate(Objects.nonNull(empeducationRequestBean.getAttendedtodate()) ? LocalDate.parse(empeducationRequestBean.getAttendedtodate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : empeducationEntity.getEeduAttendedtodate());
		empeducationEntity.setEeduClass(Objects.nonNull(empeducationRequestBean.getClass()) ? empeducationRequestBean.getEduclass().trim() : empeducationEntity.getEeduClass());
		empeducationEntity.setEeduCoursename(Objects.nonNull(empeducationRequestBean.getCoursename()) ? empeducationRequestBean.getCoursename().trim() : empeducationEntity.getEeduCoursename());
		empeducationEntity.setEeduDegree(Objects.nonNull(empeducationRequestBean.getDegree()) ? empeducationRequestBean.getDegree().trim() : empeducationEntity.getEeduDegree());
		empeducationEntity.setEeduInstitute(Objects.nonNull(empeducationRequestBean.getInstitute()) ? empeducationRequestBean.getInstitute().trim() : empeducationEntity.getEeduInstitute());
		empeducationEntity.setEeduIpaddress(Objects.nonNull(empeducationRequestBean.getIpaddress()) ? empeducationRequestBean.getIpaddress().trim() : empeducationEntity.getEeduIpaddress());
		empeducationEntity.setEeduMachinename(Objects.nonNull(empeducationRequestBean.getMachinename()) ? empeducationRequestBean.getMachinename().trim() : empeducationEntity.getEeduMachinename());
		empeducationEntity.setEeduMainsubjetcs(Objects.nonNull(empeducationRequestBean.getMainsubjetcs()) ? empeducationRequestBean.getMainsubjetcs().trim() : empeducationEntity.getEeduMainsubjetcs());
		empeducationEntity.setEeduModifiedon(LocalDateTime.now()) ; 
		empeducationEntity.setEeduModule(Objects.nonNull(empeducationRequestBean.getModule()) ? empeducationRequestBean.getModule().trim() : empeducationEntity.getEeduModule());
		empeducationEntity.setEeduPercofmarks(Objects.nonNull(empeducationRequestBean.getPercofmarks()) ? empeducationRequestBean.getPercofmarks() : empeducationEntity.getEeduPercofmarks());
		empeducationEntity.setEeduSite(GenericAuditContextHolder.getContext().getSite()) ; 
		empeducationEntity.setEeduUserid(GenericAuditContextHolder.getContext().getUserid()) ; 


		return empeducationEntity;
	};

}
