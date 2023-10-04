package kraheja.sales.repository;
 
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

	@Query("select min(o.outrateCK.outrStartdate) from Outrate o where o.outrateCK.outrBldgcode=:bldgcode and o.outrateCK.outrFlatnum=:flatno and o.outrateCK.outrWing=:wing and o.outrBilltype=:billtype")
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
	
	@Query("select nvl(o.outrAuxirate, 0) from Outrate o where o.outrateCK.outrBldgcode= :bldgcode and o.outrateCK.outrWing= :wing and o.outrateCK.outrFlatnum = :flatno and o.outrBilltype= :billtype")
	String findOutrAuxiRateMonthWise(String bldgcode, String wing, String flatno, String billtype);
	
	@Query("select nvl(o.outrAuxiadmin, 0) from Outrate o where o.outrateCK.outrBldgcode= :bldgcode and o.outrateCK.outrWing= :wing and o.outrateCK.outrFlatnum = :flatno and o.outrBilltype= :billtype")
	String findAdminRateMonthWise(String bldgcode, String wing, String flatno, String billtype);
	
	@Query("select nvl(o.outrInfra_Tds,0) as outrInfra_Tds from Outrate o where o.outrateCK.outrBldgcode= :bldgcode and o.outrateCK.outrWing= :wing and o.outrateCK.outrFlatnum = :flatno and o.outrBilltype= :billtype")
	String findTdsRateMonthWise(String bldgcode, String wing, String flatno, String billtype);
	
}