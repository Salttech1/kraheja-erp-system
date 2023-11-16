package kraheja.adminexp.billing.dataentry.adminAdvancePayment.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.adminexp.billing.dataentry.adminAdvancePayment.entity.Admadvance1;
import kraheja.adminexp.billing.dataentry.adminAdvancePayment.entity.AdmadvanceCK1;


@Repository
public interface AdmadvanceRepository1 extends JpaRepository<Admadvance1, AdmadvanceCK1> {
	Admadvance1 findByAdmadvanceCK_AdvnSer(String ser);

	@Query("select a from Admadvance1 a where trim(a.admadvanceCK.advnSer) = :ser or trim(a.advnPinvno) = :pinvno")
	List<Admadvance1> findByAdmadvanceCK_AdvnSerOrAdvnPinvno(String ser, String pinvno);

	@Modifying
	@Query("Update Admadvance a SET a.advnActranser = :advntranser, a.advnStatus = :advnStatus, a.advnPassedon = :advnPassedon, a.advnSite = :advnSite, a.advnUserid = :advnUserid, a.advnToday = :advnToday where trim(a.admadvanceCK.advnSer) = :ser and trim(a.advnPinvno) = :pinvno")
	void updatePbilldQtyByPartyAndSuppBillAndMatGroup(String advntranser, String advnStatus,  LocalDate advnPassedon,String advnSite,String advnUserid,  LocalDateTime advnToday,String pinvno,String ser );

}
