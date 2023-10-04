package kraheja.sales.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor

public class LoanhistoryCK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String lhistLoanco;

	@Column
	private String lhistLoannum;

	@Column
	private String lhistLoanclosedate;

	@Column
	private String lhistOwnerid;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
