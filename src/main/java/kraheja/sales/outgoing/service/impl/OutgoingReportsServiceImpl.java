package kraheja.sales.outgoing.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.service.serviceimpl.BuildingServiceImpl;
import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import kraheja.sales.bean.request.OutgoingReportsRequestBean;
import kraheja.sales.infra.service.impl.OutinfraServiceImpl;
import kraheja.sales.outgoing.service.OutgoingReportsService;

@Service
@Transactional
public class OutgoingReportsServiceImpl implements OutgoingReportsService {

	private static final Logger logger = LoggerFactory.getLogger(OutgoingReportsServiceImpl.class);

	@Autowired
	private OutinfraServiceImpl outinfraServiceImpl;

	@Autowired
	private BuildingServiceImpl buildingServiceImpl;

	@PersistenceContext
	private EntityManager entityManager;

	public String initBldgInfo(ResponseEntity<?> bldgInfo, String colNameToSearch, int indexOfSearchChars,
			int extractChars) {
		int indexOfInfo;
		String bldgValToReturn;
		indexOfInfo = bldgInfo.getBody().toString().indexOf(colNameToSearch) + indexOfSearchChars;
		bldgValToReturn = bldgInfo.getBody().toString().substring(indexOfInfo, indexOfInfo + extractChars);
		indexOfInfo = bldgValToReturn.indexOf(",");
		if (indexOfInfo > 0) {
			bldgValToReturn = bldgValToReturn.substring(0, indexOfInfo);
		}
		return bldgValToReturn;
	}

	public String initStartOgMonth(String ownerId, String bldgCode, String flatNo, String wing, String billMonth,
			String billType, String billMode) {
		String startDate, startOgMonth = "";
		Integer month;
		startOgMonth = "";
		Query query;
		for (int i = 0; i < 3; i++) {
			query = this.entityManager.createNativeQuery("SELECT outr_startdate FROM outrate WHERE outr_bldgcode = '"
					+ bldgCode + "' AND outr_flatnum = '" + flatNo + "' AND outr_wing = '" + wing + "' " + "AND "
					+ billMonth + " BETWEEN outr_startdate AND outr_enddate AND outr_billtype = '" + billType + "' ");
			startDate = String.valueOf(query.getSingleResult());
			if (startDate != "") {
				startOgMonth = billMonth;
				break;
			}
			if (billMode == "M") {
				break;
			}
//			month = Integer.parseInt ( "1000")  ;   
			month = Integer.parseInt(billMonth.substring(5, 6)) + 1;
			if (month.toString().length() == 1) {
				billMonth = billMonth.substring(0, 4) + "0" + month.toString();
			} else {
				billMonth = billMonth.substring(0, 4) + month.toString();
			}
		}

		return startOgMonth;
	}

//	public Double initGstInfo(ResponseEntity<?> gstInfo, String colNameToSearch, int indexOfSearchChars,
//			int extractChars) {
//		int indexOfInfo;
//		Double gstValToReturn;
//		indexOfInfo = gstInfo.getBody().toString().indexOf(colNameToSearch) + indexOfSearchChars;
//		gstValToReturn = Double
//				.valueOf(gstInfo.getBody().toString().substring(indexOfInfo, indexOfInfo + extractChars));
//		indexOfInfo = gstValToReturn.toString().indexOf(",");
//		if (indexOfInfo > 0) {
//			gstValToReturn = Double.valueOf(gstValToReturn.toString().substring(0, indexOfInfo));
//		}
//		return gstValToReturn;
//	}

	public String initQtrEndDate(String billDate, String billType, String ownerId) {
		String qtrEndPeriod = "";
		Number ogMonths;

		switch (billDate.substring(3, 5)) {
		case "01":
		case "02":
		case "03":
			qtrEndPeriod = billDate.substring(6).concat("03");
			break;
		case "04":
		case "05":
		case "06":
			qtrEndPeriod = billDate.substring(6).concat("06");
			break;
		case "07":
		case "08":
		case "09":
			qtrEndPeriod = billDate.substring(6).concat("09");
			break;
		case "10":
		case "11":
		case "12":
			qtrEndPeriod = billDate.substring(6).concat("12");
			break;
		}
		Query query;
		if (billType.equals("F")) {
			query = this.entityManager
					.createNativeQuery("SELECT Nvl(fown_ogmonths,0) FROM flatowner WHERE fown_ownerid = '" + ownerId
							+ "' AND fown_ownertype = '0'");
			ogMonths = (Number) query.getSingleResult();
			if (ogMonths.equals(0)) {
				qtrEndPeriod = "";
			} else {
				query = this.entityManager.createNativeQuery("SELECT to_char(add_months(to_date('" + billDate
						+ "','dd/mm/yyyy') ," + String.valueOf(ogMonths) + " -1),'yyyymm') from dual ");
				qtrEndPeriod = String.valueOf(query.getSingleResult());
			}
		}
		return qtrEndPeriod;

	}

