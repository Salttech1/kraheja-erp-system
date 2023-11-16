package kraheja.sales.outgoing.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

//import org.apache.http.HttpStatus;
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
		int count;
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

//	public String calcInfra(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId, String parkingNo,
//			String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
//			String billModeDesc, String invoiceNo, String irnNo, String sessionId, Boolean ceilingRequired) {
//
//		String particulars, particularCode, wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, billFromDate,
//				qtrEndDateCalc = "", qtrYear = "", currBillDate = "", currBillMonth, status;
//		Double amount = 0.0;
//		Integer startMonth, endMonth;
//
//		try {
//			particulars = "Infra Maintenance Expenses(Service Code " + hsnCode + ")";
//			particularCode = "INFR";
//			if (ownerId == carParkOwnerId) {
//				billNo = unitBillNo;
//			}
//			bldgCode = ownerId.substring(0, 4);
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
//						amount = fetchOgRate("Nvl(outr_infra,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
//								ceilingRequired);
//						if (currBillDate == "") {
//							currBillDate = billDate;
//						}
//						status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particulars, amount,
//								currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
//								particularCode, billModeDesc, invoiceNo, irnNo);
//						if (status != "SUCCESS") {
//							parkingNo = "";
//							return status;
//						}
//					}
//					bldgCode = bldgCode;
//				} else {
//					endMonth = Integer.parseInt(billDate.substring(3, 5));
////					if (endMonth > 12) {
////						qtrYear = billDate.substring(6, 10) + 1;
////						endMonth = 1;
////					}else {
//					qtrYear = billDate.substring(6, 10);
////					}
//					if (endMonth <= 9) {
//						currBillMonth = qtrYear + "0" + endMonth.toString();
//					} else {
//						currBillMonth = qtrYear + endMonth.toString();
//					}
//					amount = fetchOgRate("Nvl(outr_infra,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
//							ceilingRequired);
//					if (currBillDate == "") {
//						currBillDate = billDate;
//					}
//					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particulars, amount,
//							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
//							particularCode, billModeDesc, invoiceNo, irnNo);
//					if (status != "SUCCESS") {
//						parkingNo = "";
//						return status;
//					}
//
//				}
//			} else {
//				return "Can't initialise Outgoing Month";
//			}
//
//		} catch (Exception e) {
//			return e.getMessage() + "\r\n" + e.getStackTrace().toString();
//		}
//
//		return "SUCCESS";
//
//	}

	public String calcParticularsAmt(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId,
			String parkingNo, String carParkOwnerId, String unitBillNo, String billType, String billMode,
			String billDate, String amtColName, String billModeDesc, String invoiceNo, String irnNo, String sessionId,
			Boolean ceilingRequired, String particularDesc, String particularCode) {

		String wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, billFromDate, qtrEndDateCalc = "",
				qtrYear = "", currBillDate = "", currBillMonth, status;
		Double amount = 0.0;
		Integer startMonth, endMonth;

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
						amount = fetchOgRate("Nvl(outr_maint,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
								ceilingRequired);
						if (currBillDate == "") {
							currBillDate = billDate;
						}
						status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
								currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
								particularCode, billModeDesc, invoiceNo, irnNo);
						if (status != "SUCCESS") {
							parkingNo = "";
							return status;
						}
					}
					bldgCode = bldgCode;
				} else {
					endMonth = Integer.parseInt(billDate.substring(3, 5));
//					if (endMonth > 12) {
//						qtrYear = billDate.substring(6, 10) + 1;
//						endMonth = 1;
//					}else {
					qtrYear = billDate.substring(6, 10);
//					}
					if (endMonth <= 9) {
						currBillMonth = qtrYear + "0" + endMonth.toString();
					} else {
						currBillMonth = qtrYear + endMonth.toString();
					}
					amount = fetchOgRate("Nvl(outr_infra,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
							ceilingRequired);
					if (currBillDate == "") {
						currBillDate = billDate;
					}
					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particularDesc, amount,
							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
							particularCode, billModeDesc, invoiceNo, irnNo);
					if (status != "SUCCESS") {
						parkingNo = "";
						return status;
					}

				}
			} else {
				return "Can't initialise Outgoing Month";
			}

		} catch (Exception e) {
			return e.getMessage() + "\r\n" + e.getStackTrace().toString();
		}

		return "SUCCESS";

	}

