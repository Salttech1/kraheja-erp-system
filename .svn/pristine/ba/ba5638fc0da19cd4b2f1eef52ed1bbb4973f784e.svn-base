package kraheja.adminexp.billing.dataentry.service;

import java.text.ParseException;
import java.util.List;
import org.springframework.http.ResponseEntity;
import kraheja.adminexp.billing.dataentry.bean.request.AdmbillhRequestBean;

public interface AdmbillhService {

	ResponseEntity<?> fetchAdmbillhBySer(String  ser) ;

	ResponseEntity<?> addAdmbillh(AdmbillhRequestBean admbillhRequestBean) throws ParseException;
	
	ResponseEntity<?> calculateTds(String suppbilldt, Double amount, String billtype, String partycode, String coy);

	ResponseEntity<?> updateAdmbillh(AdmbillhRequestBean admbillhRequestBean) throws ParseException;

	ResponseEntity<?> checkserExists(String ser);

}