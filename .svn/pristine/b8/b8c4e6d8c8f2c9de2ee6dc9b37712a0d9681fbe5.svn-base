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

public class FlatpayCK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String fpayBldgcode;

	@Column
	private String fpayFlatnum;

	@Column
	private String fpayOwnerid;

	@Column
	private String fpayDuedate;

	@Column
	private String fpayPaiddate;

	@Column
	private String fpayNarrative;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}