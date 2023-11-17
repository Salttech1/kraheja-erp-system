package kraheja.sales.infra.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import kraheja.sales.bean.entitiesresponse.InfrBillDBResponse;
import kraheja.sales.entity.Infrbill;
import kraheja.sales.entity.InfrbillCK;
import kraheja.sales.entity.Infrsaogrp01_Print;
import kraheja.sales.infra.mappers.InfraBillBillResponseMapper;
import kraheja.sales.infra.service.PrintBillService;
import kraheja.sales.repository.InfrBillRepository;
import kraheja.sales.repository.Infrsaogrp01_PrintRepository;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class PrintBillServiceImpl implements PrintBillService {

	@Autowired private Infrsaogrp01_PrintRepository printRepository;
	@Autowired
	private InfrBillRepository infrBillRepository;
	@Autowired private BuildingRepository buildingRepository;
	
	@Autowired
	private InfraBillBillResponseMapper infrbillMapper;

	@Override
	public void printBill(String chargeCode, String billType, double sessionId) {
		
		String invoiceNum = "";
		String isPrint = "Y";
		String userId = GenericAuditContextHolder.getContext().getUserid();
		String siteName = GenericAuditContextHolder.getContext().getSite();
		
		// FETCH BILL DETAILS FROM TEMPORARY TABLE INFRSAOGRP01_PRINT
		InfrBillDBResponse printDbResponse = printRepository.getInfrsaogrp01Print(sessionId);
		log.debug("print repository return: {}", printDbResponse);
		
		//TODO change the condition only for non Null
		if (Objects.nonNull(printDbResponse)) {
			String ownerId = printDbResponse.getInfrOwnerId();
			String bldgCode = ownerId.substring(0, 4);
			String month = printDbResponse.getInfrMonth();
			// INSERT/UPDATE VALUES IN INFRBILL TABLE 
			// AS OF OUR NEW SYSTEM INSTATE OF FETCH FIRST BILL NUMBER AND THEN UPDATE IT, FETCH COMPLETE OBJECT AND SET VALUES AS REQUIRED.
			Infrbill infrbill = infrBillRepository.findBillNumberByOwnerIdAndMonth(ownerId, month, chargeCode, billType);
			log.debug("infrbill obtaint : {}", infrbill);
			if (Objects.nonNull(infrbill)) {
				infrbill.setInfrInterest(printDbResponse.getInfrInterest());
				infrbill.setInfrIntarrears(printDbResponse.getInfrIntarrears());
				infrbill.setInfrUserid(userId);
				infrbill.setInfrToday(LocalDateTime.now());
				Infrbill updateInfrBill = infrBillRepository.save(infrbill);
				log.debug("updateInfrBill obtaint: {}", updateInfrBill);
			}else{
				
				String bldgCity = buildingRepository.findBldgCityByBuildingCK_BldgCode(bldgCode);
				log.debug("bldgCity obtaint: {}", bldgCity);
				
				if (bldgCity.equals("") || bldgCity.isBlank()) {
					bldgCity = "MUM";
				}
				String bldgCompany = buildingRepository.findBldgCompanyByBldgCode(bldgCode);
				log.debug("bldgCompany obtaint: {}", bldgCompany);
				
				//TODO INVOICE NUMBER CREATOR ENTITY AND CLASSID IS NOT CREATED
				if (bldgCity.equalsIgnoreCase("MUM")) {
					invoiceNum = bldgCompany + "TXM" + sessionId; 			
//					invoiceNum = "KRHOTXM23000983";
				}else {
					invoiceNum = bldgCompany + "TXP" + sessionId; 	
//					invoiceNum = "KRHOTXP23000983";
				}
				
				Infrsaogrp01_Print infrsaogrp01 = printRepository.findByInfrsaogrp01_printCKSaogrpSessidAndSaogrpOwnerid(sessionId, ownerId.trim());
				log.debug("findByInfrsaogrp01 : {}", infrsaogrp01);
//				infrsaogrp01.getInfrsaogrp01_printCK().setSaogrpBillnum(bldgCompany);
				if (Objects.nonNull(infrsaogrp01)) {
					infrsaogrp01.getInfrsaogrp01_printCK().setSaogrpInvoiceno(invoiceNum);
					printRepository.save(infrsaogrp01);
				}
				
				InfrbillCK infrbillCK = InfrbillCK.builder()
						.infrBillnum(infrsaogrp01.getInfrsaogrp01_printCK().getSaogrpBillnum())
						.infrOwnerId(infrsaogrp01.getSaogrpOwnerid())
						.infrMonth(infrsaogrp01.getSaogrpBillmonth())
						.build();
				
				Infrbill infrbill1 = Infrbill.builder()
				.infrbillCK(infrbillCK)
				.infrAdmincharges(infrsaogrp01.getSaogrpAdmincharges())
				.infrAmtos(infrsaogrp01.getSaogrpOutrate())
				.infrAmtpaid(infrsaogrp01.getSaogrpBillamt())
				.infrArrears(infrsaogrp01.getSaogrpBillarrears())
				.infrBillamt(infrsaogrp01.getSaogrpBillamt())
				.infrBilldate(infrsaogrp01.getSaogrpBilldate())
				.infrBillprinton(infrsaogrp01.getSaogrpBillfrom())
				.infrBillprintyn(isPrint)
//				.infrBillrevnum(null)
				.infrBilltype(billType)
				.infrBldgcode(infrsaogrp01.getSaogrpBldgcode())
				.infrCgst(infrsaogrp01.getSaogrpCgst())
				.infrCgstperc(infrsaogrp01.getSaogrpCgstperc())
				.infrChargecode(chargeCode)
				.infrFlatnum(infrsaogrp01.getSaogrpFlatnum())
				.infrFromdate(infrsaogrp01.getSaogrpBillfrom())
				.infrIgst(infrsaogrp01.getSaogrpIgst())
				.infrIgstperc(infrsaogrp01.getSaogrpIgstperc())
				.infrIntarrears(infrsaogrp01.getSaogrpIntarrears())
				.infrInterest(infrsaogrp01.getSaogrpInterest())
				.infrInvoiceno(invoiceNum)
				.infrIrnno(infrsaogrp01.getSaogrpIrnno())
//				.infrRate(infrsaogrp01.getI)
				.infrSgst(infrsaogrp01.getSaogrpSgst())
				.infrSgstperc(infrsaogrp01.getSaogrpSgstperc())
				.infrSite(siteName)
				.infrUserid(userId)
				.infrToday(LocalDateTime.now())
				.infrWing(infrsaogrp01.getSaogrpWing())
				.infrTodate(infrsaogrp01.getSaogrpBillto())
				.infrGstyn("Y")
				.build();
				
				log.debug("infrbill before persist into database: {}", infrbill1);
				infrBillRepository.save(infrbill1);
//				BillResponse billResponse = infrbillMapper.mapInfraBillBillResponse(saveInfrbill);
//				return billResponse;
			}
		}
		
//		return null;
	}

	@Override
	public void deleteBill(double sessionId, String OwnerId) {
		printRepository.deleteInfrsaogrp01_printBySessionIdAndOwnerId(sessionId, OwnerId);
	}

}
