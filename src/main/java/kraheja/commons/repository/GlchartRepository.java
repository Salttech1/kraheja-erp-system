package kraheja.commons.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.commons.entity.Glchart;
import kraheja.commons.entity.GlchartCK;
import kraheja.sales.bean.entitiesresponse.GlchartDBResponse;

@Repository
public interface GlchartRepository extends JpaRepository<Glchart, GlchartCK> {
//	@Query("SELECT e FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum AND e.glChartCK.chartClosedate = '01/JAN/2050'")
//	Glchart findBychartAcnum(String acnum); 
	
	@Query("SELECT NVL(e.chartPb1len, 0), NVL(e.chartPb1start, 0), NVL(e.chartPb2len, 0), NVL(e.chartPb2start, 0), NVL(e.chartMb1len, 0), NVL(e.chartMb1start, 0) FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum")
	String findBychartAcnum(String acnum); 
	
	@Query("SELECT NVL(e.chartCfpaygroup, ' '), NVL(e.chartPgroupc, ' ') FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum")
	String findPaygroupByCharAcnum(String acnum);
	
	@Query("SELECT NVL(e.chartCfrecgroup, ' '), NVL(e.chartRgroupc, ' ') FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum")
	String findRecgroupByCharAcnum(String acnum);
	
	@Query("SELECT NVL(e.chartMinoryn, ' '), NVL(e.chartValidminors, ' '), NVL(e.chartPostprojonly, ' '), NVL(e.chartPostglonly, ' ') FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum ")
	String findchartMinorAndchartPostByCharAcnum(String acnum);
	
	@Query("SELECT NVL(e.chartMinoryn, ' '), NVL(e.chartValidminors, ' '), NVL(e.chartValidparties, ' ') FROM Glchart e WHERE trim(e.glChartCK.chartAcnum) = :acnum ")
	String findchartMinorAndchartValidPartiesByCharAcnum(String acnum);
	
	@Query("select new kraheja.sales.bean.entitiesresponse.GlchartDBResponse(g.chartCfrecgroup, g.chartRgroupc) from Glchart g where g.glChartCK.chartAcnum= :chartAcnum and g.glChartCK.chartClosedate is null")
	GlchartDBResponse fetchChartCfrecgroup(String chartAcnum);

}