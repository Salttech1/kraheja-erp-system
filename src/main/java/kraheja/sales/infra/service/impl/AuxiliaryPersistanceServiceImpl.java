package kraheja.sales.infra.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.Actranh;
import kraheja.commons.entity.ActranhCK;
import kraheja.commons.entity.Actranhx;
import kraheja.commons.entity.ActranhxCK;
import kraheja.commons.entity.Inchq;
import kraheja.commons.entity.InchqCK;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.ActranhxRepository;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.GlchartRepository;
import kraheja.commons.repository.InchqRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.utils.GenericAccountingLogic;
import kraheja.constant.ApiResponseCode;
import kraheja.constant.ApiResponseMessage;
import kraheja.constant.Result;
import kraheja.exception.InternalServerError;
import kraheja.sales.bean.entitiesresponse.AuxiBuildingDBResponse;
import kraheja.sales.bean.entitiesresponse.GlchartDBResponse;
import kraheja.sales.bean.request.ChequeRequest;
import kraheja.sales.bean.request.InchequeRequest;
import kraheja.sales.bean.response.GridResponse;
import kraheja.sales.bean.response.InchequeDetailResponse;
import kraheja.sales.bean.response.InchequeResponse;
import kraheja.sales.entity.Outinfra;
import kraheja.sales.entity.OutinfraCK;
import kraheja.sales.infra.service.AuxiliaryPersistanceService;
import kraheja.sales.infra.utilities.NumberGenerator;
import kraheja.sales.repository.OutinfraRepository;
import lombok.extern.log4j.Log4j2;

/**
 * <p>
 * this class is use to persist data row wise in database.
 * </p>
 * 
 * @author sazzad.alom
 * @since 28-OCTBER-2023
 * @version 1.0.0
 */
@Log4j2
@Service
@Transactional
public class AuxiliaryPersistanceServiceImpl implements AuxiliaryPersistanceService {

	@Autowired
	private PartyRepository partyRepository;
	@Autowired
	private BuildingRepository buildingRepository;
	@Autowired
	private GlchartRepository glchartRepository;
	@Autowired
	private CompanyRepository companyRepository;
	@Autowired
	private InchqRepository inchqRepository;
	
	@Autowired
	private OutinfraRepository outinfraRepository;
	
	@Autowired
	private ActranhRepository actranhRepository;
	
	
	@Autowired
	private GenericAccountingLogic genericAccountingLogic;
	
	@Autowired
	private ActranhxRepository actranhxRepository;
	

