package kraheja.adminexp.billing.dataentry.intercompany.bean.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class InterCompanyRequest {
	private String companyCode;
	private String projectCode;
	private LocalDateTime billDate;
	private LocalDateTime billToDate;
	private LocalDateTime billFromDate;
}
