package kraheja.adminexp.billing.dataentry.service.serviceimpl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import kraheja.adminexp.billing.dataentry.bean.request.AdmadvanceRequestBean;
import kraheja.adminexp.billing.dataentry.bean.request.AdmbillhRequestBean;
import kraheja.adminexp.billing.dataentry.entity.Admadvance;
import kraheja.adminexp.billing.dataentry.entity.Admbillh;
import kraheja.adminexp.billing.dataentry.mappers.AdmadvanceEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.mappers.AdmbillhEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.repository.AdmadvanceRepository;
import kraheja.adminexp.billing.dataentry.controller.AdmadvanceController;
import kraheja.adminexp.billing.dataentry.repository.AdmbillhRepository;
import kraheja.adminexp.billing.dataentry.service.AdmadvanceService;
import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.entity.Address;
import kraheja.commons.entity.Minors;
import kraheja.commons.entity.Party;
import kraheja.commons.enums.AdSegment;
import kraheja.commons.enums.AdType;
import kraheja.commons.enums.MinType;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.mappers.pojoentity.AddressMapper;
import kraheja.commons.mappers.pojoentity.PartyMapper;
import kraheja.commons.repository.AddressRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.MinorsRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.ActrandxRepository;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.ActranhxRepository;
import kraheja.commons.utils.CommonConstraints;
import kraheja.commons.utils.CommonUtils;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
//import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import lombok.Data;

//@SuppressWarnings({ "unused", "unused" })
@Service
@Transactional
public  class AdmadvanceServiceImpl implements AdmadvanceService {

	private static final Logger logger = LoggerFactory.getLogger(AdmadvanceServiceImpl.class);

	@Autowired
	private AdmadvanceRepository admadvanceRepository;
	
	@Autowired
	private  PartyRepository partyRepository;
	
	@Autowired
	private  AddressRepository addressRepository;
	
	@Autowired
	private  MinorsRepository minorsRepository;
	
	@Autowired
	private  ActrandRepository actrandRepository;
	
	@Autowired
	private  ActrandxRepository actrandxRepository;
	
	@Autowired
	private  ActranhRepository actranhRepository;
	
	@Autowired
	private  ActranhxRepository actranhxRepository;
	
	@Autowired
	private  EntityRepository entityRepository;
	@Override
	public ResponseEntity<?> fetchAdmadvanceBySer(String ser) throws ParseException{
		Admadvance admadvanceEntity = this.admadvanceRepository.findByAdmadvanceCK_AdvnSer(ser);
		logger.info("AdmadvanceEntity :: {}", admadvanceEntity);
		if (admadvanceEntity != null) {
			Party partyEntity = this.partyRepository.findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(admadvanceEntity.getAdvnPartycode().trim(),
					CommonUtils.INSTANCE.closeDateInLocalDateTime(),admadvanceEntity.getAdvnPartytype().trim());
			
			return ResponseEntity.ok(ServiceResponseBean.builder()
									.data(AdmadvanceEntityPojoMapper.fetchAdmadvanceEntityPojoMapper
									.apply(new Object[] { Arrays.asList(admadvanceEntity), partyEntity})
									).status(Boolean.TRUE).build());
			}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No record found for your selections in Admadvance").build());
	}

