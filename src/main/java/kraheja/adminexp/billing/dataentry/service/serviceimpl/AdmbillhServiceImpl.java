package kraheja.adminexp.billing.dataentry.service.serviceimpl;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.swing.text.html.parser.Entity;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kraheja.arch.projbldg.dataentry.entity.Building;
import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;

import kraheja.adminexp.billing.dataentry.bean.request.AdmbilldRequestBean;
import kraheja.adminexp.billing.dataentry.bean.request.AdmbillhRequestBean;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.ActrandxRepository;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.ActranhxRepository;
import kraheja.commons.entity.Address;
import kraheja.commons.entity.Party;
import kraheja.commons.repository.AddressRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.ExnarrRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.utils.CommonConstraints;
import kraheja.commons.utils.CommonUtils;
import kraheja.adminexp.billing.dataentry.entity.Admadvance;
import kraheja.adminexp.billing.dataentry.entity.Admbilld;
import kraheja.adminexp.billing.dataentry.entity.Admbillh;
import kraheja.adminexp.billing.dataentry.mappers.AdmadvanceEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.mappers.AdmbilldEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.mappers.AdmbillhEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.repository.AdmadvanceRepository;
import kraheja.adminexp.billing.dataentry.repository.AdmbilldRepository;
import kraheja.adminexp.billing.dataentry.repository.AdmbillhRepository;
import kraheja.adminexp.billing.dataentry.service.AdmbillhService;
import kraheja.commons.bean.AccountingBean;
import kraheja.commons.bean.ActrandBean;
import kraheja.commons.bean.ActranhBean;
import kraheja.commons.bean.ExnarrBean;
import kraheja.commons.bean.request.PartyAddressRequestBean;
import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.entity.Actrand;
import kraheja.commons.entity.Actranh;
import kraheja.commons.entity.Address;
import kraheja.commons.entity.Company;
import kraheja.commons.entity.Party;
import kraheja.commons.enums.AdSegment;
import kraheja.commons.enums.AdType;
import kraheja.commons.enums.PartyType;
import kraheja.commons.enums.TranTypeEnum;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.mappers.entityentity.ActrandxEntityEntityMapper;
import kraheja.commons.mappers.entityentity.ActranhxEntityEntityMapper;
import kraheja.commons.mappers.pojoentity.AddPojoEntityMapper;
import kraheja.commons.mappers.pojoentity.AddressMapper;
import kraheja.commons.mappers.pojoentity.ExnarrMapper;
import kraheja.commons.mappers.pojoentity.PartyMapper;
import kraheja.commons.mappers.pojoentity.UpdatePojoEntityMapper;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.ActrandxRepository;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.ActranhxRepository;
import kraheja.commons.repository.AddressRepository;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.MinorsRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.utils.CommonConstraints;
import kraheja.commons.utils.CommonUtils;
import kraheja.commons.utils.GenericAccountingLogic;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import kraheja.commons.utils.ValueContainer;
import kraheja.purch.bean.request.MaterialPaymentRequestBean;
import kraheja.purch.enums.BillTypeEnum;
import kraheja.purch.materialpayments.service.impl.MaterialPaymentsServiceImpl;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Service
@Transactional
@Slf4j
public class AdmbillhServiceImpl implements AdmbillhService {

	private static final Logger logger = LoggerFactory.getLogger(AdmbillhServiceImpl.class);

	@Autowired
	private AdmbillhRepository admbillhRepository;

	@Autowired
	private AdmadvanceRepository admadvanceRepository;

	@Autowired
	private AdmbilldRepository admbilldRepository;

	@Autowired
	private ExnarrRepository exnarrRepository;

	@PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PartyRepository partyRepository;

	@Autowired
	private ActrandxRepository actrandxRepository;

	@Autowired
	private ActranhxRepository actranhxRepository;

	@Autowired
	private ActranhRepository actranhRepository;

	@Autowired
	private ActrandRepository actrandRepository;

	@Autowired
	private BuildingRepository buildingRepository;

	@Autowired
	private MinorsRepository minorsRepository;

	@Autowired
	private EntityRepository entityRepository;

