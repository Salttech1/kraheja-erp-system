package kraheja.sales.infra.service.impl;

import java.lang.invoke.MethodHandles;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.commons.entity.Hsnsacmaster;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.constant.ApiResponseMessage;
import kraheja.exception.InternalServerError;
import kraheja.sales.bean.entitiesresponse.Balance;
import kraheja.sales.bean.request.AuxilaryRequest;
import kraheja.sales.bean.response.AuxilaryResponse;
import kraheja.sales.bean.response.GridResponse;
import kraheja.sales.infra.service.AuxilaryService;
import kraheja.sales.infra.utilities.DateUtill;
import kraheja.sales.repository.OutinfraRepository;
import kraheja.sales.repository.OutrateRepository;

@Service
public class AuxilaryServiceImpl implements AuxilaryService {
	private static final Logger log = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	@Autowired
	private HsnsacmasterRepository hsnsacmasterRepository;
	@Autowired
	private OutinfraRepository outinfraRepository;
	@Autowired
	private OutrateRepository outrateRepository;

	@Override
	public AuxilaryResponse getGridData(AuxilaryRequest request) {

		String strStartDate = null;
		
		try {
		/**
		 * check here the type of service and type of bill set from here the query.
		 */
		if ("AUXI".equalsIgnoreCase(request.getChargeCode())) {

			strStartDate = startYearMonthFromDatabase(request.getBuildingCode(), request.getWing(),
					request.getFlatNum(), request.getBillType());
			log.debug("strStartDate : {}", strStartDate);

			if (strStartDate.isEmpty() || strStartDate == null) {
				strStartDate = DateUtill.startYearMonthFromInput(request.getDate());
			}
		} else {
			
		}
		List<Balance> balanceList = outinfraRepository.findPreviousBalance(
				(request.getBuildingCode() + request.getWing() + request.getFlatNum()), strStartDate,
				request.getChargeCode(), request.getBillType());

			if (!balanceList.isEmpty()) {
				for (Balance balance : balanceList) {
				}
			}
			request.setDate(strStartDate);
			List<GridResponse> respone = taxCalculation(request);
			log.debug("respone : {}", respone);

			String totalMonthCount = String.valueOf(respone.size());
			String endMonth = DateUtill.endMonth((respone.size() - 1) + Integer.parseInt(strStartDate));

			return AuxilaryResponse.builder().result("success").responseCode("00").message("successfully fetch.")
					.startMonth(strStartDate).endMonth(endMonth).totalMonth(totalMonthCount).data(respone).build();

		} catch (NullPointerException e) {
			log.debug("exception occured : {}", e.getMessage());
			throw new InternalServerError(ApiResponseMessage.START_DATE_NOT_SPECIFIED);
		} catch (Exception e) {
		log.debug("exception occured : {}", e.getMessage());
		throw new InternalServerError(e.getMessage());
	}
	}

	// use this method to fetch start date
	// TODO add if condition for AUXI and INAP
	private String startYearMonthFromDatabase(String buildingCode, String wing, String flatNum, String billType) {
		return outrateRepository.fetchStartDate(buildingCode, wing, flatNum, billType);
	}

	

