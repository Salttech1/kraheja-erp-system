package kraheja.sales.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;

import org.hibernate.annotations.Type;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Infrsaogrp01_PrintCK implements Serializable{

	private static final long serialVersionUID = 1L;

	@Type(type = "kraheja.commons.utils.CharType") 
	private String saogrpBillnum ; 

	 
	private Double saogrpSessid ; 

	@Type(type = "kraheja.commons.utils.CharType") 
	private String saogrpInvoiceno ; 


}