	ObjectMapper objectMapper = new ObjectMapper();

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> fetchAdmbillhBySer(String ser) {
		Admbillh admbillhEntity = this.admbillhRepository.findByAdmbillhCK_AdblhSer(ser);
		logger.info("AdmbillhEntity :: {}", admbillhEntity);
		if (admbillhEntity != null) {
			Party partyEntity = this.partyRepository
					.findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(
							admbillhEntity.getAdblhPartycode().trim(), CommonUtils.INSTANCE.closeDateInLocalDateTime(),
							admbillhEntity.getAdblhPartytype().trim());

			Address addressEntity = this.addressRepository
					.findByAddressCK_AdrAdownerAndAddressCK_AdrAdsegmentAndAddressCK_AdrAdtypeAndAddressCK_AdrAdser(
							admbillhEntity.getAdblhPartycode().trim(), CommonConstraints.INSTANCE.adrAdsegment,
							CommonConstraints.INSTANCE.addresstype, CommonConstraints.INSTANCE.adrAdser);
			logger.info("Address :: {}", addressEntity);

			List<Admbilld> admbilldEntityList = this.admbilldRepository.findByAdmbilldCK_AdbldSer(ser.trim());
			logger.info("AdmbillhEntity :: {}", admbillhEntity);

			Admadvance admadvanceEntityList = this.admadvanceRepository.findByAdmadvanceCK_AdvnSer(ser.trim());
			logger.info("AdmbillhEntity :: {}", admbillhEntity);

			return ResponseEntity.ok(ServiceResponseBean.builder()
					.data(AdmbillhEntityPojoMapper.fetchAdmbillhEntityPojoMapper
							.apply(new Object[] { Arrays.asList(admbillhEntity), partyEntity, addressEntity,
									admbilldEntityList, admadvanceEntityList }))
					.status(Boolean.TRUE).build());
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE)
				.message("No record found for your selections in Admbillh").build());
	}

	@Override
	public ResponseEntity<?> fetchAdvancePaidDetail(String partyCode, String buildingCode, String coy,
			Double advanceAdjust, String tranMode) {
		Double totPaidAdvn, adjustedAdvn, DecLocBalAdvn = null, advnToAdjust;

		totPaidAdvn = this.admadvanceRepository
				.findAdvnAmountSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhstatusAndAdblhtype(
						partyCode.trim(), buildingCode.trim(), coy.trim(), "Z");
		log.info("DecLocTotPaidAdvn", totPaidAdvn);

		adjustedAdvn = this.admbillhRepository
				.findAdblhAdjustSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhtypeAndadblhadvanceAdjust(
						partyCode.trim(), buildingCode.trim(), coy.trim(), "Z");
		log.info("DecLocAdjustedAdvn", adjustedAdvn);

		// AS per discussing with Vikram sir this logic is kept same as VB as told by
		// him.
		if (Objects.nonNull(advanceAdjust)) {
//			totPaidAdvn = this.admbillhRepository
//					.findAdblhAmountSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhstatusAndAdblhtype(
//							partyCode.trim(), buildingCode.trim(), coy.trim(), "Z");
//			log.info("DecLocTotPaidAdvn", totPaidAdvn);
//
//			adjustedAdvn = this.admbillhRepository
//					.findAdblhAmountSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhstatusAndAdblhtype(
//							partyCode.trim(), buildingCode.trim(), coy.trim(), "Z");

//			log.info("DecLocAdjustedAdvn", adjustedAdvn);

			// DecLocOtherAdvos = DecLocOtherAdvance - (DecLocOtherAdvanceReturn +
			// DecLocOtherAdvanceAdjust); //TO ASK ABOUT MINUS

			advnToAdjust = advanceAdjust;

			if (tranMode.equals("E")) { 
				adjustedAdvn = adjustedAdvn - advnToAdjust;
			}
			if (totPaidAdvn > 0) {
				DecLocBalAdvn = totPaidAdvn - adjustedAdvn;
			}
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(totPaidAdvn).build());
	}

