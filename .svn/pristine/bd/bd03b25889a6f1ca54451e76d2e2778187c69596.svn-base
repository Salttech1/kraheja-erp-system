package kraheja.sales.bookings.dataentry.service.serviceimpl;

import java.text.ParseException;
import java.util.Objects;

import javax.persistence.EntityManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.repository.EntityRepository;
import kraheja.sales.bean.request.BookingRequestBean;
import kraheja.sales.entity.Booking;
import kraheja.sales.bookings.dataentry.service.BookingService;
import kraheja.sales.repository.BookingRepository;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	private static final Logger logger = LoggerFactory.getLogger(BookingServiceImpl.class);

	@Autowired
	private  BookingRepository bookingRepository;
	
//	@Autowired
//	private  AddressRepository addressRepository;

//	@Autowired
//	private  PartyRepository partyRepository;

	@Autowired
	private  EntityRepository entityRepository;

	private static final String BOOKING_LOG = "BookingEntity :: {}";
//	private static final String PARTY_CODE_LOG = "partyCode :: {}";
//	private static final String PARTY_ENTIY_LOG = "partyEntity :: {}";

	@Autowired
	private EntityManager entityManager;


//	@Override
//	public ResponseEntity<?> fetchBookingByBldgCodeAndWingAndFlatNum(BookingRequestBean bookingRequestBean)
//			throws ParseException {
//		Booking bookingEntity = this.bookingRepository.findByBldgCodeAndWingAndFlatNum(
//				bookingRequestBean.getBldgCode().trim(), bookingRequestBean.getWing().trim(),bookingRequestBean.getFlatNum());
//		logger.info(BOOKING_LOG , bookingEntity);
//
//		if (bookingEntity != null) {
//			/*
//			 * String partyCode = bookingRequestBean.getCompanyCode().trim()
//			 * .concat(depositorRequestBean.getDepositorId().trim());
//			 * logger.info(PARTY_CODE_LOG , partyCode);
//			 * 
//			 * Party partyEntity =
//			 * this.partyRepository.findByPartyCodeAndParClosedateAndParPartytype(partyCode,
//			 * CommonUtils.INSTANCE.closeDate(), CommonConstraints.INSTANCE.DEPOSITORS);
//			 * logger.info(PARTY_ENTIY_LOG , partyEntity);
//			 * 
//			 * Address addressEntity = this.addressRepository.
//			 * findByAdrAdownerAndAdrAdsegmentAndAdrAdtypeAndAdrAdser( partyCode,
//			 * CommonConstraints.INSTANCE.adrAdsegment,
//			 * CommonConstraints.INSTANCE.addresstype, CommonConstraints.INSTANCE.adrAdser);
//			 * logger.info("AddressEntity :: {} " , addressEntity);
//			 */
//			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(BookingResponseBean.builder()
//					.bldgCode(bookingEntity.getBookingCK().getBookBldgcode())
//					.wing(bookingEntity.getBookingCK().getBookWing())
//					.flatNum(bookingEntity.getBookingCK().getBookFlatnum())
//					.bookedBy(bookingEntity.getBookBookedby())
//					.agPrice(bookingEntity.getBookAgprice()).build() ));
//
////					.addressResponseBean(AddressResponseBean.builder()
////							.adline1(addressEntity.getAdrAdline1())
////							.adline2(addressEntity.getAdrAdline2())
////							.adline3(addressEntity.getAdrAdline3())
////							.adline4(addressEntity.getAdrAdline4())
////							.adline5(addressEntity.getAdrAdline5())
////							.pincode(addressEntity.getAdrPincode())
////							.fax(addressEntity.getAdrFax())
////							.township(addressEntity.getAdrTownship())
////							.city(addressEntity.getAdrCity())
////							.state(addressEntity.getAdrState())
////							.country(addressEntity.getAdrCountry())
////							.phonemobile(addressEntity.getAdrPhonemobile())
////							.phoneoff(addressEntity.getAdrPhoneoff())
////							.phoneres(addressEntity.getAdrPhoneres())
////							.email(addressEntity.getAdrEmail()).build())
////					.build()).build());
//		}
//		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Booking found with this Building + Wing + FlatNum").build());
//	}

	@Override
	public ResponseEntity<?> addBooking(BookingRequestBean bookingRequestBean) throws ParseException {
		String bldgCode = bookingRequestBean.getBldgCode().trim();
		String wing = bookingRequestBean.getWing().trim();
		String flatnum = bookingRequestBean.getFlatNum().trim();
		String bookedBy = bookingRequestBean.getBookedBy().trim();
		Double agPrice = bookingRequestBean.getAgPrice();		
		String ownerId = bldgCode.concat(wing.toString()).concat(flatnum.toString());

		//logger.info(PARTY_CODE_LOG , partyCode);

//		DbEntity entityEntity = this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntChar1(CommonConstraints.INSTANCE.ENTITY_SITE, CommonConstraints.INSTANCE.ENTITY_CHAR1);
//		logger.info("Entity :: {}" , entityEntity);

		/*
		 * Party partyEntity =
		 * this.partyRepository.findByPartyCodeAndParClosedateAndParPartytype(partyCode,
		 * CommonUtils.INSTANCE.closeDate(), CommonConstraints.INSTANCE.DEPOSITORS);
		 * logger.info(PARTY_ENTIY_LOG , partyEntity);
		 * 
		 * Address addressEntity = this.addressRepository.
		 * findByAdrAdownerAndAdrAdsegmentAndAdrAdtypeAndAdrAdser( partyCode,
		 * CommonConstraints.INSTANCE.adrAdsegment,
		 * CommonConstraints.INSTANCE.addresstype, CommonConstraints.INSTANCE.adrAdser);
		 * logger.info("AddressEntity :: {}" , addressEntity);
		 * 
		 * if(depositorRequestBean.getPartyRequestBean().getPayeeIfsc1() != null &&
		 * depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().length() >
		 * BigInteger.ZERO.intValue()) {
		 * if(depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().length() == 11)
		 * { Payeebank payeebank =
		 * this.payeebankRepository.findByPybkCode(depositorRequestBean.
		 * getPartyRequestBean().getPayeebankcode1()); if(payeebank == null) return
		 * ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).
		 * message("Please enter bank details before adding IFSC details.").build());
		 * if(!depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().substring(0,
		 * 4).equals(payeebank.getPybkCode())) return
		 * ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).
		 * message("Please enter proper IFSC code.").build()); }else return
		 * ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).
		 * message("IFSC length should be equal to or greater than 11").build()); }
		 * this.depositorRepository.addDepositor( newDepositorId.toString(),
		 * StringUtils.isNotBlank(depositorRequestBean.getTitle()) ?
		 * depositorRequestBean.getTitle().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getName()) ?
		 * depositorRequestBean.getName().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getProprietor()) ?
		 * depositorRequestBean.getProprietor().trim() : null,
		 * depositorRequestBean.getCompanyCode(), BigInteger.ZERO.doubleValue(),
		 * BigInteger.ZERO.doubleValue(), BigInteger.ZERO.doubleValue(),
		 * BigInteger.ZERO.doubleValue(),
		 * StringUtils.isNotBlank(depositorRequestBean.getRemarks()) ?
		 * depositorRequestBean.getRemarks().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getCity()) ?
		 * depositorRequestBean.getCity().trim() : null,
		 * StringUtils.isNotBlank(entityEntity.getEntityCk().getEntId()) ?
		 * entityEntity.getEntityCk().getEntId().trim() : null,
		 * depositorRequestBean.getUserid(), CommonUtils.INSTANCE.currentDate(),
		 * CommonConstraints.INSTANCE.SITE,
		 * StringUtils.isNotBlank(depositorRequestBean.getDeptype()) ?
		 * depositorRequestBean.getDeptype().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getTaxalwaysyn()) ?
		 * depositorRequestBean.getTaxalwaysyn().trim() :
		 * CommonConstraints.INSTANCE.LETTER_Y , StringUtils.isNotBlank(
		 * depositorRequestBean.getTds15hyn()) ?
		 * depositorRequestBean.getTds15hyn().trim() : null, StringUtils.isNotBlank(
		 * depositorRequestBean.getTds15gyn()) ?
		 * depositorRequestBean.getTds15gyn().trim() : null,
		 * BigInteger.ZERO.doubleValue(), BigInteger.ZERO.doubleValue(),
		 * BigInteger.ZERO.doubleValue(),
		 * StringUtils.isNotBlank(depositorRequestBean.getClubref()) ?
		 * depositorRequestBean.getClubref().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPannum1()) ?
		 * depositorRequestBean.getPannum1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPannum2()) ?
		 * depositorRequestBean.getPannum2().trim() : null,
		 * Objects.nonNull(depositorRequestBean.getBirthdate()) ?
		 * depositorRequestBean.getBirthdate() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getInsupd()) ?
		 * depositorRequestBean.getInsupd().trim() : null);
		 * 
		 * this.addressRepository.addAddress(partyCode,
		 * CommonConstraints.INSTANCE.addresstype, CommonConstraints.INSTANCE.adrAdser,
		 * CommonConstraints.INSTANCE.adrAdser,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getTitle(
		 * )) ? depositorRequestBean.getAddressRequestBean().getTitle().trim() : null ,
		 * depositorRequestBean.getAddressRequestBean().getFname(),
		 * depositorRequestBean.getAddressRequestBean().getMname(),
		 * depositorRequestBean.getAddressRequestBean().getLname(),
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd1())
		 * ? depositorRequestBean.getAddressRequestBean().getAd1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd2())
		 * ? depositorRequestBean.getAddressRequestBean().getAd2().trim(): null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd3())
		 * ? depositorRequestBean.getAddressRequestBean().getAd3().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd4())
		 * ? depositorRequestBean.getAddressRequestBean().getAd4().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd5())
		 * ? depositorRequestBean.getAddressRequestBean().getAd5().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getTown()
		 * ) ? depositorRequestBean.getAddressRequestBean().getTown().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getCity()
		 * ) ? depositorRequestBean.getAddressRequestBean().getCity().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getState(
		 * )) ? depositorRequestBean.getAddressRequestBean().getState().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getCountry()) ?
		 * depositorRequestBean.getAddressRequestBean().getCountry().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getPincode()) ?
		 * depositorRequestBean.getAddressRequestBean().getPincode().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getPhoneoff()) ?
		 * depositorRequestBean.getAddressRequestBean().getPhoneoff().trim() : null,
		 * CommonConstraints.INSTANCE.adrAdsegment,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getPhoneres()) ?
		 * depositorRequestBean.getAddressRequestBean().getPhoneres().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getFax())
		 * ? depositorRequestBean.getAddressRequestBean().getFax().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getEmail(
		 * )) ? depositorRequestBean.getAddressRequestBean().getEmail().trim() : null,
		 * Objects.nonNull(depositorRequestBean.getAddressRequestBean().getBirthday()) ?
		 * depositorRequestBean.getAddressRequestBean().getBirthday() : null,
		 * entityEntity.getEntityCk().getEntId(),
		 * depositorRequestBean.getAddressRequestBean().getUserid(),
		 * CommonUtils.INSTANCE.currentDate(),
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getOrigsite()) ?
		 * depositorRequestBean.getAddressRequestBean().getOrigsite().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getInsupd
		 * ()) ? depositorRequestBean.getAddressRequestBean().getInsupd().trim() : null
		 * , StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().
		 * getPhonemobile()) ?
		 * depositorRequestBean.getAddressRequestBean().getPhonemobile().trim() : null);
		 * 
		 * this.partyRepository.addParty(partyCode,
		 * CommonConstraints.INSTANCE.DEPOSITORS,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getTitle())
		 * ? depositorRequestBean.getPartyRequestBean().getTitle().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPartyname()) ?
		 * depositorRequestBean.getPartyRequestBean().getPartyname().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getConstt()
		 * ) ? depositorRequestBean.getPartyRequestBean().getConstt().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getCity())
		 * ? depositorRequestBean.getPartyRequestBean().getCity().trim() : null,
		 * CommonUtils.INSTANCE.currentDate(),
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPmtacnum
		 * ()) ? depositorRequestBean.getPartyRequestBean().getPmtacnum().trim() : null,
		 * null, null, null, null, null, CommonConstraints.INSTANCE.validParty,
		 * CommonConstraints.INSTANCE.validMinor, entityEntity.getEntityCk().getEntId(),
		 * //TODO: fetch entity from site
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getUserid()
		 * ) ? depositorRequestBean.getPartyRequestBean().getUserid().trim() : null,
		 * CommonUtils.INSTANCE.currentDate(), CommonUtils.INSTANCE.closeDate(),
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getInsupd()
		 * ) ? depositorRequestBean.getPartyRequestBean().getInsupd().trim() : null,
		 * null, null, null, //change to null null, //change to null null, null, null,
		 * null, StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeebankcode1()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeebankcode1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeBranch1()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeBranch1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeacNum1()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeacNum1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeIfsc1()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeBranch2()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeBranch2().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeBranch2()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeBranch2().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeacNum2()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeacNum2().trim() : null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().
		 * getPayeeIfsc2()) ?
		 * depositorRequestBean.getPartyRequestBean().getPayeeIfsc2().trim() : null,
		 * null, null, null, null, null,
		 * StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getAadharno
		 * ()) ? depositorRequestBean.getPartyRequestBean().getAadharno().trim() :
		 * null);
		 */		
//		if(depositorRequestBean.getIsTransferSeries())
//			this.entityRepository.updateIncrementCounter("FDDTF", depositorRequestBean.getCompanyCode().trim(), Double.valueOf(newDepositorId)); 
//		else
//			this.entityRepository.updateIncrementCounter("FDDNO", depositorRequestBean.getCompanyCode().trim(), Double.valueOf(newDepositorId));
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Owner Id " + ownerId + " added successfully.").build());
	}

	@Override
	public ResponseEntity<?> updateBooking(BookingRequestBean bookingRequestBean) throws ParseException {
		Booking bookingEntity = this.bookingRepository.findByBldgCodeAndWingAndFlatNum(
				bookingRequestBean.getBldgCode().trim(), bookingRequestBean.getWing().trim(), bookingRequestBean.getFlatNum().trim());
		logger.info(BOOKING_LOG , bookingEntity);

//		if(depositorEntity != null) {
//			String partyCode = depositorRequestBean.getCompanyCode().trim()
//					.concat(depositorRequestBean.getDepositorId().trim());
//			logger.info(PARTY_CODE_LOG , partyCode);
//
//			DbEntity entityEntity = this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntChar1(CommonConstraints.INSTANCE.ENTITY_SITE, CommonConstraints.INSTANCE.ENTITY_CHAR1);
//			logger.info("Entity :: {}" , entityEntity);
//
//			Party partyEntity = this.partyRepository.findByPartyCodeAndParClosedateAndParPartytype(partyCode,
//					CommonUtils.INSTANCE.closeDate(), CommonConstraints.INSTANCE.DEPOSITORS);
//			logger.info(PARTY_ENTIY_LOG , partyEntity);
//
//			Address addressEntity = this.addressRepository.findByAdrAdownerAndAdrAdsegmentAndAdrAdtypeAndAdrAdser(
//					partyCode, CommonConstraints.INSTANCE.adrAdsegment, CommonConstraints.INSTANCE.addresstype,
//					CommonConstraints.INSTANCE.adrAdser);
//			logger.info("AddressEntity :: {}" , addressEntity);
//
//			if(depositorRequestBean.getPartyRequestBean().getPayeeIfsc1() != null && depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().length() > BigInteger.ZERO.intValue()) {
//				if(depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().length() == 11) {
//					Payeebank payeebank = this.payeebankRepository.findByPybkCode(depositorRequestBean.getPartyRequestBean().getPayeebankcode1());
//					if(!depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().substring(0, 4).equals(payeebank.getPybkCode()))
//						return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Please enter proper IFSC code.").build());
//				}else
//					return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("IFSC length should be equal to or greater than 11").build());
//			}
//
//			this.depositorRepository.addDepositor(
//					depositorRequestBean.getDepositor(),
//					StringUtils.isNotBlank(depositorRequestBean.getTitle()) ? depositorRequestBean.getTitle().trim() : null,
//							StringUtils.isNotBlank(depositorRequestBean.getName()) ? depositorRequestBean.getName().trim() : null,
//									StringUtils.isNotBlank(depositorRequestBean.getProprietor()) ? depositorRequestBean.getProprietor().trim() : null, 
//											depositorRequestBean.getCompanyCode(),
//											BigInteger.ZERO.doubleValue(), 
//											BigInteger.ZERO.doubleValue(),
//											BigInteger.ZERO.doubleValue(),
//											BigInteger.ZERO.doubleValue(), 
//											StringUtils.isNotBlank(depositorRequestBean.getRemarks()) ? depositorRequestBean.getRemarks().trim() : null,
//													StringUtils.isNotBlank(depositorRequestBean.getCity()) ? depositorRequestBean.getCity().trim() : null, 
//															StringUtils.isNotBlank(entityEntity.getEntityCk().getEntId()) ? entityEntity.getEntityCk().getEntId().trim() : null,
//																	depositorRequestBean.getUserid(), 
//																	CommonUtils.INSTANCE.currentDate(),
//																	CommonConstraints.INSTANCE.SITE,
//																	StringUtils.isNotBlank(depositorRequestBean.getDeptype()) ? depositorRequestBean.getDeptype().trim() : null,
//																			StringUtils.isNotBlank(	depositorEntity.getDeptrTaxalwaysyn()) ? depositorEntity.getDeptrTaxalwaysyn().trim() : null , 
//																					StringUtils.isNotBlank(	depositorRequestBean.getTds15hyn()) ? depositorRequestBean.getTds15hyn().trim() : null, 
//																							StringUtils.isNotBlank(	depositorRequestBean.getTds15gyn()) ? depositorRequestBean.getTds15gyn().trim() : null,
//																									BigInteger.ZERO.doubleValue(),
//																									BigInteger.ZERO.doubleValue(), 
//																									BigInteger.ZERO.doubleValue(),
//																									StringUtils.isNotBlank(depositorRequestBean.getClubref()) ? depositorRequestBean.getClubref().trim() : null, 
//																											StringUtils.isNotBlank(depositorRequestBean.getPannum1()) ? depositorRequestBean.getPannum1().trim() : null,
//																													StringUtils.isNotBlank(depositorRequestBean.getPannum2()) ? depositorRequestBean.getPannum2().trim() : null,
//																															Objects.nonNull(depositorRequestBean.getBirthdate()) ? depositorRequestBean.getBirthdate() : null,
//																																	StringUtils.isNotBlank(depositorRequestBean.getInsupd()) ? depositorRequestBean.getInsupd().trim() : null);
//
//			this.addressRepository.addAddress(partyCode, CommonConstraints.INSTANCE.addresstype, CommonConstraints.INSTANCE.adrAdser, CommonConstraints.INSTANCE.adrAdser,
//					StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getTitle()) ? depositorRequestBean.getAddressRequestBean().getTitle().trim() : null , depositorRequestBean.getAddressRequestBean().getFname(), depositorRequestBean.getAddressRequestBean().getMname(), depositorRequestBean.getAddressRequestBean().getLname(), StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd1()) ? depositorRequestBean.getAddressRequestBean().getAd1().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd2()) ? depositorRequestBean.getAddressRequestBean().getAd2().trim(): null,
//							StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd3()) ? depositorRequestBean.getAddressRequestBean().getAd3().trim() : null, 
//									StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd4()) ? depositorRequestBean.getAddressRequestBean().getAd4().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getAd5()) ? depositorRequestBean.getAddressRequestBean().getAd5().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getTown()) ? depositorRequestBean.getAddressRequestBean().getTown().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getCity()) ? depositorRequestBean.getAddressRequestBean().getCity().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getState()) ? depositorRequestBean.getAddressRequestBean().getState().trim() : null, 
//											StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getCountry()) ? depositorRequestBean.getAddressRequestBean().getCountry().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getPincode()) ? depositorRequestBean.getAddressRequestBean().getPincode().trim() : null, StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getPhoneoff()) ? depositorRequestBean.getAddressRequestBean().getPhoneoff().trim() : null, CommonConstraints.INSTANCE.adrAdsegment,
//													StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getPhoneres()) ? depositorRequestBean.getAddressRequestBean().getPhoneres().trim() : null,
//															StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getFax()) ? depositorRequestBean.getAddressRequestBean().getFax().trim() : null,
//																	StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getEmail()) ? depositorRequestBean.getAddressRequestBean().getEmail().trim() : null, 
//																			Objects.nonNull(depositorRequestBean.getAddressRequestBean().getBirthday()) ? depositorRequestBean.getAddressRequestBean().getBirthday() : null,
//																					entityEntity.getEntityCk().getEntId(),
//																					depositorRequestBean.getAddressRequestBean().getUserid(), 
//																					CommonUtils.INSTANCE.currentDate(), 
//																					StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getOrigsite()) ? depositorRequestBean.getAddressRequestBean().getOrigsite().trim() : null,
//																							StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getInsupd()) ? depositorRequestBean.getAddressRequestBean().getInsupd().trim() : null , 
//																									StringUtils.isNotBlank(depositorRequestBean.getAddressRequestBean().getPhonemobile()) ? depositorRequestBean.getAddressRequestBean().getPhonemobile().trim() : null);
//
//			this.partyRepository.addParty(partyCode, CommonConstraints.INSTANCE.DEPOSITORS,
//					StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getTitle()) ? depositorRequestBean.getPartyRequestBean().getTitle().trim() : null, 
//							StringUtils.isNotBlank(partyEntity.getParPartyname()) ? partyEntity.getParPartyname().trim() : null,
//									StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getConstt()) ? depositorRequestBean.getPartyRequestBean().getConstt().trim() : null,
//											StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getCity()) ? depositorRequestBean.getPartyRequestBean().getCity().trim() : null,
//													partyEntity.getParOpendate(), 
//													StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPmtacnum()) ? depositorRequestBean.getPartyRequestBean().getPmtacnum().trim() : null,
//															null,
//															null,
//															null, 
//															null,
//															null, 
//															CommonConstraints.INSTANCE.validParty, 
//															CommonConstraints.INSTANCE.validMinor, 
//															entityEntity.getEntityCk().getEntId(), //TODO: fetch entity from site 
//															StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getUserid()) ? depositorRequestBean.getPartyRequestBean().getUserid().trim() : null,
//																	CommonUtils.INSTANCE.currentDate(), 
//																	CommonUtils.INSTANCE.closeDate(),
//																	null, 
//																	null,
//																	null, 
//																	null, 
//																	null, 
//																	null, 
//																	null, 
//																	null, 
//																	null, 
//																	StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeebankcode1()) ? depositorRequestBean.getPartyRequestBean().getPayeebankcode1().trim() : null, 
//																			StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeBranch1()) ? depositorRequestBean.getPartyRequestBean().getPayeeBranch1().trim() : null, 
//																					StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeacNum1()) ? depositorRequestBean.getPartyRequestBean().getPayeeacNum1().trim() : null, 
//																							StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeIfsc1()) ? depositorRequestBean.getPartyRequestBean().getPayeeIfsc1().trim() : null, 
//																									StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeBranch2()) ? depositorRequestBean.getPartyRequestBean().getPayeeBranch2().trim() : null,
//																											StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeBranch2()) ? depositorRequestBean.getPartyRequestBean().getPayeeBranch2().trim() : null, 
//																													StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeacNum2()) ? depositorRequestBean.getPartyRequestBean().getPayeeacNum2().trim() : null,
//																															StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getPayeeIfsc2()) ? depositorRequestBean.getPartyRequestBean().getPayeeIfsc2().trim() : null, 
//																																	null,
//																																	null, 
//																																	null,
//																																	null, 
//																																	null, 
//																																	StringUtils.isNotBlank(depositorRequestBean.getPartyRequestBean().getAadharno()) ? depositorRequestBean.getPartyRequestBean().getAadharno().trim() : null);
//			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Updated Successfully").build());
//		}	
		return new ResponseEntity<>(HttpStatus.CONFLICT);
	}

	@Override
	public ResponseEntity<?> checkBldgCodeAndWingAndFlatnumExists(String bldgCode,String Wing,String flatNum) {
		
		Booking bookingEntity = this.bookingRepository.findByBldgCodeAndWingAndFlatNum(
				bldgCode, Wing, flatNum);
		logger.info(BOOKING_LOG , bookingEntity);
		if(Objects.isNull(bookingEntity))
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).build()); 
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Building + Wing + FlatNum already exists.").build()); 
	}

}
