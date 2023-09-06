package kraheja.payroll.repository;

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
	
	@Query(value="SELECT :formula as amt\r\n"
			+ "FROM   v_empsalarypackage\r\n"
			+ "WHERE  trim(vspk_empcode) = :empcode \r\n"
			+ "       AND vspk_todate = (SELECT MAX(vspk_todate)\r\n"
			+ "                               FROM   v_empsalarypackage\r\n"
			+ "                               WHERE  trim(vspk_empcode) = :empcode)",nativeQuery = true)
	Double GetOutputValue(String formula,String empcode);

}