	@Override
	public ResponseEntity<?> addAdmadvance(AdmadvanceRequestBean admadvanceRequestBean) throws ParseException {
	String ser = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER","#AA", GenericAuditContextHolder.getContext().getSite());
	logger.info("Admadvance :: {}", ser);
		
//		if(Objects.nonNull(admadvanceRequestBean)) {
//			if(CollectionUtils.isNotEmpty(admadvanceRequestBean.getAdvanceamt())) {
//				this.admadvanceRepository.saveAll(AdmadvanceEntityPojoMapper.addAdmadvanceEntityPojoMapper.apply(new Object[] {Arrays.asList(admadvanceRequestBean), ser}));
//			}
//		}
//		
//		admadvanceRequestBean.getAddressRequestBean().setAdsegment(AdSegment.PARTY.toString());
//		
		GenericAuditContextHolder.getContext().setTransactionNo(ser);
		GenericAuditContextHolder.getContext().setAuditable(Boolean.TRUE);
//
//		admadvanceRequestBean.getPartyRequestBean().setPartytype(CommonConstraints.INSTANCE.miscellaneousParty);
//		admadvanceRequestBean.getPartyRequestBean().setGstno(admadvanceRequestBean.getGstValdiationBean().gstNumber);
//		
//		this.partyRepository.save(PartyMapper.addPartyPojoEntityMapping.apply(new Object[] {admadvanceRequestBean.getPartyRequestBean(), GenericAuditContextHolder.getContext().getSite(), admadvanceRequestBean.getPartycode()}));
//
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Data Added Successfully").build());
	}
//
	@Override
	public ResponseEntity<?> updateAdmadvance(AdmadvanceRequestBean admadvanceRequestBean) throws ParseException {
//		
//		String SiteFromDBEntity = this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntChar1(CommonConstraints.INSTANCE.ENTITY_SITE, CommonConstraints.INSTANCE.ENTITY_CHAR1);
//		logger.info("Entity :: {}" , SiteFromDBEntity);
//		
//	Admadvance admadvanceEntity = this.admadvanceRepository.findByAdmadvanceCK_AdvnSer(admadvanceRequestBean.getSer()) ; 
//		
//		Admadvance admadvanceEntityList = this.admadvanceRepository.findByAdmadvanceCK_AdvnSer(admadvanceRequestBean.getSer()) ; 
//		logger.info("AdmbilldEntityList :: {}", admadvanceEntityList);
//
//		if(Objects.nonNull(admadvanceEntity)) {
//
//			Party partyEntity = this.partyRepository.findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(admadvanceEntity.getAdvnPartycode().trim(),
//					CommonUtils.INSTANCE.closeDateInLocalDateTime(), CommonConstraints.INSTANCE.miscellaneousParty);
//
//			if(Objects.nonNull(admadvanceRequestBean))
//				this.admadvanceRepository.save(AdmadvanceEntityPojoMapper.updateAdmadvanceEntityPojoMapper.apply(admadvanceEntity, admadvanceRequestBean));
//			
//			if(CollectionUtils.isNotEmpty(admadvanceRequestBean.getAdmadvanceRequestBeanList())) {
//				List<admadvanceRequestBean> admadvanceRequestBeanToAdd = new ArrayList<>();
//				
//				admadvanceRequestBean.getAdmadvanceRequestBeanList().stream().filter(filter ->{
//					return filter.getIsAdd();
//				}).map(admbilldRequestBean ->{
//					admbilldRequestBeanToAdd.add(admbilldRequestBean);
//					return admbillhRequestBean;
//				}).collect(Collectors.toList());
//				logger.info("admbilldRequestBeanToAdd :: {}" , admadvanceRequestBeanToAdd);
//				
//				if (CollectionUtils.isNotEmpty(admadvanceRequestBeanToAdd))
//					this.admadvanceRepository.saveAll(AdmadvanceEntityPojoMapper.addadmadvanceEntityPojoMapper.apply(new Object[] {admadvanceRequestBeanToAdd,admbillhEntity.getAdmadvanceCK().getAdvnSer()}));
//				
//				admadvanceEntityList.stream().map(admbilldEntity -> {
//					admadvanceRequestBean.getAdmadvanceRequestBeanList().stream().filter(filter ->{
//						return admadvanceEntity.getadmadvanceCK().getAdbldSer().trim().equals(filter.getSer().trim()) && admadvanceEntity.getAdmadvanceCK().getAdvnSer().equals(filter.getAdvnSer()) && !filter.getIsAdd();
//					}).map(admadvanceRequestBean ->{
//						this.admadvanceRepository.save(AdmadvanceEntityPojoMapper.updateAdmadvanceEntityPojoMapper.apply(admadvanceEntity, admadvanceRequestBean));
//						return admbilldRequestBean;
//					}
//					).collect(Collectors.toList());
//					return admbilldEntity;
//					}).collect(Collectors.toList());
//				}
//			
//			
//			if(Objects.isNull(addressEntity)) {
//				admadvanceRequestBean.getAddressRequestBean().setAdsegment(AdSegment.PARTY.toString());
//				this.addressRepository.save(AddressMapper.addAddressPojoEntityMapping.apply(new Object[] {admadvanceRequestBean.getAddressRequestBean(), SiteFromDBEntity, admadvanceRequestBean.getSer().trim()}));
//			}
//			else {
//				this.addressRepository.save(AddressMapper.updateAddressPojoEntityMapping.apply(addressEntity, admadvanceRequestBean.getAddressRequestBean()));
//			}
//			
//				GenericAuditContextHolder.getContext().setTransactionNo(admadvanceRequestBean.getSer());
//				GenericAuditContextHolder.getContext().setAuditable(Boolean.TRUE);
//				
//				if(Objects.nonNull(admadvanceRequestBean.getPartyRequestBean())) {
//					admadvanceRequestBean.getPartyRequestBean().setGstno(admadvanceRequestBean.getGstValdiationBean().gstNumber);
//					this.partyRepository.save(PartyMapper.updatePartyEntityPojoMapper.apply(partyEntity, admadvanceRequestBean.getPartyRequestBean()));
//				}
//				return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Updated Successfully").build());
//		} else {
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Data Found").build());
		}
//	}

	@Override
	public ResponseEntity<?> checkserExists(String ser) {
		@SuppressWarnings("unchecked")
		List <Admadvance> admadvanceEntity = (List<Admadvance>) this.admadvanceRepository.findByAdmadvanceCK_AdvnSer(ser.trim());
		logger.info("AdmadvanceEntity :: {}", admadvanceEntity);
		if (admadvanceEntity != null) {
			return ResponseEntity.ok(ServiceResponseBean.builder().message(ser +" is exists under Ser " + ((Admadvance) admadvanceEntity).getAdmadvanceCK().getAdvnSer()).status(Boolean.TRUE).build());
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).build());
	}
}