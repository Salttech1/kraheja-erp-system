package kraheja.adminexp.billing.dataentry.intercompany.bean.response;

import java.util.List;

import kraheja.payload.GenericResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@ToString
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class AddInterCompanyResponse extends GenericResponse{
	private List<AddInterCompanyData> interCompanyData;
	
	
}
