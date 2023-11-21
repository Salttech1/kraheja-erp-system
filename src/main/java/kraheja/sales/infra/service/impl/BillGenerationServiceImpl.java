package kraheja.sales.infra.service.impl;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.persistence.Tuple;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.Hsnsacmaster;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import kraheja.constant.ApiResponseCode;
import kraheja.constant.ApiResponseMessage;
import kraheja.constant.Result;
import kraheja.exception.InternalServerError;
import kraheja.sales.bean.entitiesresponse.DBResponseForNewInfrBill;
import kraheja.sales.bean.entitiesresponse.InfrBillDBResponse;
import kraheja.sales.entity.Infrsaogrp01_Print;
import kraheja.sales.entity.Infrsaogrp01_PrintCK;
import kraheja.sales.infra.bean.request.InfraAuxiBillRequest;
import kraheja.sales.infra.bean.response.BillResponse;
import kraheja.sales.infra.service.BillGenerationService;
import kraheja.sales.repository.FlatownerRepository;
import kraheja.sales.repository.FlatsOutrateRepository;
import kraheja.sales.repository.InfrBillRepository;
import kraheja.sales.repository.Infrsaogrp01_PrintRepository;
import kraheja.sales.repository.OutinfraRepository;
import kraheja.sales.repository.OutrateRepository;
import kraheja.utility.DateUtill;
import lombok.extern.log4j.Log4j2;

/**
 * <p>this service implementation for generate a bill.</p>
 * @author sazzad.alom
 * @since 20-OCTOBER-2023
 * @version 1.0.0
 */

@Log4j2
@Service
@Transactional
public class BillGenerationServiceImpl implements BillGenerationService {

	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private HsnsacmasterRepository hsnsacmasterRepository;
	@Autowired
	private EntityRepository entityRepository;
	@Autowired
	private FlatsOutrateRepository flatsOutrateRepository;
	@Autowired
	private FlatownerRepository flatownerRepository;

	@Autowired
	private OutrateRepository outrateRepository;
	
	@Autowired
	private InfrBillRepository infrBillRepository;
	
	@Autowired
	private OutinfraRepository outinfraRepository;
	
	@Autowired
	private Infrsaogrp01_PrintRepository printRepository;
	
	@Autowired
	private CompanyRepository companyRepository;
	
