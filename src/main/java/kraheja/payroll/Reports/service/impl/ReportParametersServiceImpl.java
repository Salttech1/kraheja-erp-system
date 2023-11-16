package kraheja.payroll.Reports.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
//import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import javax.persistence.Tuple;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.utils.CommonConstraints;
import kraheja.commons.utils.ExcelUtils;
import kraheja.commons.utils.MyWordUtils;
import kraheja.payroll.Reports.service.ReportParametersService;
import kraheja.payroll.bean.response.ReportParametersRequestBean;
import kraheja.payroll.repository.ReportParametersRepository;
import kraheja.payroll.salarycomputation.service.impl.SalaryComputationServiceImpl;

@Service
public class ReportParametersServiceImpl implements ReportParametersService {

	@Value("${spring.datasource.url}")
	private String dbUrl;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private ReportParametersRepository reportParametersRepository;
	
	private static final Logger logger = LoggerFactory.getLogger(SalaryComputationServiceImpl.class);

	@Override
	public ResponseEntity<?> GetReportParameters() {
		Tuple t =  this.reportParametersRepository.GetReportParameters();
		
		ReportParametersRequestBean reportParametersRequestBean = new ReportParametersRequestBean(
                t.get(0, String.class),
                t.get(1, String.class),
                t.get(2, String.class),
                t.get(3, String.class),
                t.get(4, String.class),
                t.get(5, String.class),
                t.get(6, String.class),
                t.get(7, String.class),
                t.get(8, String.class),
                t.get(9, String.class),
                t.get(10, String.class),
                t.get(11, String.class),
                t.get(12, String.class),
                t.get(13, String.class),
                t.get(14, String.class),
                t.get(15, String.class),
                t.get(16, String.class));
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(reportParametersRequestBean).build());
	}
	
	@Override
	public ResponseEntity<?> pfExcelCreation(String comanyCode, String paymonth) {
		String sqlQuery;
		sqlQuery = "select 				( \r\n"
				+ "				Select 	to_char(eper_pfuan,'999999999999') \r\n"
				+ "				from 		emppersonal \r\n"
				+ "				where 	eper_empcode = vspd_empcode \r\n"
				+ "               and 		vspd_paymonth between to_char(eper_effectivefrom,'YYYYMM') and to_char(eper_effectiveupto,'YYYYMM') \r\n"
				+ "			) 					as Member_ID, \r\n"
				+ "			( \r\n"
				+ "				select 	replace(eper_fullname,'''',' ')\r\n"
				+ "				from 		emppersonal \r\n"
				+ "				where 	eper_empcode = vspd_empcode \r\n"
				+ "				and 		vspd_paymonth between to_char(eper_effectivefrom,'YYYYMM') and to_char(eper_effectiveupto,'YYYYMM') \r\n"
				+ "			) 					as Member_Name, \r\n"
				+ "           (nvl(vspd_basic,0)+nvl(vspd_hra,0)+nvl(vspd_ta,0)+nvl(vspd_soca,0)+nvl(vspd_ca,0)) as Gross_Wages,\r\n"
				+ "			vspd_pfwages 		as EPF_Wages, \r\n"
				+ "			( \r\n"
				+ "				CASE WHEN (vspd_empypenf) > 0 THEN \r\n"
				+ "				case 	when (vspd_pfwages) >= (select ent_num1 from entity where ent_class = 'PAYRL' and ent_id = 'FPFUL') \r\n"
				+ "						then (select ent_num1 from entity where ent_class = 'PAYRL' and ent_id = 'FPFUL')  \r\n"
				+ "						else (vspd_pfwages) \r\n"
				+ "				end \r\n"
				+ "				ELSE \r\n"
				+ "					0 \r\n"
				+ "END \r\n"
				+ "			) 					as EPS_Wages,			\r\n"
				+ "			( \r\n"
				+ "				CASE WHEN (vspd_pfwages) > 0 THEN \r\n"
				+ "				case 	when (vspd_pfwages) >= (select ent_num1 from entity where ent_class = 'PAYRL' and ent_id = 'FPFUL') \r\n"
				+ "						then (select ent_num1 from entity where ent_class = 'PAYRL' and ent_id = 'FPFUL')  \r\n"
				+ "						else (vspd_pfwages) \r\n"
				+ "				end \r\n"
				+ "				ELSE \r\n"
				+ "					0 \r\n"
				+ "END \r\n"
				+ "			) 					as EDLI_Wages,			\r\n"
				+ "			(vspd_pf + vspd_vpf) 			as EPF_Contribution_due, \r\n"
				+ "			vspd_empypenf	as EPS_Contribution_remitted, \r\n"
				+ "			vspd_empypf 	as Diff_EPF_EPS_Contri_due, \r\n"
				+ "			vspd_daysabsent as NCP_Days, \r\n"
				+ "			0 as Refund_of_advances \r\n"
				+ "from 		v_empsalarypaid \r\n"
				+ "where 	trim(vspd_paymonth) =  '".concat(paymonth).concat("' \r\n")	
				+ "and		(trim(vspd_coy) 	>= '".concat(comanyCode).concat("' \r\n")
				+ "and 		trim(vspd_coy)      <= '".concat(comanyCode).concat("') \r\n")	
				+ "and 		trim(vspd_salarytype) IN ('S') \r\n"
				+ "and 		vspd_pf 			> 0 \r\n"
				+ "and 		trim(vspd_paystatus) = 'P' \r\n"
				+ "order by to_number(Trim(Member_ID)) " ; 
		try {
			File excelFile = ExcelUtils.INSTANCE.export(sqlQuery, dbUrl, dbUsername, dbPassword);

			String fileName = CommonConstraints.INSTANCE.DD_MM_YYYY_HH_MM_SS_FORMATTER.format(LocalDateTime.now()).concat(CommonConstraints.INSTANCE.XLS_EXTENSION);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(new InputStreamResource(new FileInputStream(excelFile)));
		} catch (FileNotFoundException e) {
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error while creating report.").build());
	}

	@Override
	public ResponseEntity<?> empwiseMonthlySummary(String coyCode, String deptCodes, String empCodes,
			String salaryTypes, String paymonth, String paymentDate,  String empType ) {
		String sqlQuery = "", sqlQuery1a = "", sqlQuery1b = "" , sqlQueryFull = "" ,hotelPropYN ;
		
		sqlQuery1a = "select  Co,DENSE_RANK() over (ORDER By employee ) as Srno, Employee,Gender,Age,EdCd,Dept as Desg, \r\n"
		+ "Earndedamt,Pr,Ab,Enc,edhm_computeno  \r\n" 
		+ "from coysalarypackage, V_employeewisemthlysumm  \r\n" 
		+ "where 		cspk_orderinexcelrp > 0 and cspk_coy = co and rtrim(cspk_earndedcode) = rtrim(edcd) and  \r\n" 
		+ "paymonth between to_char(cspk_effectivefrom,'yyyymm') and to_char(cspk_effectiveupto,'yyyymm')" ;
		

		sqlQuery1b = "select	Co, DENSE_RANK() over (ORDER By employee ) as Srno, Employee,Gender,Age,EdCd,Dept as Desg,Earndedamt,Pr,Ab,Enc,edhm_computeno \r\n"
				+ "	FROM    v_emplmthlysummGross \r\n"
				+ "	WHERE   Earndedamt > 0 " ;
		
		

			if(StringUtils.isAllBlank(coyCode)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and ('ALL'  = '" + coyCode +"'  OR co = '"+ coyCode + "') " ;
			}	
			if(StringUtils.isAllBlank(deptCodes) )  {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + deptCodes + ") OR dept in (" + deptCodes + ")) ";
			}
			if(StringUtils.isAllBlank(empCodes)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + empCodes + ") OR empl_code in (" + empCodes + ")) ";
			}	
			if(StringUtils.isAllBlank(salaryTypes)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + salaryTypes + ") OR salarytype in (" + salaryTypes + ")) ";
			}	
			if(StringUtils.isAllBlank(paymonth)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and ('ALL' = '" + paymonth + "'  OR paymonth = '" + paymonth + "') " ;
			}
			// Need to add a condition for hotel property
			hotelPropYN = reportParametersRepository.GetHotelPropYN() ;
			logger.info("hotelPropYN :: {}", hotelPropYN);
			
			if(StringUtils.equals(hotelPropYN, "Y")) {
				if(StringUtils.isAllBlank(empType)) {
					// do nothing as there is nothing to concatenate to main query
				}
				else
				{
					sqlQuery = sqlQuery + " and ejin_emptype = '" + empType + "' " ;
				}
			}

			sqlQueryFull = sqlQuery1a + sqlQuery + " union all " + sqlQuery1b + sqlQuery + " order by employee,edhm_computeno " ;
			
		try {
			logger.info("sqlQuery :: {}", sqlQueryFull);
			File excelFile = ExcelUtils.INSTANCE.export(sqlQueryFull, dbUrl, dbUsername, dbPassword);

			String fileName = CommonConstraints.INSTANCE.DD_MM_YYYY_HH_MM_SS_FORMATTER.format(LocalDateTime.now()).concat(CommonConstraints.INSTANCE.XLS_EXTENSION);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(new InputStreamResource(new FileInputStream(excelFile)));
		} catch (FileNotFoundException e) {
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error while creating report.").build());
	}

	
	@Override
	public ResponseEntity<?> gratuityForm(String templateFileWithPath, String[][] fieldsList){
	
		try {
			File createWord = MyWordUtils.INSTANCE.replplaceholdersworddoc(templateFileWithPath, fieldsList) ;
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error creating file.").build()); 		
	}
	
	@Override
	public ResponseEntity<?> WordReplaceText(String templateFileWithPath, String[][] fieldsList){ 
////	    private static final String SOURCE_FILE = "lipsum.doc";
////	    private static final String OUTPUT_FILE = "new-lipsum.doc";
	    String SOURCE_FILE = "\\\\prodsrv\\FaPayDocs\\GratuityForm.doc";
	    String OUTPUT_FILE = "D:\\ATemp\\GratuityForm.doc";
//
//	    public static void main(String[] args) throws Exception {
//	        WordReplaceText instance = new WordReplaceText();
//	        try (HWPFDocument doc = instance.openDocument(SOURCE_FILE)) {
//	            if (doc != null) {
//	                HWPFDocument newDoc = instance.replaceText(doc, "dolor", "d0l0r");
//	                instance.saveDocument(newDoc, OUTPUT_FILE);
//	            }
//	        }
//	    }
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error creating file.").build());
	}
	

	@Override
	public ResponseEntity<?> socSalaryDetExcelCreation(String empCodes, String paymonthfrom , String paymonthto) {
		String sqlQuery;

		sqlQuery = "select 	vspd_empcode, (select eper_fullname from emppersonal where eper_empcode = vspd_empcode) as name, \r\n"
				+ "			vspd_paymonth, \r\n"
				+ "			sum(vspd_basic) 	as Basic, \r\n"
				+ "			sum(vspd_hra) 		as Hra, \r\n"
				+ "			sum(vspd_ta) 		as Transport, \r\n"
				+ "			sum(vspd_soca) 	as Social, \r\n"
				+ "			sum(vspd_ca) 		as City, \r\n"
				+ "			sum(vspd_Bonus) 	as Bonus,  \r\n"
				+ "			sum(vspd_exgratia) as Exgratia  \r\n"
				+ "from 		v_empsalarypaid \r\n"
				+ "where 	vspd_empcode in (".concat(empCodes).concat(") \r\n")
				+ "and 		vspd_salarytype in ('S','I','B') \r\n"
				+ "and 		vspd_paystatus = 'P' \r\n"
				+ "and 		vspd_paymonth between '".concat(paymonthfrom).concat("' and '").concat(paymonthto).concat("' \r\n")
				+ "group by vspd_empcode, vspd_paymonth  \r\n"
				+ "order by vspd_paymonth, Name " ;
		try {
			File excelFile = ExcelUtils.INSTANCE.export(sqlQuery, dbUrl, dbUsername, dbPassword);

			String fileName = CommonConstraints.INSTANCE.DD_MM_YYYY_HH_MM_SS_FORMATTER.format(LocalDateTime.now()).concat(CommonConstraints.INSTANCE.XLS_EXTENSION);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(new InputStreamResource(new FileInputStream(excelFile)));
		} catch (FileNotFoundException e) {
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error while creating report.").build());
	}

	
	@Override
	public ResponseEntity<?> gorssSalaryDetExcelCreation(String paymonthfrom) {
		String sqlQuery,  sqlBaseQuery;
		sqlBaseQuery = "select count(*) from salaryplan where splan_paymonth = '".concat(paymonthfrom).concat("' and splan_salarytype = 'M' and splan_coy in ('FEHO')") ;
		logger.info("month :: {}", paymonthfrom);
		Query sqlBasequery = entityManager.createNativeQuery("select count(*) from salaryplan where splan_paymonth = '" + paymonthfrom + "' and splan_salarytype = 'M' and splan_coy in ('FEHO')");
		Number counter = (Number) sqlBasequery.getSingleResult();
		logger.info("Counter :: {}", counter);

		if (counter.equals(BigDecimal.ZERO)) {
			sqlQuery = "SELECT vspk_coy                                                                                                                                                            AS\r\n"
					+ "       Company,\r\n"
					+ "       vspk_empcode                                                                                                                                                        AS\r\n"
					+ "       Empcode,\r\n"
					+ "       (SELECT eper_fullname\r\n"
					+ "        FROM   emppersonal\r\n"
					+ "        WHERE  eper_empcode = vspk_empcode\r\n"
					+ "               AND SYSDATE BETWEEN eper_effectivefrom AND eper_effectiveupto)                                                                                              AS NAME,\r\n"
					+ "       (SELECT site\r\n"
					+ "        FROM   v_paymastsite\r\n"
					+ "        WHERE  sitecode = (SELECT ejin_worksite\r\n"
					+ "                           FROM   empjobinfo\r\n"
					+ "                           WHERE  ejin_empcode = vspk_empcode\r\n"
					+ "                                  AND SYSDATE BETWEEN ejin_effectivefrom AND ejin_effectiveupto))                                                                          AS\r\n"
					+ "       WorkSite,\r\n"
					+ "       (SELECT ejin_department\r\n"
					+ "        FROM   empjobinfo\r\n"
					+ "        WHERE  ejin_empcode = vspk_empcode\r\n"
					+ "               AND SYSDATE BETWEEN ejin_effectivefrom AND ejin_effectiveupto)                                                                                              AS\r\n"
					+ "       Department,\r\n"
					+ "       vspk_basic + vspk_hra + vspk_soca + vspk_ca + vspk_ta                                                                                                               AS\r\n"
					+ "       Gross,\r\n"
					+ "		(	case 	when 	vspk_fromdate >= to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm') \r\n")
					+ "							then (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) - extract (day from vspk_fromdate) + 1 \r\n")
					+ "					when 	vspk_fromdate <  to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm') \r\n")
					+ "							then (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) end ) - nvl((	select 	sum(ltrn_noofdays) from(leavetrn) \r\n")
					+ "																														where		(ltrn_empcode = vspk_empcode) \r\n"
					+ "																														and 		ltrn_fromdate >= to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm')  \r\n")
					+ "																														and 		ltrn_todate <= last_day(to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm'))),0)  						as noofdaysworked, \r\n")
					+ "       (SELECT empm_dayspaid\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       PayDays,\r\n"
					+ "       (SELECT empm_daysabsent\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       AbsentDays,\r\n"
					+ "       Round(( ( vspk_basic + vspk_hra + vspk_soca + vspk_ca + vspk_ta ) * (SELECT empm_dayspaid\r\n"
					+ "                                                                            FROM   emppaymonth\r\n"
					+ "                                                                            WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "                                                                                   AND empm_empcode = vspk_empcode\r\n"
					+ "                                                                                   AND empm_salarytype = 'S') / ( Extract(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm'))) ) ), -1) AS Pay,\r\n")
					+ "		'=ROUND((F' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '*H' ||  To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1)  \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) || ',-1)'  as Payformula\r\n")
					+ "FROM   v_empsalarypackage\r\n"
					+ "WHERE  vspk_todate = '01-JAN-2050'\r\n"
					+ "       AND vspk_empcode IN (SELECT empm_empcode\r\n"
					+ "                            FROM   emppaymonth\r\n"
					+ "                            WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "                                   AND empm_paystatus <> 'H'\r\n"
					+ "                                   AND empm_salarytype = 'S')\r\n"
					+ "ORDER  BY vspk_coy, vspk_empcode" ;
		
		} else {
			sqlQuery = "SELECT vspk_coy                                                                                                                                                            AS\r\n"
					+ "       Company,\r\n"
					+ "       vspk_empcode                                                                                                                                                        AS\r\n"
					+ "       Empcode,\r\n"
					+ "       (SELECT eper_fullname\r\n"
					+ "        FROM   emppersonal\r\n"
					+ "        WHERE  eper_empcode = vspk_empcode\r\n"
					+ "               AND SYSDATE BETWEEN eper_effectivefrom AND eper_effectiveupto)                                                                                              AS NAME,\r\n"
					+ "       (SELECT site\r\n"
					+ "        FROM   v_paymastsite\r\n"
					+ "        WHERE  sitecode = (SELECT ejin_worksite\r\n"
					+ "                           FROM   empjobinfo\r\n"
					+ "                           WHERE  ejin_empcode = vspk_empcode\r\n"
					+ "                                  AND SYSDATE BETWEEN ejin_effectivefrom AND ejin_effectiveupto))                                                                          AS\r\n"
					+ "       WorkSite,\r\n"
					+ "       (SELECT ejin_department\r\n"
					+ "        FROM   empjobinfo\r\n"
					+ "        WHERE  ejin_empcode = vspk_empcode\r\n"
					+ "               AND SYSDATE BETWEEN ejin_effectivefrom AND ejin_effectiveupto)                                                                                              AS\r\n"
					+ "       Department,\r\n"
					+ "       (SELECT empm_dayspaid\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = Getpreviousyrmth(Getpreviousyrmth('".concat(paymonthfrom).concat("'))\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       PayDaysPrev2PrevMth,\r\n"
					+ "       (SELECT empm_dayspaid\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = Getpreviousyrmth('".concat(paymonthfrom).concat("')\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       PayDaysPrevMth,\r\n"
					+ "       (SELECT empm_dayspaid\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       PayDaysCurMth,\r\n"
					+ "       (SELECT empm_daysabsent\r\n"
					+ "        FROM   emppaymonth\r\n"
					+ "        WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "               AND empm_empcode = vspk_empcode\r\n"
					+ "               AND empm_salarytype = 'S')                                                                                                                                  AS\r\n"
					+ "       AbsentDays,\r\n"
					+ "		(	case 	when 	vspk_fromdate >= to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm') \r\n")
					+ "							then (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) - extract (day from vspk_fromdate) + 1 \r\n")
					+ "					when 	vspk_fromdate <  to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm') \r\n")
					+ "							then (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) end ) - nvl((	select 	sum(ltrn_noofdays) from(leavetrn) \r\n")
					+ "																														where		(ltrn_empcode = vspk_empcode) \r\n"
					+ "																														and 		ltrn_fromdate >= to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm')  \r\n")
					+ "																														and 		ltrn_todate <= last_day(to_date('01' || '".concat(paymonthfrom).concat("','ddyyyymm'))),0)  						as noofdaysworked, \r\n")
					+ "       vspk_basic + vspk_hra + vspk_soca + vspk_ca + vspk_ta                                                                                                               AS\r\n"
					+ "       RateGross,\r\n"
					+ "       vspk_medical                                                                                                                                                        AS\r\n"
					+ "       RateMedical,\r\n"
					+ "       vspk_attire                                                                                                                                                         AS\r\n"
					+ "       RateAttire,\r\n"
					+ "       vspk_uniform                                                                                                                                                        AS\r\n"
					+ "       RateUniform,\r\n"
					+ "       Round(( ( vspk_basic + vspk_hra + vspk_soca + vspk_ca + vspk_ta ) * (SELECT empm_dayspaid\r\n"
					+ "                                                                            FROM   emppaymonth\r\n"
					+ "                                                                            WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "                                                                                   AND empm_empcode = vspk_empcode\r\n"
					+ "                                                                                   AND empm_salarytype = 'S') / ( Extract(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm'))) ) ), -1) AS Pay,\r\n")
					+ "		'=ROUND((H' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '*K' ||  To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1)  \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) || ',-1)'  as Payformula,     \r\n")
					+ "		'=ROUND((L' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*F' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth(Getpreviousyrmth('".concat(paymonthfrom).concat("')), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((L' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*G' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth('".concat(paymonthfrom).concat("'), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((L' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*H' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) || ',0)' as Medical,\r\n")
					+ "		'=ROUND((M' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*F' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth(Getpreviousyrmth('".concat(paymonthfrom).concat("')), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((M' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*G' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth('".concat(paymonthfrom).concat("'), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((M' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*H' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) || ',0)' as Attire,\r\n")
					+ "		'=ROUND((N' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*F' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth(Getpreviousyrmth('".concat(paymonthfrom).concat("')), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((N' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*G' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date(Getpreviousyrmth('".concat(paymonthfrom).concat("'), 'yyyymm')))) \r\n")
					+ "						|| ',0)+ROUND((N' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| '/12*H' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ')/' || (EXTRACT(DAY FROM Last_day(To_date('".concat(paymonthfrom).concat("', 'yyyymm')))) || ',0)' as Uniform,\r\n")
					+ "		'=SUM(P' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) \r\n"
					+ "						|| ':S' || To_char(row_number() OVER (ORDER BY vspk_coy,vspk_empcode) + 1) || ')' as GrossPayable\r\n"
					+ "FROM   v_empsalarypackage\r\n"
					+ "WHERE  vspk_todate = '01-JAN-2050'\r\n"
					+ "       AND vspk_empcode IN (SELECT empm_empcode\r\n"
					+ "                            FROM   emppaymonth\r\n"
					+ "                            WHERE  empm_paymonth = '".concat(paymonthfrom).concat("'\r\n")
					+ "                                   AND empm_paystatus <> 'H'\r\n"
					+ "                                   AND empm_salarytype = 'S')\r\n"
					+ "ORDER  BY vspk_coy, vspk_empcode" ;
		
		}
		logger.info("sqlQuery :: {}", sqlQuery);
		try {
			File excelFile = ExcelUtils.INSTANCE.export(sqlQuery, dbUrl, dbUsername, dbPassword);

			String fileName = CommonConstraints.INSTANCE.DD_MM_YYYY_HH_MM_SS_FORMATTER.format(LocalDateTime.now()).concat(CommonConstraints.INSTANCE.XLS_EXTENSION);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(new InputStreamResource(new FileInputStream(excelFile)));
		} catch (FileNotFoundException e) {
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error while creating report.").build());
	}

	
	@Override
	public ResponseEntity<?> empwiseMonthlySummaryPT(String coyCode, String deptCodes, String empCodes,
			String salaryTypes, String paymonth, String paymentDate,  String empType ) {
		String sqlQuery = "", sqlQuery1a = "", sqlQuery1b = "" , sqlQueryFull = "" ,hotelPropYN ;
		
		sqlQuery1a = "select * from (\r\n"
				+ "						select 	empl_code, paymonth, employee, gender, age, dept as desg, pr, ab, enc, edcd, sum(earndedamt) Amt\r\n"
				+ "						from 		v_empwisemthsummptnew where 1 = 1 " ;
		
			if(StringUtils.isAllBlank(coyCode)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and ('ALL'  = '" + coyCode +"'  OR co = '"+ coyCode + "') " ;
			}	
			if(StringUtils.isAllBlank(deptCodes) )  {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + deptCodes + ") OR dept in (" + deptCodes + ")) ";
			}
			if(StringUtils.isAllBlank(empCodes)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + empCodes + ") OR empl_code in (" + empCodes + ")) ";
			}	
			if(StringUtils.isAllBlank(salaryTypes)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and (( 'ALL') in (" + salaryTypes + ") OR salarytype in (" + salaryTypes + ")) ";
			}	
			if(StringUtils.isAllBlank(paymonth)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and ('ALL' = '" + paymonth + "'  OR paymonth = '" + paymonth + "') " ;
			}
			// Need to add a condition for hotel property
			hotelPropYN = reportParametersRepository.GetHotelPropYN() ;
			logger.info("hotelPropYN :: {}", hotelPropYN);
			
			if(StringUtils.equals(hotelPropYN, "Y")) {
				if(StringUtils.isAllBlank(empType)) {
					// do nothing as there is nothing to concatenate to main query
				}
				else
				{
					sqlQuery = sqlQuery + " and ejin_emptype = '" + empType + "' " ;
				}
			}

			if(StringUtils.isAllBlank(paymentDate)) {
				// do nothing as there is nothing to concatenate to main query
			}
			else
			{
				sqlQuery = sqlQuery + " and EMPM_PAYDATE = to_date('" + paymentDate.toString() + "','dd/mm/yyyy')" ;
			}	
			
			
			sqlQuery1b = "						group by empl_code,  paymonth, employee, gender, age, dept , pr, ab, enc, edcd\r\n"
					+ "						having	sum(earndedamt) > 0\r\n"
					+ "					) \r\n"
					+ "						pivot( sum(amt)  for edcd in (\r\n"
					+ "																'BASIC' BASIC,\r\n"
					+ "																'HRA' HRA,\r\n"
					+ "																'TA' TA,\r\n"
					+ "																'SOCA' SOCA,\r\n"
					+ "																'CA' CA,\r\n"
					+ "																'GROSS' GROSS,\r\n"
					+ "																'PT' PT,\r\n"
					+ "																'ESIC' ESIC, \r\n"
					+ "																'IT' IT,\r\n"
					+ "																'PF' PF,\r\n"
					+ "																'VPF' VPF,\r\n"
					+ "																'BLOAN' BLOAN,\r\n"
					+ "																'CLOAN' CLOAN,\r\n"
					+ "																'OTHDED' OTHDED,\r\n"
					+ "																'NETPAY' NETPAY,\r\n"
					+ "																'MEDICAL' MEDICAL, \r\n"
					+ "																'ATTIRE' ATTIRE, \r\n"
					+ "																'UNIFORM' UNIFORM, \r\n"
					+ "																'LTA' LTA,\r\n"
					+ "																'GRATUITY' GRATUITY, \r\n"
					+ "																'LEAVEENC' LEAVEENC, \r\n"
					+ "																'BONUS' BONUS,\r\n"
					+ "																'EXGRATIA' EXGRATIA, \r\n"
					+ "																'CONVRIEMB' CONVRIEMB, \r\n"
					+ "																'ENTA' ENTA, \r\n"
					+ "																'NOTPAYRECO' NOTPAYRECO,\r\n"
					+ "																'NOTICEPAY' NOTICEPAY,\r\n"
					+ "																'ADVRECO' ADVRECO \r\n"
					+ "																) \r\n"
					+ "				)\r\n"
					+ "" ;
			
			sqlQueryFull = sqlQuery1a + sqlQuery + sqlQuery1b ;
		try {
			logger.info("sqlQuery :: {}", sqlQueryFull);
			File excelFile = ExcelUtils.INSTANCE.export(sqlQueryFull, dbUrl, dbUsername, dbPassword);

			String fileName = CommonConstraints.INSTANCE.DD_MM_YYYY_HH_MM_SS_FORMATTER.format(LocalDateTime.now()).concat(CommonConstraints.INSTANCE.XLS_EXTENSION);
			return ResponseEntity.ok()
					.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
					.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
					.body(new InputStreamResource(new FileInputStream(excelFile)));
		} catch (FileNotFoundException e) {
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("Error while creating report.").build());
	}
	
	
}
