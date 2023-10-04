package kraheja.sales.bean.request;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import kraheja.sales.bean.response.GridResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@Builder
@NotEmpty
@AllArgsConstructor
@ToString
public class InchequeRequest {
	private String siteName;
	private String userId;
	private String bank;
	private String outstat;
	private String fundSource;
	private String acType;
	private String chequeDate;
	private String chequeNumber;
	private String chequeAmount;
	
	private List<GridResponse> gridRequest;
}
