package kraheja.adminexp.billing.dataentry.repository;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.adminexp.billing.dataentry.entity.Intercoybillheader;
import kraheja.adminexp.billing.dataentry.entity.IntercoybillheaderCK;

@Repository
public interface IntercoybillheaderRepository extends JpaRepository<Intercoybillheader, IntercoybillheaderCK>{

	@Query(value = "select max(icbeh_periodto) from intercoybillheader where trim(icbeh_coy)=? and trim(icbeh_projcode)=?", nativeQuery = true)
	LocalDateTime fetchMaxPeriod(String companyCode, String projectCode);
}
