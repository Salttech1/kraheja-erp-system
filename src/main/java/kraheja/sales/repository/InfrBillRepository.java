package kraheja.sales.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.sales.bean.entitiesresponse.DBResponseForNewInfrBill;
import kraheja.sales.bean.entitiesresponse.InfrBillDBResponse;
import kraheja.sales.entity.Infrbill;
import kraheja.sales.entity.InfrbillCK;

@Repository
public interface InfrBillRepository extends JpaRepository<Infrbill, InfrbillCK> {

	@Query("select new kraheja.sales.bean.entitiesresponse.InfrBillDBResponse("
			+ "b.infrbillCK.infrBillnum as infrBillnum,"
			+ "b.infrInvoiceno as infrInvoiceNo,"
			+ "b.infrIrnno as infrIrnNo,"
			+ "b.infrbillCK.infrOwnerId as infrOwnerId,"
			+ "b.infrBldgcode as infrBldgCode,"
			+ "b.infrWing as infrWing,"
			+ "b.infrFlatnum as infrFlatnum,"
			+ "b.infrbillCK.infrMonth as infrMonth,"
			+ "b.infrBillamt as infrBillamt,"
			+ "b.infrBilldate as infrBilldate,"
			+ "b.infrFromdate as infrFromdate,"
			+ "b.infrTodate as infrTodate,"
			+ "b.infrAmtos as infrAmtos,"
			+ "b.infrArrears as infrArrears,"
			+ "b.infrInterest as infrInterest,"
			+ "b.infrAdmincharges as infrAdmincharges,"
			+ "b.infrCgst as infrCgst,"
			+ "b.infrSgst as infrSgst,"
			+ "b.infrIgst as infrIgst,"
			+ "b.infrCgstperc as infrCgstperc,"
			+ "b.infrSgstperc as infrSgstperc,"
			+ "b.infrIgstperc as infrIgstperc) from Infrbill b where trim(b.infrbillCK.infrMonth) = :date and trim(b.infrbillCK.infrOwnerId)= :ownerId and trim(b.infrChargecode) = :chargeCode and trim(b.infrGstyn)= 'Y' and trim(b.infrBilltype) = :billType order by infr_ownerid")
	InfrBillDBResponse fetchDetail(String date, String ownerId, String chargeCode, String billType);
	
	@Query(value = "select max(infr_billnum) as billnum from infrbill where trim(infr_ownerid)=? and trim(infr_bldgcode)=? and trim(infr_wing)=? and trim(infr_chargecode) = ? and trim(infr_billtype)= ? and infr_billdate < ?", nativeQuery = true)
	String fetchBillNumber(String ownerid, String bldgcode, String wing, String chargeCode, String billtype, String date);
	

	@Query(value = "select nvl(infr_month, '199001') from infrbill where trim(infr_ownerid)=? and trim(infr_bldgcode)=? and trim(infr_wing)=? and trim(infr_chargecode)=? and infr_billnum=? and trim(infr_billtype)=?", nativeQuery = true)
	String getInfrMonth(String ownerId, String bldgcode, String wing, String chargecode, String billnum, String billtype);
	
	@Query("SELECT new kraheja.sales.bean.entitiesresponse.DBResponseForNewInfrBill(COALESCE(infr.infrBilldate,'01-01-1900') as billDate, "
	        + "(COALESCE(infr.infrBillamt, 0) + COALESCE(infr.infrAdmincharges, 0) "
	        + "+ COALESCE(infr.infrCgst, 0) + COALESCE(infr.infrSgst, 0) "
	        + "+ COALESCE(infr.infrIgst, 0) + COALESCE(infr.infrServtax, 0) "
	        + "+ COALESCE(infr.infrSwachhcess, 0) + COALESCE(infr.infrKrishicess, 0)) as balance, "
	        + "COALESCE(infr.infrArrears, 0) as arrears, "
	        + "COALESCE(infr.infrInterest, 0) as interest, "
	        + "COALESCE(infr.infrIntarrears, 0) as intarrears )"
	        + "FROM Infrbill infr "
	        + "WHERE trim(infr.infrbillCK.infrOwnerId) = 'ORHHHF0000H' "
	        + "AND infr.infrChargecode = 'AUXI' "
	        + "AND infr.infrbillCK.infrMonth >= '202310' "
	        + "AND infr.infrBilltype = 'N'")
	DBResponseForNewInfrBill fetchBillDateAndOldBalanceAndArearsAndInterestAndIntArears();
}
