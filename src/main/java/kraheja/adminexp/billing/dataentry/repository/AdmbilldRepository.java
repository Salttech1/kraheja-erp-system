package kraheja.adminexp.billing.dataentry.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kraheja.adminexp.billing.dataentry.entity.Admbilld ;
import kraheja.adminexp.billing.dataentry.entity.AdmbilldCK;
@Repository
public interface AdmbilldRepository extends JpaRepository<Admbilld, AdmbilldCK> {

	 
	List<Admbilld> findByAdmbilldCK_AdbldSer(String ser) ;

	

}