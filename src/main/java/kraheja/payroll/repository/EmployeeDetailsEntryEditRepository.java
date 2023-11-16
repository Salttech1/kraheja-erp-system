package kraheja.payroll.repository;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Tuple;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.payroll.entity.Empjobinfo;
import kraheja.payroll.entity.EmpjobinfoCK;

@Repository
public interface EmployeeDetailsEntryEditRepository extends JpaRepository<Empjobinfo, EmpjobinfoCK>{
	@Query(value = "select ('Select ' || efor_formula || ' from v_empsalarypackage where vspk_empcode = ' || '''WD009''' || ' and vspk_todate = ' || '''01-JAN-2050''') as Q from emppayformula where efor_coy = 'UNIQ' and efor_emptype = 'S' and efor_jobtype = 'P' and efor_earndedcode = 'MTHLYGROSS'", nativeQuery = true)
	String GetMonthGrossQuery(String empCode,String empCoy,String empType,String empJobtype);
	
	@Query(value = ":MyQuery", nativeQuery = true)
	Double GetMonthGross(String MyQuery);
	
	@Query(value="SELECT ejin_company as coy,\r\n"
			+ "       ejin_emptype as emptype,\r\n"
			+ "       ejin_jobtype as jobtype \r\n"
			+ "FROM   empjobinfo\r\n"
			+ "WHERE  trim(ejin_empcode) = :empCode \r\n"
			+ "       AND ejin_effectiveupto = (SELECT MAX(ejin_effectiveupto)\r\n"
			+ "                                 FROM   empjobinfo\r\n"
			+ "                                 WHERE  trim(ejin_empcode) = :empCode )",nativeQuery = true)
	Tuple GetEmployeeJobinfoForFormula(String empCode);
	
	
	@Query(value="SELECT 'Select ' || efor_formula || ' from ' || efor_table || ' where ' || efor_condition as formula\r\n"
			+ "FROM   emppayformula\r\n"
			+ "WHERE  trim(efor_coy) = :empCoy \r\n"
			+ "       AND trim(efor_emptype) = :empType\r\n"
			+ "       AND trim(efor_jobtype) = :empJobtype\r\n"
			+ "       AND trim(efor_earndedcode) = :earndedcode",nativeQuery = true)
	String GetFormula(String empCoy,char empType,char empJobtype,String earndedcode);
	
	@Query(value="SELECT CSPK_EARNDEDCODE AS earndedcode,\r\n"
			+ "       EDHM_DISPLAYNAME AS EarnDesc,\r\n"
			+ "       CSPK_PAYCYCLE    AS paycycle,\r\n"
			+ "       CSPK_RATECYCLE   AS ratecycle\r\n"
			+ "FROM   coysalarypackage,\r\n"
			+ "       EARNDEDMASTER\r\n"
			+ "WHERE  EDHM_EARNDEDCODE = CSPK_EARNDEDCODE\r\n"
			+ "       AND TRIM(CSPK_COY) = :coycode\r\n"
			+ "       AND CSPK_EFFECTIVEUPTO = :closeDate\r\n"
			+ "       AND CSPK_SHOWINSALPACK = 'Y'\r\n"
			+ "       AND CSPK_EARNDEDCODE IN (SELECT EDHM_EARNDEDCODE\r\n"
			+ "                                FROM   EARNDEDMASTER\r\n"
			+ "                                WHERE  EDHM_EARNDEDTYPE = 'A')\r\n"
			+ "ORDER  BY EDHM_COMPUTENO,\r\n"
			+ "          CSPK_RATECYCLE DESC,\r\n"
			+ "          CSPK_PAYCYCLE DESC,\r\n"
			+ "          CSPK_EARNDEDCODE",nativeQuery = true)
	List<Tuple> fetchCompanySalPackage(String coycode,String closeDate);
	
	@Query(value="SELECT CSPK_EARNDEDCODE,\r\n"
			+ "       EDHM_DISPLAYNAME AS DedDesc,\r\n"
			+ "       CSPK_PAYCYCLE    AS payment,\r\n"
			+ "       CSPK_RATECYCLE   AS Modes\r\n"
			+ "FROM   coysalarypackage,\r\n"
			+ "       EARNDEDMASTER\r\n"
			+ "WHERE  EDHM_EARNDEDCODE = CSPK_EARNDEDCODE\r\n"
			+ "       AND Trim(CSPK_COY) = :coycode\r\n"
			+ "       AND CSPK_EFFECTIVEUPTO = :closeDate\r\n"
			+ "       AND CSPK_SHOWINSALPACK = 'Y'\r\n"
			+ "       AND CSPK_EARNDEDCODE IN (SELECT EDHM_EARNDEDCODE\r\n"
			+ "                                FROM   EARNDEDMASTER\r\n"
			+ "                                WHERE  EDHM_EARNDEDTYPE = 'D')\r\n"
			+ "       AND edhm_schemeyn = 'N'\r\n"
			+ "ORDER  BY EDHM_COMPUTENO,\r\n"
			+ "          CSPK_RATECYCLE DESC,\r\n"
			+ "          CSPK_PAYCYCLE DESC,\r\n"
			+ "          CSPK_EARNDEDCODE",nativeQuery = true)
	List<Tuple> fetchCompanySalDedPackage(String coycode,String closeDate);

}
