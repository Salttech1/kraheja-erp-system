package kraheja.adminexp.billing.dataentry.invoiceCreation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kraheja.adminexp.billing.dataentry.invoiceCreation.entity.Invoicedetail;
import kraheja.adminexp.billing.dataentry.invoiceCreation.entity.InvoicedetailCK;

@Repository
public interface InvoicedetailRepository extends JpaRepository<Invoicedetail, InvoicedetailCK> {

	List<Invoicedetail> findByInvoicedetailCK_InvdTrtypeAndInvoicedetailCK_InvdInvoicenoAndInvoicedetailCK_InvdCodeAndInvoicedetailCK_InvdSrno(
			String trtype, String invoiceno, String code, Double srno);

}