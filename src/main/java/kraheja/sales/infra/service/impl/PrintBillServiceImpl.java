package kraheja.sales.infra.service.impl;

import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.filter.GenericAuditContextHolder;
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
		
		if (!Objects.nonNull(printDbResponse)) {
			// INSERT/UPDATE VALUES IN INFRBILL TABLE
			Infrbill infrbill = infrBillRepository.findBillNumberByOwnerIdAndMonth(printDbResponse.getInfrOwnerId(), printDbResponse.getInfrMonth(), chargeCode, billType);
			log.debug("infrbill obtaint : {}", infrbill);
			infrbill.setInfrInterest(12733.00);
			infrbill.setInfrIntarrears(42322.00);
			infrbill.setInfrUserid(userId);
			infrbill.setInfrToday(LocalDateTime.now());
			
			Infrbill updateInfrBill = infrBillRepository.save(infrbill);
			log.debug("updateInfrBill obtaint: {}", updateInfrBill);
		}else {
			String bldgCity = buildingRepository.findBldgCityByBuildingCK_BldgCode("ORHH");
			log.debug("bldgCity obtaint: {}", bldgCity);
			if (bldgCity.equals("") || bldgCity.isBlank()) {
				bldgCity = "MUM";
			}
			String bldgCompany = buildingRepository.findBldgCompanyByBldgCode("ORHH");
			log.debug("bldgCompany obtaint: {}", bldgCompany);
			
			//TODO INVOICE NUMBER CREATOR ENTITY AND CLASSID IS NOT CREATED
			if (bldgCity.equalsIgnoreCase("MUM")) {
//				invoiceNum = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "INBIL", GenericAuditContextHolder.getContext().getSite());
				invoiceNum = "KRHOTXM23000983";
			}else {
//				invoiceNum = GenericCounterIncrementLogicUtil.generateTranNoWithSite("#NSER", "INBIL", GenericAuditContextHolder.getContext().getSite());
				invoiceNum = "KRHOTXP23000983";
			}
			Infrsaogrp01_Print infrsaogrp01 = printRepository.findByInfrsaogrp01_printCKSaogrpSessidAndSaogrpOwnerid(542557.00, "ORHHHF0000H");
			log.debug("findByInfrsaogrp01 : {}", infrsaogrp01);
//			infrsaogrp01.getInfrsaogrp01_printCK().setSaogrpBillnum(bldgCompany);
			
			if (Objects.nonNull(infrsaogrp01)) {
				infrsaogrp01.getInfrsaogrp01_printCK().setSaogrpInvoiceno(invoiceNum);
				printRepository.save(infrsaogrp01);
			}
			
			InfrbillCK infrbillCK = InfrbillCK.builder()
					.infrBillnum(infrsaogrp01.getInfrsaogrp01_printCK().getSaogrpBillnum())
					.infrOwnerId(infrsaogrp01.getSaogrpOwnerid())
					.infrMonth(infrsaogrp01.getSaogrpBillmonth())
					.build();
			
			Infrbill infrbill = Infrbill.builder()
			.infrbillCK(infrbillCK)
			.infrAdmincharges(infrsaogrp01.getSaogrpAdmincharges())
			.infrAmtos(infrsaogrp01.getSaogrpOutrate())
			.infrAmtpaid(infrsaogrp01.getSaogrpBillamt())
			.infrArrears(infrsaogrp01.getSaogrpBillarrears())
			.infrBillamt(infrsaogrp01.getSaogrpBillamt())
			.infrBilldate(infrsaogrp01.getSaogrpBilldate())
			.infrBillprinton(infrsaogrp01.getSaogrpBillfrom())
			.infrBillprintyn(isPrint)
//			.infrBillrevnum(null)
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
//			.infrRate(infrsaogrp01.getI)
			.infrSgst(infrsaogrp01.getSaogrpSgst())
			.infrSgstperc(infrsaogrp01.getSaogrpSgstperc())
			.infrSite(siteName)
			.infrUserid(userId)
			.infrToday(LocalDateTime.now())
			.infrWing(infrsaogrp01.getSaogrpWing())
			.infrTodate(infrsaogrp01.getSaogrpBillto())
			.build();
			
			log.debug("infrbill before persist into database: {}", infrbill);
			infrBillRepository.save(infrbill);
//			BillResponse billResponse = infrbillMapper.mapInfraBillBillResponse(saveInfrbill);
//			return billResponse;
		}
		
//		return null;
	}

}