//	public String calcMaintGST(String hsnCode, String qtrEndDate, String qtrEndyyyymm, String ownerId, String parkingNo,
//			String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
//			String billModeDesc, String invoiceNo, String irnNo, String sessionId, Boolean ceilingRequired) {
//
//		String particulars, particularCode, wing, billNo = "", bldgCode, flatNum, billMonth, startOgMonth, billFromDate,
//				qtrEndDateCalc = "", qtrYear = "", currBillDate = "", currBillMonth, status;
//		Double amount = 0.0;
//		Integer startMonth, endMonth;
//
//		try {
//			bldgCode = ownerId.substring(0, 4);
//			if (billType == "F") {
//				if (bldgCode == "OIHF") {
//					particulars = "Adhoc Outgoings (Service Code " + hsnCode + ")";
//				} else {
//					particulars = "Lumpsum Outgoings (Service Code " + hsnCode + ")";
//				}
//			} else {
//				particulars = "Other Maintenance Expenses (Service Code " + hsnCode + ")";
//			}
//			particularCode = "OMEX";
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
//						amount = fetchOgRate("Nvl(outr_maint,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
//								ceilingRequired);
//						if (currBillDate == "") {
//							currBillDate = billDate;
//						}
//						status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particulars, amount,
//								currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
//								particularCode, billModeDesc, invoiceNo, irnNo);
//						if (status != "SUCCESS") {
//							parkingNo = "";
//							return status;
//						}
//					}
//					bldgCode = bldgCode;
//				} else {
//					endMonth = Integer.parseInt(billDate.substring(3, 5));
////					if (endMonth > 12) {
////						qtrYear = billDate.substring(6, 10) + 1;
////						endMonth = 1;
////					}else {
//					qtrYear = billDate.substring(6, 10);
////					}
//					if (endMonth <= 9) {
//						currBillMonth = qtrYear + "0" + endMonth.toString();
//					} else {
//						currBillMonth = qtrYear + endMonth.toString();
//					}
//					amount = fetchOgRate("Nvl(outr_infra,0)", bldgCode, wing, parkingNo, currBillMonth, billType,
//							ceilingRequired);
//					if (currBillDate == "") {
//						currBillDate = billDate;
//					}
//					status = insertogbilltemp(ownerId, bldgCode, wing, flatNum, parkingNo, particulars, amount,
//							currBillDate, qtrEndDate, sessionId, billNo, unitBillNo, carParkOwnerId, currBillMonth,
//							particularCode, billModeDesc, invoiceNo, irnNo);
//					if (status != "SUCCESS") {
//						parkingNo = "";
//						return status;
//					}
//
//				}
//			} else {
//				return "Can't initialise Outgoing Month";
//			}
//
//		} catch (Exception e) {
//			return e.getMessage() + "\r\n" + e.getStackTrace().toString();
//		}
//
//		return "SUCCESS";
//
//	}

