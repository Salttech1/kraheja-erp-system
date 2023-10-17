package kraheja.adminexp.billing.dataentry.adminAdvancePayment.service.serviceImpl;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.request.AdmadvanceRequestBean;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.AdmAdvancePaymentFetchResponse;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.GenericResponse;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.entity.Admadvance1;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.mappers.AdmadvanceEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.repository.AdmadvanceRepository1;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.service.AdminAdvancePaymentPassingService;
import kraheja.arch.projbldg.dataentry.entity.Building;
import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.Actrand;
import kraheja.commons.entity.Company;
import kraheja.commons.entity.Party;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.utils.GenericAccountingLogic;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class AdminAdvancePaymentPassingServiceImpl implements AdminAdvancePaymentPassingService {

	@Autowired
	AdmadvanceRepository1 admadvanceRepository;
	
	@Autowired
    ActranhRepository actranhRepository;
	
	@Autowired
	ActrandRepository actrandRepository;

	@Autowired
	PartyRepository partyRepository;

	@Autowired
	CompanyRepository companyRepository;

	@Autowired
	BuildingRepository buildingRepository;

	@Autowired
	private GenericAccountingLogic genericAccountingLogic;
	
	@Override
	public GenericResponse<AdmAdvancePaymentFetchResponse> fetchAdmadvanceByPinvAndSer(String pinv, String ser) {
		log.info("Inside Fetch Admin Advance Payment Passing Service Implementation .");

		Admadvance1 admadvanceEntity = this.admadvanceRepository.findByAdmadvanceCK_AdvnSerAndAdvnPinvno(ser, pinv);

		if (admadvanceEntity != null) {

			Party party = this.partyRepository.findByPartyCodeAndParPartytypeAndBillDate(
					admadvanceEntity.getAdvnPartycode().trim(), admadvanceEntity.getAdvnPartytype(),
					admadvanceEntity.getAdvnDate());

			LocalDate localDate = admadvanceEntity.getAdvnDate();

			Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
			Company company = this.companyRepository
					.findByCompanyCK_CoyCodeAndBillDate(admadvanceEntity.getAdvnCoy().trim(), date);

			Building building = this.buildingRepository.findByBuildingCK_BldgCode(admadvanceEntity.getAdvnBldgcode());

			AdmAdvancePaymentFetchResponse admAdvancePaymentFetchResponse = AdmAdvancePaymentFetchResponse.builder()
					.admAdvance(admadvanceEntity).partyName(party.getParPartyname()).companyName(company.getCoyName())
					.buildingName(building.getBldgName())
					.payAmount(admadvanceEntity.getAdvnAdvanceamt() - admadvanceEntity.getAdvnTdsamount()).build();
			log.info(admAdvancePaymentFetchResponse);

			return new GenericResponse<>(true, "Data fetched successfully", admAdvancePaymentFetchResponse);

		}
		return new GenericResponse<>(false, "No record found for your selections in Admadvance");
	}

	@Override
	public GenericResponse<Void> updateAdmadvance(AdmadvanceRequestBean admadvanceRequestBean) {
		log.info("Inside Update Admin Advance Payment Passing Service Implementation .");
		Admadvance1 admadvanceEntity = this.admadvanceRepository
				.findByAdmadvanceCK_AdvnSer(admadvanceRequestBean.getSer());
		
		if (admadvanceEntity != null) {

			Admadvance1 admadvance = AdmadvanceEntityPojoMapper.updateAdmadvanceEntityPojoMapper.apply(admadvanceEntity,
					admadvanceRequestBean);
			List <Actrand> actrandEntityList = new ArrayList<Actrand>() ;
			String tranSer = GenericCounterIncrementLogicUtil.generateTranNoWithSite("PMSER", "#P",GenericAuditContextHolder.getContext().getSite());
			String tranType = "PF" ;
			String tranDate = new SimpleDateFormat("dd/MM/yyyy").format(new Date());
			String ledgCode = "GL" ;
			String partyType = admadvanceEntity.getAdvnPartytype();
			String partyCode = admadvanceEntity.getAdvnPartycode() ;
			Double tranAmount= Double.valueOf(admadvanceEntity.getAdvnAdvanceamt()-admadvanceEntity.getAdvnTdsamount());
			String vouDate=admadvanceEntity.getAdvnPinvdate().toString();
			String vouNum=admadvanceEntity.getAdvnActranser();
			String postedYn="N";
			String balancedYn="N";
			String closingjvYn="N";
			String bbookYn="N";
			String cbookYn="N";
            String narrationMsg=admadvanceEntity.getAdvnNarration();
            String coy =admadvanceEntity.getAdvnCoy();
            String clearacYn = "Y"; 
			String reverseYn = "N"; 
			Boolean isUpdate=false;
			
			String tranAcMajor="11401026";
			String mintype=" ";
			String mincode=" ";
			String project=admadvanceEntity.getAdvnProject();
/*Pending*/ String acMinor="";
/*Pending*/ String xAcMajor="";
            String xmintype=" ";
			String domain="";
			String matGroup="";
			String matCode="";
			int itemQty=0;
			String docNum=admadvanceEntity.getAdvnActranser();
			String docDate = admadvanceEntity.getAdvnDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
			String docType="A";
			
//			this.genericAccountingLogic.updateActranh(tranSer, tranDate, ledgCode, partyType, partyCode,tranAmount , 
//					vouNum, vouDate, postedYn, balancedYn, closingjvYn, bbookYn, cbookYn, narrationMsg, coy, clearacYn, reverseYn,tranType, isUpdate) ;
//			List<ActrandBean> actrandList = new ArrayList<>();
//			List<ActrandBean> initialiseActrandBreakups  = new ArrayList<>();
//
//								 Integer bunumCounter = bunumCounter.equals(BigInteger.ZERO.intValue()) ? bunumCounter + 1 : bunumCounter + 2;
//
//								initialiseActrandBreakups = GenericAccountingLogic.InitialiseActrandBreakups(tranType, tranAcMajor, mintype, mincode, partyType, partyCode,project, acMinor,xAcMajor, 
//										xmintype, xPartyType, xPartyCode,xProject, xAcMinor, tranAmt,bldgCode, property,uom,gstTranDate,bunumCounter,narrationMsg,
//										transer,ledgCode,prop, coy,vouNum,vouDate,period,domain,matGroup,matCode,itemQty,docNum,docDate,docType,docParType,docParCode) ;
//								
//								actrandEntityList.addAll(AddPojoEntityMapper.addActrandPojoEntityMapping.apply(initialiseActrandBreakups)) ;
//			this.admadvanceRepository.save(admadvance);

			return new GenericResponse<>(true, "Admin advance Bill Passed Successfully.");

		}
		return new GenericResponse<>(false, "Admin advance Payment could not be passed.");

	}

}
