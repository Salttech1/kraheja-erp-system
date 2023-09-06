package kraheja.commons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.commons.entity.Actrand;
import kraheja.commons.entity.ActrandCK;

@Repository
public interface ActrandRepository extends JpaRepository<Actrand, ActrandCK> {
	
	@Modifying 
	@Query("delete Actrand e where trim(e.actrandCK.actdTranser)=:actdTranser")
	public void deleteActrand(String actdTranser);
	
		
	@Query("SELECT e from Actrand e where e.actrandCK.actdTranser=:transer AND e.actrandCK.actdCoy=:coy AND e.actdProprietor=:propritor")
	public Actrand findByActdTranserAndActdCoyAndActdProp(String transer, String coy, String propritor);
	
	
	@Query("SELECT e from Actrand e where trim(e.actrandCK.actdTranser) =:transerNo order by e.actrandCK.actdTranser asc, e.actrandCK.actdBunum ASC")
	public List<Actrand> findActdByTranserNo(String transerNo);

	
	public List<Actrand> findByActrandCK_ActdTranserAndActrandCK_ActdCoy(String transer, String coy);
	
	@Query("Select a from Actrand a where trim(a.actrandCK.actdTranser) = :transer and a.actrandCK.actdBunum = :bnum")
	Actrand findActdMinorByTranserAndBnum(String transer, Integer bnum);
}
