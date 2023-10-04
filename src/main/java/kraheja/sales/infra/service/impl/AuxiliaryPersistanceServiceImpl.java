package kraheja.sales.infra.service.impl;

import java.lang.invoke.MethodHandles;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.base.Optional;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.Inchq;
import kraheja.commons.entity.InchqCK;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.GlchartRepository;
import kraheja.commons.repository.InchqRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.sales.bean.entitiesresponse.AuxiBuildingDBResponse;
import kraheja.sales.bean.entitiesresponse.GlchartDBResponse;
import kraheja.sales.bean.request.InchequeRequest;
import kraheja.sales.bean.response.GridResponse;
import kraheja.sales.infra.service.AuxiliaryPersistanceService;
import lombok.extern.log4j.Log4j2;

/**
 * <p>this class is use to persist data row wise in database.</p>
 * @author sazzad.alom
 * @since 28-OCTBER-2023
 * @version 1.0.0
 * */
@Log4j2
@Service
public class AuxiliaryPersistanceServiceImpl implements AuxiliaryPersistanceService {

	@Autowired private PartyRepository partyRepository;
	@Autowired private BuildingRepository buildingRepository;
	@Autowired private  GlchartRepository glchartRepository;
	@Autowired private CompanyRepository companyRepository;
	@Autowired private InchqRepository inchqRepository;



	
	/**
	 * <p>this method is use to get party code and minor map.</p>
	 * */
	private Map<String, String> getPartyCodeAndMinor(String bldgCode, String wing, String flatNumber, String chargeCode) {
		
		Map<String, String> partyCodeMinorMap = new HashMap<>();
		String partyCode = "";
		String lastOwnerid = "";
		int ownerIdCount = 0;
		String ownerid = "";
		String minor = "";

		if (flatNumber.length() == 5) {
			flatNumber = flatNumber + " ";
		}

		if (wing.equals("")) {
			wing = " ";
		}
		partyCode = bldgCode + wing + flatNumber + "%";
		
		
		lastOwnerid = partyRepository.getPartyCode(partyCode, "F");
		log.debug("partyCode from db : {}", lastOwnerid);
		
		if (lastOwnerid.equals("")) {
			ownerIdCount=1;
		}else {
			ownerIdCount = Integer.parseInt(lastOwnerid.substring(lastOwnerid.length() - 1));
		}
		ownerid = bldgCode + wing + flatNumber;
		if (ownerid.length() <= 10) {
			partyCode = bldgCode + wing + flatNumber + " " + String.valueOf(ownerIdCount);
			minor = bldgCode + wing + chargeCode + flatNumber + " " + String.valueOf(ownerIdCount);
		}else {
			partyCode = bldgCode + wing + flatNumber + String.valueOf(ownerIdCount);
			minor = bldgCode + wing + chargeCode + flatNumber + String.valueOf(ownerIdCount);
		}
		partyCodeMinorMap.put("partyCode", partyCode);
		partyCodeMinorMap.put("minor", minor);
		return partyCodeMinorMap;
	}


