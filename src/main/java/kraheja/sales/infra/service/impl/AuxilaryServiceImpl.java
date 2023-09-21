package kraheja.sales.infra.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.commons.entity.Hsnsacmaster;
import kraheja.commons.entity.Inchq;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.commons.repository.InchqRepository;
import kraheja.payload.GenericResponse;
import kraheja.sales.bean.entitiesresponse.PreviousDBBalance;
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
	private String end, startYearMonth;
	private double cgst, sgst, igst;
	private String year;
	private String strYear;
	private String strMon;
	private int intMon;
	
	  // W_saogde16_first ---- SAog03.pbl
     int retvalue;
     String Strwing, StrFlatno, StrbldgCode; 
     String LocRecNo, StrTranser;
     String Strcoy, StrProperty, StrProject, StrMaintcoy, StrProp ;
     String StrIn_Insert_Row; //Becomes N when row is inserted into cheqe details table
     String StrToday, StrSite, StrUserName;
     String StrPriPartyCode, StrPriOwnerid, StrPriMinor, StrPriConnectionString, StrPriNeumonic;
     String StrPriBillType; // "F" or "N"
     String Str_AUXI_INAP ;  //'contains AUXI OR INAP 
     String StrMainBillMode;
     boolean BolEntryForm;
     boolean bolcalcstax ; // This variable is used for PUNE site as there is no infra 
    //'in pune for both normal as well as first infra - Monish 21.7.10
     
	
	private final InchqRepository inchqRepository;
	
	@Autowired private HsnsacmasterRepository hsnsacmasterRepository;
	@Autowired private OutinfraRepository outinfraRepository;
	@Autowired private OutrateRepository outrateRepository;
	
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
		return GenericResponse.builder().result("faild").responseCode("100").message("incheqe details faild to save.").build();
	}


	// String receiptDate = "12/09/2023"; // Replace with your input date
    public String startYearMonthFromInput(String receiptDate) {
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

    // use this method to fetch start date 
    // TODO add if condition for AUXI and INAP
  	private String startYearMonthFromDatabase(String buildingCode, String wing, String flatNum, String billType) {
  		return outrateRepository.fetchStartDate(buildingCode, wing, flatNum, billType);
  	}
  	
	@Override
	public AuxilaryResponse getGridData(GenericRequest request) {
		
		/********************* Main function started ***************************************/
		 //Changes from here
		String strStartYrMonth, strTDS_Rate = "";
        double intAdmincharges = 0, intInfraOutRate = 0 ;
        String strStartDate;
        double intReceiptAmt;
        double intReceipt_TDS ;
          
               
        //' IntIntrestActAmount As Decimal
         // Find Start Year Month
        strStartYrMonth = startYearMonthFromInput(request.getDate());
        strStartDate = startYearMonthFromDatabase(request.getBuildingCode() , request.getWing(), request.getFlatNum(), request.getBillType());
        log.debug("strStartDate : {}",strStartDate);

        intReceiptAmt = Double.parseDouble(request.getTotalAmt());
        intReceipt_TDS = Double.parseDouble(request.getReceiptAmtTds());
		
     // Retrieve & find Out Previous Balance  for That Particular Owner-id From out-infra table
        List<Tuple> findPrevOgRecords = outinfraRepository.findPrevOgRecords((request.getBuildingCode() + request.getWing() + request.getFlatNum()), strStartDate, request.getChargeCode(), request.getBillType());
		log.debug("findPrevOgRecords: {}", findPrevOgRecords.toString());
	
		List<GridResponse> data = new ArrayList<>();
		if(!findPrevOgRecords.isEmpty()) {
			for (Tuple result : findPrevOgRecords) {
				PreviousDBBalance prevResponse = PreviousDBBalance.builder().build();
				prevResponse.setPrevOutMonth(result.get(4, String.class));
				prevResponse.setPrevAdmin(result.get(3, Double.class).toString());
			    prevResponse.setPrevCGSTTax(result.get(2, Double.class).toString());
			    prevResponse.setPrevSGSTTax(result.get(6, Double.class).toString());
			    prevResponse.setDblprevIGSTTax(result.get(7, Double.class).toString());
			    prevResponse.setPrevAmtint(result.get(1, Double.class).toString());
			    prevResponse.setPrevAmtPaid(result.get(0, Double.class).toString());
			    prevResponse.setPrevTds(result.get(8, Double.class).toString());
			    
			    if (intReceiptAmt > 0) {
			    	 String infraAuxiOutRate = outrateRepository.fetchMaintainanceRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("infraAuxiOutRate from db : {}", infraAuxiOutRate);
					    
					    if (infraAuxiOutRate == "") {
					    	intInfraOutRate = 0 ;
						}else
					    prevResponse.setPrevInfraOutRate(infraAuxiOutRate);
					    
					    
					    String infraAdminOutRate = outrateRepository.fetchAdminRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("infraAdminOutRate from db : {}", infraAdminOutRate);
					    if (infraAdminOutRate == "") {
					    	intAdmincharges = 0;
						}else
					    prevResponse.setPrevAdmincharges(infraAdminOutRate);
					    
					    
					    strTDS_Rate = outrateRepository.fetchTDSRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("tdsRate : {}", strTDS_Rate);
					    if (strTDS_Rate == null) {
					    	strTDS_Rate = "0";
						}
				}
			    // IntReceiptAmt, IntInfraOutRate,IntAdmincharges, DblPrev_AmtPaid, dblprev_CGST_tax, dblprev_Admin,strOutMonth, dblprev_SGST_tax, dblprev_IGST_tax, IntReceipt_TDS, strTDS_Rate, dblprev_TDS)
			    GridResponse calculateAllocation = this.calculateAllocation(intReceiptAmt, intInfraOutRate, intAdmincharges, (result.get(0, Double.class)),
			    		(result.get(2, Double.class)), (result.get(3, Double.class)), 
			    		(result.get(4, String.class)), (result.get(6, Double.class)), (result.get(7, Double.class)), 
			    		intReceipt_TDS, (result.get(8, Double.class)), Double.parseDouble(strTDS_Rate));
			    data.add(calculateAllocation);
			}
		}

		

		
		
		if (data.isEmpty()) {
			return AuxilaryResponse.builder()
					.result("failed")
					.responseCode("100")
					.message("no data founded.")
					.build();
		}
			
			return AuxilaryResponse.builder()
					.result("success")
					.responseCode("00")
					.message("successfully fetch data.")
					.startMonth(startYearMonth)
					.endMonth(end)
					.totalMonth(String.valueOf(data.size()))
					.data(data)
					.build();
	}
	

	public GridResponse calculateAllocation(double intReceiptAmt, double intMaintRate, double intAdminRate,
			double prevMaintPaid, double prevCGSTtaxPaid, double prevAdminPaid, String outMonth,
			double prevSGSTtaxPaid, double prevIGSTtaxPaid, double tDSReceiptAmt, double tDSRate, double prevTDStaxPaid) {
		
		Hsnsacmaster hsnsacmaster = hsnsacmasterRepository.findByHsnsacmasterCKHsmsCode("995419");
		log.debug("hsnsacmaster : {}", hsnsacmaster);

		Double lnglocCGSTPerc = hsnsacmaster.getHsmsCgstperc();
		Double lnglocSGSTPerc = hsnsacmaster.getHsmsCgstperc();
		Double lnglocIGSTPerc = hsnsacmaster.getHsmsCgstperc();
		
		GridResponse response = GridResponse.builder().build();

		double prevCGSTAdmin = 0, prevSGSTAdmin, prevIGSTAdmin;
		double prevCGSTMaint = 0, prevSGSTMaint = 0, prevIGSTMaint;

		if (intReceiptAmt <= 0) {
		}
		String billType = "F";
		double cgstOnAdminRate, sgstOnAdminRate, igstOnAdminRate = 0;
		double cgstOnMaintRate, sgstOnMaintRate, igstOnMaintRate;
		double prevCgstAdmin, pevSgstAdmin, prevIgstAdmin = 0;
		double prevCgstMaint, prevSgstMaint, prevIgstMaint;
		double adjAdmin, adjMaint, adjTDS = 0;
		double adjCGSTAdmin, adjSGSTAdmin, adjIGSTAdmin;
		double adjCGSTMaint, adjSGSTMaint, adjIGSTMaint;
		
		// Calculate GST ON ADMIN
		if (billType.equals("F")) {
			cgstOnAdminRate = Math.round((lnglocCGSTPerc * intAdminRate) / 100.0);
			sgstOnAdminRate = Math.round((lnglocSGSTPerc * intAdminRate) / 100.0);
			sgstOnAdminRate = Math.round((lnglocIGSTPerc * intAdminRate) / 100.0);

			cgstOnMaintRate = Math.round((lnglocCGSTPerc * intMaintRate) / 100.0);
			sgstOnMaintRate = Math.round((lnglocSGSTPerc * intMaintRate) / 100.0);
			igstOnMaintRate = Math.round((lnglocIGSTPerc * intMaintRate) / 100.0);
		} else {
			cgstOnAdminRate = Math.ceil((lnglocCGSTPerc * intAdminRate) / 100.0);
			sgstOnAdminRate = Math.ceil((lnglocSGSTPerc * intAdminRate) / 100.0);
			sgstOnAdminRate = Math.ceil((lnglocIGSTPerc * intAdminRate) / 100.0);

			cgstOnMaintRate = Math.ceil((lnglocCGSTPerc * intMaintRate) / 100.0);
			sgstOnMaintRate = Math.ceil((lnglocSGSTPerc * intMaintRate) / 100.0);
			igstOnMaintRate = Math.ceil((lnglocIGSTPerc * intMaintRate) / 100.0);
		}

		// Bifurcate GST into Admin GST and Maint GST
		if (prevCGSTtaxPaid >= cgstOnAdminRate) {
			prevCgstAdmin = cgstOnAdminRate; // adjust full-amount
			prevCgstMaint = prevCGSTtaxPaid - prevCgstAdmin; // adjust remaining
		} else {
			prevCgstAdmin = prevCGSTtaxPaid;
			prevCgstMaint = 0;
		}

		if (prevSGSTtaxPaid >= sgstOnAdminRate) {
			prevSGSTAdmin = sgstOnAdminRate;
			prevSGSTMaint = prevSGSTtaxPaid - prevSGSTAdmin;
		} else {
			prevSGSTAdmin = prevSGSTtaxPaid;
			prevSgstMaint = 0;
		}

		if (prevIGSTtaxPaid >= igstOnAdminRate) {
			prevIGSTAdmin = igstOnAdminRate;
			prevIGSTMaint = prevIGSTtaxPaid - prevIgstAdmin;
		} else {
			prevIGSTAdmin = prevIGSTtaxPaid;
			prevIGSTMaint = 0;
		}

		// Check if FULL Amount already received
		if ((prevAdminPaid + prevCGSTAdmin + prevSGSTAdmin + prevIGSTAdmin + prevMaintPaid + prevCGSTMaint
				+ prevSGSTMaint + prevIGSTMaint) >= (intAdminRate + cgstOnAdminRate + sgstOnAdminRate + igstOnAdminRate
						+ intMaintRate + cgstOnMaintRate + sgstOnMaintRate + igstOnMaintRate)) {
			intReceiptAmt = 0; 
		}

		// If Admin amount remains to be adjusted
		/*****************ADMIN Start**************************************/
		adjAdmin = intAdminRate - prevAdminPaid;
		adjCGSTAdmin = cgstOnAdminRate - prevCgstAdmin;
		adjSGSTAdmin = sgstOnAdminRate - prevSGSTAdmin;
		adjIGSTAdmin = igstOnAdminRate - prevIGSTAdmin;
		if (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin > 0 && intReceiptAmt > 0) {
			if (intReceiptAmt >= adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin) {
				intReceiptAmt = intReceiptAmt - (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin);
			} else {
				if (intReceiptAmt <= 2) {
					adjAdmin = intReceiptAmt;
					adjCGSTAdmin = 0;
					adjSGSTAdmin = 0;
					adjIGSTAdmin = 0;
					intReceiptAmt = 0;
				}else {
					double factor = intReceiptAmt / (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin);
					if (billType.equals("F")) {
						 adjCGSTAdmin = Math.round(adjCGSTAdmin * factor);
			             adjSGSTAdmin = Math.round(adjSGSTAdmin * factor);
			             adjIGSTAdmin = Math.round(adjIGSTAdmin * factor);
					}else {
						 adjCGSTAdmin = Math.ceil(adjCGSTAdmin * factor);
			             adjSGSTAdmin = Math.ceil(adjSGSTAdmin * factor);
			             adjIGSTAdmin = Math.ceil(adjIGSTAdmin * factor);
					}
					adjAdmin = intReceiptAmt - adjCGSTAdmin - adjSGSTAdmin - adjIGSTAdmin;
					intReceiptAmt = 0;
				}
			}

		}
		adjAdmin = 0;
	   	adjCGSTAdmin = 0;
	   	adjSGSTAdmin = 0;
	   	adjIGSTAdmin = 0;
	   	
	    /*****************ADMIN END**************************************/
	   	
	   	// If MAINT amount remains to be adjusted
        /*****************MAINT Start**************************************/
	   	adjMaint = intMaintRate - prevMaintPaid;
	   	adjCGSTMaint = cgstOnMaintRate - prevCGSTMaint;
	   	adjSGSTMaint = sgstOnMaintRate - prevSGSTMaint;
	   	adjIGSTMaint = igstOnMaintRate - prevIGSTMaint;
	    if(adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint > 0  && intReceiptAmt > 0 ) {
	    	if (intReceiptAmt >=adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint) {
	    		intReceiptAmt = intReceiptAmt - (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);
			}else {
				if(intReceiptAmt <= 2) {
					adjMaint = intReceiptAmt; 
		            adjCGSTMaint = 0;
		            adjSGSTMaint = 0;
		            adjIGSTMaint = 0;
		            intReceiptAmt = 0;
				}else {
					 double factor = intReceiptAmt / (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);
					if (billType.equals("F")) {
						adjCGSTMaint = Math.round(adjCGSTMaint * factor);
						adjCGSTMaint = Math.round(adjSGSTMaint * factor);
		                adjIGSTMaint = Math.round(adjIGSTMaint * factor);
					}else {
						adjCGSTMaint = Math.ceil(adjCGSTMaint * factor);
		                adjSGSTMaint = Math.ceil(adjSGSTMaint * factor);
		                adjIGSTMaint = Math.ceil(adjIGSTMaint * factor);
					}
					adjMaint = intReceiptAmt - adjCGSTMaint - adjSGSTMaint - adjIGSTMaint;
		            intReceiptAmt = 0;
				}
			}
	    }
	    adjMaint = 0;
	    adjCGSTMaint = 0;
	    adjSGSTMaint = 0;
	    adjIGSTMaint = 0;
	   	/*****************MAINT END**************************************/
	    
	    //If TDS amount remains to be adjusted
        /*****************TDS Start**************************************/
	    //Check if FULL Amount already received
	    
	    if(tDSReceiptAmt < 0 ) {
	    	if(prevTDStaxPaid <= (tDSRate)){
	    		
	    	}else {
	    		adjTDS = tDSRate - prevTDStaxPaid;
	    		if(adjTDS < 0) {
	    			if(tDSReceiptAmt < adjTDS) {
	    				tDSReceiptAmt = tDSReceiptAmt - adjTDS;
	    			}else {
	    				adjTDS = tDSReceiptAmt;
	                    tDSReceiptAmt = 0;
					}
	    			adjTDS = 0;
	    		}
			}
	    }
	    if ((adjAdmin == 0.0) && ((adjCGSTMaint+adjCGSTAdmin) == 0.0) && (adjMaint == 0.0) && ((adjSGSTMaint+adjSGSTAdmin) == 0.0) && ((adjIGSTMaint+adjIGSTAdmin) == 0.0) && (adjTDS == 0) ) {
			
		}else {
			return GridResponse.builder()
					.monthName(outMonth)
					.narration("")
					.auxiPaid(String.valueOf(adjMaint))
					.intPaid("0")
					.cgst(String.valueOf(adjCGSTMaint+adjCGSTAdmin))
					.sgst(String.valueOf(adjSGSTMaint+adjSGSTAdmin))
					.igst(String.valueOf(adjIGSTMaint+adjIGSTAdmin))
					.admin(String.valueOf(adjAdmin))
					.cgstPercent(String.valueOf(lnglocCGSTPerc))
					.sgstPercent(String.valueOf(lnglocIGSTPerc))
					.igstPercent(String.valueOf(lnglocIGSTPerc))
					.tds(String.valueOf(adjTDS))
					.build();
		}
		return response;

	}
}
