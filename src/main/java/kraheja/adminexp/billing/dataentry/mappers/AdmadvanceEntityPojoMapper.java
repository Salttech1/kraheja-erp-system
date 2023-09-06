package kraheja.adminexp.billing.dataentry.mappers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.apache.commons.collections4.CollectionUtils;

import kraheja.commons.entity.Party;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.mappers.pojoentity.PartyMapper;
import kraheja.commons.utils.CommonConstraints;
import kraheja.adminexp.billing.dataentry.bean.request.AdmadvanceRequestBean;
import kraheja.adminexp.billing.dataentry.bean.response.AdmadvanceResponseBean;
import kraheja.adminexp.billing.dataentry.bean.response.AdmadvanceResponseBean.AdmadvanceResponseBeanBuilder;
import java.util.stream.Collectors;
import kraheja.adminexp.billing.dataentry.entity.Admadvance;
import kraheja.adminexp.billing.dataentry.entity.AdmadvanceCK;

public interface AdmadvanceEntityPojoMapper {
	@SuppressWarnings("unchecked")
public static Function	<Object[], List<AdmadvanceResponseBean>> fetchAdmadvanceEntityPojoMapper = objectArray -> {
	AdmadvanceResponseBeanBuilder admadvanceBeanBuilder = AdmadvanceResponseBean.builder();
	List<Admadvance> admadvanceEntityList = (List<Admadvance>) (Objects.nonNull(objectArray[BigInteger.ZERO.intValue()])
			? objectArray[BigInteger.ZERO.intValue()] : null);
	Party partyEntity = (Party) (Objects.nonNull(objectArray[BigInteger.ONE.intValue()])
			? objectArray[BigInteger.ONE.intValue()]
					: null);
return admadvanceEntityList.stream().map(admadvanceEntity -> {
return AdmadvanceResponseBean.builder()
.ser(admadvanceEntity.getAdmadvanceCK().getAdvnSer())
					.actranser(admadvanceEntity.getAdvnActranser())
					.advanceamt(admadvanceEntity.getAdvnAdvanceamt())
					.basicamt(admadvanceEntity.getAdvnBasicamt())
					.bldgcode(admadvanceEntity.getAdvnBldgcode())
					.coy(admadvanceEntity.getAdvnCoy())
					.date(Objects.nonNull(admadvanceEntity.getAdvnDate()) ? admadvanceEntity.getAdvnDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.fotoamount(admadvanceEntity.getAdvnFotoamount())
					.gstamt(admadvanceEntity.getAdvnGstamt())
					.gstperc(admadvanceEntity.getAdvnGstperc())
					.narration(admadvanceEntity.getAdvnNarration())
					.orderby(admadvanceEntity.getAdvnOrderby())
					.origsite(admadvanceEntity.getAdvnOrigsite())
					.paidamount(admadvanceEntity.getAdvnPaidamount())
					.paiddate(Objects.nonNull(admadvanceEntity.getAdvnPaiddate()) ? admadvanceEntity.getAdvnPaiddate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.paidref(admadvanceEntity.getAdvnPaidref())
					.partycode(admadvanceEntity.getAdvnPartycode())
					.partytype(admadvanceEntity.getAdvnPartytype())
					.passedon(Objects.nonNull(admadvanceEntity.getAdvnPassedon()) ? admadvanceEntity.getAdvnPassedon().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.pinvdate(Objects.nonNull(admadvanceEntity.getAdvnPinvdate()) ? admadvanceEntity.getAdvnPinvdate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.pinvno(admadvanceEntity.getAdvnPinvno())
					.project(admadvanceEntity.getAdvnProject())
					.site(admadvanceEntity.getAdvnSite())
					.status(admadvanceEntity.getAdvnStatus())
					.tdsacmajor(admadvanceEntity.getAdvnTdsacmajor())
					.tdsamount(admadvanceEntity.getAdvnTdsamount())
					.tdsperc(admadvanceEntity.getAdvnTdsperc())
					.today(admadvanceEntity.getAdvnToday())
					.userid(admadvanceEntity.getAdvnUserid())
.build(); 
}).collect(Collectors.toList());


			//if(Objects.nonNull(partyEntity)) 
				//admadvanceResponseBean.partyResponseBean(PartyMapper.fetchPartyEntityPojoMapper.apply(new Object[] {partyEntity}));
};



	public static Function<Object[], List <Admadvance>> addAdmadvancePojoEntityMapper = objectArray -> { 
		@SuppressWarnings("unchecked")
		List<AdmadvanceRequestBean> admadvanceRequestBeanList = (List<AdmadvanceRequestBean>) (Objects
				.nonNull(objectArray[BigInteger.ZERO.intValue()]) ? objectArray[BigInteger.ZERO.intValue()] : null);
		String ser = (String) (Objects.nonNull(objectArray[BigInteger.ONE.intValue()])
				? objectArray[BigInteger.ONE.intValue()]
						:null);
return admadvanceRequestBeanList.stream().map(admadvanceRequestBean -> {
return Admadvance.builder().admadvanceCK(AdmadvanceCK.builder()
					.advnSer(admadvanceRequestBean.getSer())
		.build())

					.advnActranser(admadvanceRequestBean.getActranser())
					.advnAdvanceamt(Objects.nonNull(admadvanceRequestBean.getAdvanceamt()) ? admadvanceRequestBean.getAdvanceamt() : BigInteger.ZERO.doubleValue())
					.advnBasicamt(Objects.nonNull(admadvanceRequestBean.getBasicamt()) ? admadvanceRequestBean.getBasicamt() : BigInteger.ZERO.doubleValue())
					.advnBldgcode(admadvanceRequestBean.getBldgcode())
					.advnCoy(admadvanceRequestBean.getCoy())
					.advnDate(Objects.nonNull(admadvanceRequestBean.getDate()) ? LocalDate.parse(admadvanceRequestBean.getDate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.advnFotoamount(Objects.nonNull(admadvanceRequestBean.getFotoamount()) ? admadvanceRequestBean.getFotoamount() : BigInteger.ZERO.doubleValue())
					.advnGstamt(Objects.nonNull(admadvanceRequestBean.getGstamt()) ? admadvanceRequestBean.getGstamt() : BigInteger.ZERO.doubleValue())
					.advnGstperc(Objects.nonNull(admadvanceRequestBean.getGstperc()) ? admadvanceRequestBean.getGstperc() : BigInteger.ZERO.doubleValue())
					.advnNarration(admadvanceRequestBean.getNarration())
					.advnOrderby(admadvanceRequestBean.getOrderby())
					.advnOrigsite(GenericAuditContextHolder.getContext().getSite())
					.advnPaidamount(Objects.nonNull(admadvanceRequestBean.getPaidamount()) ? admadvanceRequestBean.getPaidamount() : BigInteger.ZERO.doubleValue())
					.advnPaiddate(Objects.nonNull(admadvanceRequestBean.getPaiddate()) ? LocalDate.parse(admadvanceRequestBean.getPaiddate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.advnPaidref(admadvanceRequestBean.getPaidref())
					.advnPartycode(admadvanceRequestBean.getPartycode())
					.advnPartytype(admadvanceRequestBean.getPartytype())
					.advnPassedon(Objects.nonNull(admadvanceRequestBean.getPassedon()) ? LocalDate.parse(admadvanceRequestBean.getPassedon(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.advnPinvdate(Objects.nonNull(admadvanceRequestBean.getPinvdate()) ? LocalDate.parse(admadvanceRequestBean.getPinvdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
					.advnPinvno(admadvanceRequestBean.getPinvno())
					.advnProject(admadvanceRequestBean.getProject())
					.advnSite(GenericAuditContextHolder.getContext().getSite())
					.advnStatus(admadvanceRequestBean.getStatus())
					.advnTdsacmajor(admadvanceRequestBean.getTdsacmajor())
					.advnTdsamount(Objects.nonNull(admadvanceRequestBean.getTdsamount()) ? admadvanceRequestBean.getTdsamount() : BigInteger.ZERO.doubleValue())
					.advnTdsperc(Objects.nonNull(admadvanceRequestBean.getTdsperc()) ? admadvanceRequestBean.getTdsperc() : BigInteger.ZERO.doubleValue())
					.advnToday(LocalDateTime.now())
					.advnUserid(GenericAuditContextHolder.getContext().getUserid())
		
.build();
}).collect(Collectors.toList());
} ;

	public static BiFunction<Admadvance, AdmadvanceRequestBean, Admadvance> updateAdmadvanceEntityPojoMapper = (admadvanceEntity, admadvanceRequestBean) -> {
		admadvanceEntity.getAdmadvanceCK().setAdvnSer(Objects.nonNull(admadvanceRequestBean.getSer()) ? admadvanceRequestBean.getSer().trim() : admadvanceEntity.getAdmadvanceCK().getAdvnSer());

		admadvanceEntity.setAdvnActranser(Objects.nonNull(admadvanceRequestBean.getActranser()) ? admadvanceRequestBean.getActranser().trim() : admadvanceEntity.getAdvnActranser());
		admadvanceEntity.setAdvnAdvanceamt(Objects.nonNull(admadvanceRequestBean.getAdvanceamt()) ? admadvanceRequestBean.getAdvanceamt() : admadvanceEntity.getAdvnAdvanceamt());
		admadvanceEntity.setAdvnBasicamt(Objects.nonNull(admadvanceRequestBean.getBasicamt()) ? admadvanceRequestBean.getBasicamt() : admadvanceEntity.getAdvnBasicamt());
		admadvanceEntity.setAdvnBldgcode(Objects.nonNull(admadvanceRequestBean.getBldgcode()) ? admadvanceRequestBean.getBldgcode().trim() : admadvanceEntity.getAdvnBldgcode());
		admadvanceEntity.setAdvnCoy(Objects.nonNull(admadvanceRequestBean.getCoy()) ? admadvanceRequestBean.getCoy().trim() : admadvanceEntity.getAdvnCoy());
		admadvanceEntity.setAdvnDate(Objects.nonNull(admadvanceRequestBean.getDate()) ? LocalDate.parse(admadvanceRequestBean.getDate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admadvanceEntity.getAdvnDate());
		admadvanceEntity.setAdvnFotoamount(Objects.nonNull(admadvanceRequestBean.getFotoamount()) ? admadvanceRequestBean.getFotoamount() : admadvanceEntity.getAdvnFotoamount());
		admadvanceEntity.setAdvnGstamt(Objects.nonNull(admadvanceRequestBean.getGstamt()) ? admadvanceRequestBean.getGstamt() : admadvanceEntity.getAdvnGstamt());
		admadvanceEntity.setAdvnGstperc(Objects.nonNull(admadvanceRequestBean.getGstperc()) ? admadvanceRequestBean.getGstperc() : admadvanceEntity.getAdvnGstperc());
		admadvanceEntity.setAdvnNarration(Objects.nonNull(admadvanceRequestBean.getNarration()) ? admadvanceRequestBean.getNarration().trim() : admadvanceEntity.getAdvnNarration());
		admadvanceEntity.setAdvnOrderby(Objects.nonNull(admadvanceRequestBean.getOrderby()) ? admadvanceRequestBean.getOrderby().trim() : admadvanceEntity.getAdvnOrderby());
		admadvanceEntity.setAdvnOrigsite(GenericAuditContextHolder.getContext().getSite()) ; 
		admadvanceEntity.setAdvnPaidamount(Objects.nonNull(admadvanceRequestBean.getPaidamount()) ? admadvanceRequestBean.getPaidamount() : admadvanceEntity.getAdvnPaidamount());
		admadvanceEntity.setAdvnPaiddate(Objects.nonNull(admadvanceRequestBean.getPaiddate()) ? LocalDate.parse(admadvanceRequestBean.getPaiddate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admadvanceEntity.getAdvnPaiddate());
		admadvanceEntity.setAdvnPaidref(Objects.nonNull(admadvanceRequestBean.getPaidref()) ? admadvanceRequestBean.getPaidref().trim() : admadvanceEntity.getAdvnPaidref());
		admadvanceEntity.setAdvnPartycode(Objects.nonNull(admadvanceRequestBean.getPartycode()) ? admadvanceRequestBean.getPartycode().trim() : admadvanceEntity.getAdvnPartycode());
		admadvanceEntity.setAdvnPartytype(Objects.nonNull(admadvanceRequestBean.getPartytype()) ? admadvanceRequestBean.getPartytype().trim() : admadvanceEntity.getAdvnPartytype());
		admadvanceEntity.setAdvnPassedon(Objects.nonNull(admadvanceRequestBean.getPassedon()) ? LocalDate.parse(admadvanceRequestBean.getPassedon(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admadvanceEntity.getAdvnPassedon());
		admadvanceEntity.setAdvnPinvdate(Objects.nonNull(admadvanceRequestBean.getPinvdate()) ? LocalDate.parse(admadvanceRequestBean.getPinvdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admadvanceEntity.getAdvnPinvdate());
		admadvanceEntity.setAdvnPinvno(Objects.nonNull(admadvanceRequestBean.getPinvno()) ? admadvanceRequestBean.getPinvno().trim() : admadvanceEntity.getAdvnPinvno());
		admadvanceEntity.setAdvnProject(Objects.nonNull(admadvanceRequestBean.getProject()) ? admadvanceRequestBean.getProject().trim() : admadvanceEntity.getAdvnProject());
		admadvanceEntity.setAdvnSite(GenericAuditContextHolder.getContext().getSite()) ; 
		admadvanceEntity.setAdvnStatus(Objects.nonNull(admadvanceRequestBean.getStatus()) ? admadvanceRequestBean.getStatus().trim() : admadvanceEntity.getAdvnStatus());
		admadvanceEntity.setAdvnTdsacmajor(Objects.nonNull(admadvanceRequestBean.getTdsacmajor()) ? admadvanceRequestBean.getTdsacmajor().trim() : admadvanceEntity.getAdvnTdsacmajor());
		admadvanceEntity.setAdvnTdsamount(Objects.nonNull(admadvanceRequestBean.getTdsamount()) ? admadvanceRequestBean.getTdsamount() : admadvanceEntity.getAdvnTdsamount());
		admadvanceEntity.setAdvnTdsperc(Objects.nonNull(admadvanceRequestBean.getTdsperc()) ? admadvanceRequestBean.getTdsperc() : admadvanceEntity.getAdvnTdsperc());
		admadvanceEntity.setAdvnToday(LocalDateTime.now()) ; 
		admadvanceEntity.setAdvnUserid(GenericAuditContextHolder.getContext().getUserid()) ; 


		return admadvanceEntity;
	};

}