	/**
	 * <p>
	 * this method is use to get party code and minor map.
	 * </p>
	 */
	private Map<String, String> getPartyCodeAndMinor(String bldgCode, String wing, String flatNumber,String chargeCode) {

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
			ownerIdCount = 1;
		} else {
			ownerIdCount = Integer.parseInt(lastOwnerid.substring(lastOwnerid.length() - 1));
		}
		ownerid = bldgCode + wing + flatNumber;
		if (ownerid.length() <= 10) {
			partyCode = bldgCode + wing + flatNumber + " " + String.valueOf(ownerIdCount);
			minor = bldgCode + wing + chargeCode + flatNumber + " " + String.valueOf(ownerIdCount);
		} else {
			partyCode = bldgCode + wing + flatNumber + String.valueOf(ownerIdCount);
			minor = bldgCode + wing + chargeCode + flatNumber + String.valueOf(ownerIdCount);
		}
		partyCodeMinorMap.put("partyCode", partyCode);
		partyCodeMinorMap.put("minor", minor);
		return partyCodeMinorMap;
	}

	@Override
	public InchequeResponse saveIncheqe(String bldgCode, String wing, String flatNumber, String chargeCode,String siteName,String userId,
			InchequeRequest inchequeRequest) {
		log.debug("inchequeRequest: {}", inchequeRequest);
		String result = Result.FAILED;
		String message = ApiResponseMessage.INCHEQ_DETAIL_FAILED_TO_SAVE ;
		String responseCode = ApiResponseCode.FAILED;
		String ownerId = "";
		String chequeNo = "";
		double totalReceiptAmt = 0.00;
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
		String outInfraPersistResult = this.saveOutInfra(inchequeRequest.getGridRequest(),userId, siteName, bldgCode, wing, flatNumber, chargeCode, inchequeRequest.getReceiptDate());
		log.debug("out infra persist result: {}", outInfraPersistResult);
		
/*
 * this method is use for insert int actranh table
 */
		ActranhCK actranhCk = ActranhCK.builder().acthTranser(NumberGenerator.getTranser()).acthCoy(buildingDBResponse.getBldgCoy()).build();
		Actranh actranh = Actranh.builder()
				.actranhCK(actranhCk)
				.acthTrantype("RC")
				.acthTrandate(LocalDateTime.now())
				.acthLedgcode("")
				.acthPartytype("F")
				.acthPartycode(partyCode)
				.acthTranamt(totalReceiptAmt)
				.acthProprietor(buildingDBResponse.getBldgProp())
				.acthVounum(NumberGenerator.getReceiptNumber())
				.acthVoudate(LocalDateTime.now().toLocalDate())
				.acthPostedyn("N")
				.acthSite(siteName)
				.acthUserid(userId)
				.acthToday(LocalDateTime.now())
				.acthClearacyn("N")
				.acthReverseyn("N")
				.acthProvyn("N")
				.build();
		
		String updateActranhResult = this.updateActranh(actranh);
		log.debug("actranh persistance result: {}", updateActranhResult);

		
		
		List<ChequeRequest> cheques = inchequeRequest.getCheques();
		
		List<InchequeDetailResponse> chequeResponse = new ArrayList<>();
	
		if (wing.equals("")) {
			ownerId = bldgCode + " " + flatNumber;
			wing = " ";
		} else {
			ownerId = bldgCode + wing + flatNumber;
		}
		try {
			for (ChequeRequest chequeRequest : cheques) {
				
				chequeNo = chequeRequest.getChequeNumber();
				totalReceiptAmt = Double.parseDouble(chequeRequest.getChequeAmount());
			
				if (chequeNo.equals("")) {
					inchqMode = "Q";
				} else {
					inchqMode = "C";
				}
				InchqCK ck = InchqCK.builder().inchqNum(chequeRequest.getChequeNumber()) // 1
						.inchqBank(chequeRequest.getBank()) // 4
//             		.inchqCoy("SOGR")
//             		.inchqRecnum("")
						.build();

				Inchq inchqEntity = Inchq.builder().inchqCk(ck).inchqPaymode(inchqMode) // 0
						.inchqAmount(totalReceiptAmt) // 2
						.inchqDate(chequeRequest.getChequeDate()) // 3 
						.inchqOutstat(chequeRequest.getOutstat()) // 5
//             		.inchqResubcount(0.00)
//             		.inchqRemark(null)
						.inchqProprietor(buildingDBResponse.getBldgProp())
//             		.inchqOrigsys(partyCode)
						.inchqPartycode(partyCode).inchqFund(chequeRequest.getFundSource())
						.inchqActype(chequeRequest.getAcType())
//             		.inchqLoanyn("")
						.inchqSite(siteName)
						.inchqUserid(userId)
						.inchqToday(LocalDateTime.now())
//             		.inchqOrigsite("")
//             		.inchqCoybank("")
						.build();

				Inchq save = inchqRepository.save(inchqEntity);
				InchequeDetailResponse inchequeDetailResponse = new InchequeDetailResponse();
				if (!(save.getInchqCk().getInchqBank()).equals(null)) {
					inchequeDetailResponse.setChequeNumber(save.getInchqCk().getInchqNum());
					inchequeDetailResponse.setChequeAmount(save.getInchqAmount().toString());;
					inchequeDetailResponse.setMessage("incheq detail save successfully.");
					result = Result.SUCCESS;
					responseCode = ApiResponseCode.SUCCESS;
					message = "incheq detail save successfully.";
				} else {
					inchequeDetailResponse.setChequeNumber(save.getInchqCk().getInchqNum());
					inchequeDetailResponse.setChequeAmount(save.getInchqAmount().toString());;
					inchequeDetailResponse.setMessage("incheq detail failed to save.");
				}
				chequeResponse.add(inchequeDetailResponse);
				
			}
		}
		
		catch (Exception exception) {
			log.error("exception occured :{}", exception.getMessage());
			throw new InternalServerError(exception.getMessage());
		}
		
		return InchequeResponse.builder().result(result).message(message).responseCode(responseCode).chequeResponse(chequeResponse).build();
	}

	



	private String updateActranh(Actranh actranh) {
		Actranh hasRecord = actranhRepository.fetchActranh(actranh.getActranhCK().getActhTranser());
		log.debug("actranh has already recorded: {}", hasRecord);
		if (Objects.nonNull(hasRecord)) {
			ActranhxCK actranhxCK = ActranhxCK.builder().acthCoy(actranh.getActranhCK().getActhCoy()).acthRevision(actranh.getActhReverseyn()).acthToday(LocalDateTime.now()).acthTranser(actranh.getActranhCK().getActhTranser()).build();
			Actranhx actranhx = Actranhx.builder()
			.actranhxCK(actranhxCK)
			.acthTrantype(actranh.getActhTrantype())
			.acthTrandate(actranh.getActhTrandate().toLocalDate())
			.acthPartytype(actranh.getActhPartytype())
			.acthPartycode(actranh.getActhPartycode())
			.acthTranamt(actranh.getActhTranamt())
			.acthProprietor(actranh.getActhProprietor())
			.acthVounum(actranh.getActhVounum())
			.acthVoudate(actranh.getActhVoudate())
			.acthPostedyn(actranh.getActhPostedyn())
			.acthSite(actranh.getActhSite())
			.acthUserid(actranh.getActhUserid())
			.build();
			Actranhx save = actranhxRepository.save(actranhx);
			log.debug("actranhx data obtaint: {}", save);
		}
		Actranh save = actranhRepository.save(actranh);
		if (Objects.nonNull(save)) {
			return Result.SUCCESS;
		}
		return Result.FAILED;
	}

	private String saveOutInfra(List<GridResponse> gridRequest, String userId, String siteName, String bldgCode,String wing,String flatNumber,String chargeCode,LocalDateTime receiptDate) {
		String outInfraSaveResult = Result.FAILED;
		
		String ownerId = bldgCode + wing + flatNumber;
		AuxiBuildingDBResponse buildingDBResponse = buildingRepository.findBuildingByCode(bldgCode);
		if (flatNumber.length() == 5) {
			flatNumber = flatNumber + " ";
		}

		if (wing.equals("")) {
			wing = "";
		}
		if (wing.equals("")) {
			ownerId = bldgCode + " " + flatNumber;
			wing = " ";
		} 
		
		for (GridResponse grid : gridRequest) {
			OutinfraCK outinfraCK = OutinfraCK
					.builder()
					.infRecnum(NumberGenerator.getReceiptNumber())
					.infOwnerid(ownerId)
					.infBldgcode(bldgCode)
					.infMonth(grid.getMonthName())
					.infNarrcode(grid.getNarrationCode())
					.build();
			
			Outinfra outinfra = Outinfra.builder()
			.outinfraCK(outinfraCK)
			.infWing(wing)
			.infFlatnum(flatNumber)
			.infCoy(buildingDBResponse.getBldgCoy())
			.infAmtdue(0.00)
			.infAmtpaid(grid.getAuxiPaid())
			.infAmtos(0.00)
			.infAmtint(grid.getIntPaid())
			.infOrigint(0.00)
			.infChargecode(chargeCode)
			.infRecdate(receiptDate.toLocalDate())
//			.infRecprintyn("N")
			.infCancelledyn("N")
			.infRemarks(grid.getNarration())
			.infSite(siteName)  
			.infUserid(userId)
			.infToday(LocalDateTime.now())
			.infOrigsite(siteName)
			.infGstyn("Y")
			.infRectype("N")
			.infCgst(grid.getCgst())
			.infSgst(grid.getSgst())
			.infIgst(grid.getIgst())
			.infCgstperc(grid.getCgstPercent())
			.infSgstperc(grid.getSgstPercent())
			.infIgstperc(grid.getIgstPercent())
			.build();
			
//			Outinfra saveOutinfra = outinfraRepository.save(outinfra);
			Outinfra saveOutinfra = new Outinfra();
			
			if (Objects.nonNull(saveOutinfra)) {
				outInfraSaveResult = Result.SUCCESS;
			}
		}
		return outInfraSaveResult;
	}
	
	
}
