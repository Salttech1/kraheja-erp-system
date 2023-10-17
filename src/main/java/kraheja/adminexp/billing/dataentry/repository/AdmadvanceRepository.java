package kraheja.adminexp.billing.dataentry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.adminexp.billing.dataentry.entity.Admadvance ;
import kraheja.adminexp.billing.dataentry.entity.AdmadvanceCK;
@Repository
public interface AdmadvanceRepository extends JpaRepository<Admadvance, AdmadvanceCK> {

	 
	Admadvance findByAdmadvanceCK_AdvnSer(String ser) ; 
	
	@Query("SELECT	NVL(SUM(e.advnAdvanceamt), 0) FROM Admadvance e WHERE (e.advnStatus = 5 OR e.advnStatus = 7) AND TRIM(e.advnPartycode) = :partyCode AND TRIM(e.advnBldgcode)  = :bldgCode AND TRIM(e.advnCoy) = :coy AND TRIM(e.advnPartytype)  = :partyType")
	Double findAdvnAmountSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhstatusAndAdblhtype(String partyCode, String bldgCode, String coy, String partyType);
}