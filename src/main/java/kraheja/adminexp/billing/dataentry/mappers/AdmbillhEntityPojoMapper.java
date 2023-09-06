package kraheja.adminexp.billing.dataentry.mappers;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.apache.commons.collections4.CollectionUtils;

import kraheja.adminexp.billing.dataentry.bean.request.AdmbillhRequestBean;
import kraheja.adminexp.billing.dataentry.bean.response.AdmbillhResponseBean;
import kraheja.adminexp.billing.dataentry.bean.response.AdmbillhResponseBean.AdmbillhResponseBeanBuilder;
import kraheja.adminexp.billing.dataentry.entity.Admbilld;
import kraheja.adminexp.billing.dataentry.entity.Admbillh;
import kraheja.adminexp.billing.dataentry.entity.AdmbillhCK;
import kraheja.commons.entity.Address;
import kraheja.commons.entity.Party;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.mappers.pojoentity.PartyMapper;
import kraheja.commons.mappers.pojoentity.AddressMapper.AddressEntityPojoMapper;
import kraheja.commons.utils.CommonConstraints;

public interface AdmbillhEntityPojoMapper {
	@SuppressWarnings("unchecked")
	public static Function<Object[], List<AdmbillhResponseBean>> fetchAdmbillhEntityPojoMapper = objectArray -> {
		AdmbillhResponseBeanBuilder admbillhBeanBuilder = AdmbillhResponseBean.builder();
		List<Admbillh> admbillhEntityList = (List<Admbillh>) (Objects.nonNull(objectArray[BigInteger.ZERO.intValue()])
				? objectArray[BigInteger.ZERO.intValue()]
						: null);
		Party partyEntity = (Party) (Objects.nonNull(objectArray[BigInteger.ONE.intValue()])
				? objectArray[BigInteger.ONE.intValue()]
						: null);
		Address addressEntity = (Address) (Objects.nonNull(objectArray[CommonConstraints.INSTANCE.TWO.intValue()])
				? objectArray[CommonConstraints.INSTANCE.TWO.intValue()]
						: null);
		
		List<Admbilld> admbilldEntityList = (List<Admbilld>) (Objects.nonNull(objectArray[CommonConstraints.INSTANCE.THREE.intValue()])
				? objectArray[CommonConstraints.INSTANCE.THREE.intValue()]
						: null);
		
		return admbillhEntityList.stream().map(admbillhEntity -> {
			admbillhBeanBuilder.ser(admbillhEntity.getAdmbillhCK().getAdblhSer())
			.acmajor(admbillhEntity.getAdblhAcmajor())
			.acminor(admbillhEntity.getAdblhAcminor())
			.actranser(admbillhEntity.getAdblhActranser())
			.advnadjust(admbillhEntity.getAdblhAdvnadjust())
			.billamount(admbillhEntity.getAdblhBillamount())
			.billtype(admbillhEntity.getAdblhBilltype())
			.bldgcode(admbillhEntity.getAdblhBldgcode())
			.clearacdate(Objects.nonNull(admbillhEntity.getAdblhClearacdate()) ? admbillhEntity.getAdblhClearacdate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.clearacperson(admbillhEntity.getAdblhClearacperson())
			.coy(admbillhEntity.getAdblhCoy())
			.debitamt(admbillhEntity.getAdblhDebitamt())
			.exptype(admbillhEntity.getAdblhExptype())
			.fotoamount(admbillhEntity.getAdblhFotoamount())
			.fromdate(Objects.nonNull(admbillhEntity.getAdblhFromdate()) ? admbillhEntity.getAdblhFromdate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.gstrevchgyn(admbillhEntity.getAdblhGstrevchgyn())
			.mintype(admbillhEntity.getAdblhMintype())
			.narration(admbillhEntity.getAdblhNarration())
			.orderedby(admbillhEntity.getAdblhOrderedby())
			.origsite(admbillhEntity.getAdblhOrigsite())
			.origsys(admbillhEntity.getAdblhOrigsys())
			.paidamount(admbillhEntity.getAdblhPaidamount())
			.paiddate(Objects.nonNull(admbillhEntity.getAdblhPaiddate()) ? admbillhEntity.getAdblhPaiddate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.paidref(admbillhEntity.getAdblhPaidref())
			.partycode(admbillhEntity.getAdblhPartycode())
			.partytype(admbillhEntity.getAdblhPartytype())
			.passedon(Objects.nonNull(admbillhEntity.getAdblhPassedon()) ? admbillhEntity.getAdblhPassedon().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.project(admbillhEntity.getAdblhProject())
			.rundate(Objects.nonNull(admbillhEntity.getAdblhRundate()) ? admbillhEntity.getAdblhRundate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.site(admbillhEntity.getAdblhSite())
			.status(admbillhEntity.getAdblhStatus())
			.sunclass(admbillhEntity.getAdblhSunclass())
			.sunid(admbillhEntity.getAdblhSunid())
			.suppbilldt(Objects.nonNull(admbillhEntity.getAdblhSuppbilldt()) ? admbillhEntity.getAdblhSuppbilldt().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.suppbillno(admbillhEntity.getAdblhSuppbillno())
			.tdsacmajor(admbillhEntity.getAdblhTdsacmajor())
			.tdsamount(admbillhEntity.getAdblhTdsamount())
			.tdsperc(admbillhEntity.getAdblhTdsperc())
			.todate(Objects.nonNull(admbillhEntity.getAdblhTodate()) ? admbillhEntity.getAdblhTodate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
			.today(admbillhEntity.getAdblhToday())
			.userid(admbillhEntity.getAdblhUserid())
			;
			if(Objects.nonNull(partyEntity)) {
				admbillhBeanBuilder.partyResponseBean(PartyMapper.fetchPartyEntityPojoMapper.apply(new Object[] {partyEntity}));
			}
			if(CollectionUtils.isNotEmpty(admbilldEntityList)) {
				admbillhBeanBuilder.admbilldResponseBeanList(AdmbilldEntityPojoMapper.fetchAdmbilldEntityPojoMapper.apply(new Object[] {admbilldEntityList}));
			}
			if(Objects.nonNull(addressEntity)) {
				admbillhBeanBuilder.addressResponseBean(AddressEntityPojoMapper.fetchAddressEntityPojoMapper.apply(new Object[] {addressEntity}));
			}
			return admbillhBeanBuilder.build();
		}).collect(Collectors.toList());
	};

	public static Function<Object[],List <Admbillh>> addAdmbillhEntityPojoMapper = objectArray -> {
		@SuppressWarnings("unchecked")
		List <AdmbillhRequestBean> admbillhRequestBeanList = (List <AdmbillhRequestBean>) (Objects
				.nonNull(objectArray[BigInteger.ZERO.intValue()]) ? objectArray[BigInteger.ZERO.intValue()] : null);
		String ser = (String) (Objects.nonNull(objectArray[BigInteger.ONE.intValue()])
				? objectArray[BigInteger.ONE.intValue()]
: null);
		return  admbillhRequestBeanList.stream().map(admbillhRequestBean ->{
			return  Admbillh.builder()
		.admbillhCK(AdmbillhCK.builder()
				.adblhSer(ser)
				.build())
		.adblhAcmajor(admbillhRequestBean.getAcmajor())
		.adblhAcminor(admbillhRequestBean.getAcminor())
		.adblhActranser(admbillhRequestBean.getActranser())
		.adblhAdvnadjust(admbillhRequestBean.getAdvnadjust())
		.adblhBillamount(admbillhRequestBean.getBillamount())
		.adblhBilltype(admbillhRequestBean.getBilltype())
		.adblhBldgcode(admbillhRequestBean.getBldgcode())
		.adblhClearacdate(Objects.nonNull(admbillhRequestBean.getClearacdate()) ? LocalDate.parse(admbillhRequestBean.getClearacdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhClearacperson(admbillhRequestBean.getClearacperson())
		.adblhCoy(admbillhRequestBean.getCoy())
		.adblhDebitamt(admbillhRequestBean.getDebitamt())
		.adblhExptype(admbillhRequestBean.getExptype())
		.adblhFotoamount(admbillhRequestBean.getFotoamount())
		.adblhFromdate(Objects.nonNull(admbillhRequestBean.getFromdate()) ? LocalDate.parse(admbillhRequestBean.getFromdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhGstrevchgyn(admbillhRequestBean.getGstrevchgyn())
		.adblhMintype(admbillhRequestBean.getMintype())
		.adblhNarration(admbillhRequestBean.getNarration())
		.adblhOrderedby(admbillhRequestBean.getOrderedby())
		.adblhOrigsite(admbillhRequestBean.getOrigsite())
		.adblhOrigsys(admbillhRequestBean.getOrigsys())
		.adblhPaidamount(admbillhRequestBean.getPaidamount())
		.adblhPaiddate(Objects.nonNull(admbillhRequestBean.getPaiddate()) ? LocalDate.parse(admbillhRequestBean.getPaiddate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhPaidref(admbillhRequestBean.getPaidref())
		.adblhPartycode(admbillhRequestBean.getPartycode())
		.adblhPartytype(admbillhRequestBean.getPartytype())
		.adblhPassedon(Objects.nonNull(admbillhRequestBean.getPassedon()) ? LocalDate.parse(admbillhRequestBean.getPassedon(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhProject(admbillhRequestBean.getProject())
		.adblhRundate(Objects.nonNull(admbillhRequestBean.getRundate()) ? LocalDate.parse(admbillhRequestBean.getRundate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhSite(GenericAuditContextHolder.getContext().getSite())
		.adblhStatus(admbillhRequestBean.getStatus())
		.adblhSunclass(admbillhRequestBean.getSunclass())
		.adblhSunid(admbillhRequestBean.getSunid())
		.adblhSuppbilldt(Objects.nonNull(admbillhRequestBean.getSuppbilldt()) ? LocalDate.parse(admbillhRequestBean.getSuppbilldt(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhSuppbillno(admbillhRequestBean.getSuppbillno())
		.adblhTdsacmajor(admbillhRequestBean.getTdsacmajor())
		.adblhTdsamount(admbillhRequestBean.getTdsamount())
		.adblhTdsperc(admbillhRequestBean.getTdsperc())
		.adblhTodate(Objects.nonNull(admbillhRequestBean.getTodate()) ? LocalDate.parse(admbillhRequestBean.getTodate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER): null)
		.adblhToday(LocalDateTime.now())
		.adblhUserid(GenericAuditContextHolder.getContext().getUserid())
		.build()
		;
	}).collect(Collectors.toList());
};

	public static BiFunction<Admbillh, AdmbillhRequestBean, Admbillh> updateAdmbillhEntityPojoMapper = (admbillhEntity, admbillhRequestBean) -> {
		admbillhEntity.getAdmbillhCK().setAdblhSer(Objects.nonNull(admbillhRequestBean.getSer()) ? admbillhRequestBean.getSer().trim() : admbillhEntity.getAdmbillhCK().getAdblhSer());
		admbillhEntity.setAdblhAcmajor(Objects.nonNull(admbillhRequestBean.getAcmajor()) ? admbillhRequestBean.getAcmajor().trim() : admbillhEntity.getAdblhAcmajor());
		admbillhEntity.setAdblhAcminor(Objects.nonNull(admbillhRequestBean.getAcminor()) ? admbillhRequestBean.getAcminor().trim() : admbillhEntity.getAdblhAcminor());
		admbillhEntity.setAdblhActranser(Objects.nonNull(admbillhRequestBean.getActranser()) ? admbillhRequestBean.getActranser().trim() : admbillhEntity.getAdblhActranser());
		admbillhEntity.setAdblhAdvnadjust(Objects.nonNull(admbillhRequestBean.getAdvnadjust()) ? admbillhRequestBean.getAdvnadjust() : admbillhEntity.getAdblhAdvnadjust());
		admbillhEntity.setAdblhBillamount(Objects.nonNull(admbillhRequestBean.getBillamount()) ? admbillhRequestBean.getBillamount() : admbillhEntity.getAdblhBillamount());
		admbillhEntity.setAdblhBilltype(Objects.nonNull(admbillhRequestBean.getBilltype()) ? admbillhRequestBean.getBilltype().trim() : admbillhEntity.getAdblhBilltype());
		admbillhEntity.setAdblhBldgcode(Objects.nonNull(admbillhRequestBean.getBldgcode()) ? admbillhRequestBean.getBldgcode().trim() : admbillhEntity.getAdblhBldgcode());
		admbillhEntity.setAdblhClearacdate(Objects.nonNull(admbillhRequestBean.getClearacdate()) ? LocalDate.parse(admbillhRequestBean.getClearacdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhClearacdate());
		admbillhEntity.setAdblhClearacperson(Objects.nonNull(admbillhRequestBean.getClearacperson()) ? admbillhRequestBean.getClearacperson().trim() : admbillhEntity.getAdblhClearacperson());
		admbillhEntity.setAdblhCoy(Objects.nonNull(admbillhRequestBean.getCoy()) ? admbillhRequestBean.getCoy().trim() : admbillhEntity.getAdblhCoy());
		admbillhEntity.setAdblhDebitamt(Objects.nonNull(admbillhRequestBean.getDebitamt()) ? admbillhRequestBean.getDebitamt() : admbillhEntity.getAdblhDebitamt());
		admbillhEntity.setAdblhExptype(Objects.nonNull(admbillhRequestBean.getExptype()) ? admbillhRequestBean.getExptype().trim() : admbillhEntity.getAdblhExptype());
		admbillhEntity.setAdblhFotoamount(Objects.nonNull(admbillhRequestBean.getFotoamount()) ? admbillhRequestBean.getFotoamount() : admbillhEntity.getAdblhFotoamount());
		admbillhEntity.setAdblhFromdate(Objects.nonNull(admbillhRequestBean.getFromdate()) ? LocalDate.parse(admbillhRequestBean.getFromdate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhFromdate());
		admbillhEntity.setAdblhGstrevchgyn(Objects.nonNull(admbillhRequestBean.getGstrevchgyn()) ? admbillhRequestBean.getGstrevchgyn().trim() : admbillhEntity.getAdblhGstrevchgyn());
		admbillhEntity.setAdblhMintype(Objects.nonNull(admbillhRequestBean.getMintype()) ? admbillhRequestBean.getMintype().trim() : admbillhEntity.getAdblhMintype());
		admbillhEntity.setAdblhNarration(Objects.nonNull(admbillhRequestBean.getNarration()) ? admbillhRequestBean.getNarration().trim() : admbillhEntity.getAdblhNarration());
		admbillhEntity.setAdblhOrderedby(Objects.nonNull(admbillhRequestBean.getOrderedby()) ? admbillhRequestBean.getOrderedby().trim() : admbillhEntity.getAdblhOrderedby());
		admbillhEntity.setAdblhOrigsite(Objects.nonNull(admbillhRequestBean.getOrigsite()) ? admbillhRequestBean.getOrigsite().trim() : admbillhEntity.getAdblhOrigsite());
		admbillhEntity.setAdblhOrigsys(Objects.nonNull(admbillhRequestBean.getOrigsys()) ? admbillhRequestBean.getOrigsys().trim() : admbillhEntity.getAdblhOrigsys());
		admbillhEntity.setAdblhPaidamount(Objects.nonNull(admbillhRequestBean.getPaidamount()) ? admbillhRequestBean.getPaidamount() : admbillhEntity.getAdblhPaidamount());
		admbillhEntity.setAdblhPaiddate(Objects.nonNull(admbillhRequestBean.getPaiddate()) ? LocalDate.parse(admbillhRequestBean.getPaiddate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhPaiddate());
		admbillhEntity.setAdblhPaidref(Objects.nonNull(admbillhRequestBean.getPaidref()) ? admbillhRequestBean.getPaidref().trim() : admbillhEntity.getAdblhPaidref());
		admbillhEntity.setAdblhPartycode(Objects.nonNull(admbillhRequestBean.getPartycode()) ? admbillhRequestBean.getPartycode().trim() : admbillhEntity.getAdblhPartycode());
		admbillhEntity.setAdblhPartytype(Objects.nonNull(admbillhRequestBean.getPartytype()) ? admbillhRequestBean.getPartytype().trim() : admbillhEntity.getAdblhPartytype());
		admbillhEntity.setAdblhPassedon(Objects.nonNull(admbillhRequestBean.getPassedon()) ? LocalDate.parse(admbillhRequestBean.getPassedon(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhPassedon());
		admbillhEntity.setAdblhProject(Objects.nonNull(admbillhRequestBean.getProject()) ? admbillhRequestBean.getProject().trim() : admbillhEntity.getAdblhProject());
		admbillhEntity.setAdblhRundate(Objects.nonNull(admbillhRequestBean.getRundate()) ? LocalDate.parse(admbillhRequestBean.getRundate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhRundate());
		//admbillhEntity.setAdblhSite(Objects.nonNull(admbillhRequestBean.getSite()) ? admbillhRequestBean.getSite().trim() : admbillhEntity.getAdblhSite());
		admbillhEntity.setAdblhSite(GenericAuditContextHolder.getContext().getSite());
		admbillhEntity.setAdblhStatus(Objects.nonNull(admbillhRequestBean.getStatus()) ? admbillhRequestBean.getStatus().trim() : admbillhEntity.getAdblhStatus());
		admbillhEntity.setAdblhSunclass(Objects.nonNull(admbillhRequestBean.getSunclass()) ? admbillhRequestBean.getSunclass().trim() : admbillhEntity.getAdblhSunclass());
		admbillhEntity.setAdblhSunid(Objects.nonNull(admbillhRequestBean.getSunid()) ? admbillhRequestBean.getSunid().trim() : admbillhEntity.getAdblhSunid());
		admbillhEntity.setAdblhSuppbilldt(Objects.nonNull(admbillhRequestBean.getSuppbilldt()) ? LocalDate.parse(admbillhRequestBean.getSuppbilldt(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhSuppbilldt());
		admbillhEntity.setAdblhSuppbillno(Objects.nonNull(admbillhRequestBean.getSuppbillno()) ? admbillhRequestBean.getSuppbillno().trim() : admbillhEntity.getAdblhSuppbillno());
		admbillhEntity.setAdblhTdsacmajor(Objects.nonNull(admbillhRequestBean.getTdsacmajor()) ? admbillhRequestBean.getTdsacmajor().trim() : admbillhEntity.getAdblhTdsacmajor());
		admbillhEntity.setAdblhTdsamount(Objects.nonNull(admbillhRequestBean.getTdsamount()) ? admbillhRequestBean.getTdsamount() : admbillhEntity.getAdblhTdsamount());
		admbillhEntity.setAdblhTdsperc(Objects.nonNull(admbillhRequestBean.getTdsperc()) ? admbillhRequestBean.getTdsperc() : admbillhEntity.getAdblhTdsperc());
		admbillhEntity.setAdblhTodate(Objects.nonNull(admbillhRequestBean.getTodate()) ? LocalDate.parse(admbillhRequestBean.getTodate(), CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER) : admbillhEntity.getAdblhTodate());
		admbillhEntity.setAdblhToday(Objects.nonNull(admbillhRequestBean.getToday()) ? admbillhRequestBean.getToday() : admbillhEntity.getAdblhToday());
		//admbillhEntity.setAdblhUserid(Objects.nonNull(admbillhRequestBean.getUserid()) ? admbillhRequestBean.getUserid().trim() : admbillhEntity.getAdblhUserid());
		admbillhEntity.setAdblhUserid(GenericAuditContextHolder.getContext().getUserid());


		return admbillhEntity;
	};

}
