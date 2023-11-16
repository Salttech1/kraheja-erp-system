
package kraheja.adminexp.billing.dataentry.service;

import java.text.ParseException;

import org.springframework.http.ResponseEntity;
import kraheja.adminexp.billing.dataentry.bean.request.AdmadvanceRequestBean;

public interface AdmadvanceService {

	ResponseEntity<?> fetchAdmadvanceBySer(String  ser) throws ParseException;

	ResponseEntity<?> updateAdmadvance(AdmadvanceRequestBean admadvanceRequestBean) throws ParseException;

	ResponseEntity<?> addAdmadvance(AdmadvanceRequestBean admadvanceRequestBean) throws ParseException;

	ResponseEntity<?> checkserExists(String ser);
	
	
}    
