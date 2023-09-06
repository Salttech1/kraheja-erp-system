package kraheja.adminexp.vehicleexp.dataentry.bean.response;

import java.util.List;

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

public class VehCertCancellationBean {

	private List<UnPostedCertBean> unPostedCertBean;
	private List<UnPosterCertDetailBean> unPosterCertDetailBean;
}