//	private List<ActrandBean> insActrand(Integer bunumCounter, AdmbillhRequestBean AdmbillhRequestBean,
//			AccountingBean accoutingDataForTran, AccountingBean accoutingDataForContraTran,
//			AccountingBean accoutingDataCashFlow, AccountingBean accoutingDataCashFlowForContra, Double tranamt,
//			String bldgcode, String property, String ser, String trantype, String doctype, String docnum,
//			String docdate, String narrative) {
//		List<ActrandBean> actrandBeanList = new ArrayList<>();
//		String narrationMsg;
//		String exnNarrationMsg = "";
//		if (Objects.nonNull(AdmbillhRequestBean.getMatname()) && (AdmbillhRequestBean.getMatname().trim()
//				.concat(CommonConstraints.INSTANCE.SPACE_STRING).concat(narrative)).length() < 40) {
//			narrationMsg = AdmbillhRequestBean.getMatname().trim()
//					.concat(CommonConstraints.INSTANCE.SPACE_STRING).concat(narrative);
//		} else {
//			narrationMsg = AdmbillhRequestBean.getMatname();
//			exnNarrationMsg = narrative;
//		}
//		for (int i = 1; i <= 2; i++) {
//			if (i % 2 == 1) {
//				actrandBeanList.add(ActrandBean.builder().transer(ser).bunum(bunumCounter).trantype(trantype)
//						.trandate(LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)).ledgcode("PL")
//						.coy(AdmbillhRequestBean.getCoy().trim())
//						.mintype(StringUtils.isNotEmpty(accoutingDataForTran.getMinType())
//								? accoutingDataForTran.getMinType()
//								: CommonConstraints.INSTANCE.SPACE_STRING)
//						.partytype(accoutingDataForTran.getPartyType()).partycode(accoutingDataForTran.getPartyCode())
//						.acmajor(accoutingDataForTran.getAcMajor())
//						.acminor(StringUtils.isNotEmpty(accoutingDataForTran.getAcminor())
//								? accoutingDataForTran.getAcminor()
//								: CommonConstraints.INSTANCE.SPACE_STRING)
//						.mincode(accoutingDataForTran.getMinCode()).vounum(AdmbillhRequestBean.getAuthnum())
//						.voudate(AdmbillhRequestBean.getAuthdate()).tranamt(tranamt).narrative(narrationMsg)
//						.project(accoutingDataForTran.getProject()).period(CommonConstraints.INSTANCE.BLANK_STRING)
//						.property(property).bldgcode(bldgcode).domain("M")
//						.matgroup(AdmbillhRequestBean.getMatgroup()).cfgroup(accoutingDataCashFlow.getCfGroup())
//						.cfcode(accoutingDataCashFlow.getCfCode()).doctype(doctype).docnum(docnum).docdate(docdate)
//						.docpartype(PartyType.S.toString()).docparcode(AdmbillhRequestBean.getPartycode())
//						// X columns (Contra entry)
//						.xproject(accoutingDataForContraTran.getProject())
//						.xacmajor(accoutingDataForContraTran.getAcMajor())
//						.xacminor(accoutingDataForContraTran.getAcminor())
//						.xmintype(accoutingDataForContraTran.getMinType())
//						.xpartycode(accoutingDataForContraTran.getPartyCode())
//						.xpartytype(accoutingDataForContraTran.getPartyType()).xreftranser(ser)
//						.xrefbunum(bunumCounter + 1)
//						.xreftrandate(LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER))
//						.site(GenericAuditContextHolder.getContext().getSite())
//						.userid(GenericAuditContextHolder.getContext().getUserid()).today(LocalDateTime.now()).build());
//			} else {
//				actrandBeanList.add(ActrandBean.builder().transer(ser).bunum(bunumCounter + 1).trantype(trantype)
//						.trandate(LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)).ledgcode("PL")
//						.proprietor(AdmbillhRequestBean.getProp())
//						.coy(AdmbillhRequestBean.getCoy().trim())
//						.mintype(StringUtils.isNotEmpty(accoutingDataForContraTran.getMinType())
//								? accoutingDataForContraTran.getMinType()
//								: CommonConstraints.INSTANCE.SPACE_STRING)
//						.mincode(accoutingDataForContraTran.getMinCode())
//						.partytype(accoutingDataForContraTran.getPartyType())
//						.partycode(accoutingDataForContraTran.getPartyCode())
//						.project(accoutingDataForContraTran.getProject())
//						.acminor(StringUtils.isNotEmpty(accoutingDataForContraTran.getAcminor())
//								? accoutingDataForContraTran.getAcminor()
//								: CommonConstraints.INSTANCE.SPACE_STRING)
//						.acmajor(accoutingDataForContraTran.getAcMajor())
//						.vounum(AdmbillhRequestBean.getAuthnum())
//						.voudate(AdmbillhRequestBean.getAuthdate()).tranamt(tranamt * -1)
//						.period(CommonConstraints.INSTANCE.BLANK_STRING).narrative(narrationMsg)
//						.project(accoutingDataForContraTran.getProject()).bldgcode(bldgcode).property(property)
//						.domain("M").matgroup(AdmbillhRequestBean.getMatgroup())
//						.cfgroup(accoutingDataCashFlowForContra.getCfGroup())
//						.cfcode(accoutingDataCashFlowForContra.getCfCode()).doctype(doctype).docnum(docnum)
//						.docdate(docdate).docpartype(PartyType.S.toString())
//						.docparcode(AdmbillhRequestBean.getPartycode())
//						// X columns (Contra entry)
//						.xreftranser(ser).xacminor(accoutingDataForTran.getAcminor())
//						.xacmajor(accoutingDataForTran.getAcMajor()).xmintype(accoutingDataForTran.getMinType())
//						.xpartycode(accoutingDataForTran.getPartyCode()).xproject(accoutingDataForTran.getProject())
//						.xreftrandate(LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER))
//						.xpartytype(accoutingDataForTran.getPartyType()).xrefbunum(bunumCounter)
//						.site(GenericAuditContextHolder.getContext().getSite())
//						.userid(GenericAuditContextHolder.getContext().getUserid()).today(LocalDateTime.now()).build());
//			}
//			if (StringUtils.isNotBlank(exnNarrationMsg))
//				this.exnarrRepository.save(ExnarrMapper.addExnarrMapper.apply(ExnarrBean.builder().transer(ser)
//						.bunum(bunumCounter.doubleValue()).coy(AdmbillhRequestBean.getCoy().trim())
//						.linenum(Double.valueOf(i)).narrtype("S").today(LocalDateTime.now())
//						.site(GenericAuditContextHolder.getContext().getSite())
//						.userid(GenericAuditContextHolder.getContext().getUserid()).exnarr(exnNarrationMsg).build()));
//
//		}
//		return actrandBeanList;
//	}

	@SuppressWarnings("unchecked")
	@Override
	public ResponseEntity<?> addAdmbillh(AdmbillhRequestBean admbillhRequestBean) throws ParseException {
		// String siteFromDBEntity =
		// this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntChar1(CommonConstraints.INSTANCE.ENTITY_SITE,
		// CommonConstraints.INSTANCE.ENTITY_CHAR1);
		String ser = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "BOSER",
				GenericAuditContextHolder.getContext().getSite());
		logger.info("Admbillh :: {}", ser);

		if (Objects.nonNull(admbillhRequestBean)) {
			if (CollectionUtils.isNotEmpty(admbillhRequestBean.getAdmbilldRequestBeanList())) {
				this.admbillhRepository.saveAll(AdmbillhEntityPojoMapper.addAdmbillhEntityPojoMapper
						.apply(new Object[] { Arrays.asList(admbillhRequestBean), ser }));
			}
			if (CollectionUtils.isNotEmpty(admbillhRequestBean.getAdmbilldRequestBeanList())) {
				this.admbilldRepository.saveAll(AdmbilldEntityPojoMapper.addAdmbilldEntityPojoMapper
						.apply(new Object[] { admbillhRequestBean.getAdmbilldRequestBeanList() }));
			}

			if (CollectionUtils.isNotEmpty(admbillhRequestBean.getAdmadvanceRequestBeanList())) {
				this.admadvanceRepository.saveAll(AdmadvanceEntityPojoMapper.addAdmadvancePojoEntityMapper
						.apply(new Object[] { admbillhRequestBean.getAdmadvanceRequestBeanList() }));
			}
		}

		admbillhRequestBean.getAddressRequestBean().setAdsegment(AdSegment.PARTY.toString());
		admbillhRequestBean.getAddressRequestBean().setSer(CommonConstraints.INSTANCE.adrAdserZero);
		admbillhRequestBean.getAddressRequestBean().setSer(CommonConstraints.INSTANCE.adrAdsegment);
		admbillhRequestBean.getAddressRequestBean().setAdtype(AdType.LOC.toString());
		admbillhRequestBean.getAddressRequestBean().setTopser(CommonConstraints.INSTANCE.adrAdser);
		admbillhRequestBean.getAddressRequestBean().setState(admbillhRequestBean.state);

		this.addressRepository.save(AddressMapper.addAddressPojoEntityMapping
				.apply(new Object[] { admbillhRequestBean.getAddressRequestBean(), ser }));
		GenericAuditContextHolder.getContext().setTransactionNo(ser);
		GenericAuditContextHolder.getContext().setAuditable(Boolean.TRUE);

		admbillhRequestBean.getPartyRequestBean().setPartytype(CommonConstraints.INSTANCE.miscellaneousParty);
		admbillhRequestBean.getPartyRequestBean().setGstno(admbillhRequestBean.gstNumber);

		this.partyRepository.save(
				PartyMapper.addPartyPojoEntityMapping.apply(new Object[] { admbillhRequestBean.getPartyRequestBean(),
						GenericAuditContextHolder.getContext().getSite(), admbillhRequestBean.getPartycode() }));

		return ResponseEntity
				.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Data Added Successfully").build());
	}

	// Calculation of tds amount and rate on screen on focus change
	@Override
	public ResponseEntity<?> calculateTds(String suppbilldt, Double amount, String billtype, String partycode,
			String coy) {
		Double tdsPercentage = BigInteger.ZERO.doubleValue();
		Integer tdsAmount = BigInteger.ZERO.intValue();
		if (billtype.equals(BillTypeEnum.TRANSPORT.getValue()) || billtype.equals(BillTypeEnum.LABOUR.getValue())) {
			if (isCoyLtd(suppbilldt, partycode).equals("Y")) {
				if (StringUtils.isNotBlank(suppbilldt)) {
					Integer tdsrate = this.entityRepository
							.findEntNum1ByEntityCk_EntClassAndEntityCk_EntChar1BetweenEntityDates("TDSPA",
									PartyType.S.toString(),
									LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER));
					tdsPercentage = tdsrate != null ? Double.valueOf(tdsrate) : BigInteger.ZERO.doubleValue();
				} else {
					Integer tdsrate = this.entityRepository
							.findEntNum2ByEntityCk_EntClassAndEntityCk_EntChar1BetweenEntityDates("TDSPA",
									PartyType.S.toString(),
									LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER));
					tdsPercentage = tdsrate != null ? Double.valueOf(tdsrate) : BigInteger.ZERO.doubleValue();
				}
			}
			if (isCoyLtd(suppbilldt, partycode).equals("Y")) {
				String tdsrate = this.entityRepository
						.findByEntityCk_EntClassAndEntityCk_EntIdBetweenEntityDates("TDSPA", "00000",
								LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER))
						.split(CommonConstraints.INSTANCE.COMMA_STRING)[0];
				tdsPercentage = StringUtils.isNotBlank(tdsrate) ? Double.valueOf(tdsrate)
						: BigInteger.ZERO.doubleValue();
			} else {
				String tdsrate = this.entityRepository
						.findByEntityCk_EntClassAndEntityCk_EntIdBetweenEntityDates("TDSPA", "00000",
								LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER))
						.split(CommonConstraints.INSTANCE.COMMA_STRING)[1];
				tdsPercentage = StringUtils.isNotBlank(tdsrate) ? Double.valueOf(tdsrate)
						: BigInteger.ZERO.doubleValue();
			}

			Party partyEntity = this.partyRepository
					.findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(partycode,
							CommonUtils.INSTANCE.closeDateInLocalDateTime(), PartyType.S.toString());
			if (billtype.equals(BillTypeEnum.TRANSPORT.getValue())
					&& StringUtils.isNotBlank(partyEntity.getParPmtacnum())) {
				if (LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)
						.compareTo(LocalDate.parse("01/04/2014", CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)) < 0)
					tdsPercentage = BigInteger.ZERO.doubleValue();
				else if (LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)
						.compareTo(LocalDate.parse("14/05/2020", CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)) < 0
						|| LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).compareTo(
								LocalDate.parse("31/03/2021", CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)) > 0)
					tdsPercentage = 2D;
				else
					tdsPercentage = 1.5;
			}
			if (billtype.equals(BillTypeEnum.LABOUR.getValue())
					&& StringUtils.isNotBlank(partyEntity.getParPmtacnum())) {
				if (LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)
						.compareTo(LocalDate.parse("14/05/2020", CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)) < 0
						|| LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).compareTo(
								LocalDate.parse("31/03/2021", CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER)) > 0)
					tdsPercentage = 2D;
				else
					tdsPercentage = 1.5;
			}
			if (StringUtils.isBlank(suppbilldt))
				suppbilldt = "01/01/1980";
			Integer tdsPerc = this.entityRepository
					.findEntNum1ByEntityCk_EntClassAndEntityCk_EntChar1BetweenEntityDates("TDSPA", partycode,
							LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER));
			if (tdsPerc != null)
				tdsPercentage = tdsPerc.doubleValue();
			tdsAmount = (int) Math.round((amount * tdsPercentage) / 100);

			Map<String, Object> tdsValuesMap = new HashMap<>();
			tdsValuesMap.put("tdsPercentage", tdsPercentage);
			tdsValuesMap.put("tdsAmount", tdsAmount);
			return ResponseEntity.ok(ServiceResponseBean.builder().data(tdsValuesMap).status(Boolean.TRUE).build());
		} else {
			Map<String, Object> tdsValuesMap = new HashMap<>();
			tdsValuesMap.put("tdsPercentage", tdsPercentage);
			tdsValuesMap.put("tdsAmount", tdsAmount);
			return ResponseEntity.ok(ServiceResponseBean.builder().data(tdsValuesMap).status(Boolean.TRUE).build());
		}
	}

	private String isCoyLtd(String suppbilldt, String partycode) {
		if (StringUtils.isBlank(suppbilldt))
			suppbilldt = LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER);

		Query ltdQuery = this.entityManager
				.createNativeQuery("SELECT FUNC_GetCompanyType(:partytype, :partycode, :suppbilldt) FROM dual");

		ltdQuery.setParameter("partytype", PartyType.S.toString());
		ltdQuery.setParameter("partycode", partycode);
		ltdQuery.setParameter("suppbilldt", LocalDate.parse(suppbilldt, CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER));

		Object singleResult = ltdQuery.getSingleResult();
		logger.info("SingleResult :: ", singleResult.toString());

		if (singleResult.toString().equals("Individual") || singleResult.toString().equals("HUF"))
			return "N";
		else
			return "Y";
	}