	public Double calcParticularsAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId,
			String parkingNo, String carParkOwnerId, String unitBillNo, String billType, String billMode,
			String billDate, String amtColName, String billModeDesc, String invoiceNo, String irnNo, String sessionId,
			Boolean ceilingRequired, String particularDesc, String particularCode) {
//
//		String wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, billFromDate, qtrEndDateCalc = "",
//				qtrYear = "", currBillDate = "", currBillMonth, status;
		Double amount = 0.0;
//		Double cGstAmt, sGstAmt, iGstAmt = 0.0;
//		Integer startMonth, endMonth, gstAmt;
//
//		try {
//			bldgCode = ownerId.substring(0, 4);
//
//			if (ownerId == carParkOwnerId) {
//				billNo = unitBillNo;
//			}
//			wing = ownerId.substring(4, 5);
//			flatNum = ownerId.substring(5).trim();
//			billMonth = billDate.substring(6) + billDate.substring(3, 5);
//
//			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
//			if (startOgMonth != "") {
//				if (billMode == "Q") {
//					if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
//						qtrEndDateCalc = startOgMonth;
//					} else {
//						qtrEndDateCalc = qtrEndDate;
//					}
//					startMonth = Integer.parseInt(billDate.substring(3, 5));
//					endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
//					qtrYear = qtrEndDateCalc.substring(6, 10);
//					if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
//							&& startOgMonth.substring(0, 4) == qtrYear) {
//						startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
//						currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
//					}
//					for (startMonth = startMonth; startMonth <= endMonth; startMonth++) {
//						currBillMonth = qtrYear;
//						if (startMonth <= 9) {
//							currBillMonth = currBillMonth + "0" + startMonth;
//						} else {
//							currBillMonth = currBillMonth + startMonth;
//						}
//						amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
//						if (currBillDate == "") {
//							currBillDate = billDate;
//						}
////						status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
////								currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
////								particularCode, billModeDesc, invoiceNo, irnNo);
////						if (status != "SUCCESS") {
////							return -3.0 ;	
////						}
//					}
//					bldgCode = bldgCode;
//				} else {
//					endMonth = Integer.parseInt(billDate.substring(3, 5));
//					qtrYear = billDate.substring(6, 10);
//					if (endMonth <= 9) {
//						currBillMonth = qtrYear + "0" + endMonth.toString();
//					} else {
//						currBillMonth = qtrYear + endMonth.toString();
//					}
//					amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
//					if (currBillDate == "") {
//						currBillDate = billDate;
//					}
////					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
////							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
////							particularCode, billModeDesc, invoiceNo, irnNo);
////					if (status != "SUCCESS") {
////						parkingNo = "";
////						return amount;
////					}
//				}
//			} else {
//				return -1.0; // Start OG Month is blank
//			}
//
//		} catch (Exception e) {
//			return -2.0; // Error in amount calculation
//		}
//
////		if (calcGstAmt) {
////			if (LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) > 0
////					|| LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) == 0) {
//////				(int) (0.5 + Double.parseDouble(String.valueOf(query.getSingleResult())));
////
////			}
////		}
//
		return amount;
//
	}

	public String calcInfraAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId, String parkingNo,
			String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
			String billModeDesc, String invoiceNo, String irnNo, String sessionId) {

		String wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, qtrEndDateCalc = "", qtrYear = "",
				currBillDate = "", currBillMonth, status, amtColName, particularDesc, particularCode;
		Double amount = 0.0;
		Integer startMonth, endMonth, ceilamount;

		particularDesc = "Infra Maintenance Expenses(Service Code " + hsnCode + ")";
		particularCode = "INFR";
		amtColName = "Nvl(outr_infra,0)";

		try {
			bldgCode = ownerId.substring(0, 4);

			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			wing = ownerId.substring(4, 5);
			flatNum = ownerId.substring(5).trim();
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (startOgMonth != "") {
				if (billMode == "Q") {
					if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
						qtrEndDateCalc = startOgMonth;
					} else {
						qtrEndDateCalc = qtrEndDate;
					}
					startMonth = Integer.parseInt(billDate.substring(3, 5));
					endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
					qtrYear = qtrEndDateCalc.substring(6, 10);
					if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
							&& startOgMonth.substring(0, 4) == qtrYear) {
						startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
						currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
					}
					for (startMonth = startMonth; startMonth <= endMonth; startMonth++) {
						currBillMonth = qtrYear;
						if (startMonth <= 9) {
							currBillMonth = currBillMonth + "0" + startMonth;
						} else {
							currBillMonth = currBillMonth + startMonth;
						}
						amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
						if (LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) > 0
								|| LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) == 0) {
							ceilamount = (int) (0.5 + amount);
							amount = Double.parseDouble(String.valueOf(ceilamount));
						}
						if (currBillDate == "") {
							currBillDate = billDate;
						}
						status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
								currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
								particularCode, billModeDesc, invoiceNo, irnNo);
						if (status != "SUCCESS") {
							return status;
						}
					}