	@Override
	public BillResponse getBillDetail(InfraAuxiBillRequest billRequest) {
		String sessionId = GenericCounterIncrementLogicUtil.generateTranNo("#SESS", "#SESS");
		log.debug("session id obtaint: {}", sessionId);
		
		String bldgCode, wing, flatNum, billDesc, quaterEndDate;
		Double adminRate = 0.00, infrRate = 0.00;
		String billDate = billRequest.getBillDate();

		String billRecDate = DateUtill.dateFormatter(billRequest.getBillRecDate());
		log.debug("billRecDate obtaint: {}", billRecDate);

		billRequest.setBillDate(billRecDate);
		
		Map<String, Double> gstRate = this.getGstRate(billRequest);
		log.debug("gstRate obtaint: {}", gstRate);

		String yearMonth = DateUtill.startYearMonthFromInput(billRecDate);
		log.debug("yearMonth obtaint value obtaint after passed date utill : {}", yearMonth);
		
		// Accept the value like 202311
		String quaterEndYearMonth = this.getQuaterEndYearMonth(yearMonth);
		log.debug("quaterEndYearMonth obtaint after passed getQuaterEndYearMonth: {}", quaterEndYearMonth);

		
		List<String> flatOwnerIdList = flatsOutrateRepository.findDistinctFlatOwnerIds(yearMonth,quaterEndYearMonth, billRequest.getOwnerIdFrom(),
				billRequest.getOwnerIdTo(),billRequest.getBillType());
		log.debug("flatOwnerIdList obtaint: {}", flatOwnerIdList);


		String yearPart = billRequest.getBillDate().substring(6, 10);
		String monthPart = billRequest.getBillDate().substring(3, 5);
		yearMonth = yearPart+monthPart;
		
		int startQuarterMonth = Integer.parseInt(yearMonth) ;
		
		for (String flatOwnerId : flatOwnerIdList) {
			String flatOwnerIdTrim = flatOwnerId.trim();
			bldgCode = flatOwnerId.substring(0, 4);
			wing = flatOwnerId.substring(4, 5);
			flatNum = flatOwnerId.substring(5, 12).trim();
			String billMode = flatownerRepository.getBillMode(flatOwnerId);
			log.debug("billMode:{} bldgCode:{} wing:{} flatnumm:{}", billMode, bldgCode,  wing, flatNum);
			
			String ogStartMonth = this.getOGStartMonth(bldgCode, wing, flatNum, billRequest.getBillType(), yearMonth);
			log.debug("ogStartMonth obtaint: {}", ogStartMonth);
			
			String getOGEndMonth = this.getOGEndMonth(bldgCode, wing, flatNum, billRequest.getBillType(), yearMonth);
			log.debug("getOGEndMonth obtaint: {}", getOGEndMonth);
			
			if (ogStartMonth == null || getOGEndMonth == null) {
				throw new InternalServerError(ApiResponseMessage.ADMIN_AND_MAINTANACE_RATE_ZERO);
			}
			
			
			Map<String, Double> rate = this.getRate(bldgCode, wing, flatNum, billRequest.getBillType(), billRequest.getChargeCode(), ogStartMonth);
			log.debug("infraRate and adminRate obtaint: {}", rate);
			
			if (!ogStartMonth.equals(getOGEndMonth) && !ogStartMonth.equals("")) {
				if (billMode.equals("Q")) {
					billDesc = "Quaterly";
					quaterEndDate = getQuaterEndYearMonth(yearMonth);
					if(Integer.parseInt(ogStartMonth) > startQuarterMonth) {
						startQuarterMonth = Integer.parseInt(ogStartMonth);
					}
					while(startQuarterMonth <= Integer.parseInt(quaterEndDate)) {
						infrRate = rate.get("infraRate") + infrRate;
						adminRate = rate.get("adminRate") + adminRate;
						Map<String, Integer> totalGstGetter = this.totalGstGetter(billRequest.getBillType(), gstRate, infrRate, adminRate);
						log.debug("totalGstGetter obtaint: {}", totalGstGetter);

						startQuarterMonth = Integer.parseInt(DateUtill.increaseMonth(String.valueOf(startQuarterMonth)));
					}
				}
				else {
					infrRate = rate.get("infraRate");
					adminRate = rate.get("adminRate");
					
					billDesc = "Monthly";
					int month = Integer.parseInt(yearMonth.substring(4, 6));
					int year = Integer.parseInt(yearMonth.substring(0, 4));
					int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
					
					Map<String, Integer> totalGstGetter = this.totalGstGetter(billRequest.getBillType(), gstRate, infrRate, adminRate);
					log.debug("totalGstGetter obtaint: {}", totalGstGetter);
				}
			}
			
			//CHECK IF BILL ALREADY THERE
			InfrBillDBResponse infrBillDBResponse = this.infrBillRepository.fetchDetail(yearMonth, flatOwnerIdTrim, billRequest.getChargeCode(), billRequest.getBillType());
			log.debug("infrBillDBResponse obtaint : {}", infrBillDBResponse);
			
			//AS PER ATUL REQUIREMENT 
			String bldgCompany = buildingRepository.findBldgCompanyByBldgCode(bldgCode);
			String companyName = companyRepository.findByCompanyCKCoyCode(bldgCompany);
			
		
			if (Objects.nonNull(infrBillDBResponse)) {
				//TODO FETCH ALREADY HAVE A SESSION ID  
				List<String> fetchSessionId = printRepository.findSessionIdByBillNumAndMonthAndOwnerId(infrBillDBResponse.getInfrBillnum(),infrBillDBResponse.getInfrMonth(), flatOwnerId.trim() );
				if (StringUtils.isNoneEmpty(fetchSessionId.get(0))) {
					sessionId = fetchSessionId.get(0);
				}else {
					Infrsaogrp01_PrintCK printCK = Infrsaogrp01_PrintCK.builder()
							.saogrpBillnum(infrBillDBResponse.getInfrBillnum())
							.saogrpInvoiceno(infrBillDBResponse.getInfrInvoiceNo()) 
							.saogrpSessid(Double.parseDouble(sessionId))
							.build(); 
					
					Infrsaogrp01_Print print = Infrsaogrp01_Print
							.builder()
							.infrsaogrp01_printCK(printCK)
							.saogrpOwnerid(flatOwnerId)
							.saogrpBldgcode(bldgCode)
							.saogrpWing(wing)
							.saogrpFlatnum(flatNum)
							.saogrpBillmonth(yearMonth)
							.saogrpBilldate(billRequest.getBillRecDate().toLocalDate())
							.saogrpBillamt(infrRate)
							.saogrpBillarrears(infrBillDBResponse.getInfrArrears())
							.saogrpInterest(infrBillDBResponse.getInfrInterest())
							.saogrpIntarrears(infrBillDBResponse.getInfrIntarrears())
							.saogrpBillfrom(billRequest.getBillRecDate().toLocalDate())
							.saogrpBillto(infrBillDBResponse.getInfrTodate())
							.saogrpOutrate(infrRate)
							.saogrpAdmincharges(adminRate)
							.saogrpCgst(infrBillDBResponse.getInfrCgst())
							.saogrpSgst(infrBillDBResponse.getInfrSgst())
							.saogrpIgst(infrBillDBResponse.getInfrIgst())
							.saogrpCgstperc(infrBillDBResponse.getInfrCgstperc())
							.saogrpSgstperc(infrBillDBResponse.getInfrSgstperc())
							.saogrpIgstperc(infrBillDBResponse.getInfrIgstperc())
							.build();
					
					printRepository.save(print);
				}
			
				
				return BillResponse
						.builder()
						.result(Result.SUCCESS)
						.responseCode(ApiResponseCode.SUCCESS)
						.message(ApiResponseMessage.BILL_FETCH_SUCCESSFULLY)
						.billNumber(infrBillDBResponse.getInfrBillnum())
						.ownerId(infrBillDBResponse.getInfrOwnerId())
						.month(infrBillDBResponse.getInfrMonth())
						.billDate(infrBillDBResponse.getInfrBilldate())
						.billFromDate(infrBillDBResponse.getInfrFromdate())
						.billToDate(infrBillDBResponse.getInfrTodate())
						.billAmount(infrBillDBResponse.getInfrAmtos())
						.billArrears(infrBillDBResponse.getInfrArrears())
						.interest(infrBillDBResponse.getInfrInterest())
						.interestArrears(infrBillDBResponse.getInfrIntarrears())
						.admin(infrBillDBResponse.getInfrAdmincharges())
						.cgst(infrBillDBResponse.getInfrCgst())
						.sgst(infrBillDBResponse.getInfrSgst())
						.igst(infrBillDBResponse.getInfrIgst())
						.cgstPerc(infrBillDBResponse.getInfrCgstperc())
						.sgstPerc(infrBillDBResponse.getInfrSgstperc())
						.igstPerc(infrBillDBResponse.getInfrIgstperc())
						.invoiceNumber(infrBillDBResponse.getInfrInvoiceNo())
						.sessionId(sessionId)
						.buildingCode(bldgCode)
						.companyName(companyName)
						.build();
			}else {
				String billNumber = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "INBIL", GenericAuditContextHolder.getContext().getSite());
				log.debug("billNumber: {}", billNumber);
				
				infrRate = rate.get("infraRate");
				adminRate = rate.get("adminRate");
				
				Map<String, Integer> totalGstGetter = this.totalGstGetter(billRequest.getBillType(), gstRate, infrRate, adminRate);
				log.debug("totalGstGetter obtaint: {}", totalGstGetter);
				
				int month = Integer.parseInt(String.valueOf(startQuarterMonth-1).substring(4, 6));
				if (month == 0) {
					month = 12; 				
				}
				int year = Integer.parseInt(String.valueOf(startQuarterMonth).substring(0, 4));
				String month2D = String.format("%02d", month);
				
				int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
				String billToDate = lastDayOfMonth + "/" + month2D + "/" + year ;
				
				Map<String, Double> bill = this.getBillForOwner(billRequest, flatOwnerIdTrim, bldgCode, wing);
			
				
				Infrsaogrp01_PrintCK printCK = Infrsaogrp01_PrintCK.builder()
						.saogrpBillnum(billNumber)
						.saogrpInvoiceno("") ////TODO replace it with invoiceNumber
						.saogrpSessid(Double.parseDouble(sessionId))
						.build(); 
				
				Infrsaogrp01_Print print = Infrsaogrp01_Print
						.builder()
						.infrsaogrp01_printCK(printCK)
						.saogrpOwnerid(flatOwnerId)
						.saogrpBldgcode(bldgCode)
						.saogrpWing(wing)
						.saogrpFlatnum(flatNum)
						.saogrpBillmonth(yearMonth)
						.saogrpBilldate(billRequest.getBillRecDate().toLocalDate())
						.saogrpBillamt(infrRate)
						.saogrpBillarrears(bill.get("calArrears"))
						.saogrpInterest(bill.get("billInterest"))
						.saogrpIntarrears(bill.get("interestArrears"))
						.saogrpBillfrom(billRequest.getBillRecDate().toLocalDate())
						.saogrpBillto(DateUtill.convertStringToDateFormat(billToDate))
						.saogrpOutrate(infrRate)
						.saogrpAdmincharges(adminRate)
						.saogrpCgst(totalGstGetter.get("cgstAmount").doubleValue())
						.saogrpSgst(totalGstGetter.get("sgstAmount").doubleValue())
						.saogrpIgst(totalGstGetter.get("igstAmount").doubleValue())
						.saogrpCgstperc(gstRate.get("cgstPer"))
						.saogrpSgstperc(gstRate.get("sgstPer"))
						.saogrpIgstperc(gstRate.get("igstPer"))
						.build();
				
				
				Infrsaogrp01_Print savePrintRepository = printRepository.save(print);
				log.debug("savePrintRepository data obtaint: {}", savePrintRepository);

				
				return BillResponse.builder()
						.result(Result.SUCCESS)
						.responseCode(ApiResponseCode.SUCCESS)
						.message(ApiResponseMessage.BILL_CALCULATED_SUCCESSFULLY)
						.billNumber(billNumber)
						.ownerId(flatOwnerIdTrim)
						.month(yearMonth)
						.billDate(billRequest.getBillRecDate().toLocalDate())
						.billFromDate(billRequest.getBillRecDate().toLocalDate())
						.billToDate(DateUtill.convertStringToDateFormat(billToDate))
						.billAmount(infrRate)
						.billArrears(bill.get("calArrears"))
						.interest(bill.get("billInterest"))
						.interestArrears(bill.get("interestArrears"))
						.admin(adminRate)
						.cgst(totalGstGetter.get("cgstAmount"))
						.sgst(totalGstGetter.get("sgstAmount"))
						.igst(totalGstGetter.get("igstAmount"))
						.cgstPerc(gstRate.get("cgstPer"))
						.sgstPerc(gstRate.get("sgstPer"))
						.igstPerc(gstRate.get("igstPer"))
						.invoiceNumber("")
						.sessionId(sessionId)
						.buildingCode(bldgCode)
						.companyName(companyName)
						.build();
			}
		}
		return BillResponse.builder().result(Result.FAILED).responseCode(ApiResponseCode.FAILED).message(ApiResponseMessage.FLAT_OWNER_ID_NOT_AVAILABLE).build();
	}

	private Map<String, Double> getBillForOwner(InfraAuxiBillRequest billRequest,String flatOwnerId, String bldgCode, String wing) {
		String flatNum = flatOwnerId.substring(5, 11).trim();
		
		String lastBillNumber = infrBillRepository.fetchBillNumber(flatOwnerId, bldgCode, wing, billRequest.getChargeCode(),  billRequest.getBillType(), billRequest.getBillDate());
		log.debug("lastBillNumber : {}", lastBillNumber);

		// ADDED DEFAULT VALUE 199001
		String getMaxMonth = infrBillRepository.getInfrMonth(flatOwnerId, bldgCode, wing, billRequest.getChargeCode(), lastBillNumber, billRequest.getBillType());
		log.debug("getMaxMonth : {}", getMaxMonth);
		
		Timestamp fetchFromDate = infrBillRepository.fetchFromDate();
		LocalDate lastRecDate = fetchFromDate.toLocalDateTime().toLocalDate();
		log.debug("fromDate : {}", lastRecDate);

		// ownerId, chargeCode, month, billtype
		List<DBResponseForNewInfrBill> dBResponseForNewInfrBill1 = infrBillRepository.fetchBillDateAndOldBalanceAndArearsAndInterestAndIntArears(flatOwnerId, billRequest.getChargeCode(),"202310", billRequest.getBillType() );
		log.debug("date and balance and arearsandinterest and intarears obtaint : {}", dBResponseForNewInfrBill1);
		DBResponseForNewInfrBill dBResponseForNewInfrBill = dBResponseForNewInfrBill1.get(0);
		
		
		return this.calculateIntrest(bldgCode, wing, flatNum, dBResponseForNewInfrBill,billRequest.getChargeCode(), billRequest.getBillType(),  billRequest.getBillDate());
	}

	private Map<String, Double> calculateIntrest(String bldgCode, String wing, String flatNum, DBResponseForNewInfrBill newInfrBill,String chargeCode, String billType, String billDate) {
		
		Map<String, Double> interestMap = new HashMap<>();
		
		Double billArrears = newInfrBill.getArrears();
		Double oldBalance = newInfrBill.getBalance();
		Double intArrears = newInfrBill.getIntarrears();
		Double billInterest = newInfrBill.getInterest();
		Double interestArrears = newInfrBill.getIntarrears();
		
		String billYearMonth = DateUtill.dateToyearMonth(billDate);
		String quaterEndYearMonth = this.getQuaterEndYearMonth(billYearMonth);
		LocalDate convBillDate = DateUtill.convertStringToDateFormat(billDate);
		double intrestRate = 0.00;
		double advanceAmt = 0.00;
		double intAdvanceAmt = 0.00;
		
		//IF ARREARS IS NEGATIVE I.E. AMOUNT RECEIVED IN ADVANCE, THE BILL AMOUNT SHOULD BE DEDUCTED FROM BILLARREARS
		if (billArrears < 0) {
			if (Math.abs(billArrears) >= oldBalance) {
				billArrears = billArrears + oldBalance;
				oldBalance = 0.00;
			}else {
				oldBalance = billArrears + oldBalance;
				billArrears = 0.00;
			}
		}
		intArrears = intArrears + billInterest;
		billInterest = 0.00;
		
		double calArrears = billArrears;
		double calIntArrears = interestArrears;
		
		//-------------INTEREST RATES-------------
		if (bldgCode.equals("OM04") || bldgCode.equals("OM21")) {
			intrestRate = 15.00;
		}else {
			intrestRate = entityRepository.fetchIntereRate();
		}
		if (intrestRate == 0) {
			throw new InternalServerError("interest rates zero");
		}
		
		Timestamp lastTimeStamp = outinfraRepository.fetchLastReceptDate((bldgCode + wing + flatNum),chargeCode, billType );
		Timestamp sqlTimestamp = lastTimeStamp;
		LocalDate lastRecDate = sqlTimestamp.toLocalDateTime().toLocalDate();
		//RECEIPT CALCULATION STARTS FROM HERE
		List<Tuple> tupleObj = outinfraRepository.fetchRecDateAndAmtPaidAndIntPaid(bldgCode,chargeCode, wing, flatNum, lastRecDate, newInfrBill.getBillDate(), billType);
		log.debug("tupleObj : {}", tupleObj);

		LocalDate infRecdate = null; 
		long totalDays = 0l;
		double amtpaid, intPaid, perDayInterest;
			for (Tuple tuple : tupleObj) {
				if (tuple.get(0) == null) {
					infRecdate = DateUtill.convertStringToDateFormat(quaterEndYearMonth);
				}else {
					sqlTimestamp = tuple.get(0, Timestamp.class);
					infRecdate = sqlTimestamp.toLocalDateTime().toLocalDate();
				}
			        totalDays = ChronoUnit.DAYS.between(infRecdate, newInfrBill.getBillDate());
//			        BigDecimal bigDecimal = tuple.get(1, BigDecimal.class);
			        amtpaid =  tuple.get(1, BigDecimal.class).doubleValue();
			        intPaid = tuple.get(2, BigDecimal.class).doubleValue();
			        
			        //------------- START CALCULATING INTEREST ON AMTPAID ------------- 
			        if (amtpaid > 0) {
						if (amtpaid >= billArrears) {
							amtpaid = amtpaid - billArrears;
							if (billArrears > 0) {
								perDayInterest = (billArrears/100) * intrestRate/365 ;
								billInterest = billInterest + Math.round(perDayInterest * totalDays);
							}
							billArrears = 0.00;
							calArrears = 0.00;

							int month = newInfrBill.getBillDate().getMonthValue();
							int year = newInfrBill.getBillDate().getYear();
							int lastDate = DateUtill.getDaysInMonth(year, month);
							String lastDateOfBill = lastDate + "/" + month + "/" + year;
							
							if (amtpaid >= oldBalance ) {
								amtpaid = amtpaid - oldBalance;
								
								
								LocalDate convertlastDateOfBill = DateUtill.convertStringToDateFormat(lastDateOfBill);
								LocalDate recdate = infRecdate;
								int dateComparison = DateUtill.dateComparison(recdate,convertlastDateOfBill);
								if (dateComparison < 0) {
									perDayInterest = (oldBalance/100) * intrestRate/365 ;
									totalDays = ChronoUnit.DAYS.between(LocalDate.parse(lastDateOfBill), infRecdate);
									billInterest = billInterest + Math.round(perDayInterest * totalDays);
								}
								oldBalance=0.00;
								advanceAmt = advanceAmt + amtpaid; // Excess received amount
							}
							else {
								 oldBalance = oldBalance - amtpaid;
								 lastDate = DateUtill.getDaysInMonth(convBillDate.getYear(), convBillDate.getMonthValue());
								 lastDateOfBill = lastDate + "/" + month + "/" + year;
								 int dateComparison = DateUtill.dateComparison(infRecdate, DateUtill.convertStringToDateFormat(lastDateOfBill));
									
								 if (dateComparison < 0) {
										perDayInterest = (oldBalance/100) * intrestRate/365 ;
										totalDays = ChronoUnit.DAYS.between( infRecdate, DateUtill.convertStringToDateFormat(lastDateOfBill));
										billInterest = billInterest + Math.round(perDayInterest * totalDays);
									} 
							}
						}else {
							billArrears = billArrears - amtpaid;
							calArrears = billArrears;
							perDayInterest = (amtpaid/100) * intrestRate/365 ;
							billInterest = billInterest + Math.round(perDayInterest * totalDays);
							amtpaid = 0.00;
						}
					}
			      //------------- END CALCULATING INTEREST ON AMTPAID ------------- 
			        
				  //------------- START CALCULATING INTEREST ON INT PAID -------------
			        if (intPaid > 0) {
			        	if (intPaid >= intArrears) {
			        		intPaid = intPaid - intArrears;
			        		if (intArrears > 0) {
			        			totalDays = ChronoUnit.DAYS.between( infRecdate, convBillDate); 
			        			perDayInterest = (intArrears/100) * intrestRate/365 ;
			        			billInterest = billInterest + Math.round(perDayInterest * totalDays);
							}
			        		intArrears = 0.00;
			        		interestArrears = intArrears;
			        		intAdvanceAmt = intAdvanceAmt + intPaid;
						}else {
							totalDays = ChronoUnit.DAYS.between( infRecdate, convBillDate); 
							intArrears = intArrears - intPaid;
							interestArrears = intArrears;
							perDayInterest = (intPaid/100) * intrestRate/365 ;
							billInterest = billInterest + Math.round(perDayInterest * totalDays);
							intPaid = 0.00;
						}
			        }
			      //------------- END CALCULATING INTEREST ON INT PAID -------------
			        amtpaid = 0.00;
			        intPaid = 0.00;
			}
			totalDays = ChronoUnit.DAYS.between( infRecdate, convBillDate);
			
			if (billArrears  > 0) {
				perDayInterest = (billArrears/100) * intrestRate/365 ;
				billInterest = billInterest + Math.round(perDayInterest * totalDays);
			}
			if (oldBalance  > 0) {
				perDayInterest = (oldBalance/100) * intrestRate/365 ;
				billInterest = billInterest + Math.round(perDayInterest * totalDays);
			}
			if (intArrears  > 0) {
				perDayInterest = (intArrears/100) * intrestRate/365 ;
				billInterest = billInterest + Math.round(perDayInterest * totalDays);
			}
			if (billInterest  < 25) {
				billInterest = 0.00;
			}
			if (advanceAmt  > 0) {
				billArrears = advanceAmt * -1;
				calArrears = billArrears;
				advanceAmt = 0.00;
			}else {
				billArrears = billArrears + oldBalance;
				calArrears = billArrears;
				oldBalance = 0.00;
			}
			if (intAdvanceAmt > 0) {
				billInterest = billInterest - intAdvanceAmt;
				intAdvanceAmt = 0.00;
			}
			interestMap.put("billInterest", billInterest);
			interestMap.put("calArrears", calArrears);
			interestMap.put("interestArrears", interestArrears);
		return interestMap;
	}

	private Map<String, Integer> totalGstGetter(String billType, Map<String, Double> gstRate, Double infraRate, Double adminRate) {
		Map<String, Integer> gstMap = new HashMap<>();
		Integer cgstAmount = 0, sgstAmount = 0, igstAmount = 0;
		
		if (billType.equals("F")) {
			cgstAmount = (int) Math.round((gstRate.get("cgstPer") * adminRate) / 100.0) + cgstAmount;
			sgstAmount = (int) Math.round((gstRate.get("sgstPer") * adminRate) / 100.0) + sgstAmount;
			igstAmount = (int) Math.round((gstRate.get("igstPer") * adminRate) / 100.0) + igstAmount;
			
			cgstAmount = (int) Math.round((gstRate.get("cgstPer") * infraRate) / 100.0) + cgstAmount;
			sgstAmount = (int) Math.round((gstRate.get("sgstPer") * infraRate) / 100.0) + sgstAmount;
			igstAmount = (int) Math.round((gstRate.get("igstPer") * infraRate) / 100.0) + igstAmount;
		}
		else {
			cgstAmount = (int) Math.ceil((gstRate.get("cgstPer") * adminRate) / 100.0);
			sgstAmount = (int) Math.ceil((gstRate.get("sgstPer") * adminRate) / 100.0);
			igstAmount = (int) Math.ceil((gstRate.get("igstPer") * adminRate) / 100.0);
				
			cgstAmount = (int) Math.ceil((gstRate.get("cgstPer") * infraRate) / 100.0);
			sgstAmount = (int) Math.ceil((gstRate.get("sgstPer") * infraRate) / 100.0);
			igstAmount = (int) Math.ceil((gstRate.get("igstPer") * infraRate) / 100.0);
			}
		gstMap.put("cgstAmount", cgstAmount);
		gstMap.put("sgstAmount", sgstAmount);
		gstMap.put("igstAmount", igstAmount);
		return gstMap;
		
	}

	private Map<String, Double> getRate(String bldgCode, String wing, String flatNum, String billType, String chargeCode, String ogStartMonth) {
		log.debug("getRate receipt value bldgCode:{} wing: {} flatNum: {} billType: {} chargeCode: {}",bldgCode, wing, flatNum, billType, chargeCode );

		HashMap<String, Double> rateMap = new HashMap<>();
		double adminRate,maintRate; 
		/**
		 * CHECK CONDITION MONTH INFRA AND AUXI FOR THE OUTRATE RATE
		 */
		if (chargeCode.equals("INAP")) {
			adminRate = outrateRepository.findAdminRateMonthWiseForInfra(bldgCode, wing, flatNum, billType, ogStartMonth);
			maintRate = outrateRepository.findOutrAuxiRateMonthWiseForInfra(bldgCode, wing, flatNum, billType, ogStartMonth);
		} else {
			adminRate = outrateRepository.findAdminRateMonthWiseForAuxi(bldgCode, wing, flatNum, billType, ogStartMonth);
			maintRate = outrateRepository.findOutrAuxiRateMonthWiseForAuxi(bldgCode, wing, flatNum, billType, ogStartMonth);
		}
		log.debug("adminRate from db: {}", adminRate);
		log.debug("auxiRate from db: {}", maintRate);		
		rateMap.put("adminRate", adminRate);
		rateMap.put("infraRate", maintRate);
		
		
		return rateMap;
	}

	private String getOGStartMonth(String bldgCode, String wing, String flatNum, String billType, String date) {
		return outrateRepository.fetchStartDateBybldgCodeWingFlatBillTypeAndBetweenDate(bldgCode, wing, flatNum, billType,date);
	}

	private String getOGEndMonth(String bldgCode, String wing, String flatNum, String billType, String date) {
		return outrateRepository.fetchEndDateBybldgCodeWingFlatBillTypeAndBetweenDate(bldgCode, wing, flatNum, billType,date);
	}

	/**
	 * <p>
	 * this method use for get GST Percent state wise.
	 * </p>
	 */
	private Map<String, Double> getGstRate(InfraAuxiBillRequest request) {
		Map<String, Double> gstMap = new HashMap<>();

		String buildingState = buildingRepository
				.findBldgSalesstateByBuildingCK_BldgCode(request.getOwnerIdFrom().substring(0, 4));
		log.debug("building state obtaint: {}", buildingState);

		Integer entityCount = entityRepository.getEntityCount(buildingState);
		log.debug("entityCount obtaint: {}", entityCount);

		Hsnsacmaster gstRate = hsnsacmasterRepository.gstRate("995419",
				DateUtill.convertStringToDateFormat(request.getBillDate()));
		log.debug("gstRate : {}", gstRate);

		gstMap.put("cgstPer", 0.00);
		gstMap.put("sgstPer", 0.00);
		gstMap.put("igstPer", 0.00);
		if (entityCount > 0) {
			gstMap.put("ugstPer", gstRate.getHsmsUgstperc());
		}
		if (buildingState.equals("MAH")) {
			gstMap.put("cgstPer", gstRate.getHsmsCgstperc());
			gstMap.put("sgstPer", gstRate.getHsmsSgstperc());
		} else {
			gstMap.put("igstPer", gstRate.getHsmsIgstperc());
		}
		return gstMap;
	}

	private String getQuaterEndYearMonth(String billDate) {
		log.debug("billDate receipt the value into getQuaterEndYearMonth: {}", billDate);

		String quaterMonth, quaterYear;
		int intMonth, IntYear;
       //202311
		intMonth = Integer.parseInt(billDate.substring(4, 6));
		IntYear = Integer.parseInt(billDate.substring(0,4));

		switch (intMonth) {
		case 1:
		case 2:
		case 3:
			quaterMonth = "03";
			quaterYear = IntYear + quaterMonth;
			break;
		case 4:
		case 5:
		case 6:
			quaterMonth = "06";
			quaterYear = IntYear + quaterMonth;
			break;
		case 7:
		case 8:
		case 9:
			quaterMonth = "09";
			quaterYear = IntYear + quaterMonth;
			break;
		case 10:
		case 11:
		case 12:
			quaterMonth = "12";
			quaterYear = IntYear + quaterMonth;
			break;
		default:
			// Handle invalid month here, if needed
			quaterYear = ""; // or some other default value
			break;
		}

		return quaterYear;
	}


}
