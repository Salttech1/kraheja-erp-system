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
import kraheja.sales.bean.entitiesresponse.Balance;
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
	private double receiptAmt, cgst, sgst, igst;
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
        double receiptTDSInput ;
          
               
        //' IntIntrestActAmount As Decimal
         // Find Start Year Month
        strStartYrMonth = startYearMonthFromInput(request.getDate());
        strStartDate = startYearMonthFromDatabase(request.getBuildingCode() , request.getWing(), request.getFlatNum(), request.getBillType());
        log.debug("strStartDate : {}",strStartDate);

        receiptAmt = Double.parseDouble(request.getTotalAmt());
        receiptTDSInput = Double.parseDouble(request.getReceiptAmtTds());
		
     // Retrieve & find Out Previous Balance  for That Particular Owner-id From out-infra table
     List<Balance> balanceList = outinfraRepository.findPreviousBalance((request.getBuildingCode()+request.getWing()+request.getFlatNum()), strStartDate, request.getChargeCode(), request.getBillType());
		log.debug("balance : {}", balanceList);

		List<GridResponse> data = new ArrayList<>();
		if(!balanceList.isEmpty()) {
			String infraAuxiOutRate = "", infraAdminOutRate = "", tdsRate = "";
			for (Balance balance : balanceList) {
			    if (receiptAmt > 0) {
			    	  infraAuxiOutRate = outrateRepository.fetchMaintainanceRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("infraAuxiOutRate from db : {}", infraAuxiOutRate);
					    
					    if (infraAuxiOutRate == "") {
					    	infraAuxiOutRate = "0" ;
						}
					    
					    infraAdminOutRate = outrateRepository.fetchAdminRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("infraAdminOutRate from db : {}", infraAdminOutRate);
					    if (infraAdminOutRate == "") {
					    	infraAdminOutRate = "0";
						}
					    
					    tdsRate = outrateRepository.fetchTDSRateForAuxilliary(request.getBuildingCode(), request.getWing(),  request.getFlatNum(), request.getBillType());
					    log.debug("tdsRate : {}", strTDS_Rate);
					    if (tdsRate == null) {
					    	tdsRate = "0";
						}
					    
				}
			    // IntReceiptAmt, IntInfraOutRate,IntAdmincharges, DblPrev_AmtPaid, dblprev_CGST_tax, dblprev_Admin,strOutMonth, dblprev_SGST_tax, dblprev_IGST_tax, IntReceipt_TDS, strTDS_Rate, dblprev_TDS)
			    GridResponse calculateAllocation = this.calculateAllocation(balance, Double.parseDouble(infraAuxiOutRate), Double.parseDouble(infraAdminOutRate), Double.parseDouble(tdsRate), request.getBillType(), receiptTDSInput);
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
	

	public GridResponse calculateAllocation(Balance balance, double maintRate, double adminRate, double tdsRate, String billType, double receiptTDSInput) {
		
		int cgstOnAdminRate, sgstOnAdminRate, igstOnAdminRate = 0;
		int cgstOnMaintRate, sgstOnMaintRate, igstOnMaintRate;
		double prevCgstMaint, prevSgstMaint, prevIgstMaint;
		
		double prevCgstAdmin, pevSgstAdmin, prevIgstAdmin = 0;
		
		double adjAdmin, adjMaint, adjTDS = 0;
		double adjCGSTAdmin, adjSGSTAdmin, adjIGSTAdmin;
		double adjCGSTMaint, adjSGSTMaint, adjIGSTMaint;
		
		Hsnsacmaster hsnsacmaster = hsnsacmasterRepository.findByHsnsacmasterCKHsmsCode("995419");
		log.debug("hsnsacmaster : {}", hsnsacmaster);

		Double lnglocCGSTPerc = hsnsacmaster.getHsmsCgstperc();
		Double lnglocSGSTPerc = hsnsacmaster.getHsmsCgstperc();
		Double lnglocIGSTPerc = hsnsacmaster.getHsmsCgstperc();
		

		double prevCGSTAdmin = 0, prevSGSTAdmin, prevIGSTAdmin;
		double prevCGSTMaint = 0, prevSGSTMaint = 0, prevIGSTMaint;

		if (receiptAmt <= 0) {
		}
		
		// Calculate GST ON ADMIN
		if (billType.equals("F")) {
			cgstOnAdminRate = (int) Math.round((lnglocCGSTPerc * adminRate) / 100.0);
			sgstOnAdminRate = (int) Math.round((lnglocSGSTPerc * adminRate) / 100.0);
			sgstOnAdminRate = (int) Math.round((lnglocIGSTPerc * adminRate) / 100.0);

			cgstOnMaintRate = (int) Math.round((lnglocCGSTPerc * maintRate) / 100.0);
			sgstOnMaintRate = (int) Math.round((lnglocSGSTPerc * maintRate) / 100.0);
			igstOnMaintRate = (int) Math.round((lnglocIGSTPerc * maintRate) / 100.0);
		} else {
			cgstOnAdminRate = (int) Math.ceil((lnglocCGSTPerc * adminRate) / 100.0);
			sgstOnAdminRate = (int) Math.ceil((lnglocSGSTPerc * adminRate) / 100.0);
			sgstOnAdminRate = (int) Math.ceil((lnglocIGSTPerc * adminRate) / 100.0);

			cgstOnMaintRate = (int) Math.ceil((lnglocCGSTPerc * maintRate) / 100.0);
			sgstOnMaintRate = (int) Math.ceil((lnglocSGSTPerc * maintRate) / 100.0);
			igstOnMaintRate = (int) Math.ceil((lnglocIGSTPerc * maintRate) / 100.0);
		}

		// Bifurcate GST into Admin GST and Maint GST
		if (balance.getCgst() >= cgstOnAdminRate) {
			prevCgstAdmin = cgstOnAdminRate; 
			prevCgstMaint = balance.getCgst() - prevCgstAdmin;
		} else {
			prevCgstAdmin = balance.getCgst();
			prevCgstMaint = 0;
		}

		if (balance.getSgst() >= sgstOnAdminRate) {
			prevSGSTAdmin = sgstOnAdminRate;
			prevSGSTMaint = balance.getSgst() - prevSGSTAdmin;
		} else {
			prevSGSTAdmin = balance.getSgst();
			prevSgstMaint = 0;
		}

		if (balance.getIgst() >= igstOnAdminRate) {
			prevIGSTAdmin = igstOnAdminRate;
			prevIGSTMaint = balance.getIgst() - prevIGSTAdmin;
		} else {
			prevIGSTAdmin = balance.getIgst();
			prevIGSTMaint = 0;
		}

		// Check if FULL Amount already received
		if ((balance.getAdminharges() + prevCGSTAdmin + prevSGSTAdmin + prevIGSTAdmin + balance.getAmtPaid() + prevCGSTMaint
				+ prevSGSTMaint + prevIGSTMaint) >= (adminRate + cgstOnAdminRate + sgstOnAdminRate + igstOnAdminRate
						+ maintRate + cgstOnMaintRate + sgstOnMaintRate + igstOnMaintRate)) {
			receiptAmt = 0;
		}

		// If Admin amount remains to be adjusted
		/*****************ADMIN Start**************************************/
		adjAdmin = adminRate - balance.getAdminharges();
		adjCGSTAdmin = cgstOnAdminRate - prevCgstAdmin;
		adjSGSTAdmin = sgstOnAdminRate - prevSGSTAdmin;
		adjIGSTAdmin = igstOnAdminRate - prevIGSTAdmin;
		
		if ((adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin) > 0 && receiptAmt > 0) {
			
			if (receiptAmt >= (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin)) {
				
				receiptAmt = receiptAmt - (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin);
			} 
			else {
				if (receiptAmt <= 2) {
					adjAdmin = receiptAmt;
					adjCGSTAdmin = 0;
					adjSGSTAdmin = 0;
					adjIGSTAdmin = 0;
					receiptAmt = 0;
				}else {
					double factor = (receiptAmt / (adjAdmin + adjCGSTAdmin + adjSGSTAdmin + adjIGSTAdmin));
					if (billType.equals("F")) {
						int adjCGSTAdm = (int) Math.round(adjCGSTAdmin * factor);
						adjCGSTAdmin = adjCGSTAdm;
						
			            int adjSGSTAdm = (int)Math.round(adjSGSTAdmin * factor);
			            adjSGSTAdmin = adjSGSTAdm;
			            
			            int adjIGSTAdm = (int) Math.round(adjIGSTAdmin * factor);
			             adjIGSTAdmin = adjIGSTAdm;
			             
					}else {
						 adjCGSTAdmin = Math.ceil(adjCGSTAdmin * factor);
			             adjSGSTAdmin = Math.ceil(adjSGSTAdmin * factor);
			             adjIGSTAdmin = Math.ceil(adjIGSTAdmin * factor);
					}
					adjAdmin = receiptAmt - adjCGSTAdmin - adjSGSTAdmin - adjIGSTAdmin;
					receiptAmt = 0;
				}
			}

		}else {
			adjAdmin = 0;
		   	adjCGSTAdmin = 0;
		   	adjSGSTAdmin = 0;
		   	adjIGSTAdmin = 0;
		}
		
	    /*****************ADMIN END**************************************/
	   	
	   	// If MAINT amount remains to be adjusted
        /*****************MAINT Start**************************************/
	   	adjMaint = maintRate - balance.getAmtPaid();
	   	adjCGSTMaint = cgstOnMaintRate - prevCGSTMaint;
	   	adjSGSTMaint = sgstOnMaintRate - prevSGSTMaint;
	   	adjIGSTMaint = igstOnMaintRate - prevIGSTMaint;
	   	
	    if((adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint) > 0  && receiptAmt > 0 ) {
	    	
	    	if (receiptAmt >= (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint)) {
	    		receiptAmt = receiptAmt - (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);
	    		
			}else {
				if(receiptAmt <= 2) {
					adjMaint = receiptAmt; 
		            adjCGSTMaint = 0;
		            adjSGSTMaint = 0;
		            adjIGSTMaint = 0;
		            receiptAmt = 0;
				}else {
					 double factor = receiptAmt / (adjMaint + adjCGSTMaint + adjSGSTMaint + adjIGSTMaint);
					if (billType.equals("F")) {
						int adjCGSTMa = (int) Math.round(adjCGSTMaint * factor);
						adjCGSTMaint = adjCGSTMa;
						
						int adjSGSTMa = (int) Math.round(adjSGSTMaint * factor);
						adjCGSTMaint = adjSGSTMa;
						
						int adjIGSTMa = (int) Math.round(adjIGSTMaint * factor);
		                adjIGSTMaint = adjIGSTMa;
		                
					}else {
						adjCGSTMaint = Math.ceil(adjCGSTMaint * factor);
		                adjSGSTMaint = Math.ceil(adjSGSTMaint * factor);
		                adjIGSTMaint = Math.ceil(adjIGSTMaint * factor);
					}
					adjMaint = receiptAmt - adjCGSTMaint - adjSGSTMaint - adjIGSTMaint;
		            receiptAmt = 0;
				}
			}
	    }else {
	    	 adjMaint = 0;
	 	    adjCGSTMaint = 0;
	 	    adjSGSTMaint = 0;
	 	    adjIGSTMaint = 0;
	    }
	   	/*****************MAINT END**************************************/
	    
	    
	    //If TDS amount remains to be adjusted
        /*****************TDS Start**************************************/
	    //Check if FULL Amount already received
	    
	    if(receiptTDSInput < 0 ) {
	    	if(balance.getTds() <= tdsRate){
	    		
	    	}else {
	    		adjTDS = tdsRate - balance.getTds();
	    		
	    		if(adjTDS < 0) {
	    			
	    			if(receiptTDSInput < adjTDS) {
	    				
	    				receiptTDSInput = receiptTDSInput - adjTDS;
	    				
	    			}else {
	    				
	    				adjTDS = receiptTDSInput;
	    				
	    				receiptTDSInput = 0;
					}
	    		}
	    		else {
	    			adjTDS = 0;
				}
			}
	    }
	    
	    if ((adjMaint == 0.0) && ((adjCGSTAdmin+adjCGSTMaint) == 0.0) && (adjAdmin == 0.0) && ((adjSGSTAdmin+adjSGSTMaint) == 0.0) && ((adjIGSTMaint+adjIGSTAdmin) == 0.0) && (adjTDS == 0) ) {
	    		return GridResponse.builder().build();
	 		}else {
	 			return GridResponse.builder()
	 					.monthName(balance.getMonth())
	 					.narration("")
	 					.narrationCode("FP")
	 					.auxiPaid(String.valueOf(adjMaint))
	 					.intPaid("0")
	 					.cgst(String.valueOf(adjCGSTAdmin + adjCGSTMaint))
	 					.sgst(String.valueOf(adjSGSTAdmin + adjSGSTMaint))
	 					.igst(String.valueOf(adjIGSTAdmin + adjIGSTMaint))
	 					.admin(String.valueOf(adjAdmin))
	 					.cgstPercent(String.valueOf(lnglocCGSTPerc))
	 					.sgstPercent(String.valueOf(lnglocIGSTPerc))
	 					.igstPercent(String.valueOf(lnglocIGSTPerc))
	 					.tds(String.valueOf(adjTDS))
	 					.build();
	 		}
	}
}