//	public ResponseEntity<?> returnErrorResponseEntity(String message) {
//		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(message).build());
//	}

	public Integer calcWaterElecNaTax(String particulars, String parCode, String hsnCode, String qtrEndDate,
			String ownerId, String carParkOwnerId, String unitBillNo, String billType, String billMode, String billDate,
			String colName, Boolean ceilingRequired) {

		String wing, billNo = " ", bldgCode, flatNum, billMonth, startOgMonth, billFromDate;
		Double amount = 0.0;
		Integer returnValue = 0;

		try {
			if (ownerId == carParkOwnerId) {
				billNo = unitBillNo;
			}
			bldgCode = ownerId.substring(0, 4);
			wing = ownerId.substring(4, 5);
			flatNum = ownerId.substring(5);
			billMonth = billDate.substring(6) + billDate.substring(3, 5);

			startOgMonth = initStartOgMonth(ownerId, bldgCode, flatNum, wing, billMonth, billType, billMode);
			if (Integer.parseInt(startOgMonth) > Integer.parseInt(billMonth)) {
				billFromDate = "01/" + startOgMonth.substring(4, 6) + "/" + startOgMonth.substring(0, 4);
			} else {
				billFromDate = "01/" + billMonth.substring(4, 6) + "/" + billMonth.substring(0, 4);
			}
			while (Integer.parseInt(billMonth) <= Integer.parseInt(qtrEndDate)) {
				amount = fetchOgRate(colName, bldgCode, wing, flatNum, billMonth, billType, ceilingRequired);

			}

		} catch (Exception e) {
			returnValue = -1;
		}

		return returnValue;
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
			String billType, Boolean ceilingRequired) {

		String sqlString = "", ceilingMonth = "";
		Double amount = 0.0;

		if (ceilingRequired = true) {
			sqlString = "SELECT Trim(ent_char2) FROM entity WHERE ent_class = 'OGBIL' AND ent_id = 'CEIL' AND ent_char1 = '"
					+ billType + "' ";

			Query query = this.entityManager.createNativeQuery(sqlString);
			ceilingMonth = String.valueOf(query.getSingleResult());

			if (Integer.parseInt(billMonth) >= Integer.parseInt(ceilingMonth)) {
				sqlString = "SELECT floor(" + colName + ")";
			}
		}

		if (ceilingMonth == "") {
			sqlString = "SELECT " + colName;
		}

		sqlString = sqlString + " FROM outrate WHERE outr_bldgcode = '" + bldgCode + "' AND outr_wing = '" + wing + "' "
				+ "AND outr_flatnum = '" + flatNum + "' AND '" + billMonth
				+ "' BETWEEN outr_startdate AND outr_enddate AND outr_billtype = '" + billType + "' ";

		Query query = this.entityManager.createNativeQuery(sqlString);
		amount = Double.parseDouble(String.valueOf(query.getSingleResult()));

		return amount;
	}

	public ResponseEntity<?> processOgBills(OutgoingReportsRequestBean outgoingReportsRequestBean) {

		Double cgst = 0.0;
		Double sgst = 0.0;
		Double igst = 0.0;
		Double ugst = 0.0;
		String desc, hsnCode = "995419";

		// Fetch GST % and SAC Description
		ResponseEntity<?> gstResponseBean = outinfraServiceImpl.fetchGstRates();
		desc = initBldgInfo(gstResponseBean, ", description", 14, 50);
		cgst = Double.valueOf(initBldgInfo(gstResponseBean, "cgstperc", 9, 4));
		sgst = Double.valueOf(initBldgInfo(gstResponseBean, ", sgstperc", 11, 4));
		igst = Double.valueOf(initBldgInfo(gstResponseBean, ", igstperc", 11, 4));

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

		// Fetch building info (sales state, city, type, company)
		String bldgSalesState, bldgCity, bldgType, bldgCoy, qtrEndDate, qtrEndDateParking, billMonth, qtrEndyyyymm,
				qtrEndyyyymmParking, particularDesc, particularCode, colName;
		String bldgCode, billType, billMode, billModeDesc, ownerIdFrom, ownerIdUpto, ownerId;
		Query query;
//		Number ogMonths;
		int noOfRows, infraBldgCount;
		boolean genInfraAmt = false;
		bldgCode = outgoingReportsRequestBean.getFlatOwnerFrom().substring(0, 4);
		billType = outgoingReportsRequestBean.getBillType();

		ResponseEntity<?> bldgInfo = this.buildingServiceImpl.fetchBuildingByCode(bldgCode);

		bldgSalesState = initBldgInfo(bldgInfo, ", salesstate", 13, 3);
		bldgCity = initBldgInfo(bldgInfo, ", city", 7, 5);
		bldgType = initBldgInfo(bldgInfo, ", bldgtype", 11, 1);
		bldgCoy = initBldgInfo(bldgInfo, ", coy", 6, 5);
		ownerIdFrom = outgoingReportsRequestBean.getFlatOwnerFrom();
		ownerIdUpto = outgoingReportsRequestBean.getFlatOwnerUpto();
		qtrEndyyyymm = "";
		qtrEndDate = "";

//		String gsttype = "";
//		String strlochsmscode = "995419"; // To fetch the GST % and Description from the hsnsacmaster
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
				parkingOwnerId, parkingNo, parkingNos, status; // ,
//		int totParkings;
		// flatOwner
		// =
		// "";
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
//				try {
//				query = this.entityManager.createNativeQuery(
//						"SELECT count(*) FROM entity WHERE ent_class = 'OGBIL' AND ent_id = 'INCAL' AND ent_char1 = '"
//								+ bldgCode + "' AND " + "to_date('" + billDate
//								+ "','dd/mm/yyyy') BETWEEN ent_date1 AND ent_date2",
//						Integer.class);
//				noOfRows = query.getFirstResult();

				if (genInfraAmt == true) {
					// Calculate Infra Amount
					particularDesc = "Infra Maintenance Expenses(Service Code " + hsnCode + ")";
					particularCode = "INFR";
					colName = "Nvl(outr_infra,0)";
//					status = calcInfra(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
//							parkingOwnerId, billNum, billType, billMode, billDate, colName, billModeDesc, invoiceNo,
//							irnNo, sessionId, true, particularDesc, particularCode);
					status = calcParticularsAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
							parkingOwnerId, billNum, billType, billMode, billDate, colName, billModeDesc, invoiceNo,
							irnNo, sessionId, true, particularDesc, particularCode);
					if (status != "SUCCESS") {
						return ResponseEntity
								.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
					}
				}