	@Override
	public String saveIncheqe(String bldgCode, String wing, String flatNumber, String chargeCode, InchequeRequest inchequeRequest) {
		log.debug("inchequeRequest: {}", inchequeRequest);
		
		double totalReceiptAmt = Double.parseDouble(inchequeRequest.getChequeAmount());
        String ownerId = "";
        String chequeNo = inchequeRequest.getChequeNumber(); 
        String inchqMode = "C";
		
		Map<String, String> partyCodeAndMinor = getPartyCodeAndMinor(bldgCode, wing, flatNumber, chargeCode);
		String partyCode = partyCodeAndMinor.get("partyCode");
		String minor = partyCodeAndMinor.get("minor");
		log.debug("partyCode : {} minor: {}", partyCode, minor);
		
		AuxiBuildingDBResponse buildingDBResponse = buildingRepository.findBuildingByCode(bldgCode);
		log.debug("buildingDBResponse : {} ", buildingDBResponse);
//		GlchartDBResponse glchartDBResponse = glchartRepository.fetchChartCfrecgroup("11401233");
		GlchartDBResponse glchartDBResponse = new GlchartDBResponse();
		log.debug("glchartDBResponse : {}", glchartDBResponse);
		
		String rgroupc = String.format("%1$6s", glchartDBResponse.getChartRgroupc());
		String cfrecgroup = glchartDBResponse.getChartCfrecgroup();
		
		if (rgroupc.length() > 6) {
			rgroupc = rgroupc.substring(rgroupc.length() - 6);
		}
		if (rgroupc.equals("XXXXXX")) {
			rgroupc = "";
		}
		if (cfrecgroup.equals("XXXXXX")) {
			cfrecgroup = "";
		}
		List<GridResponse> gridRequest = new ArrayList<>();
		gridRequest.add(GridResponse.builder().monthName("202306").build());
		gridRequest.add(GridResponse.builder().monthName("202308").build());
		int last = gridRequest.size()-1;
		GridResponse firsGridObject = gridRequest.get(0);
		GridResponse lastGridObject = gridRequest.get(last);
		
		String year = firsGridObject.getMonthName().substring(4,6);
		String month = firsGridObject.getMonthName().substring(0,4);
		String startFrom = "01/"+month+"/"+year;
		
		year = lastGridObject.getMonthName().substring(4,6);
		month = lastGridObject.getMonthName().substring(0,4);
		
		 // Create a Calendar instance
        Calendar calendar = Calendar.getInstance();
        // Set the calendar date to the last day of the specified month and year
        calendar.set(Integer.parseInt(year), Integer.parseInt(month) - 1, 1);
        int lastDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        String endTill = lastDay + "/" + month + "/" + year;

        
//        if (adjAmt == totalReceiptAmt) {
//        	 AuxiBuildingDBResponse auxiBuildingDBResponse = buildingRepository.findBuildingByCode(bldgCode);
//        	 log.debug("auxiBuildingDBResponse : {}", auxiBuildingDBResponse);
//        	 
//        	 if (auxiBuildingDBResponse.getBldgMaintcoy() == null) {
//        		 
//        		 auxiBuildingDBResponse.setBldgMaintcoy(auxiBuildingDBResponse.getBldgCoy());
//			}
//        	
//			String coyProp = companyRepository.findCoyPropByCodeAndClosedate(auxiBuildingDBResponse.getBldgMaintcoy());
//    		log.debug("coyProp : {}", coyProp);
//		}
        
        if (wing.equals("")) {
        	ownerId = bldgCode + " " + flatNumber;
			wing = " ";
		}else {
			ownerId = bldgCode + wing + flatNumber;
		}
        if (chequeNo.equals("")) {
			inchqMode = "Q";
		}else {
			inchqMode = "C";
		}
        
        LocalDateTime inchqDate = LocalDateTime.parse(inchequeRequest.getChequeDate(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
       log.debug("inchqDate : {}", inchqDate);

        InchqCK ck = InchqCK.builder()
        		.inchqNum(inchequeRequest.getChequeNumber()) //1
        		.inchqBank(inchequeRequest.getBank()) //4
//        		.inchqCoy("SOGR")
//        		.inchqRecnum("")
        		.build();
        
        Inchq inchqEntity = Inchq.builder()
        		.inchqCk(ck)
        		.inchqPaymode(inchqMode) //0
        		.inchqAmount(totalReceiptAmt) //2
        		.inchqDate(inchqDate) //3
        		.inchqOutstat(inchequeRequest.getOutstat()) //5
//        		.inchqResubcount(0.00)
//        		.inchqRemark(null)
        		.inchqProprietor(buildingDBResponse.getBldgProp())
//        		.inchqOrigsys(partyCode)
        		.inchqPartycode(partyCode)
        		.inchqFund(inchequeRequest.getFundSource())
        		.inchqActype(inchequeRequest.getAcType())
//        		.inchqLoanyn("")
        		.inchqSite(inchequeRequest.getSiteName())
        		.inchqUserid(inchequeRequest.getUserId())
        		.inchqToday(LocalDateTime.now())
//        		.inchqOrigsite("")
//        		.inchqCoybank("")
        		.build();
        
        
        String result = "Faild";
        Inchq save = inchqRepository.save(inchqEntity);
        if (!(save.getInchqCk().getInchqBank()).equals(null)) {
        	result = "incheq detail save successfully.";
		}else {
			result = "incheq detail failed to save.";
		}
		return result;
	}

}
