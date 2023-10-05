package kraheja.sales.bean.response;

import java.util.List;

import kraheja.payroll.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class InchequeResponse extends GenericResponse{
	private List<InchequeDetailResponse> chequeResponse;
}
