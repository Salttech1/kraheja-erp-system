package kraheja.payroll.bean;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@JsonInclude(Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor

public class TotalComputationDetailRequestBean {
	private String coy ;
	private String paymonth;
	private String saltype ;	
	private String empcode ;
	private String emptype ;
}