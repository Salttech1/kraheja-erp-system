package kraheja.sales.repository;
 
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.sales.entity.Outrate ;
import kraheja.sales.entity.OutrateCK;
@Repository
public interface OutrateRepository extends JpaRepository<Outrate, OutrateCK> {

	 
	Outrate findByOutrateCK_OutrBldgcodeAndOutrateCK_OutrFlatnumAndOutrateCK_OutrWingAndOutrateCK_OutrStartdate(String bldgcode, String flatnum, String wing, String startdate) ;  
	
	List<Outrate> findByOutrateCK_OutrBldgcodeAndOutrateCK_OutrFlatnumAndOutrateCK_OutrWing(String bldgcode, String flatnum, String wing) ;
	
//	List<Outrate> findByOutrateCK_OutrBldgcodeAndOutrateCK_OutrFlatnum(String bldgcode, String flatnum) ;

	@Query("select COALESCE(min(o.outrateCK.outrStartdate), '201707') from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrFlatnum=:flatno and o.outrateCK.outrWing=:wing and o.outrBilltype=:billtype")
	String fetchStartDate(String bldgcode, String wing, String flatno, String billtype);  
	
	//@Query("select o.outrAuxirate as outrAuxirate from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatno and o.outrateCK.outrStartdate<=:month and o.outrEnddate>=:month and o.outrBilltype='F'") //NS 14.08.2023
//	@Query("select o.outrAuxirate from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatno and o.outrateCK.outrStartdate<=:month and o.outrEnddate>=:month and o.outrBilltype=:billtype") //NS 14.08.2023
//	String fetchMaintainanceRateForAuxilliary(String bldgcode, String wing, String flatno, String month, String billtype);
	@Query("select o.outrAuxirate from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatno and o.outrBilltype=:billtype") //NS 08.09.2023(Change has been done to the API to reduce the requests by permission of Utpal sir.
	String fetchMaintainanceRateForAuxilliary(String bldgcode, String wing, String flatno, String billtype);
	
	@Query("select o.outrAuxiadmin from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatno and o.outrBilltype=:billtype") //NS 17.08.2023 //NS 08.09.2023(Change has been done to the API to reduce the requests by permission of Utpal sir.
	String fetchAdminRateForAuxilliary(String bldgcode, String wing, String flatno, String billtype);
	
	@Query("select o.outrAuxi_Tds from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatno and o.outrBilltype=:billtype") //NS 17.08.2023 //NS 08.09.2023(Change has been done to the API to reduce the requests by permission of Utpal sir.
	String fetchTDSRateForAuxilliary(String bldgcode, String wing, String flatno, String billtype);
	
	@Query("select o.outrateCK.outrStartdate from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatnum and o.outrBilltype=:billtype") //NS 04.09.2023
	String fetchStartdateForAuxiGSTFirst(String bldgcode, String wing, String flatnum, String billtype);
	
	@Query("select o.outrEnddate from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrWing=:wing and o.outrateCK.outrFlatnum =:flatnum and o.outrBilltype=:billtype") //NS 04.09.2023
	String fetchEndDeteForAuxiGSTFirst(String bldgcode, String wing, String flatnum, String billtype);

	//	OUTR_INFRRATE OUTRINFRRATE RATE FETCHING START BY SAZZAD
	@Query(value = "select nvl(outr_infrrate, 0) where trim(outr_bldgcode)=? and trim(outr_wing)=? and trim(outr_flatnum)=? and outr_billtype=? and ? between outr_startdate and outr_enddate", nativeQuery = true)
	double findOutrAuxiRateMonthWiseForInfra(String bldgcode, String wing, String flatno, String billtype, String startDate);
	
	//outrInfradmin
	@Query(value = "select nvl(outr_infradmin, 0.00) from outrate where trim(outr_bldgcode)=? and trim(outr_wing)=? and trim(outr_flatnum)=? and outr_billtype=? and ? between outr_startdate and outr_enddate", nativeQuery = true)
	double findAdminRateMonthWiseForInfra(String bldgcode, String wing, String flatno, String billtype, String startDate);
	
	
	@Query(value = "select nvl(outr_auxirate, 0) from outrate where trim(outr_bldgcode)=? and trim(outr_wing)=? and trim(outr_flatnum)=? and outr_billtype=? and ? between outr_startdate and outr_enddate", nativeQuery = true)
	double findOutrAuxiRateMonthWiseForAuxi(String bldgcode, String wing, String flatno, String billtype, String startDate);
	
	@Query(value = "select nvl(outr_auxiadmin, 0) from outrate where trim(outr_bldgcode)=? and trim(outr_wing)=? and trim(outr_flatnum)=? and outr_billtype=? and ? between outr_startdate and outr_enddate", nativeQuery = true)
	double findAdminRateMonthWiseForAuxi(String bldgcode, String wing, String flatno, String billtype, String startDate);
	
	@Query(value = "select nvl(outr_auxiadmin, 0) from outrate where outr_bldgcode=? and outr_wing=' ' and trim(outr_flatnum)=? and outr_billtype=? ", nativeQuery = true)
	double findAdminRateForEmptyWing(String bldgCode, String flatNumber, String billType);
	
	@Query(value = "select nvl(outr_auxirate, 0) from outrate where outr_bldgcode=? and outr_wing=' ' and trim(outr_flatnum)=? and outr_billtype=? ", nativeQuery = true)
	double findAuxiRateForEmptyWing(String bldgCode, String flatNumber, String billType);
//	OUTR_INFRRATE OUTRINFRRATE RATE FETCHING END BY SAZZAD
	
	@Query("select COALESCE(min(o.outrateCK.outrStartdate), '201707') from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrFlatnum=:flatno and o.outrateCK.outrWing=:wing and o.outrBilltype=:billtype")
	String findAdminRateMonthWiseForAuxi(String bldgcode, String wing, String flatno, String billtype);  
	
	@Query(value = "select nvl(outr_infra_tds, 0) from outrate where outr_bldgcode=? and outr_wing=' ' and trim(outr_flatnum)=? and outr_billtype=? and outr_startdate < ?", nativeQuery = true)
	double findTdsRateForEmptyWingMonthWise(String bldgcode, String flatno, String billtype, String startDate);
	
	@Query(value = "select nvl(outr_infra_tds, 0) from outrate where trim(outr_bldgcode)=? and trim(outr_wing)=? and trim(outr_flatnum)=? and outr_billtype=? and ? between outr_startdate and outr_enddate", nativeQuery = true)
	double findTdsRateMonthWise(String bldgcode, String wing, String flatno, String billtype, String startDate);
	
	@Query(value = "select o.outr_startdate from outrate o where trim(o.outr_bldgcode)=? and trim(o.outr_wing)=? and trim(o.outr_flatnum)=? and trim(o.outr_billtype)=? and ? between o.outr_startdate and o.outr_enddate", nativeQuery = true)
	String fetchStartDateBybldgCodeWingFlatBillTypeAndBetweenDate(String bldgcode, String wing, String flatnum, String billtype, String date);
	
	@Query(value = "select o.outr_enddate from outrate o where trim(o.outr_bldgcode)=? and trim(o.outr_wing)=? and trim(o.outr_flatnum)=? and trim(o.outr_billtype)=? and ? between o.outr_startdate and o.outr_enddate", nativeQuery = true)
	String fetchEndDateBybldgCodeWingFlatBillTypeAndBetweenDate(String bldgcode, String wing, String flatnum, String billtype, String date);

}