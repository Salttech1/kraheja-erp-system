package kraheja.adminexp.billing.dataentry.invoiceCreation.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The persistent class for the INVOICEDETAIL database table.
**/
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(indexes = {
		@Index(name = "INVOICEDETAIL1", columnList = "invdTrtype Asc, invdInvoiceno Asc, invdCode Asc, invdSrno Asc", unique = true)
})

public class Invoicedetail implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private InvoicedetailCK invoicedetailCK;

	@Column(name="INVD_ACMAJOR")
	private String invdAcmajor ;

	@Column(name="INVD_ACMINOR")
	private String invdAcminor ;

	@Column(name="INVD_CGSTPAYABLE")
	private Integer invdCgstpayable ;

	@Column(name="INVD_CGSTPER")
	private Integer invdCgstper ;

	@Column(name="INVD_GSTYN")
	private String invdGstyn ;

	@Column(name="INVD_HSNCODE")
	private String invdHsncode ;

	@Column(name="INVD_IGSTPAYABLE")
	private Integer invdIgstpayable ;

	@Column(name="INVD_IGSTPER")
	private Integer invdIgstper ;

	@Column(name="INVD_MINORTYPE")
	private String invdMinortype ;

	@Column(name="INVD_NARRATION")
	private String invdNarration ;

	@Column(name="INVD_ORIGSITE")
	private String invdOrigsite ;

	@Column(name="INVD_QUANTITY")
	private Integer invdQuantity ;

	@Column(name="INVD_RATE")
	private Integer invdRate ;

	@Column(name="INVD_SGSTPAYABLE")
	private Integer invdSgstpayable ;

	@Column(name="INVD_SGSTPER")
	private Integer invdSgstper ;

	@Column(name="INVD_SITE")
	private String invdSite ;

	@Column(name="INVD_TODAY")
	private LocalDateTime invdToday ;

	@Column(name="INVD_TRANAMTGSTPAYABLE")
	private Integer invdTranamtgstpayable ;

	@Column(name="INVD_UGSTPAYABLE")
	private Integer invdUgstpayable ;

	@Column(name="INVD_UGSTPER")
	private Integer invdUgstper ;

	@Column(name="INVD_USERID")
	private String invdUserid ;

}
