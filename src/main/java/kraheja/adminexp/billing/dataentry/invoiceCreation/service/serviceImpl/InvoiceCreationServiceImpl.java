package kraheja.adminexp.billing.dataentry.invoiceCreation.service.serviceImpl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.bean.response.GenericResponse;
import kraheja.adminexp.billing.dataentry.invoiceCreation.bean.request.CombinedEntity;
import kraheja.adminexp.billing.dataentry.invoiceCreation.bean.request.InvoicedetailRequestBean;
import kraheja.adminexp.billing.dataentry.invoiceCreation.entity.Invoicedetail;
import kraheja.adminexp.billing.dataentry.invoiceCreation.entity.Invoiceheader;
import kraheja.adminexp.billing.dataentry.invoiceCreation.mappers.InvoicedetailEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.invoiceCreation.mappers.InvoiceheaderEntityPojoMapper;
import kraheja.adminexp.billing.dataentry.invoiceCreation.repository.InvPartyMasterRepository;
import kraheja.adminexp.billing.dataentry.invoiceCreation.repository.InvoicedetailRepository;
import kraheja.adminexp.billing.dataentry.invoiceCreation.repository.InvoiceheaderRepository;
import kraheja.adminexp.billing.dataentry.invoiceCreation.service.InvoiceCreationService;
import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
@Transactional
public class InvoiceCreationServiceImpl implements InvoiceCreationService {

	@Autowired
	InvPartyMasterRepository invPartyMasterRepository;

	@Autowired
	InvoiceheaderRepository invoiceheaderRepository;

	@Autowired
	InvoicedetailRepository invoicedetailRepository;

	@Override
	public GenericResponse<List<Map<String, Object>>> fetchInvPartMasterDetails() {

		try {
			List<Object[]> invMasterList = this.invPartyMasterRepository.findInvpartyMasterData();

			if (Objects.isNull(invMasterList)) {
				return new GenericResponse<>(false, "No record found for your selections in Admadvance");
			}
			
			List<Map<String, Object>> keyValueList = new ArrayList<>();
			
			String[] columnNames = { "companyCode", "partyCode", "partyType", "partyName", "biilType", "billCode", "billDesc", "rate", "qty", "serviceNature", "hsnSac", "subject", "signAuth", "acMajor","billNote" };

			for (Object[] row : invMasterList) {
			    Map<String, Object> keyValueMap = new HashMap<>();
			    for (int i = 0; i < columnNames.length; i++) {
			        keyValueMap.put(columnNames[i], row[i]);
			    }
			    keyValueList.add(keyValueMap);
			}
			
			return new GenericResponse<>(true, "Data fetched successfully", keyValueList);

		} catch (Exception e) {

			return new GenericResponse<>(false, "Internal Server Error.");

		}

	}

	@Override
	public GenericResponse<Void> postInvoiceDetails(CombinedEntity combinedEntity) {
		try {

			InvoicedetailRequestBean firstBean = combinedEntity.getInvoicedetailRequestBean().get(0);

			String invoiceNum = firstBean.getInvoiceno();
			String tranType = firstBean.getTrtype();
			String code = firstBean.getCode();
			Double serialNo = firstBean.getSrno().doubleValue();

			Invoiceheader invoiceheader = invoiceheaderRepository.findByInvoiceheaderCK_InvhInvoiceno(invoiceNum);

			List<Invoicedetail> invoicedetail = invoicedetailRepository
					.findByInvoicedetailCK_InvdTrtypeAndInvoicedetailCK_InvdInvoicenoAndInvoicedetailCK_InvdCodeAndInvoicedetailCK_InvdSrno(
							tranType, invoiceNum, code, serialNo);
			List<InvoicedetailRequestBean> invoicedetailRequestBeans = combinedEntity.getInvoicedetailRequestBean();

			if (Objects.nonNull(invoiceheader) && Objects.nonNull(invoicedetail)) {

				invoiceheader = InvoiceheaderEntityPojoMapper.updateInvoiceheaderEntityPojoMapper.apply(invoiceheader,
						combinedEntity.getInvoiceheaderRequestBean());
				invoicedetail = InvoicedetailEntityPojoMapper.updateInvoicedetailEntitiesPojoMapper(invoicedetail,
						invoicedetailRequestBeans);

				log.info("Invoice Bill Modification.");

				invoiceheaderRepository.save(invoiceheader);
				invoicedetailRepository.saveAll(invoicedetail);

				return new GenericResponse<>(true, "Data updated successfully .");
			} else {

				invoiceheader = InvoiceheaderEntityPojoMapper.addInvoiceheaderPojoEntityMapper.apply(combinedEntity.getInvoiceheaderRequestBean());
				invoicedetail = InvoicedetailEntityPojoMapper.addInvoicedetailPojoEntityMapper.apply(invoicedetailRequestBeans);

				log.info("Invoice Bill Creation.");

				invoiceheaderRepository.save(invoiceheader);
				invoicedetailRepository.saveAll(invoicedetail);

				return new GenericResponse<>(true, "Data saved successfully .");
			}

		} catch (Exception e) {

			return new GenericResponse<>(false, "Internal Server Error.");

		}
	}

}