//				// Calculate Maintenance GST Amount
//				status = calcMaintGST(hsncode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
//						parkingOwnerId, billNum, billType, billMode, billDate, billModeDesc, invoiceNo, irnNo,
//						sessionId, true);

				if (billType == "F") {
					if (bldgCode == "OIHF") {
						particularDesc = "Adhoc Outgoings (Service Code " + hsnCode + ")";
					} else {
						particularDesc = "Lumpsum Outgoings (Service Code " + hsnCode + ")";
					}
				} else {
					particularDesc = "Other Maintenance Expenses (Service Code " + hsnCode + ")";
				}
				particularCode = "OMEX";
				colName = "Nvl(outr_maint,0)";
				status = calcParticularsAmt(hsnCode, qtrEndDateParking, qtrEndyyyymmParking, flatOwner, parkingNo,
						parkingOwnerId, billNum, billType, billMode, billDate, colName, billModeDesc, invoiceNo, irnNo,
						sessionId, true, particularDesc, particularCode);

				if (status != "SUCCESS") {
					return ResponseEntity
							.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message(status).build());
				}
				parkingNo = "";

			}
//				} catch (NoResultException e) {
//					e.getMessage();
//				}
//			}

			if (parkingNos != "") {
				parkingNos = parkingNos.substring(2);
			}
			parkingNo = "";
		}

//		String flatOwner;
//		for (flatRow = 0; flatRow < flatsList.size(); flatRow++) {
//			flatOwner = flatsList.get(flatRow).toString();
//		}

//		// Fetch GST % and SAC Description
//		Query query = this.entityManager.createNativeQuery(
//				"SELECT  hsms_desc , to_char(hsms_cgstperc) , to_char(hsms_sgstperc) , to_char(hsms_igstperc) , to_char(hsms_ugstperc) "
//						+ "FROM hsnsacmaster WHERE hsms_code = '" + strlochsmscode + "' " + "AND '" + StrLocTranDate
//						+ "' BETWEEN hsms_opendate AND hsms_closedate ",
//				Tuple.class);
//		List<Tuple> tuplesList = query.getResultList();
//		if (CollectionUtils.isNotEmpty(tuplesList)) {
//			Map<String, String> getPercMap = new HashMap<>();
//			tuplesList.stream().map(t -> {
//				getPercMap.put("description", t.get(0, String.class));
//				getPercMap.put("cgst", t.get(1, String.class));
//				getPercMap.put("sgst", t.get(2, String.class));
//				getPercMap.put("igst", t.get(3, String.class));
//				getPercMap.put("ugst", t.get(4, String.class));
//
//				return t;
//			}).collect(Collectors.toList());
//			if (getPercMap.get("description").equals(CommonConstraints.INSTANCE.NODATA_STRING)
//					&& getPercMap.get("cgst").equals(CommonConstraints.INSTANCE.NODATA_STRING)
//					&& getPercMap.get("sgst").equals(CommonConstraints.INSTANCE.NODATA_STRING)
//					&& getPercMap.get("igst").equals(CommonConstraints.INSTANCE.NODATA_STRING)
//					&& getPercMap.get("ugst").equals(CommonConstraints.INSTANCE.NODATA_STRING))
//				return ResponseEntity
//						.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Invalid HSN code.").build());
//			desc = getPercMap.get("description");
//
//			query = this.entityManager
//					.createNativeQuery("SELECT count(*) FROM entity WHERE ent_class = 'STATE' and ent_id = '"
//							+ bldgSalesState + "' " + "AND ent_char3 = 'U'", int.class);
//
//			int noOfRows = query.getFirstResult();
//			if (noOfRows > 0) {
//				ugst = Double.valueOf(getPercMap.get("ugst"));
//			} else {
//				if (bldgSalesState.equals("MAH")) {
//					cgst = Double.valueOf(getPercMap.get("cgst"));
//					sgst = Double.valueOf(getPercMap.get("sgst"));
//				} else {
//					igst = Double.valueOf(getPercMap.get("igst"));
//				}
//			}

//		String sessionId = GenericCounterIncrementLogicUtil.generateTranNo("#SESS", "#SESS");
		logger.info("sess :: {} ", sessionId);
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(sessionId)
				.message("Added successfully").build());
//		} else {
//			return ResponseEntity
//					.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No data found.").build());
//
	}
}
//}