//					bldgCode = bldgCode;
				} else {
					endMonth = Integer.parseInt(billDate.substring(3, 5));
					qtrYear = billDate.substring(6, 10);
					if (endMonth <= 9) {
						currBillMonth = qtrYear + "0" + endMonth.toString();
					} else {
						currBillMonth = qtrYear + endMonth.toString();
					}
					amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
					if (Integer.parseInt(billMonth) >= Integer.parseInt("200804")) {
						if (LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) > 0
								|| LocalDate.parse(billDate).compareTo(LocalDate.parse("01/03/2018")) == 0) {
							ceilamount = (int) (0.5 + amount);
							amount = Double.parseDouble(String.valueOf(ceilamount));
						}
					}
					if (currBillDate == "") {
						currBillDate = billDate;
					}
					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
							particularCode, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
				}
			} else {
				return "Start OG Month is blank";
			}

		} catch (Exception e) {
			return "Error while calculating Infra amount" + e.getMessage();
		}

		return "SUCCESS";

	}

	public String calcMaintAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId, String parkingNo,
			String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
			String billModeDesc, String invoiceNo, String irnNo, String sessionId, Double cGstPerc, Double sGstPerc,
			Double iGstPerc) {

		String wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, qtrEndDateCalc = "", qtrYear = "",
				currBillDate = "", currBillMonth, status, amtColName, particularDesc, particularCode;
		Double amount = 0.0;
		Integer startMonth, endMonth;

		bldgCode = ownerId.substring(0, 4);
		if (billType == "F") {
			if (bldgCode.substring(0, 4) == "OIHF") {
				particularDesc = "Adhoc Outgoings (Service Code " + hsnCode + ")";
			} else {
				particularDesc = "Lumpsum Outgoings (Service Code " + hsnCode + ")";
			}
		} else {
			particularDesc = "Other Maintenance Expenses (Service Code " + hsnCode + ")";
		}

		particularCode = "OMEX";
		amtColName = "Nvl(outr_maint,0)";

		try {

			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			wing = ownerId.substring(4, 5);
			flatNum = ownerId.substring(5).trim();
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (startOgMonth != "") {
				if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
					qtrEndDateCalc = startOgMonth;
				} else {
					qtrEndDateCalc = qtrEndDate;
				}
				startMonth = Integer.parseInt(billDate.substring(3, 5));
				endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
				qtrYear = qtrEndDateCalc.substring(6, 10);
				if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
						&& startOgMonth.substring(0, 4) == qtrYear) {
					startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
					currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
				}
				for (startMonth = startMonth; startMonth <= endMonth; startMonth++) {
					currBillMonth = qtrYear;
					if (startMonth <= 9) {
						currBillMonth = currBillMonth + "0" + startMonth;
					} else {
						currBillMonth = currBillMonth + startMonth;
					}
					amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
					if (currBillDate == "") {
						currBillDate = billDate;
					}
					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
							particularCode, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(cGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" CGST @" + cGstPerc.toString() + "%.", "CGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(sGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" SGST @" + sGstPerc.toString() + "%.", "SGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(iGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" IGST @" + iGstPerc.toString() + "%.", "IGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
				}
			} else {
				return "Start OG Month is blank";
			}

		} catch (Exception e) {
			return "Error while calculating Maintenance amount" + e.getMessage();
		}

		return "SUCCESS";

	}

	public String calcAdminAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId, String parkingNo,
			String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
			String billModeDesc, String invoiceNo, String irnNo, String sessionId, Double cGstPerc, Double sGstPerc,
			Double iGstPerc) {

		String wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, qtrEndDateCalc = "", qtrYear = "",
				currBillDate = "", currBillMonth, status, amtColName, particularDesc, particularCode;
		Double amount = 0.0;
		Integer startMonth, endMonth;

		bldgCode = ownerId.substring(0, 4);
		particularDesc = "Admin Charges (Service Code " + hsnCode + ")";

		particularCode = "ADMI";
		amtColName = "Nvl(outr_admincharges,0)";

		try {
			if (billMode == "Q") {

			}
			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			wing = ownerId.substring(4, 5);
			flatNum = ownerId.substring(5).trim();
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (startOgMonth != "") {
				if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
					qtrEndDateCalc = startOgMonth;
				} else {
					qtrEndDateCalc = qtrEndDate;
				}
				startMonth = Integer.parseInt(billDate.substring(3, 5));
				endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
				qtrYear = qtrEndDateCalc.substring(6, 10);
				if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
						&& startOgMonth.substring(0, 4) == qtrYear) {
					startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
					currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
				}
				for (startMonth = startMonth; startMonth <= endMonth; startMonth++) {
					currBillMonth = qtrYear;
					if (startMonth <= 9) {
						currBillMonth = currBillMonth + "0" + startMonth;
					} else {
						currBillMonth = currBillMonth + startMonth;
					}
					amount = fetchOgRate(amtColName, bldgCode, wing, parkingNo, currBillMonth, billType);
					if (currBillDate == "") {
						currBillDate = billDate;
					}
					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
							particularCode, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(cGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" CGST @" + cGstPerc.toString() + "%", "CGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(sGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" SGST @" + sGstPerc.toString() + "%", "SGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
					status = processGstAmt(iGstPerc, amount, billDate, ownerId, bldgCode, wing, flatNum, parkingNo,
							" IGST @" + iGstPerc.toString() + "%", "IGST", currBillDate, qtrEndDate, sessionId, billNo,
							unitBillNo, carParkOwnerId, currBillMonth, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						return status;
					}
				}
			} else {
				return "Start OG Month is blank";
			}

		} catch (Exception e) {
			return "Error while calculating Admin amount" + e.getMessage();
		}

		return "SUCCESS";

	}

	public String calcArrearsAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId,
			String parkingNo, String carParkOwnerId, String unitBillNo, String billType, String billMode,
			String billDate, String billModeDesc, String invoiceNo, String irnNo, String sessionId) {

		String wing, billNo = "", prevBillNo = "", bldgCode, flatNum, billMonth, startOgMonth, qtrEndDateCalc = "",
				qtrYear = "", currBillDate = "", currBillMonth = "", status, particularDesc, particularCode, sqlString,
				sqlWhere, sqlMonthWhere, rectDate;
		Date prevBillDate = new Date();
		Double prevBillTotAmount, totReceiptAmount, arrearsAmount = 0.0;
		Integer startMonth, endMonth;

		bldgCode = carParkOwnerId.substring(0, 4);
		particularDesc = "Arrears";
		particularCode = "ARRE";

		try {

			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			wing = carParkOwnerId.substring(4, 5);
			flatNum = carParkOwnerId.substring(5).trim();
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (startOgMonth != "") {
				if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
					qtrEndDateCalc = startOgMonth;
				} else {
					qtrEndDateCalc = qtrEndDate;
				}
				startMonth = Integer.parseInt(billDate.substring(3, 5));
				endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
				qtrYear = qtrEndDateCalc.substring(6, 10);
				if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
						&& startOgMonth.substring(0, 4) == qtrYear) {
					startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
					currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
				}
				currBillMonth = qtrYear;
				if (startMonth <= 9) {
					currBillMonth = currBillMonth + "0" + startMonth;
				} else {
					currBillMonth = currBillMonth + startMonth;
				}
			}
			if (currBillDate == "") {
				currBillDate = billDate;
			}

			sqlWhere = "FROM outbill WHERE obill_bldgcode = '" + bldgCode + "' AND obill_wing = '" + wing
					+ "' AND obill_flatnum = '" + flatNum + "' AND obill_billtype = '" + billType + "' ";

			sqlMonthWhere = "and obill_month < '" + billMonth + "')) ";

			sqlString = "SELECT obill_billnum , obill_billdate " + sqlWhere
					+ " and (obill_billnum = (select obill_billnum " + sqlWhere
					+ " and obill_month = (select MAX(obill_month) " + sqlWhere + sqlMonthWhere
					+ ") and (obill_month = (select MAX(obill_month) " + sqlWhere + sqlMonthWhere;

			Query query = this.entityManager.createNativeQuery(sqlString, Tuple.class);
			List<Tuple> billTuple = query.getResultList();

			for (Tuple billList : billTuple) {
				prevBillNo = billList.get(0, String.class);
				prevBillDate = billList.get(1, Date.class);
			}
			if (prevBillDate == null) {
				prevBillDate = new SimpleDateFormat("yyyy-MM-dd").parse("1956-01-01");
			}

			sqlString = "SELECT NVL(sum( nvl(obill_billamt,0) + nvl(obill_arrears,0) + nvl(obill_admincharges,0) + nvl(obill_cgst ,0) + "
					+ "nvl(obill_sgst ,0) + nvl(obill_igst ,0) + nvl(obill_servicetax ,0) + + nvl(obill_swachhcess ,0) + "
					+ "nvl(obill_krishicess ,0) + nvl(obill_water ,0) + nvl(obill_elect ,0) + nvl(obill_natax ,0) ),0) as arrears "
					+ sqlWhere + " AND obill_billnum = '" + prevBillNo + "' ";

			query = this.entityManager.createNativeQuery(sqlString);
			prevBillTotAmount = Double.parseDouble(String.valueOf(query.getSingleResult()));

			sqlString = "SELECT nvl(sum(nvl(out_amtpaid,0) + nvl(out_admincharges,0) + nvl(out_cgst ,0) + nvl(out_sgst ,0) + "
					+ "nvl(out_igst ,0)  + nvl(out_servtax ,0)  + nvl(out_swachhcess ,0)  + nvl(out_krishicess ,0)  + nvl(out_propertytax ,0) + "
					+ "nvl(out_water ,0) + nvl(out_elect ,0) + nvl(out_natax ,0) ),0) as RecTotal FROM outcoll "
					+ "WHERE out_bldgcode = '" + bldgCode + "' AND out_wing = '" + wing + "' AND out_flatnum = '"
					+ flatNum + "' " + "AND to_char(out_recdate,'dd/mm/yyyy') BETWEEN '"
					+ prevBillDate.toString().substring(8, 10) + prevBillDate.toString().substring(5, 7)
					+ prevBillDate.toString().substring(0, 4) + "' AND '" + currBillDate.substring(0, 2)
					+ currBillDate.substring(3, 5) + currBillDate.substring(6, 10) + "' " + "AND out_rectype = '"
					+ billType + "' AND out_cancelledyn = 'N' ";
			query = this.entityManager.createNativeQuery(sqlString);
			totReceiptAmount = Double.parseDouble(String.valueOf(query.getSingleResult()));

			arrearsAmount = prevBillTotAmount - totReceiptAmount;

			rectDate = "01" + "/" + billDate.substring(3, 5) + "/" + billDate.substring(6);

			if (billMode != "Q") {
				qtrEndDate = YearMonth.parse(billDate.substring(6) + "-" + billDate.substring(3, 5),
						DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth().toString();
			}
			status = insertogbilltemp(ownerId, bldgCode, wing, ownerId.substring(5).trim(), parkingNo, particularDesc,
					arrearsAmount, currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId,
					currBillMonth, particularCode, billModeDesc, invoiceNo, irnNo);
			if (status != "SUCCESS") {
				return status;
			}

		} catch (Exception e) {
			return "Error while calculating Arrears amount" + e.getMessage();
		}

		return "SUCCESS";

	}

	public String calcInterestAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId,
			String parkingNo, String carParkOwnerId, String unitBillNo, String billType, String billMode,
			String billDate, String billModeDesc, String invoiceNo, String irnNo, String sessionId) {

		String wing, billNo = "", prevBillNo = "", bldgCode, flatNum, billMonth, startOgMonth, qtrEndDateCalc = "",
				qtrYear = "", currBillDate = "", currBillMonth = "", status, particularDesc, particularCode, sqlString,
				sqlWhere, sqlMonthWhere, rectDate, prevBillDate = "", lastBillDate = "", intCalcFrom = "",
				intCalcFromInt = "", billMM, intFrom;
		Double billArrears = 0.0, billMonthlyOsInt = 0.0, rectAmt = 0.0, billInterest = 0.0, rectInterest = 0.0,
				totAmtReceived = 0.0, totIntReceived = 0.0, currBillInterest = 0.0, currBillInterestInterest = 0.0,
				finalIntAmt = 0.0, oldIntAmt = 0.0;
		Integer startMonth, endMonth, chqCount = 0;
		long noOfDays = 0;
		Date recDate;

		bldgCode = carParkOwnerId.substring(0, 4);
		particularDesc = "Interest On Arrears";
		particularCode = "INTC";

		try {

			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			wing = carParkOwnerId.substring(4, 5);
			flatNum = carParkOwnerId.substring(5).trim();
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (startOgMonth != "") {
				if (Integer.parseInt(startOgMonth) > Integer.parseInt(qtrEndyyyymm)) {
					qtrEndDateCalc = startOgMonth;
				} else {
					qtrEndDateCalc = qtrEndDate;
				}
				startMonth = Integer.parseInt(billDate.substring(3, 5));
				endMonth = Integer.parseInt(qtrEndDate.substring(3, 5));
				qtrYear = qtrEndDateCalc.substring(6, 10);
				if (Integer.parseInt(startOgMonth.substring(4, 6)) > startMonth
						&& startOgMonth.substring(0, 4) == qtrYear) {
					startMonth = Integer.parseInt(startOgMonth.substring(4, 6));
					currBillDate = "01" + "/" + startOgMonth.substring(4, 6) + "/" + qtrYear;
				}
				currBillMonth = qtrYear;
				if (startMonth <= 9) {
					currBillMonth = currBillMonth + "0" + startMonth;
				} else {
					currBillMonth = currBillMonth + startMonth;
				}
			}
			if (currBillDate == "") {
				currBillDate = billDate;
			}

			sqlWhere = "FROM outbill WHERE obill_bldgcode = '" + bldgCode + "' AND obill_wing = '" + wing
					+ "' AND obill_flatnum = '" + flatNum + "' AND obill_billtype = '" + billType + "' ";

			sqlMonthWhere = "and obill_month < '" + billMonth + "')) ";

			sqlString = "SELECT obill_billnum , To_Char(obill_billdate,'dd/mm/yyyy') AS obill_billdate " + sqlWhere
					+ " and (obill_billnum = (select obill_billnum " + sqlWhere
					+ " and obill_month = (select MAX(obill_month) " + sqlWhere + sqlMonthWhere
					+ ") and (obill_month = (select MAX(obill_month) " + sqlWhere + sqlMonthWhere;

			Query query = this.entityManager.createNativeQuery(sqlString, Tuple.class);
			List<Tuple> billTuple = query.getResultList();

			for (Tuple billList : billTuple) {
				prevBillNo = billList.get("obill_billnum", String.class);
				prevBillDate = billList.get("obill_billdate", String.class);
				lastBillDate = billList.get("obill_billdate", String.class);
				intCalcFrom = lastBillDate;
				intCalcFromInt = lastBillDate;
			}
			if (prevBillDate == null) {
				prevBillDate = "01/01/1900";
			}
			if (lastBillDate == "") {
				lastBillDate = billDate;
				intCalcFrom = billDate;
				intCalcFromInt = billDate;
			}

			sqlString = "SELECT NVL(sum( nvl(obill_billamt,0) + nvl(obill_arrears,0) + nvl(obill_admincharges,0) + nvl(obill_cgst ,0) + "
					+ "nvl(obill_sgst ,0) + nvl(obill_igst ,0) + nvl(obill_servicetax ,0) + + nvl(obill_swachhcess ,0) + "
					+ "nvl(obill_krishicess ,0) + nvl(obill_water ,0) + nvl(obill_elect ,0) + nvl(obill_natax ,0) ),0) as arrears , "
					+ "nvl(sum(nvl(obill_interest,0)),0) as intrest " + sqlWhere + " AND obill_billnum = '" + prevBillNo
					+ "' ";

			query = this.entityManager.createNativeQuery(sqlString, Tuple.class);
			List<Tuple> totBillTuple = query.getResultList();
			for (Tuple totBillList : totBillTuple) {
				billArrears = totBillList.get("arrears", BigDecimal.class).doubleValue();
				billInterest = totBillList.get("intrest", BigDecimal.class).doubleValue();
				billMonthlyOsInt = billInterest;
			}
			billMM = lastBillDate;

			// Calculate Interest Amount From Receipt
			sqlString = "SELECT out_recdate , round(nvl(sum(nvl(out_amtpaid,0) + nvl(out_admincharges,0) + nvl(out_cgst ,0) + nvl(out_sgst ,0) + "
					+ "nvl(out_igst ,0)  + nvl(out_servtax ,0)  + nvl(out_swachhcess ,0)  + nvl(out_krishicess ,0)  + nvl(out_propertytax ,0) + "
					+ "nvl(out_water ,0) + nvl(out_elect ,0) + nvl(out_natax ,0) ),0)) as rectotal , round(sum(out_amtint)) as intrecd "
					+ "FROM outcoll WHERE out_bldgcode = '" + bldgCode + "' AND out_wing = '" + wing
					+ "' AND out_flatnum = '" + flatNum + "' " + "AND to_char(out_recdate,'dd/mm/yyyy') > '"
					+ prevBillDate + "' AND to_char(out_recdate,'dd/mm/yyyy') <= '" + currBillDate.substring(0, 2)
					+ currBillDate.substring(3, 5) + currBillDate.substring(6, 10) + "' " + "AND out_rectype = '"
					+ billType + "' AND out_cancelledyn = 'N'  group by out_recdate ";
			query = this.entityManager.createNativeQuery(sqlString, Tuple.class);
			List<Tuple> rectTuple = query.getResultList();
			if (rectTuple.size() > 0) {
				for (Tuple rectList : rectTuple) {
//					chqCount is used as logic is different for first Receipt recd as difference is between Last Bill Date and Receipt Date 
					chqCount = chqCount + 1;
					rectAmt = rectList.get("rectotal", Double.class);
					rectInterest = rectList.get("intrecd", Double.class);
					recDate = rectList.get("out_recdate", Date.class);
					if (recDate == null) { // How can Recdate be null
						billMM = lastBillDate;
						intFrom = billMM;
					} else {
						billMM = recDate.toString();
					}
					totAmtReceived = totAmtReceived + rectAmt;
					totIntReceived = totIntReceived + rectInterest;

					noOfDays = ChronoUnit.DAYS.between(
							LocalDate.parse(lastBillDate, DateTimeFormatter.ofPattern("d/MM/yyyy")),
							LocalDate.parse(billMM, DateTimeFormatter.ofPattern("d/MM/yyyy")));

					if (billArrears > 0) {
						if (chqCount != 1) {
							intCalcFrom = lastBillDate;
						}
						if (noOfDays > 30) {
							intCalcFrom = lastBillDate;
							currBillInterest = currBillInterest
									+ procIntAmount(lastBillDate, billMM, billArrears, billMode);
//								if (currBillInterest < 0) {
//									currBillInterest = 0.0;
//								}
							intCalcFrom = billMM;
						}

					}
					billArrears = billArrears - rectAmt;
					// Monthly_os_interest
					if (billMonthlyOsInt > 0) {
						if (chqCount != 1) {
							intCalcFromInt = lastBillDate;
						}
						if (noOfDays > 30) {
							currBillInterestInterest = currBillInterestInterest
									+ procIntAmount(lastBillDate, billMM, billMonthlyOsInt, billMode);
//								if (currBillInterestInterest < 0) {
//									currBillInterestInterest = 0.0;
//								}
							intCalcFromInt = billMM;
						}
					}
					billMonthlyOsInt = billMonthlyOsInt - rectInterest;

					intFrom = billMM;
					billMM = lastBillDate;
				}
				if (billArrears > 0) {
					currBillInterest = currBillInterest + procIntAmount(intCalcFrom, billDate, billArrears, billMode);
//					if (currBillInterest < 0) {
//						currBillInterest = 0.0;
//					}
				}
				if (billMonthlyOsInt > 0) {
					currBillInterestInterest = currBillInterestInterest
							+ procIntAmount(intCalcFromInt, billDate, billMonthlyOsInt, billMode);
//					if (currBillInterestInterest < 0) {
//						currBillInterestInterest = 0.0;
//					}
				}
				finalIntAmt = oldIntAmt - totIntReceived + currBillInterestInterest + currBillInterest;
			} else { // IF NO RECEIPT RECEIVED THEN FOLLOWING
				if (billArrears > 0) {
					currBillInterest = currBillInterest + procIntAmount(lastBillDate, billDate, billArrears, billMode);
//					if (currBillInterest < 0) {
//						currBillInterest = 0.0;
//					}
				}
				if (oldIntAmt > 0) {
					currBillInterestInterest = currBillInterestInterest
							+ procIntAmount(lastBillDate, billDate, oldIntAmt, billMode);
//					if (currBillInterestInterest < 0) {
//						currBillInterestInterest = 0.0;
//					}
				}
				finalIntAmt = totIntReceived + currBillInterestInterest + currBillInterest;
			}
			if (finalIntAmt < 0) {
				finalIntAmt = 0.0;
			}

//			if (billMode != "Q") {
//				qtrEndDate = YearMonth.parse(billDate.substring(6) + "-" + billDate.substring(3, 5),
//						DateTimeFormatter.ofPattern("yyyy-MM")).atEndOfMonth().toString();
//			}
			status = insertogbilltemp(ownerId, bldgCode, wing, ownerId.substring(5).trim(), parkingNo, particularDesc,
					finalIntAmt, currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
					particularCode, billModeDesc, invoiceNo, irnNo);
			if (status != "SUCCESS") {
				return status;
			}

		} catch (Exception e) {
			return "Error while calculating Interest amount" + e.getMessage();
		}

		return "SUCCESS";

	}

	public Double procIntAmount(String lastBillDate, String billMM, Double billArrears, String billMode) {

		String sqlString;
		Double intRate = 0.0, dailyInterest = 0.0;
		long noOfDays;
		BigDecimal intAmount;

		sqlString = "SELECT ent_num1 FROM entity WHERE ent_class = '#OGIN' AND ent_id = '#OGIN' AND ent_char1='"
				+ billMode + "' ";
		Query query = this.entityManager.createNativeQuery(sqlString);
		intRate = Double.parseDouble(String.valueOf(query.getSingleResult()));

		noOfDays = ChronoUnit.DAYS.between(LocalDate.parse(lastBillDate, DateTimeFormatter.ofPattern("d/MM/yyyy")),
				LocalDate.parse(billMM, DateTimeFormatter.ofPattern("d/MM/yyyy")));

		dailyInterest = ((billArrears / 100) * intRate) / 365;
		intAmount = BigDecimal.valueOf(dailyInterest * noOfDays);
		if (intAmount.compareTo(BigDecimal.ZERO) < 0) {
			intAmount = BigDecimal.ZERO;
		}
		return intAmount.setScale(0, RoundingMode.HALF_DOWN).doubleValue();
	}

	public String processGstAmt(Double gstPerc, Double amount, String billDate, String ownerId, String bldgCode,
			String wing, String flatNum, String parkingNo, String particularDesc, String particularCode,
			String currBillDate, String qtrEndDate, String sessionId, String billNo, String unitBillNo,
			String carParkOwnerId, String currBillMonth, String billModeDesc, String invoiceNo, String irnNo) {

		int ceilGstAmt;
		String status;
		Double gstAmt = 0.0;
		billDate = billDate.substring(6, 10) + "-" + billDate.substring(3, 5) + "-" + billDate.substring(0, 2);

		try {
			if (gstPerc > 0) {
				if (LocalDate.parse(billDate).compareTo(LocalDate.parse("2018-03-01")) > 0
						|| LocalDate.parse(billDate).compareTo(LocalDate.parse("2018-03-01")) == 0) {
					ceilGstAmt = (int) (0.5 + Double.parseDouble(String.valueOf(amount * gstPerc / 100)));
					gstAmt = Double.parseDouble(String.valueOf(ceilGstAmt));
				} else {
					gstAmt = amount * gstPerc / 100;
				}
				status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, gstAmt,
						currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
						particularCode, billModeDesc, invoiceNo, irnNo);

				if (status != "SUCCESS") {
					return status;
				}
			}

		} catch (Exception e) {
			return "Error in amount calculation" + e.getMessage();
		}
		return "SUCCESS";
	}

	public String insertogbilltemp(String ownerId, String bldgCode, String wing, String flatNum, String parkingNo,
			String particulars, Double amount, String currBillDate, String qtrEndDate, String sessionId, String billNo,
			String unitBillNo, String carParkingOwnerId, String billMonth, String particularCode, String billModeDesc,
			String invoiceNo, String irnNo) {

		String sqlString;

		sqlString = "INSERT INTO saogrp01_print ( "
				+ "saogrp_ownerid , saogrp_bldgcode , saogrp_wing , saogrp_flatnum , saogrp_carpark , saogrp_particulars , saogrp_amount , "
				+ "saogrp_billfrom , saogrp_billto , saogrp_sessid , saogrp_userid , saogrp_billnum , saogrp_unitbillno , saogrp_cfownerid , "
				+ "saogrp_billmonth , saogrp_parcode , saogrp_billmode , saogrp_today , saogrp_invoiceno , saogrp_irnno) "
				+ "values ('" + ownerId + "' , '" + bldgCode + "' , '" + wing + "' , '" + flatNum + "' , '" + parkingNo
				+ "' , '" + particulars + "' , " + amount.toString() + " , to_date('" + currBillDate
				+ "','dd/mm/yyyy') , to_date('" + qtrEndDate + "','dd/mm/yyyy') , " + sessionId + " , '"
				+ GenericAuditContextHolder.getContext().getUserid() + "' , '" + billNo + "' , '" + unitBillNo + "' , '"
				+ carParkingOwnerId + "' , '" + billMonth + "' , '" + particularCode + "' , '" + billModeDesc
				+ "' , sysdate , '" + invoiceNo + "' , '" + irnNo + "')";
		try {
			Query query = this.entityManager.createNativeQuery(sqlString);
			Integer rowCount = query.executeUpdate();
			if (rowCount == 0) {
				return "Data not inserted in temp table - saogrp01_print";
			}
		}

		catch (Exception e) {
			return e.getMessage() + "\r\n" + e.getStackTrace().toString();

		}

		return "SUCCESS";
	}

	public Double fetchOgRate(String colName, String bldgCode, String wing, String flatNum, String billMonth,
			String billType) {

		String sqlString = "";
		Double amount = 0.0;

//		if (ceilingRequired = true) {
//			sqlString = "SELECT Trim(ent_char2) FROM entity WHERE ent_class = 'OGBIL' AND ent_id = 'CEIL' AND ent_char1 = '"
//					+ billType + "' ";
//
//			Query query = this.entityManager.createNativeQuery(sqlString);
//			ceilingMonth = String.valueOf(query.getSingleResult());
//
////			if (Integer.parseInt(billMonth) >= Integer.parseInt(ceilingMonth)) {
////				sqlString = "SELECT floor(" + colName + ")";
////			}
//		}
//
////		if (ceilingMonth == "") {
////			sqlString = "SELECT " + colName;
////		}

		sqlString = "SELECT " + colName + " FROM outrate WHERE outr_bldgcode = '" + bldgCode + "' AND outr_wing = '"
				+ wing + "' " + "AND outr_flatnum = '" + flatNum + "' AND '" + billMonth
				+ "' BETWEEN outr_startdate AND outr_enddate AND outr_billtype = '" + billType + "' ";

		Query query = this.entityManager.createNativeQuery(sqlString);
//		if (ceilingMonth == "") {
		amount = Double.parseDouble(String.valueOf(query.getSingleResult()));
//		} else {
//			ceilamount = (int) (0.5 + Double.parseDouble(String.valueOf(query.getSingleResult())));
//			amount = Double.parseDouble(String.valueOf(ceilamount));
//			amount = Math.ceil(Double.parseDouble(String.valueOf(query.getSingleResult())));
//		}

		return amount;
	}

	public ResponseEntity<?> processOgBills(OutgoingReportsRequestBean outgoingReportsRequestBean) {

		Double cGstPerc = 0.0;
		Double sGstPerc = 0.0;
		Double iGstPerc = 0.0;
//		Double amount = 0.0;
		String desc, hsnCode = "995419";
		// Fetch building info (sales state, city, type, company)
		String bldgSalesState, bldgCity, bldgType, bldgCoy, qtrEndDate, qtrEndDateParking, billMonth, qtrEndyyyymm,
				qtrEndyyyymmParking;
		String bldgCode, billType, billMode, billModeDesc, ownerIdFrom, ownerIdUpto;
		Query query;
		int infraBldgCount;
		boolean genInfraAmt = false;
		bldgCode = outgoingReportsRequestBean.getFlatOwnerFrom().substring(0, 4);
		billType = outgoingReportsRequestBean.getBillType();

		ResponseEntity<?> bldgInfo = this.buildingServiceImpl.fetchBuildingByCode(bldgCode);

		bldgSalesState = initBldgInfo(bldgInfo, ", salesstate", 13, 3);
		bldgCity = initBldgInfo(bldgInfo, ", city", 7, 5);
		bldgType = initBldgInfo(bldgInfo, ", bldgtype", 11, 1);
		bldgCoy = initBldgInfo(bldgInfo, ", coy", 6, 5);

		// Fetch GST % and SAC Description
		ResponseEntity<?> gstResponseBean = outinfraServiceImpl.fetchGstRates();
		desc = initBldgInfo(gstResponseBean, ", description", 14, 50);
		cGstPerc = Double.valueOf(initBldgInfo(gstResponseBean, "cgstperc", 9, 4));
		sGstPerc = Double.valueOf(initBldgInfo(gstResponseBean, ", sgstperc", 11, 4));
		if (bldgSalesState.equals("MAH")) {
		} else {
			iGstPerc = Double.valueOf(initBldgInfo(gstResponseBean, ", igstperc", 11, 4));
		}

//		cgst = initGstInfo(gstResponseBean, "cgstperc", 9, 4);
//		sgst = initGstInfo(gstResponseBean, ", sgstperc", 11, 4);
//		igst = initGstInfo(gstResponseBean, ", igstperc", 11, 4);
//		String responseData;
//		String gstResponse = gstResponseBean.getBody().toString() ;
//		JSONObject gstJson = new JSONObject(gstResponseBean.getBody());
//
//		try {
//			responseData = gstJson.getString("data");x
//		} catch (JSONException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}

//		JSONObject arrJSON = JSON.parse(gstResponseBean)

		ownerIdFrom = outgoingReportsRequestBean.getFlatOwnerFrom();
		ownerIdUpto = outgoingReportsRequestBean.getFlatOwnerUpto();
		qtrEndyyyymm = "";
		qtrEndDate = "";

		String billDate = outgoingReportsRequestBean.getBillGenerationDate();
		billMonth = billDate.substring(6).concat(billDate.substring(3, 5));

		query = this.entityManager.createNativeQuery(
				"SELECT count(*) FROM entity WHERE ent_class = 'OGBIL' AND ent_id = 'INCAL' AND ent_char1 = '"
						+ bldgCode + "' AND " + "to_date('" + billDate
						+ "','dd/mm/yyyy') BETWEEN ent_date1 AND ent_date2",
				Integer.class);
		infraBldgCount = query.getFirstResult();
		if (infraBldgCount != 0) {
			genInfraAmt = true;
		}

		if (billType.equals("F") || bldgType.equals("R")) {
			billMode = "Q";
			billModeDesc = "Quarterly"; // For 1st (Lumpsum) bill or Residential Building
			qtrEndyyyymm = initQtrEndDate(billDate, billType, ownerIdFrom);
		} else {
			billMode = "M";
			billModeDesc = "Monthly"; // For Normal bill and other than Residential Building
			qtrEndyyyymm = billDate.substring(6).concat(billDate.substring(3, 5));
		}
		query = this.entityManager
				.createNativeQuery("SELECT to_char(trunc(last_day(to_date('01/" + qtrEndyyyymm.substring(4) + "/"
						+ qtrEndyyyymm.substring(0, 4) + "','dd/mm/yyyy'))),'dd/mm/yyyy') from dual ");
		qtrEndDate = String.valueOf(query.getSingleResult());

		// Get Flats List for processing (only Main Flats Units. Parking is there in
		// another loop)
		String sqlString = "", flatNo, wing, startOgMonth, invoiceNo = "", irnNo = "", billNum = "", serNumber = "",
				parkingOwnerId, parkingNo, parkingNos, status, billNo = ""; // ,

		sqlString = "SELECT distinct flat_ownerid FROM flats, outrate WHERE flat_bldgcode = outr_bldgcode AND flat_wing = outr_wing "
				+ "AND flat_flatnum = outr_flatnum AND flat_soldyn = 'Y' AND substr(flat_flatnum,1,1) IN ('F','H','U','O') AND ('"
				+ billMonth + "' between OUTR_STARTDATE AND OUTR_ENDDATE ";
		if (bldgType.equals("R")) {
			sqlString = sqlString + "OR '" + qtrEndyyyymm + "' BETWEEN OUTR_STARTDATE AND OUTR_ENDDATE";
		}
		sqlString = sqlString + ") AND flat_ownerid between '" + ownerIdFrom + "' AND '" + ownerIdUpto
				+ "' AND outr_billtype = '" + billType + "' ORDER BY flat_ownerid ";

		query = this.entityManager.createNativeQuery(sqlString);
		List<String> flatsList = query.getResultList();
		String sessionId = GenericCounterIncrementLogicUtil.generateTranNo("#SESS", "#SESS");

		String[] owners = new String[flatsList.size()];
		for (String flatOwner : flatsList) {
			flatNo = flatOwner.substring(5).trim();
			wing = flatOwner.substring(4, 5);
			startOgMonth = initStartOgMonth(flatOwner, bldgCode, flatNo, wing, billMonth, billType, billMode);

			sqlString = " FROM outbill WHERE obill_bldgcode = '" + bldgCode + "' AND obill_ownerid = '" + flatOwner
					+ "' AND obill_month = '" + startOgMonth + "' " + "AND obill_gstyn = 'Y' AND obill_billtype = '"
					+ billType + "' ";

			// Fetch invoice generated for the selected bill date
			try {
				query = this.entityManager.createNativeQuery("SELECT nvl(obill_invoiceno,' ') " + sqlString);
				invoiceNo = String.valueOf(query.getSingleResult());
				if (invoiceNo != "") {
					invoiceNo = " ";
				}
			} catch (NoResultException e) {
				invoiceNo = " ";
			}

			// Fetch IRN No generated for the selected bill date
			try {
				query = this.entityManager.createNativeQuery("SELECT obill_irnno " + sqlString);
				irnNo = String.valueOf(query.getSingleResult());
			} catch (NoResultException e) {
				irnNo = "";
			}

			// Fetch Bill No generated for the selected bill date
			try {
				query = this.entityManager.createNativeQuery("SELECT obill_billnum " + sqlString);
				billNum = String.valueOf(query.getSingleResult());
				if (billNum == "") {
					billNum = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "OGBIL",
							GenericAuditContextHolder.getContext().getSite());
				}
			} catch (NoResultException e) {
//				billNum = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "OGBIL",
//						GenericAuditContextHolder.getContext().getSite());
				billNum = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "OGBIL", "MUM");
			}
			parkingNos = "";

			sqlString = "SELECT flat_flatnum,flat_ownerid FROM flats, outrate WHERE flat_bldgcode = outr_bldgcode AND flat_wing = outr_wing "
					+ "AND flat_flatnum = outr_flatnum AND flat_soldyn = 'Y' AND flat_accomtype in('C','R','B','G','P') AND ('"
					+ billMonth + "' between OUTR_STARTDATE AND OUTR_ENDDATE ";
			if (bldgType.equals("R")) {
				sqlString = sqlString + "OR '" + qtrEndyyyymm + "' BETWEEN OUTR_STARTDATE AND OUTR_ENDDATE";
			}
			sqlString = sqlString + ") AND flat_flatpark = '" + flatNo + "' AND flat_bldgcode = '" + bldgCode
					+ "' AND flat_wing = '" + wing + "' AND outr_billtype = '" + billType
					+ "' ORDER BY flat_flatnum,flat_ownerid ";

			query = this.entityManager.createNativeQuery(sqlString, Tuple.class);
			List<Tuple> parkingTuple = query.getResultList();

			for (Tuple parkingList : parkingTuple) {
				parkingNo = parkingList.get(0, String.class);
				parkingOwnerId = parkingList.get(1, String.class);

				if (billType.equals("F")) {
					qtrEndyyyymmParking = initQtrEndDate(billDate, billType, parkingOwnerId);
					if (qtrEndyyyymmParking == "" || qtrEndyyyymmParking == "0") {
						continue;
					}
					query = this.entityManager.createNativeQuery("SELECT to_char(trunc(last_day(to_date('01/"
							+ qtrEndyyyymmParking.substring(4) + "/" + qtrEndyyyymmParking.substring(0, 4)
							+ "','dd/mm/yyyy'))),'dd/mm/yyyy') from dual ");
					qtrEndDateParking = String.valueOf(query.getSingleResult());
				} else {
					qtrEndDateParking = qtrEndDate;
					qtrEndyyyymmParking = qtrEndyyyymm;
				}
				parkingNos = parkingNos + ", " + parkingNo;
				status = "";

//				if (ownerId == parkingOwnerId) {
				billNo = billNum;
//				}

				// Calculate Infra Amount
				if (genInfraAmt == true) {
					status = calcInfraAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
							parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
							sessionId);

					if (status != "SUCCESS") {
						return ResponseEntity
								.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
					}
				}
				// Calculate Maintenance Amount

				status = calcMaintAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
						parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
						sessionId, cGstPerc, sGstPerc, iGstPerc);

				if (status != "SUCCESS") {
					return ResponseEntity
							.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
				}

				// Calculate Admin Charges Amount (Only for Normal Bill)
				if (billType.equals("N")) {
					status = calcAdminAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
							parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
							sessionId, cGstPerc, sGstPerc, iGstPerc);

					if (status != "SUCCESS") {
						return ResponseEntity
								.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
					}
				}

				// Calculate Arrears
				status = calcArrearsAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
						parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
						sessionId);

				if (status != "SUCCESS") {
					return ResponseEntity
							.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
				}

				// Calculate Interest
				status = calcInterestAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
						parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
						sessionId);

				if (status != "SUCCESS") {
					return ResponseEntity
							.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
				}
			}

			if (parkingNos != "") {
				parkingNos = parkingNos.substring(2);
			}
			parkingNo = "";
		}

		logger.info("sess :: {} ", sessionId);
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(sessionId)
				.message("Added successfully").build());
//		} else {
//			return ResponseEntity
//					.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No data found.").build());
//
	}
}
