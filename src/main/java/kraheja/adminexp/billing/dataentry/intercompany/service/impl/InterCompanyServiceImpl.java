package kraheja.adminexp.billing.dataentry.intercompany.service.impl;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.Tuple;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.adminexp.billing.dataentry.intercompany.bean.request.InterCompanyRequest;
import kraheja.adminexp.billing.dataentry.intercompany.bean.response.AddInterCompanyData;
import kraheja.adminexp.billing.dataentry.intercompany.bean.response.AddInterCompanyResponse;
import kraheja.adminexp.billing.dataentry.intercompany.bean.response.TraiBalance;
import kraheja.adminexp.billing.dataentry.intercompany.service.InterCompanyService;
import kraheja.adminexp.billing.dataentry.repository.IntercoybillheaderRepository;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.constant.ApiResponseCode;
import kraheja.constant.ApiResponseMessage;
import kraheja.constant.Result;
import kraheja.utility.DateUtill;
import lombok.extern.log4j.Log4j2;
/**
 * <p>
 * this @service created for fetch inter company data calculate it condition wise.
 * </p>
 * 
 * @author sazzad.alom
 * @version 1.0.0
 * @since 21-NOV-2023
 */

@Log4j2
@Service
public class InterCompanyServiceImpl implements InterCompanyService {

	@Autowired
	private ActrandRepository actrandRepository;
	
	@Autowired
	private IntercoybillheaderRepository billheaderRepository;
	
	@Autowired private EntityRepository entityRepository;
	
	@Override
	public AddInterCompanyResponse addInterCompany(InterCompanyRequest request) {
		List<AddInterCompanyData> interCompanyDataList = new ArrayList<>();
		String companyCode = request.getCompanyCode();
		String projectCode = request.getProjectCode();
		LocalDate billDate = null , billFrom = null , billTo = null ;
		
		List<TraiBalance> traiBalanceList = new ArrayList<>();
		try {
			LocalDateTime maxPeriod = billheaderRepository.fetchMaxPeriod(companyCode, projectCode);
			log.debug("maxPeriod obtaint: {}", maxPeriod);
			
			if (maxPeriod == null) {
				LocalDate today = LocalDate.now();
				int year = today.getYear();
				String startApr = "01/04/" + year;
				String endJun = "30/06/" + year;
			    billDate = DateUtill.convertStringToDateFormat(startApr);
			    billFrom = billDate;
			    billTo = DateUtill.convertStringToDateFormat(endJun);
			 }
			else {
				int year = maxPeriod.getYear();
				if (maxPeriod.getMonth() == Month.DECEMBER) {
					year++;
		            billDate = DateUtill.convertStringToDateFormat("31/03/" + year);
		            billFrom = DateUtill.convertStringToDateFormat("01/01/" + year);
		            billTo = DateUtill.convertStringToDateFormat("31/03/" + year);
		        } else {
		        	int month = maxPeriod.getMonthValue();
		        	String month1 = String.format("%02d", month+1);
		        	String month3 = String.format("%02d", month+3);
		        	billDate = DateUtill.convertStringToDateFormat("01/" + month1 + "/" + year);
		        	billFrom = DateUtill.convertStringToDateFormat("01/" + month1 + "/" + year);
		        	LocalDateTime threeMonthsLater = maxPeriod.plusMonths(3);
		            int lastDayAfter3Month = threeMonthsLater.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth();
		            String lastDayAfterFormatt  = String.format("%02d", lastDayAfter3Month);
		            billTo = DateUtill.convertStringToDateFormat(lastDayAfterFormatt + "/" + month3 + "/" + year);
		        }
			}
			log.debug("billDate : {} billFrom: {} billTo: {}", billDate, billFrom, billTo);

			List<Tuple> entityAcMejor = entityRepository.fetchEntityAcMejor();
			log.debug("entityAcMejor obtaint: {}", entityAcMejor.size());

			
			List<Tuple> fetchTraiBalance = actrandRepository.fetchTraiBalance();
			log.debug("fetchTraiBalance : {}", fetchTraiBalance);
			
			
			log.debug("traiBalanceList : {} totalRecord: {}", traiBalanceList, traiBalanceList.size());
			List<Tuple> companyEntityList = entityRepository.fetchCompanyEntity(companyCode);
			log.debug("companyEntityList obtaint: {} object count: {}", companyEntityList, companyEntityList.size());
			
				
				for (Tuple traiBalanceTuple : fetchTraiBalance) {
					AddInterCompanyData addInterCompanyData = AddInterCompanyData.builder().build();
					addInterCompanyData.setAcmajor(traiBalanceTuple.get("ac_acmajor",String.class));
					addInterCompanyData.setMajorName(traiBalanceTuple.get("acname",String.class).trim());
					addInterCompanyData.setAcminor(traiBalanceTuple.get("ac_acminor",String.class) == null ? "" : traiBalanceTuple.get("ac_acminor",String.class));
					addInterCompanyData.setMinorName(traiBalanceTuple.get("acminorname",String.class) == null ? "" : traiBalanceTuple.get("acminorname",String.class));
					Character character = traiBalanceTuple.get("ac_mintype",Character.class) == null ? ' ' : traiBalanceTuple.get("ac_mintype",Character.class);
					addInterCompanyData.setAcMinType(character.toString());

					BigDecimal bigDecimal = traiBalanceTuple.get("ac_amount",BigDecimal.class);
					double traiBalanceAmount = bigDecimal.doubleValue();
					addInterCompanyData.setAcAmount(traiBalanceAmount);
					
					Map<String, Double> flexibleMap = new HashMap<>();
					for (Tuple entAcMejor : entityAcMejor) {
						double amount = 0.00;
						
						
						for (Tuple companyEntityTuple : companyEntityList) {
							
							String locCompanyAmtName =  companyEntityTuple.get("ent_char3", String.class).trim() + "-" + companyEntityTuple.get("ent_char4", String.class).trim();
							log.debug("locCompanyAmtName : {}", locCompanyAmtName);
							
							companyEntityTuple.get("ent_char1", String.class);
							companyEntityTuple.get("ent_char2", String.class);
							double localPercentage1 = companyEntityTuple.get("ent_num1", BigDecimal.class).doubleValue();
							double localPercentage2 = companyEntityTuple.get("ent_num2", BigDecimal.class).doubleValue();
						
							String acMejor = entAcMejor.get("ent_char1", String.class);
							String traiBalanceMejor = traiBalanceTuple.get("ac_acmajor",String.class);
							log.debug("acMejor: {} traiBalanceAcmajor: {}", acMejor, traiBalanceMejor);

							if (acMejor.equals(traiBalanceMejor)) {
								amount = Math.round(traiBalanceAmount / 100 * localPercentage2 );
							}else {
								amount = Math.round(traiBalanceAmount / 100 * localPercentage1 );
							}
							log.debug("locCompanyAmtName : {} amount: {}", locCompanyAmtName, amount);
							flexibleMap.put(locCompanyAmtName, amount);
						}
					}
					addInterCompanyData.setLocalCompanyData(flexibleMap);
					interCompanyDataList.add(addInterCompanyData);
				}

			
		} catch (Exception e) {
			throw new InternalError("error occured due to internal issue");
		}
		
		return AddInterCompanyResponse.builder()
				.result(Result.SUCCESS)
				.responseCode(ApiResponseCode.SUCCESS)
				.message(ApiResponseMessage.DATA_FETCH_SUCCESSFULLY)
				.interCompanyData(interCompanyDataList)
				.build();
	}

}
