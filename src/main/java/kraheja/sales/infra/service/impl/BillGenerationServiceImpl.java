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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.Hsnsacmaster;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.exception.InternalServerError;
import kraheja.sales.bean.entitiesresponse.DBResponseForNewInfrBill;
import kraheja.sales.bean.entitiesresponse.InfrBillDBResponse;
import kraheja.sales.infra.bean.request.InfraAuxiBillRequest;
import kraheja.sales.infra.bean.response.BillResponse;
import kraheja.sales.infra.service.BillGenerationService;
import kraheja.sales.infra.utilities.DateUtill;
import kraheja.sales.repository.FlatownerRepository;
import kraheja.sales.repository.FlatsOutrateRepository;
import kraheja.sales.repository.InfrBillRepository;
import kraheja.sales.repository.OutinfraRepository;
import kraheja.sales.repository.OutrateRepository;
import lombok.extern.log4j.Log4j2;

/**
 * <p>this service implementation for generate a bill.</p>
 * @author sazzad.alom
 * @since 20-OCTOBER-2023
 * @version 1.0.0
 */
@Log4j2
@Service
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
	
	@Override
	public BillResponse getBillDetail(InfraAuxiBillRequest billRequest) {
		String bldgCode, wing, flatNum, billDesc, quaterEndDate;
		Double adminRate = 0.00, infrRate = 0.00;

		Map<String, Double> gstRate = this.getGstRate(billRequest);
		log.debug("gstRate obtaint: {}", gstRate);
		
		String yearMonth = DateUtill.startYearMonthFromInput(billRequest.getBillDate());
		List<String> flatOwnerIdList = flatsOutrateRepository.findDistinctFlatOwnerIds(yearMonth,
				this.getQuaterEndYearMonth(billRequest.getBillDate()), billRequest.getOwnerIdFrom(),
				billRequest.getOwnerIdTo());
		log.debug("flatOwnerIdList obtaint: {}", flatOwnerIdList);

		for (String flatOwnerId : flatOwnerIdList) {
			String flatOwnerIdTrim = flatOwnerId.trim();
			bldgCode = flatOwnerId.substring(0, 4);
			wing = flatOwnerId.substring(4, 5);
			flatNum = flatOwnerId.substring(5, 12).trim();
			String billMode = flatownerRepository.getBillMode(flatOwnerId);
			log.debug("billMode obtaint: {}", billMode);

			String ogStartMonth = this.getOGStartMonth(bldgCode, wing, flatNum,billRequest.getBillType(), yearMonth);
			log.debug("ogStartMonth obtaint: {}", ogStartMonth);
			
			String getOGEndMonth = this.getOGEndMonth(bldgCode, wing, flatNum,billRequest.getBillType(), yearMonth);
			log.debug("getOGEndMonth obtaint: {}", getOGEndMonth);
			
			Map<String, Double> rate = this.getRate(bldgCode, wing, flatNum, billRequest.getBillType());
			log.debug("infraRate and adminRate obtaint: {}", rate);
			
			if (!ogStartMonth.equals(getOGEndMonth) && !ogStartMonth.equals("")) {
				if (billMode.equals("Q")) {
					billDesc = "Quaterly";
					quaterEndDate = getQuaterEndYearMonth(billRequest.getBillDate());
					
					String yearPart = billRequest.getBillDate().substring(6, 10);
					String monthPart = billRequest.getBillDate().substring(3, 5);
					
					int startQuarterMonth = Integer.parseInt(yearPart) + Integer.parseInt(monthPart);
					
					if(Integer.parseInt(ogStartMonth) > startQuarterMonth) {
						startQuarterMonth = Integer.parseInt(ogStartMonth);
					}
					
					while(startQuarterMonth >= Integer.parseInt(quaterEndDate)) {
						
						infrRate = rate.get("infraRate") + infrRate;
						adminRate = rate.get("adminRate") + adminRate;
						Map<String, Integer> totalGstGetter = this.totalGstGetter(billRequest.getBillType(), gstRate, infrRate, adminRate);
						log.debug("totalGstGetter obtaint: {}", totalGstGetter);

						startQuarterMonth = Integer.parseInt(DateUtill.increaseMonth(yearPart+monthPart));
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

			
			if (Objects.nonNull(infrBillDBResponse)) {
				return BillResponse.builder().billNumber(infrBillDBResponse.getInfrBillnum())
						.ownerId(infrBillDBResponse.getInfrOwnerId())
						.month(infrBillDBResponse.getInfrMonth())
						.billDate(infrBillDBResponse.getInfrBilldate().toString())
						.billFromDate(infrBillDBResponse.getInfrFromdate().toString())
						.billToDate(infrBillDBResponse.getInfrTodate().toString())
						.billAmount(String.valueOf(infrBillDBResponse.getInfrAmtos()))
						.billArrears(String.valueOf(infrBillDBResponse.getInfrArrears()))
						.interest(String.valueOf(infrBillDBResponse.getInfrInterest()))
						.interestArrears(String.valueOf(infrBillDBResponse.getInfrInterest()))
						.admin(String.valueOf(infrBillDBResponse.getInfrAdmincharges()))
						.cgst(String.valueOf(infrBillDBResponse.getInfrCgst()))
						.sgst(String.valueOf(infrBillDBResponse.getInfrSgst()))
						.igst(String.valueOf(infrBillDBResponse.getInfrIgst()))
						.cgstPerc(String.valueOf(infrBillDBResponse.getInfrCgstperc()))
						.sgstPerc(String.valueOf(infrBillDBResponse.getInfrSgstperc()))
						.igstPerc(String.valueOf(infrBillDBResponse.getInfrIgstperc()))
						.invoiceNumber(infrBillDBResponse.getInfrInvoiceNo()).build();
			}else {
//				String billNumber = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "#-Mumbai", GenericAuditContextHolder.getContext().getSite());
				String billNumber = "IN045651";
				log.debug("billNumber: {}", billNumber);
				infrRate = rate.get("infraRate");
				adminRate = rate.get("adminRate");
				
				Map<String, Integer> totalGstGetter = this.totalGstGetter(billRequest.getBillType(), gstRate, infrRate, adminRate);
				log.debug("totalGstGetter obtaint: {}", totalGstGetter);
				int month = Integer.parseInt(yearMonth.substring(4, 6));
				int year = Integer.parseInt(yearMonth.substring(0, 4));
				int lastDayOfMonth = YearMonth.of(year, month).lengthOfMonth();
				
				Map<String, Double> bill = this.getBillForOwner(billRequest, flatOwnerIdTrim, bldgCode, wing);
				
				return BillResponse.builder().billNumber(billNumber)
						.ownerId(flatOwnerIdTrim)
						.month(yearMonth)
						.billDate(billRequest.getBillDate())
						.billFromDate(billRequest.getBillDate())
						.billToDate(year + "-" + month + "-" + lastDayOfMonth)
						.billAmount(String.valueOf(bill.get("billInterest")))
						.billArrears(String.valueOf(bill.get("calArrears")))
						.interest(String.valueOf(bill.get("billInterest")))
						.interestArrears(String.valueOf(bill.get("interestArrears")))
						.admin(String.valueOf(adminRate))
						.cgst(String.valueOf(totalGstGetter.get("cgstAmount")))
						.sgst(String.valueOf(totalGstGetter.get("sgstAmount")))
						.igst(String.valueOf(totalGstGetter.get("igstAmount")))
						.cgstPerc(String.valueOf(gstRate.get("cgstPer")))
						.sgstPerc(String.valueOf(gstRate.get("sgstPer")))
						.igstPerc(String.valueOf(gstRate.get("igstPer")))
						.invoiceNumber("")
						.build();
			}
		}
		return null;
	}

	private Map<String, Double> getBillForOwner(InfraAuxiBillRequest billRequest,String flatOwnerId, String bldgCode, String wing) {
		String flatNum = flatOwnerId.substring(5, 11).trim();
		String lastBillNumber = infrBillRepository.fetchBillNumber(flatOwnerId, bldgCode, wing, billRequest.getChargeCode(),  billRequest.getBillType(), billRequest.getBillDate());
		log.debug("lastBillNumber : {}", lastBillNumber);

		String getMaxMonth = infrBillRepository.getInfrMonth(flatOwnerId, bldgCode, wing, billRequest.getChargeCode(), lastBillNumber, billRequest.getBillType());
		log.debug("getMaxMonth : {}", getMaxMonth);
		
		DBResponseForNewInfrBill dBResponseForNewInfrBill = infrBillRepository.fetchBillDateAndOldBalanceAndArearsAndInterestAndIntArears();
		log.debug("date and balance and arearsandinterest and intarears obtaint : {}", dBResponseForNewInfrBill);
		
		
		
		return this.calculateIntrest(bldgCode, wing, flatNum, dBResponseForNewInfrBill, billRequest.getBillType(),  billRequest.getBillDate());
	}

	private Map<String, Double> calculateIntrest(String bldgCode, String wing, String flatNum, DBResponseForNewInfrBill newInfrBill, String billType, String billDate) {
		
		Map<String, Double> interestMap = new HashMap<>();
		
		Double billArrears = newInfrBill.getArrears();
		Double oldBalance = newInfrBill.getBalance();
		Double intArrears = newInfrBill.getIntarrears();
		Double billInterest = newInfrBill.getInterest();
		Double interestArrears = newInfrBill.getIntarrears();
		
		
		String quaterEndYearMonth = this.getQuaterEndYearMonth(billDate);
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
		
		Timestamp lastTimeStamp = outinfraRepository.fetchLastReceptDate((bldgCode + wing + flatNum),"AUXI", billType );
		Timestamp sqlTimestamp = lastTimeStamp;
		LocalDate lastRecDate = sqlTimestamp.toLocalDateTime().toLocalDate();
		//RECEIPT CALCULATION STARTS FROM HERE
		List<Tuple> tupleObj = outinfraRepository.fetchRecDateAndAmtPaidAndIntPaid(bldgCode,"AUXI", wing, flatNum, lastRecDate, newInfrBill.getBillDate(), billType);
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

	private Map<String, Double> getRate(String bldgCode, String wing, String flatNum, String billType) {
		HashMap<String, Double> rateMap = new HashMap<>();
		
		String adminRateDB = outrateRepository.findAdminRateMonthWise(bldgCode, wing, flatNum, billType);
		double adminRate = Double.parseDouble(adminRateDB);
		log.debug("adminRate from db: {}", adminRate);
		rateMap.put("adminRate", adminRate);
		
		String auxiRateDb = outrateRepository.findOutrAuxiRateMonthWise(bldgCode, wing, flatNum, billType);
		double maintRate = Double.parseDouble(auxiRateDb);
		log.debug("auxiRate from db: {}", maintRate);
		rateMap.put("infraRate", maintRate);
		
		
		return rateMap;
	}

	private String getOGStartMonth(String bldgCode, String wing, String flatNum, String billType, String date) {
		return outrateRepository.fetchStartDeteBybldgCodeWingFlatBillTypeAndBetweenDate(bldgCode, wing, flatNum, billType,date);
	}

	private String getOGEndMonth(String bldgCode, String wing, String flatNum, String billType, String date) {
		return outrateRepository.fetchEndDeteBybldgCodeWingFlatBillTypeAndBetweenDate(bldgCode, wing, flatNum, billType,date);
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
		String quaterMonth, quaterYear;
		int intMonth, IntYear;

		intMonth = Integer.parseInt(billDate.substring(3, 5));
		IntYear = Integer.parseInt(billDate.substring(6));

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
