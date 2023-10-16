package kraheja.sales.bean.request;

import java.time.LocalDateTime;
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
	private double cgstAmt;
	private double sgstAmt;
	private double igstAmt;
	private double tdsAmt;
	private double transactionAmt;
	private LocalDateTime receiptDate;
	private List<ChequeRequest> cheques;
	private List<GridResponse> gridRequest;
}
