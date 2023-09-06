package kraheja.sales.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class BookingCK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String bookBldgcode;

	@Column
	private String bookWing;

	@Column
	private String bookFlatnum;

	@Column
	private String bookOwnerid;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
