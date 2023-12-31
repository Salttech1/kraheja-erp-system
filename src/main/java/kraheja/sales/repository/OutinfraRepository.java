package kraheja.sales.repository;

import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import kraheja.sales.entity.Outinfra ;
import kraheja.sales.entity.OutinfraCK;
@Repository
public interface OutinfraRepository extends JpaRepository<Outinfra, OutinfraCK> {

	 
	Outinfra findByOutinfraCK_InfBldgcodeAndOutinfraCK_InfOwneridAndOutinfraCK_InfRecnumAndOutinfraCK_InfMonthAndOutinfraCK_InfNarrcode(String bldgcode, String ownerid, String recnum, String month, String narrcode) ;
	
	@Query("select  sum(nvl(o.infAmtpaid,0)) as amtPaid,  sum(nvl(o.infAmtint,0)) as amtint,  sum(nvl(o.infCgst,0)) as CGST, sum(nvl(o.infAdmincharges, 0)) as admincharges,  o.outinfraCK.infMonth, o.outinfraCK.infOwnerid,  sum(nvl(o.infSgst,0)) as SGST,  sum(nvl(o.infIgst,0)) as IGST,  sum(nvl(o.infTds,0)) as TDS from Outinfra o where o.outinfraCK.infOwnerid=:ownerid  and o.infCancelledyn='N'  and o.outinfraCK.infMonth>=:month  and o.infChargecode=:chargeCode  and o.infRectype =:recType  and o.infGstyn= 'Y' group by o.outinfraCK.infOwnerid, o.outinfraCK.infBldgcode, o.infWing, o.infFlatnum, o.outinfraCK.infMonth order by o.outinfraCK.infOwnerid, o.outinfraCK.infBldgcode, o.infWing, o.infFlatnum, o.outinfraCK.infMonth")
	List<Tuple> findPrevOgRecords(String ownerid, String month, String chargeCode, String recType);
}