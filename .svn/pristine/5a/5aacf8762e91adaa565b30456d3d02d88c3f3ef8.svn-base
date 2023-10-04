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

public class FlatownerCK implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Column
	private String fownOwnerid;

	@Column
	private String fownBldgcode;

	@Column
	private String fownWing;

	@Column
	private String fownFlatnum;

	@Column
	private String fownOwnertype;

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}