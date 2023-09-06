package kraheja.purch.entity;
import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class MaterialCK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Column
	@Type(type = "kraheja.commons.utils.CharType") 
	private String matMatgroup ; 

	@Column
	@Type(type = "kraheja.commons.utils.CharType") 
	private String matMatcode ; 

	@Column
	@Type(type = "kraheja.commons.utils.CharType") 
	private String matItemcode ; 

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
}