//		//Accounting Logic and entry 
//				//Actranh
//				if(admbillhRequestBean.getIsUpdate()) {
//					Actranh actranh = this.actranhRepository.findByActranhCK_ActhTranserAndActranhCK_ActhCoy(admbillhRequestBean.getSer(), admbillhRequestBean.getCoy().trim());
//					if(Objects.nonNull(actranh)) {
//						if(StringUtils.isNotBlank(actranh.getActhBbookyn()) && actranh.getActhBbookyn().trim().equals("Y")) {
//							Integer counter = this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntId("#REVN", "#REVN").intValue();
//
//							this.actranhxRepository.save(ActranhxEntityEntityMapper.addActranhxEntityPojoMapper.apply(new Object[] {actranh, counter.toString(), companyEntity.getCoyCurrepnum() + 1 }));
//							companyEntity.setCoyCurrepnum(companyEntity.getCoyCurrepnum() + 1);
//							this.companyRepository.save(companyEntity);
//							List<Actrand> actrandList = this.actrandRepository.findByActrandCK_ActdTranserAndActrandCK_ActdCoy(admbillhRequestBean.getSer(), admbillhRequestBean.getCoy());
//							if(CollectionUtils.isNotEmpty(actrandList)) {
//								this.actrandxRepository.saveAll(ActrandxEntityEntityMapper.addActrandxEntityPojoMapper.apply(actrandList, counter.toString()));
//							}
//						}
//
//						admbillh admbillhEntity = this.admbillhRepository.findByadmbillhCK_admbillhSer(admbillhRequestBean.getSer().trim());
//						LOGGER.info("admbillh :: {}", admbillhEntity);
//
//						admbilld admbilldEntity = this.admbilldRepository.findByadmbilldCK_admbilldSerAndadmbilldCK_admbilldLineno(admbillhRequestBean.getSer().trim(), BigInteger.ONE.intValue());
//						LOGGER.info("admbilld :: {}", admbilldEntity);
//
//						admbillhRequestBean.setOmspurcyn(Objects.nonNull(admbillhRequestBean.getState()) && admbillhRequestBean.getState().equals("MAH") ? "N" : "Y");
//
//						//				if(admbillhRequestBean.getRetention() > 0 && admbillhRequestBean.getRetainos() <= 0) // Condition added after being added in old erp today as on 03/05/2023
//						//                	admbillhRequestBean.setRetainos(admbillhRequestBean.getRetention());
//
//						this.admbillhRepository.save(admbillhMapper.updateadmbillhPojoEntityMapper.apply(admbillhEntity, admbillhRequestBean));
//						this.pbilldRepository.save(admbillMapper.updateadmbillEntityPojoMapper.apply(admbillEntity, admbillhRequestBean.getadmbilldRequestBean()));
//
//						//this.pbillvatRepository.deletePbillvatBySer(pbillhRequestBean.getSer().trim());
//						// We delete exisiting records and insert new ones
//						//this.pbillvatRepository.saveAll(PbillvatMapper.addPbillvatPojoEntityMapper.apply(new Object[] {pbillhRequestBean.getPbillvatRequestBean(), pbillhRequestBean.getSer(), uom, quantity, rate}));
//
//						// We delete exisiting records and insert new ones
//						//				this.dcRepository.deleteDcByEntryNo(admbillhRequestBean.getSer());
//						//Query createQuery = this.entityManager.createQuery("Delete Dc d where trim(d.dcCK.dcpEntryno) = :entryno");
//						createQuery.setParameter("entryno", admbillhRequestBean.getSer());
//						int rowCount = createQuery.executeUpdate();
//						//List<Dc> apply = DcMapper.addDcEntityPojoMapper.apply(admbillhRequestBean.getDcRequestBean(), admbillhRequestBean.getSer());
//						//				if(rowCount != 0)
//						//this.dcRepository.saveAll(apply);
//					}
//					ActranhBean actranhBean = ActranhBean.builder()
//							.proprietor(pbillhRequestBean.getProp())
//							.coy(pbillhRequestBean.getCoy())
//							.partycode(pbillhRequestBean.getPartycode())
//							.tranamt(Double.valueOf(pbillhRequestBean.getAmount() - pbillhRequestBean.getTdsamount()))
//							.narrative(pbillhRequestBean.getNarration())
//							.vounum(pbillhRequestBean.getSuppbillno())
//							.voudate(pbillhRequestBean.getSuppbilldt())
//							.bbookyn("N")
//							.site(GenericAuditContextHolder.getContext().getSite())
//							.userid(GenericAuditContextHolder.getContext().getUserid())
//							.build(); 
//
//					this.actranhRepository.save(UpdatePojoEntityMapper.updateActranhEntityPojoMapper.apply(actranh, actranhBean));
//					this.actrandRepository.deleteActrand(pbillhRequestBean.getSer().trim());
//				}else {
//					this.admbillhRepository.save(admbillhMapper.addadmbillhPojoEntityMapper.apply(admbillhRequestBean));
//
//					admbillhRequestBean.getadmbilldRequestBean().setUom(uom);
//					admbillhRequestBean.getadmbilldRequestBean().setQuantity(quantity);
//					this.admbilldRepository.save(admbillhMapper.addadmbillhPojoEntityMapper.apply(admbillhRequestBean.getadmbilldRequestBean(), serNumber));
//
//					//this.pbillvatRepository.saveAll(PbillvatMapper.addPbillvatPojoEntityMapper.apply(new Object[] {pbillhRequestBean.getPbillvatRequestBean(), serNumber, uom, quantity, rate}));
//					//this.dcRepository.saveAll(DcMapper.addDcEntityPojoMapper.apply(pbillhRequestBean.getDcRequestBean(), serNumber));
//
//					this.actranhRepository.save(AddPojoEntityMapper.addActranhPojoEntityMapping.apply(new Object[] {ActranhBean.builder()
//							.transer(pbillhRequestBean.getSer())
//							.trantype(TranTypeEnum.BA.toString())
//							.trandate(gstTranDate)	
//							.ledgcode("PL")
//							.partytype(CommonConstraints.INSTANCE.SUPPLIERS)
//							.partycode(pbillhRequestBean.getPartycode())
//							.tranamt(Double.valueOf(pbillhRequestBean.getAmount() - pbillhRequestBean.getTdsamount()))
//							.voudate(pbillhRequestBean.getSuppbilldt())
//							.vounum(pbillhRequestBean.getSuppbillno())
//							.postedyn("Y")
//							.balancedyn("Y")
//							.closingjvyn("N")
//							.bbookyn("N")
//							.cbookyn("N")
//							.narrative(pbillhRequestBean.getNarration())
//							.proprietor(companyEntity.getCompanyCK().getCoyProp().trim())
//							.coy(pbillhRequestBean.getCoy())
//							.site(GenericAuditContextHolder.getContext().getSite())
//							.userid( GenericAuditContextHolder.getContext().getUserid())
//							.clearacyn("Y")
//							.reverseyn("N")
//							.build()}));
//				}
//}

	@Override
	public ResponseEntity<?> updateAdmbillh(AdmbillhRequestBean admbillhRequestBean) throws ParseException {

		String SiteFromDBEntity = this.entityRepository.findByEntityCk_EntClassAndEntityCk_EntChar1(
				CommonConstraints.INSTANCE.ENTITY_SITE, CommonConstraints.INSTANCE.ENTITY_CHAR1);
		logger.info("Entity :: {}", SiteFromDBEntity);

		Admbillh admbillhEntity = this.admbillhRepository.findByAdmbillhCK_AdblhSer(admbillhRequestBean.getSer());

		List<Admbilld> admbilldEntityList = this.admbilldRepository
				.findByAdmbilldCK_AdbldSer(admbillhRequestBean.getSer());
		logger.info("AdmbilldEntityList :: {}", admbilldEntityList);

		if (Objects.nonNull(admbillhEntity)) {

			Party partyEntity = this.partyRepository
					.findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(
							admbillhEntity.getAdblhPartycode().trim(), CommonUtils.INSTANCE.closeDateInLocalDateTime(),
							CommonConstraints.INSTANCE.miscellaneousParty);

			Address addressEntity = this.addressRepository
					.findByAddressCK_AdrAdownerAndAddressCK_AdrAdsegmentAndAddressCK_AdrAdtypeAndAddressCK_AdrAdser(
							admbillhRequestBean.getSer().trim(), AdSegment.PARTY.toString(), AdType.LOC.toString(),
							CommonConstraints.INSTANCE.adrAdsegment);
			logger.info("AddressEntity :: {}", addressEntity);

			if (Objects.nonNull(admbillhRequestBean))
				this.admbillhRepository.save(AdmbillhEntityPojoMapper.updateAdmbillhEntityPojoMapper
						.apply(admbillhEntity, admbillhRequestBean));

			if (CollectionUtils.isNotEmpty(admbillhRequestBean.getAdmbilldRequestBeanList())) {
				List<AdmbilldRequestBean> admbilldRequestBeanToAdd = new ArrayList<>();

				admbillhRequestBean.getAdmbilldRequestBeanList().stream().filter(filter -> {
					return filter.getIsAdd();
				}).map(admbilldRequestBean -> {
					admbilldRequestBeanToAdd.add(admbilldRequestBean);
					return admbillhRequestBean;
				}).collect(Collectors.toList());
				logger.info("admbilldRequestBeanToAdd :: {}", admbilldRequestBeanToAdd);

				if (CollectionUtils.isNotEmpty(admbilldRequestBeanToAdd))
					this.admbilldRepository.saveAll(AdmbilldEntityPojoMapper.addAdmbilldEntityPojoMapper.apply(
							new Object[] { admbilldRequestBeanToAdd, admbillhEntity.getAdmbillhCK().getAdblhSer() }));

				admbilldEntityList.stream().map(admbilldEntity -> {
					admbillhRequestBean.getAdmbilldRequestBeanList().stream().filter(filter -> {
						return admbilldEntity.getAdmbilldCK().getAdbldSer().trim().equals(filter.getSer().trim())
								&& admbilldEntity.getAdmbilldCK().getAdbldLineno().equals(filter.getLineno())
								&& !filter.getIsAdd();
					}).map(admbilldRequestBean -> {
						this.admbilldRepository.save(AdmbilldEntityPojoMapper.updateAdmbilldEntityPojoMapper
								.apply(admbilldEntity, admbilldRequestBean));
						return admbilldRequestBean;
					}).collect(Collectors.toList());
					return admbilldEntity;
				}).collect(Collectors.toList());
			}

			if (Objects.isNull(addressEntity)) {
				admbillhRequestBean.getAddressRequestBean().setAdsegment(AdSegment.PARTY.toString());
				admbillhRequestBean.getAddressRequestBean().setSer(CommonConstraints.INSTANCE.adrAdserZero);
				admbillhRequestBean.getAddressRequestBean().setSer(CommonConstraints.INSTANCE.adrAdsegment);
				admbillhRequestBean.getAddressRequestBean().setAdtype(AdType.LOC.toString());
				admbillhRequestBean.getAddressRequestBean().setTopser(CommonConstraints.INSTANCE.adrAdser);
				admbillhRequestBean.getAddressRequestBean().setState(admbillhRequestBean.state);
				this.addressRepository.save(AddressMapper.addAddressPojoEntityMapping
						.apply(new Object[] { admbillhRequestBean.getAddressRequestBean(), SiteFromDBEntity,
								admbillhRequestBean.getSer().trim() }));
			} else {
				this.addressRepository.save(AddressMapper.updateAddressPojoEntityMapping.apply(addressEntity,
						admbillhRequestBean.getAddressRequestBean()));
			}

			GenericAuditContextHolder.getContext().setTransactionNo(admbillhRequestBean.getSer());
			GenericAuditContextHolder.getContext().setAuditable(Boolean.TRUE);

			if (Objects.nonNull(admbillhRequestBean.getPartyRequestBean())) {
				admbillhRequestBean.getPartyRequestBean().setGstno(admbillhRequestBean.gstNumber);
				this.partyRepository.save(PartyMapper.updatePartyEntityPojoMapper.apply(partyEntity,
						admbillhRequestBean.getPartyRequestBean()));
			}
			return ResponseEntity
					.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Updated Successfully").build());
		} else {
			return ResponseEntity
					.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Data Found").build());
		}
	}

	@Override
	public ResponseEntity<?> checkserExists(String ser) {
		Admbillh admbillhEntity = this.admbillhRepository.findByAdmbillhCK_AdblhSer(ser.trim());
		logger.info("AdmbillhEntity :: {}", admbillhEntity);
		if (admbillhEntity != null) {
			return ResponseEntity.ok(ServiceResponseBean.builder()
					.message(ser + " is exists under Ser " + admbillhEntity.getAdmbillhCK().getAdblhSer())
					.status(Boolean.TRUE).build());
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).build());
	}
}
