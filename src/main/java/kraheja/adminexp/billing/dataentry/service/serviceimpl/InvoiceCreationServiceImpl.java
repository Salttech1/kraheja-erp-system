package kraheja.adminexp.billing.dataentry.service.serviceimpl;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kraheja.adminexp.billing.dataentry.repository.InvPartyMasterRepository;
import kraheja.adminexp.billing.dataentry.service.InvoiceCreationService;
import kraheja.commons.bean.response.ServiceResponseBean;

@Service
public class InvoiceCreationServiceImpl implements InvoiceCreationService {
	
	@Autowired
	InvPartyMasterRepository invPartyMasterRepository;
	
	
	@Override
	public ResponseEntity<?> fetchInvPartMasterDetails() {
		
		try{
			 List<Object[]>invMasterList= this.invPartyMasterRepository.findInvpartyMasterData();
			
			if(Objects.isNull(invMasterList)) {
				return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No record found for your selections in Invpartymaster").build());
			}
			
			return ResponseEntity.ok(ServiceResponseBean.builder()
					.data(invMasterList)
					.status(Boolean.TRUE)
					.build());
			
		}catch(Exception e){
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Internal Server Error.").build());

		}
		
	}

}