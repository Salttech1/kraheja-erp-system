package kraheja.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.sales.bean.entitiesresponse.InfrBillDBResponse;
import kraheja.sales.entity.Infrsaogrp01_Print;
import kraheja.sales.entity.Infrsaogrp01_PrintCK;

@Repository
public interface Infrsaogrp01_PrintRepository extends JpaRepository<Infrsaogrp01_Print, Infrsaogrp01_PrintCK>{

	@Query("select new kraheja.sales.bean.entitiesresponse.InfrBillDBResponse("
			+ "b.infrsaogrp01_printCK.saogrpBillnum as infrBillnum,"
			+ "b.infrsaogrp01_printCK.saogrpInvoiceno as infrInvoiceNo,"
			+ "b.saogrpIrnno as infrIrnNo,"
			+ "b.saogrpOwnerid as infrOwnerId,"
			+ "b.saogrpBldgcode as infrBldgCode,"
			+ "b.saogrpWing as infrWing,"
			+ "b.saogrpFlatnum as infrFlatnum,"
			+ "b.saogrpBillmonth as infrMonth,"
			+ "b.saogrpBillamt as infrBillamt,"
			+ "b.saogrpBilldate as infrBilldate,"
			+ "b.saogrpBillfrom as infrFromdate,"
			+ "b.saogrpBillto as infrTodate,"
			+ "b.saogrpOutrate as infrAmtos,"
			+ "b.saogrpBillarrears as infrArrears,"
			+ "b.saogrpIntarrears as infrIntarrears,"
			+ "b.saogrpInterest as infrInterest,"
			+ "b.saogrpAdmincharges as infrAdmincharges,"
			+ "b.saogrpCgst as infrCgst,"
			+ "b.saogrpSgst as infrSgst,"
			+ "b.saogrpIgst as infrIgst,"
			+ "b.saogrpCgstperc as infrCgstperc,"
			+ "b.saogrpSgstperc as infrSgstperc,"
			+ "b.saogrpIgstperc as infrIgstperc) from Infrsaogrp01_Print b where b.infrsaogrp01_printCK.saogrpSessid= :sessId")
	InfrBillDBResponse getInfrsaogrp01Print(double sessId);
	
	@Query("select p from Infrsaogrp01_Print p where p.infrsaogrp01_printCK.saogrpSessid= :sessId and trim(p.saogrpOwnerid)= :ownerId ")
	Infrsaogrp01_Print findByInfrsaogrp01_printCKSaogrpSessidAndSaogrpOwnerid(double sessId, String ownerId);
}
