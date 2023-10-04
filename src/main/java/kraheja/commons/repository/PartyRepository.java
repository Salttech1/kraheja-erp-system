package kraheja.commons.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import kraheja.commons.entity.Party;
import kraheja.commons.entity.PartyCK;

@Repository
public interface PartyRepository extends JpaRepository<Party, PartyCK>, CrudRepository<Party, PartyCK>{
	
//	@Query("select e from Party e WHERE trim(e.partyCk.parPartycode) = :partyCode AND e.partyCk.parClosedate = :closeDate AND  e.partyCk.parPartytype =:depositors")
//	Party findByPartyCodeAndParClosedateAndParPartytype(String partyCode, Date closeDate, String depositors);
	
	@Query("select e from Party e WHERE trim(e.partyCk.parPartycode) = :partyCode AND (e.partyCk.parClosedate = :closeDate or e.partyCk.parClosedate is null) AND  e.partyCk.parPartytype =:partyType")
	Party findByPartyCodeAndParClosedateAndParPartytype(String partyCode, LocalDateTime closeDate, String partyType);
	
	Party findByPartyCk_ParPartycodeAndPartyCk_ParClosedateAndPartyCk_ParPartytype(String partyCode, LocalDateTime closeDate, String partyType);
	
	@Query(value ="SELECT par_partycode , par_partyname , par_partytype , par_pmtacnum , par_gstno , (SELECT adr2_adrline1 FROM v_address2 WHERE adr2_adowner = par_partycode AND adr2_adsegment = 'PARTY' AND rownum = 1) || (SELECT adr2_adrline2 FROM v_address2 WHERE adr2_adowner = par_partycode AND adr2_adsegment = 'PARTY' AND rownum = 1) AS Address FROM party WHERE PAR_CLOSEDATE = :closeDate AND PAR_PARTYTYPE not in ('D','F','O')", nativeQuery = true)
	List<Tuple> fetchPartyWithAddress(LocalDateTime closeDate);
	
	@Query("SELECT e FROM Party e WHERE trim(e.partyCk.parPartycode) = :partycode AND trim(e.partyCk.parPartytype) = :partytype " +
            "AND (Upper(e.parPartyname) like '%LTD%' or upper(e.parPartyname) like '%LIMITED%' )")
	Party findLimitedPartyByCode(String partycode, String partytype);
	
	@Query("select max(p.partyCk.parPartycode) from Party p where trim(p.partyCk.parPartycode) LIKE :partyCode and p.partyCk.parPartytype =:partyType")
	String getPartyCode(@Param("partyCode") String partyCode, @Param("partyType") String partyType);
}
