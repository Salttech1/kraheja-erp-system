package kraheja.payroll.bean.response;
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
public class GetPackageforEmployeeBean {
	private String  Empcode;
	private String  Earndedcode;
	private String  Earndedtype;
	private Integer  Computeno;
	private String  Computetype;
	private String  Schemeyn;
	private String  Loanyn;
	private String  Defaultsaltype;
	private Double  Earndedrate;
	private String  Paycycle;
	private String  Ratecycle;
	private String  Schemeapplyn;
}