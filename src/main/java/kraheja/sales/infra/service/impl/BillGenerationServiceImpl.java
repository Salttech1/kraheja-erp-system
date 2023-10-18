package kraheja.sales.infra.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.entity.DbEntity;
import kraheja.commons.entity.Hsnsacmaster;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.HsnsacmasterRepository;
import kraheja.commons.utils.CommonUtils;
import kraheja.sales.infra.bean.request.InfraAuxiBillRequest;
import kraheja.sales.infra.bean.response.BillResponse;
import kraheja.sales.infra.service.BillGenerationService;
import kraheja.sales.infra.utilities.DateUtill;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class BillGenerationServiceImpl implements BillGenerationService {

	@Autowired private BuildingRepository buildingRepository;
	@Autowired HsnsacmasterRepository hsnsacmasterRepository;
	@Autowired EntityRepository entityRepository;
	
	@Override
	public BillResponse getBillDetail(InfraAuxiBillRequest billRequest) {
		Map<String, Double> gstRate = this.getGstRate(billRequest);
		log.debug("gstRate obtaint: {}", gstRate);

		
		return BillResponse.builder().billNumber("IN045712").ownerId(billRequest.getOwnerIdFrom()).month("202310")
				.billDate(billRequest.getBillDate())
				.billFromDate(billRequest.getBillDate())
				.billToDate(billRequest.getBillDate())
				.billAmount("93300")
				.billArrears("126609")
				.interest("12730")
				.interestArrears("42323")
				.admin("13995")
				.cgst("9657")
				.sgst("9657")
				.igst("0")
				.cgstPerc("9")
				.sgstPerc("9")
				.igstPerc("0")
				.invoiceNumber("KRHOTXM23000942")
				.build();
	}

	/**
	 * <p>this method use for get GST Percent state wise.</p>
	 * */
	private Map<String, Double> getGstRate(InfraAuxiBillRequest request) {
		Map<String, Double> gstMap = new HashMap<>();
		
		String buildingState = buildingRepository.findBldgSalesstateByBuildingCK_BldgCode(request.getOwnerIdFrom().substring(0,4));
		log.debug("building state obtaint: {}", buildingState);
		
		Integer entityCount = entityRepository.getEntityCount(buildingState);
		log.debug("entityCount obtaint: {}", entityCount);

		Hsnsacmaster gstRate = hsnsacmasterRepository.gstRate("995419", DateUtill.convertStringToDateFormat(request.getBillDate()));
		log.debug("gstRate : {}", gstRate);

		if (entityCount > 0) {
			gstMap.put("ugstPer", gstRate.getHsmsUgstperc());
		}
		if (buildingState.equals("MAH")) {
			gstMap.put("cgstPer", gstRate.getHsmsCgstperc());
			gstMap.put("sgstPer", gstRate.getHsmsSgstperc());
		}else {
			gstMap.put("igstPer", gstRate.getHsmsIgstperc());
		}
		return gstMap;
	}

}
