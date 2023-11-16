package kraheja.adminexp.billing.dataentry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import kraheja.adminexp.billing.dataentry.entity.Admbillh ;
import kraheja.adminexp.billing.dataentry.entity.AdmbillhCK;
@Repository
public interface AdmbillhRepository extends JpaRepository<Admbillh, AdmbillhCK> {
	 
	Admbillh findByAdmbillhCK_AdblhSer(String ser) ;
	
	@Query("SELECT	NVL(SUM(e.adblhAdvnadjust), 0) FROM Admbillh e WHERE TRIM(e.adblhPartycode) <> :partyCode AND TRIM(e.adblhBldgcode)  = :bldgCode AND TRIM(e.adblhCoy) = :coy AND TRIM(e.adblhPartytype)  = :partyType")
	Double findAdblhAdjustSumByAdblhPartycodeAndAdblhBldgcodeAndAdblhCoyAndAdblhtypeAndadblhadvanceAdjust(String partyCode, String bldgCode, String coy, String partyType);

	
	
}