	public List<GridResponse> taxCalculation(AuxilaryRequest request) {
		List<GridResponse> insertRowList = new ArrayList<>();

		String buildingCode = request.getBuildingCode();
		String wing = request.getWing();
		String flatNum = request.getFlatNum();
		String date = request.getDate();
		String billType = request.getBillType();
		String narration = request.getNarration();
		double amount = Double.parseDouble(request.getTotalAmt());
		double receiptTds = Double.parseDouble(request.getReceiptAmtTds());

		String adminRateDB = outrateRepository.findAdminRateMonthWise(buildingCode, wing, flatNum, billType);
		double adminRate = Double.parseDouble(adminRateDB);
		log.debug("adminRate from db: {}", adminRate);

		String auxiRateDb = outrateRepository.findOutrAuxiRateMonthWise(buildingCode, wing, flatNum, billType);
		double maintRate = Double.parseDouble(auxiRateDb);
		log.debug("auxiRate from db: {}", maintRate);

		String tdsRateDb = outrateRepository.findTdsRateMonthWise(buildingCode, wing, flatNum, billType);
		double tdsRate = Double.parseDouble(tdsRateDb);
		log.debug("tdsRate from db: {}", tdsRate);

		double prevMaintPaid = 0;
		double prevAmtintPaid = 0;
		double prevCGSTtaxPaid = 0;
		double prevSGSTtaxPaid = 0;
		double prevIGSTtaxPaid = 0;
		double prevAdminPaid = 0;
		double prevTDStaxPaid = 0;

		while (amount > 0) {
//			amount = this.calculateAllocation(amount, adminRate, maintRate, receiptTds, tdsRate, prevAdminPaid,
//					prevMaintPaid, prevAmtintPaid, prevCGSTtaxPaid, prevSGSTtaxPaid, prevIGSTtaxPaid, prevTDStaxPaid, billType, date);

			Hsnsacmaster gst = hsnsacmasterRepository.findByHsnsacmasterCKHsmsCode("995419");
			Double cgstPerc = gst.getHsmsCgstperc();
			Double sgstPerc = gst.getHsmsSgstperc();
			Double igstPerc = 0.00;

			double prevCGSTAdmin = 0;
			double prevSGSTAdmin;
			double prevIGSTAdmin;
			double prevCGSTMaint = 0;
			double prevSGSTMaint = 0;
			double prevIGSTMaint;
			double cgstOnAdminRate;
			double sgstOnAdminRate;
			double igstOnAdminRate = 0;
			double cgstOnMaintRate;
			double sgstOnMaintRate;
			double igstOnMaintRate;
			double prevCgstAdmin;
			double adjAdmin;
			double adjMaint;
			double adjTDS = 0;
			double adjCGSTAdmin;
			double adjSGSTAdmin;
			double adjIGSTAdmin;
			double adjCGSTMaint;
			double adjSGSTMaint;
			double adjIGSTMaint;

			// Returns Unadjusted Receipt Amount
			if (amount <= 0) {
				continue;
			}

			// Calculate GST ON ADMIN
			if (billType.equals("F")) {
				cgstOnAdminRate = (int) Math.round((cgstPerc * adminRate) / 100.0);
				sgstOnAdminRate = (int) Math.round((sgstPerc * adminRate) / 100.0);
				sgstOnAdminRate = (int) Math.round((igstPerc * adminRate) / 100.0);

				cgstOnMaintRate = (int) Math.round((cgstPerc * maintRate) / 100.0);
				sgstOnMaintRate = (int) Math.round((sgstPerc * maintRate) / 100.0);
				igstOnMaintRate = (int) Math.round((igstPerc * maintRate) / 100.0);
			} else {
				cgstOnAdminRate = (int) Math.ceil((cgstPerc * adminRate) / 100.0);
				sgstOnAdminRate = (int) Math.ceil((sgstPerc * adminRate) / 100.0);
				igstOnAdminRate = (int) Math.ceil((igstPerc * adminRate) / 100.0);

				cgstOnMaintRate = (int) Math.ceil((cgstPerc * maintRate) / 100.0);
				sgstOnMaintRate = (int) Math.ceil((sgstPerc * maintRate) / 100.0);
				igstOnMaintRate = (int) Math.ceil((igstPerc * maintRate) / 100.0);
			}

			// Bifurcate GST into Admin GST and Maint GST
			if (prevCGSTtaxPaid >= cgstOnAdminRate) {
				prevCgstAdmin = cgstOnAdminRate;
			} else {
				prevCgstAdmin = prevCGSTtaxPaid;
			}

			if (prevSGSTtaxPaid >= sgstOnAdminRate) {
				prevSGSTAdmin = sgstOnAdminRate;
				prevSGSTMaint = prevSGSTtaxPaid - prevSGSTAdmin;
			} else {
				prevSGSTAdmin = prevSGSTtaxPaid;
			}

			if (prevIGSTtaxPaid >= igstOnAdminRate) {
				prevIGSTAdmin = igstOnAdminRate;
				prevIGSTMaint = prevIGSTtaxPaid - prevIGSTAdmin;
			} else {
				prevIGSTAdmin = prevIGSTtaxPaid;
				prevIGSTMaint = 0;
			}

			// Check if FULL Amount already received
			if ((prevAdminPaid + prevCGSTAdmin + prevSGSTAdmin + prevIGSTAdmin + prevAmtintPaid + prevCGSTMaint
					+ prevSGSTMaint + prevIGSTMaint) >= (adminRate + cgstOnAdminRate + sgstOnAdminRate + igstOnAdminRate
							+ maintRate + cgstOnMaintRate + sgstOnMaintRate + igstOnMaintRate)) {
				continue;
			} else {
				// If Admin amount remains to be adjusted
				/***************** ADMIN Start **************************************/
				adjAdmin = adminRate - prevAdminPaid;
				adjCGSTAdmin = cgstOnAdminRate - prevCgstAdmin;
				adjSGSTAdmin = sgstOnAdminRate - prevSGSTAdmin;
				adjIGSTAdmin = igstOnAdminRate - prevIGSTAdmin;

				if ((adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin) > 0 && amount > 0) {

					if (amount >= (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin)) {

						amount = amount - (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin);
					} else {
						if (amount <= 2) {
							adjAdmin = amount;
							adjCGSTAdmin = 0;
							adjSGSTAdmin = 0;
							adjIGSTAdmin = 0;
							amount = 0;
						} else {
							double factor = (amount / (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin));
							if (billType.equals("F")) {
								int adjCGSTAdm = (int) Math.round(adjCGSTAdmin * factor);
								adjCGSTAdmin = adjCGSTAdm;

								int adjSGSTAdm = (int) Math.round(adjSGSTAdmin * factor);
								adjSGSTAdmin = adjSGSTAdm;

								int adjIGSTAdm = (int) Math.round(adjIGSTAdmin * factor);
								adjIGSTAdmin = adjIGSTAdm;

							} else {
								adjCGSTAdmin = Math.ceil(adjCGSTAdmin * factor);
								adjSGSTAdmin = Math.ceil(adjSGSTAdmin * factor);
								adjIGSTAdmin = Math.ceil(adjIGSTAdmin * factor);
							}
							adjAdmin = amount - adjCGSTAdmin - adjSGSTAdmin - adjIGSTAdmin;
							amount = 0;
						}
					}

				} else {
					adjAdmin = 0;
					adjCGSTAdmin = 0;
					adjSGSTAdmin = 0;
					adjIGSTAdmin = 0;
				}

				/***************** ADMIN END **************************************/

				// If MAINT amount remains to be adjusted
				/***************** MAINT Start **************************************/
				adjMaint = maintRate - prevAdminPaid;
				adjCGSTMaint = cgstOnMaintRate - prevCGSTMaint;
				adjSGSTMaint = sgstOnMaintRate - prevSGSTMaint;
				adjIGSTMaint = igstOnMaintRate - prevIGSTMaint;

				if ((adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint) > 0 && amount > 0) {

					if (amount >= (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint)) {
						amount = amount - (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);

					} else {
						if (amount <= 2) {
							adjMaint = amount;
							adjCGSTMaint = 0;
							adjSGSTMaint = 0;
							adjIGSTMaint = 0;
							amount = 0;
						} else {
							double factor = amount / (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);
							if (billType.equals("F")) {
								int adjCGSTMa = (int) Math.round(adjCGSTMaint * factor);
								adjCGSTMaint = adjCGSTMa;

								int adjSGSTMa = (int) Math.round(adjSGSTMaint * factor);
								adjSGSTMaint = adjSGSTMa;

								int adjIGSTMa = (int) Math.round(adjIGSTMaint * factor);
								adjIGSTMaint = adjIGSTMa;

							} else {
								adjCGSTMaint = Math.ceil(adjCGSTMaint * factor);
								adjSGSTMaint = Math.ceil(adjSGSTMaint * factor);
								adjIGSTMaint = Math.ceil(adjIGSTMaint * factor);
							}
							adjMaint = amount - adjCGSTMaint - adjSGSTMaint - adjIGSTMaint;
							amount = 0;
						}
					}
				} else {
					adjMaint = 0;
					adjCGSTMaint = 0;
					adjSGSTMaint = 0;
					adjIGSTMaint = 0;
				}
				/***************** MAINT END **************************************/

				// If TDS amount remains to be adjusted
				/***************** TDS Start **************************************/
				// Check if FULL Amount already received

				if (receiptTds < 0) {
					if (prevTDStaxPaid <= tdsRate) {

					} else {
						adjTDS = tdsRate - prevTDStaxPaid;

						if (adjTDS < 0) {

							if (receiptTds < adjTDS) {

								receiptTds = receiptTds - adjTDS;

							} else {

								adjTDS = receiptTds;

								receiptTds = 0;
							}
						} else {
							adjTDS = 0;
						}
					}
				}
			}
			double cgst = adjCGSTAdmin + adjCGSTMaint;
			double sgst = adjSGSTAdmin + adjSGSTMaint;
			double igst = adjIGSTAdmin + adjIGSTMaint;
			GridResponse response = insertRow(date, adjAdmin, adjMaint, cgst, sgst, igst, adjTDS, cgstPerc, sgstPerc,
					igstPerc, narration);
			insertRowList.add(response);

			date = DateUtill.increaseMonth(date);
		}

		return insertRowList;
	}

	private GridResponse insertRow(String startDate, double adjAdmin, double adjMaint, double cgst, double sgst,
			double igst, double adjTDS, double cgstPerc, double sgstPerc, double igstPerc, String narration) {
		List<GridResponse> list = new ArrayList<>();

		GridResponse response = GridResponse.builder().monthName(startDate).narration(narration).narrationCode("FP")
				.intPaid(0).cgst(cgst).sgst(sgst).igst(igst).auxiPaid(adjMaint).admin(adjAdmin).cgstPercent(cgstPerc)
				.sgstPercent(sgstPerc).igstPercent(igstPerc).tds(adjTDS).build();

		log.debug("GridResponse : {}", list);
		return response;

	}


}