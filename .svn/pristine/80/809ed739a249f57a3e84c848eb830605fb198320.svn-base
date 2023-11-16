package kraheja.payroll.Reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Tuple;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.fd.deposit.bean.InterestChequePrintingBean;
import kraheja.payroll.Reports.service.ReportParametersService;
import kraheja.payroll.bean.response.ReportParametersRequestBean;
import kraheja.payroll.repository.ReportParametersRepository;

@Service
public class ReportParametersServiceImpl implements ReportParametersService {
	
	@Autowired
	private ReportParametersRepository reportParametersRepository;

	@Override
	public ResponseEntity<?> GetReportParameters() {
		Tuple t =  this.reportParametersRepository.GetReportParameters();
		
		ReportParametersRequestBean reportParametersRequestBean = new ReportParametersRequestBean(
                t.get(0, String.class),
                t.get(1, String.class),
                t.get(2, String.class),
                t.get(3, String.class),
                t.get(4, String.class),
                t.get(5, String.class),
                t.get(6, String.class),
                t.get(7, String.class),
                t.get(8, String.class),
                t.get(9, String.class),
                t.get(10, String.class),
                t.get(11, String.class),
                t.get(12, String.class),
                t.get(13, String.class),
                t.get(14, String.class),
                t.get(15, String.class));
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(reportParametersRequestBean).build());
	}

}
