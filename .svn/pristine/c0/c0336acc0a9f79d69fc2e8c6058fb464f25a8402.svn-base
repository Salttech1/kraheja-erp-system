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

public class FlatcharCK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String fchBldgcode;

	@Column
	private String fchFlatnum;

	@Column
	private String fchAccomtype;

	@Column
	private String fchChargecode;

	@Column
	private String fchWing;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}