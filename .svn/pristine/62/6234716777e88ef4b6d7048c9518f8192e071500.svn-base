package kraheja.adminexp.billing.dataentry.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import kraheja.adminexp.billing.dataentry.entity.Admadvance ;
import kraheja.adminexp.billing.dataentry.entity.AdmadvanceCK;
@Repository
public interface AdmadvanceRepository extends JpaRepository<Admadvance, AdmadvanceCK> {

	 
	Admadvance findByAdmadvanceCK_AdvnSer(String ser) ; 

}