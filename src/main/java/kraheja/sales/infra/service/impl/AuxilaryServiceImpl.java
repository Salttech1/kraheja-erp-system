package kraheja.sales.infra.service.impl;

import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.commons.entity.Inchq;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.commons.repository.InchqRepository;
import kraheja.payload.GenericResponse;
import kraheja.sales.bean.entitiesresponse.Balance;
import kraheja.sales.bean.request.GenericRequest;
import kraheja.sales.bean.response.AuxilaryResponse;
import kraheja.sales.bean.response.GridResponse;
import kraheja.sales.infra.payload.request.IncheqRequest;
import kraheja.sales.infra.service.AuxilaryService;
import kraheja.sales.repository.OutinfraRepository;
import kraheja.sales.repository.OutrateRepository;
import kraheja.sales.utility.Utility;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class AuxilaryServiceImpl implements AuxilaryService {
	
	private String lastMonth;
	
	private final InchqRepository inchqRepository;

	@Autowired
	private HsnsacmasterRepository hsnsacmasterRepository;
	@Autowired
	private OutinfraRepository outinfraRepository;
	@Autowired
	private OutrateRepository outrateRepository;
	private String acMonth;
	public AuxilaryServiceImpl(InchqRepository inchqRepository) {
		this.inchqRepository = inchqRepository;
	}

	@Override
	public GenericResponse addInchq(IncheqRequest request) {
		Inchq incheq = Utility.incheqMapper(request);
		log.debug("ready to save inchq detail : {}", incheq);

//		Inchq save = inchqRepository.save(incheq);
//		if (!save.equals(null)) {
//			return GenericResponse.builder().result("success").responseCode("00").message("incheqe details save successfully.").build();
//		}
		return GenericResponse.builder().result("faild").responseCode("100").message("incheqe details faild to save.")
				.build();
	}


	// use this method to fetch start date
	// TODO add if condition for AUXI and INAP
	private String startYearMonthFromDatabase(String buildingCode, String wing, String flatNum, String billType) {
		return outrateRepository.fetchStartDate(buildingCode, wing, flatNum, billType);
	}

	@Override
	public AuxilaryResponse getGridData(GenericRequest request) {
		
		String strStartDate = startYearMonthFromDatabase(request.getBuildingCode(), request.getWing(), request.getFlatNum(), request.getBillType());
		log.debug("strStartDate : {}", strStartDate);
		
		if (strStartDate.isEmpty()) {
			strStartDate = startYearMonthFromInput(request.getDate());
		}

		List<Balance> balanceList = outinfraRepository.findPreviousBalance((request.getBuildingCode() + request.getWing() + request.getFlatNum()), strStartDate, request.getChargeCode(), request.getBillType());
		
		try {
			if (!balanceList.isEmpty()) {
				for (Balance balance : balanceList) {
					String month = balance.getMonth();
					if (lastMonth == null || month.compareTo(lastMonth) > 0) {
						lastMonth = month;
					}
				}
			}
			log.debug("lastMonth : {}", lastMonth);
			acMonth = increaseMonth(lastMonth);
			
			if (!lastMonth.isEmpty()) {
				request.setDate(lastMonth);
			}
			List<GridResponse> respone = taxCalculation(request);
			log.debug("respone : {}", respone);

			String endMonth = this.endMonth(Integer.parseInt(acMonth) + (respone.size()-1));
			return AuxilaryResponse.builder()
					.result("success")
					.responseCode("00")
					.message("successfully fetch.")
					.startMonth(acMonth)
					.endMonth(String.valueOf(endMonth))				
					.totalMonth(String.valueOf(respone.size()))
					.data(respone).build();
			
		} catch (Exception e) {
			log.debug("exception occured : {}", e.getMessage());

		}

		return AuxilaryResponse.builder().build();
	}

	// 202306+5
	private String endMonth(int countMonth) {
		log.debug("countMonth: {}", countMonth);

//		YearMonth inputYearMonth = YearMonth.parse(String.valueOf(countMonth), DateTimeFormatter.ofPattern("yyyyMM"));
		int year = Integer.valueOf((String.valueOf(countMonth)).substring(0, 4));
		int month = Integer.valueOf((String.valueOf(countMonth)).substring(4, 6));
		
		 if (countMonth > 12) {
	            int yearsToAdd = (month - 1) / 12;
	            year += yearsToAdd;
	            month = (month - 1) % 12 + 1;
	            YearMonth yearMonth = YearMonth.of(year, month);
	            return yearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
	        }
		return String.valueOf(countMonth); // 202311
	}

	public List<GridResponse> taxCalculation(GenericRequest request) {
		String buildingCode = request.getBuildingCode();
		String wing = request.getWing();
		String flatNum = request.getFlatNum();
		String date = request.getDate(); 
		String billType = request.getBillType();
		double amount = Double.parseDouble(request.getTotalAmt());
		
		String adminRateDB = outrateRepository.findAdminRateMonthWise(buildingCode, wing, flatNum,billType);
		double adminRate = Double.parseDouble(adminRateDB);
		log.debug("adminRate from db: {}", adminRate);

		String auxiRateDb = outrateRepository.findOutrAuxiRateMonthWise(buildingCode, wing, flatNum, billType);
		double auxiRate = Double.parseDouble(auxiRateDb);
		log.debug("auxiRate from db: {}", auxiRate);
		
		String tdsRateDb = outrateRepository.findTdsRateMonthWise(buildingCode, wing, flatNum, billType);
		double tdsRate = Double.parseDouble(tdsRateDb);
		log.debug("tdsRate from db: {}", tdsRate);
		
		int totalMonth =(int) (amount / adminRate);
		log.debug("totalMonth : {}", totalMonth);
		
		double restAmount = amount % adminRate;
		log.debug("rest amount : {}", restAmount);

		List<GridResponse> list = new ArrayList<>();
		int month = Integer.parseInt(this.increaseMonth(date));
		log.debug("month : {}", month);
	
		for(int i = 0; i<totalMonth; i++) {
			list.add(GridResponse.builder()
					.monthName(String.valueOf(month+i))
					.narration("")
					.narrationCode("FP")
					.auxiPaid(tdsRateDb)
					.intPaid("0")
					.cgst(tdsRateDb)
					.sgst(tdsRateDb)
					.igst(tdsRateDb)
					.admin(adminRateDB)
					.cgstPercent(tdsRateDb)
					.sgstPercent(tdsRateDb)
					.igstPercent(tdsRateDb)
					.tds(tdsRateDb)
					.build());
		}
		if (restAmount > 0) {
			list.add(GridResponse.builder()
					.monthName(String.valueOf(this.endMonth((month+(list.size())))) )
					.narration("")
					.narrationCode("FP")
					.auxiPaid(tdsRateDb)
					.intPaid("0")
					.cgst(tdsRateDb)
					.sgst(tdsRateDb)
					.igst(tdsRateDb)
					.admin(String.valueOf(restAmount))
					.cgstPercent(tdsRateDb)
					.sgstPercent(tdsRateDb)
					.igstPercent(tdsRateDb)
					.tds(tdsRateDb)
					.build());
		}
		
		
		return list;
	}

	private String increaseMonth(String month) {
		 // Parse the input string to a YearMonth object
        YearMonth inputYearMonth = YearMonth.parse(month, DateTimeFormatter.ofPattern("yyyyMM"));

        // Add one month to the YearMonth
        YearMonth newYearMonth = inputYearMonth.plusMonths(1);

        // Format the new YearMonth back to the YYYYMM format
        return newYearMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
	}
	
    private String startYearMonthFromInput(String receiptDate) {
        String strYear = "";
        String strMonth = "";
        String strYearMonth = "";

        if (!receiptDate.isEmpty()) {
            strYear = receiptDate.substring(6, 10);
            strMonth = receiptDate.substring(3, 5);
            strYearMonth = strYear + strMonth;
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            receiptDate = dateFormat.format(new Date());
            strYear = receiptDate.substring(6, 10);
            strMonth = receiptDate.substring(3, 5);
            strYearMonth = strYear + strMonth;
        }

        return strYearMonth;
    }
}
