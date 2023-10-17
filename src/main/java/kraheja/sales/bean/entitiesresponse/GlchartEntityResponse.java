package kraheja.sales.bean.entitiesresponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GlchartEntityResponse {
	private String chartMinoryn;
	private String chartValidminors;
	private String chartPostprojonly;
	private String chartPostglonly;
}