package kraheja.payroll.masterdetail.service.impl;

import java.io.File;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.entity.Address;
import kraheja.commons.enums.AdSegment;
import kraheja.commons.enums.AdType;
import kraheja.commons.mappers.pojoentity.AddressMapper;
import kraheja.commons.repository.AddressRepository;
import kraheja.commons.utils.CommonConstraints;
import kraheja.payroll.bean.CoysalarypackageBean;
import kraheja.payroll.bean.EmployeeDetailsResponseBean;
import kraheja.payroll.bean.EmployeeSalDetailsResponseBean;
import kraheja.payroll.bean.EmpsalarypackageEarnDedBean;
import kraheja.payroll.entity.Empassetinfo;
import kraheja.payroll.entity.Empeducation;
import kraheja.payroll.entity.Empexperience;
import kraheja.payroll.entity.Empfamily;
import kraheja.payroll.entity.Empjobinfo;
import kraheja.payroll.entity.Empleaveinfo;
import kraheja.payroll.entity.Emppersonal;
import kraheja.payroll.entity.Empreference;
import kraheja.payroll.entity.Empschemeinfo;
import kraheja.payroll.masterdetail.mappers.EmpassetinfoEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpeducationEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpexperienceEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpfamilyEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpjobinfoEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpleaveinfoEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmppersonalEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpreferenceEntityPojoMapper;
import kraheja.payroll.masterdetail.mappers.EmpschemeinfoEntityPojoMapper;
import kraheja.payroll.masterdetail.service.EmployeeDetailsEntryEditService;
import kraheja.payroll.repository.EmpassetinfoRepository;
import kraheja.payroll.repository.EmpeducationRepository;
import kraheja.payroll.repository.EmpexperienceRepository;
import kraheja.payroll.repository.EmpfamilyRepository;
import kraheja.payroll.repository.EmpjobinfoRepository;
import kraheja.payroll.repository.EmpleaveinfoRepository;
import kraheja.payroll.repository.EmployeeDetailsEntryEditRepository;
import kraheja.payroll.repository.EmppersonalRepository;
import kraheja.payroll.repository.EmpreferenceRepository;
import kraheja.payroll.repository.EmpsalarypackageRepository;
import kraheja.payroll.repository.EmpschemeinfoRepository;

@Service
@Transactional
public class EmployeeDetailsEntryEditServiceImpl implements EmployeeDetailsEntryEditService{

	private static final Logger logger = LoggerFactory.getLogger(EmployeeDetailsEntryEditServiceImpl.class);
	@Autowired
	private EmppersonalRepository emppersonalRepository;
	
	@Autowired
	private EmpeducationRepository empeducationRepository;
	
	@Autowired
	private EmpjobinfoRepository empjobinfoRepository;
	
	@Autowired
	private EmpsalarypackageRepository empsalarypackageRepository;
	
	@Autowired
	private EmpschemeinfoRepository empschemeinfoRepository;
	
	@Autowired
	private EmpleaveinfoRepository empleaveinfoRepository;
	
	@Autowired
	private EmpfamilyRepository empfamilyRepository;
	
	@Autowired
	private EmpexperienceRepository empexperienceRepository;
	
	@Autowired
	private EmpreferenceRepository empreferenceRepository;
	
	@Autowired
	private EmpassetinfoRepository empassetinfoRepository;
	
	@Autowired
	private AddressRepository addressRepository;
	
	@Autowired
	private EntityManager entityManager;
	
	@Autowired
	private EmployeeDetailsEntryEditRepository employeeDetailsEntryEditRepository;
	
	public ResponseEntity<?> fetchEmplDetails(String empcode) throws Exception{
		EmployeeDetailsResponseBean employeeDetailsResponseBean = new EmployeeDetailsResponseBean();
		byte[] empPhoto;
		String empphotopath;
		
		//get emppersonal details
		List<Emppersonal> emppersonallist = emppersonalRepository.findByEmppersonalCK_EperEmpcode(empcode);
		if(CollectionUtils.isNotEmpty(emppersonallist)) {
			employeeDetailsResponseBean.setEmppersonalResponseBean(EmppersonalEntityPojoMapper.fetchEmppersonalEntityPojoMapper.apply(emppersonallist));
		
		//get empeducation details
		List<Empeducation> empeducationlist = empeducationRepository.findByEmpeducationCK_EeduEmpcode(empcode);
		employeeDetailsResponseBean.setEmpeducationResponseBean(EmpeducationEntityPojoMapper.fetchEmpeducationEntityPojoMapper.apply(empeducationlist));
		
		//get empjobinfo details
		List<Empjobinfo> empjobinfolist = empjobinfoRepository.findByEmpjobinfoCK_EjinEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empjobinfolist)) {
			employeeDetailsResponseBean.setEmpjobinfoResponseBean(EmpjobinfoEntityPojoMapper.fetchEmpjobinfoEntityPojoMapper.apply(empjobinfolist));
		}
		
		//get salarypackage details for earnings
		List<Tuple> empsalarypackagelist = empsalarypackageRepository.findByCurrentEarnPackage(empcode);
		if(empsalarypackagelist.size()>0) {
			List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
					empsalarypackagelist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class),
							t.get(4,  BigDecimal.class).doubleValue(),
							t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(7, String.class).trim(),
							t.get(8, String.class).trim(),
							t.get(9, String.class).trim(),
							t.get(10, String.class).trim(),
							t.get(11, String.class).trim(),
							t.get(12, Timestamp.class).toLocalDateTime(),
							t.get(13, String.class).trim(),
							t.get(14, Character.class),
							t.get(15, Character.class)
							);
					}
							).collect(Collectors.toList());
			logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
			employeeDetailsResponseBean.setEmpsalarypackageResponseBean(empsalarypackageEarnDedBeanList);
		}

		//get salarypackage details for deductions
		List<Tuple> empsalarypackagededlist = empsalarypackageRepository.findByCurrentDedPackage(empcode);
		if(empsalarypackagededlist.size()>0) {
			List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
					empsalarypackagededlist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class),
							t.get(4,  BigDecimal.class).doubleValue(),
							t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(7, String.class).trim(),
							t.get(8, String.class).trim(),
							t.get(9, String.class).trim(),
							t.get(10, String.class).trim(),
							t.get(11, String.class).trim(),
							t.get(12, Timestamp.class).toLocalDateTime(),
							t.get(13, String.class).trim(),
							t.get(14, Character.class),
							t.get(15, Character.class)
							);
					}
							).collect(Collectors.toList());
			logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
			employeeDetailsResponseBean.setEmpsalarypackagededResponseBean(empsalarypackageEarnDedBeanList);
		}
		
		
		//get empschemeinfo details
		List<Empschemeinfo> empschemeinfolist = empschemeinfoRepository.findByEmpschemeinfoCK_EschEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empschemeinfolist)) {
			employeeDetailsResponseBean.setEmpschemeinfoResponseBean(EmpschemeinfoEntityPojoMapper.fetchEmpschemeinfoEntityPojoMapper.apply(empschemeinfolist));;
		}
		
		//get empleave details
		List<Empleaveinfo> empleaveinfolist = empleaveinfoRepository.findByEmpleaveinfoCK_ElinEmpcodeOrderByEmpleaveinfoCK_ElinAcyearDesc(empcode);
		if(CollectionUtils.isNotEmpty( empschemeinfolist)) {
			employeeDetailsResponseBean.setEmpleaveinfoResponseBean(EmpleaveinfoEntityPojoMapper.fetchEmpleaveinfoEntityPojoMapper.apply(empleaveinfolist));
		}
		
		//get empfamily details
		List<Empfamily> empfamilylist = empfamilyRepository.findByEmpfamilyCK_EfamEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empfamilylist)) {
			employeeDetailsResponseBean.setEmpfamilyResponseBean(EmpfamilyEntityPojoMapper.fetchEmpfamilyEntityPojoMapper.apply(empfamilylist));
		}
		
		//get empexperience details
		List<Empexperience> empexperiencelist = empexperienceRepository.findByEmpexperienceCK_EexpEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empexperiencelist)) {
			employeeDetailsResponseBean.setEmpexperienceResponseBean(EmpexperienceEntityPojoMapper.fetchEmpexperienceEntityPojoMapper.apply(empexperiencelist));
		}
		
		//get empreference details
		List<Empreference> empreferencelist = empreferenceRepository.findByEmpreferenceCK_ErefEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empreferencelist)) {
			employeeDetailsResponseBean.setEmpreferenceResponseBean(EmpreferenceEntityPojoMapper.fetchEmpreferenceEntityPojoMapper.apply(empreferencelist));
		}
		
//		get empassest details
		List<Empassetinfo> empassetinfolist = empassetinfoRepository.findByEmpassetinfoCK_EassEmpcode(empcode);
		if(CollectionUtils.isNotEmpty( empassetinfolist)) {
			employeeDetailsResponseBean.setEmpassetinfoResponseBean(EmpassetinfoEntityPojoMapper.fetchEmpassetinfoEntityPojoMapper.apply(empassetinfolist));
		}
		
//		get empaddress details
		Address addressmail = addressRepository.findByAddressCK_AdrAdownerAndAddressCK_AdrAdsegmentAndAddressCK_AdrAdtype(empcode, AdSegment.EMPL.toString(), AdType.MAIL.toString());
		if (Objects.nonNull(addressmail)) {
			employeeDetailsResponseBean.setAddressmail(AddressMapper.AddressEntityPojoMapper.fetchAddressEntityPojoMapper.apply(new Object[] {addressmail}));
		}
		Address addressres = addressRepository.findByAddressCK_AdrAdownerAndAddressCK_AdrAdsegmentAndAddressCK_AdrAdtype(empcode, AdSegment.EMPL.toString(), AdType.RES.toString());
		if (Objects.nonNull(addressres)) {
			employeeDetailsResponseBean.setAddressres(AddressMapper.AddressEntityPojoMapper.fetchAddressEntityPojoMapper.apply(new Object[] {addressres}));
		}
		
		   File empfile = new File(CommonConstraints.INSTANCE.EMPPHOTOPATH + empcode.trim() + ".jpg");
		   if (empfile.exists() && empfile.canRead()) {
			   empphotopath = CommonConstraints.INSTANCE.EMPPHOTOPATH + empcode.trim() + ".jpg";
		    } else {
		    	empphotopath = CommonConstraints.INSTANCE.EMPPHOTOPATH + "employee.jpg";
		    } 

		empPhoto = ImageToByteArray.imagetoblob(empphotopath);
		employeeDetailsResponseBean.setEmpPhoto(empPhoto);
		
		
////		String SQuery = "select (select efor_formula from emppayformula where efor_coy = 'UNIQ' and efor_emptype = 'S' and efor_jobtype = 'P' and efor_earndedcode = 'MTHLYGROSS') as gross from v_empsalarypackage where vspk_empcode = 'WD009' and vspk_todate = '01-JAN-2050' ";
//		String SQuery = "select ('Select ' || efor_formula || ' from v_empsalarypackage where vspk_empcode = ' || '''WD009''' || ' and vspk_todate = ' || '''01-JAN-2050''') as Q from emppayformula where efor_coy = 'UNIQ' and efor_emptype = 'S' and efor_jobtype = 'P' and efor_earndedcode = 'MTHLYGROSS'";
//		Query q = entityManager.createNativeQuery(SQuery);
//		List<String> resultSet = q.getResultList();
//		
//		logger.info("MonthGross :: {}", resultSet);
//		System.out.println("Query is "+ resultSet);
//		String SMQuery = "";
//		for(String FQuery:resultSet) 
//			SMQuery = FQuery;
//		
//		Query newq = entityManager.createNativeQuery(SMQuery);
//		List<Object[]> newresultSet = newq.getResultList();
//		logger.info("MonthGrossAmount :: {}", newresultSet);
		
//		logger.info("Coy :: {}", empjobinfolist.get(0).getEjinCompany());
//		logger.info("Emptype :: {}",empjobinfolist.get(0).getEjinEmptype());
//		logger.info("Jobtype :: {}",empjobinfolist.get(0).getEjinJobtype());
//		
//		String MonthlyGrossQuery = employeeDetailsEntryEditRepository.GetMonthGrossQuery(empcode,empjobinfolist.get(0).getEjinCompany(),empjobinfolist.get(0).getEjinEmptype(),empjobinfolist.get(0).getEjinJobtype());
//		logger.info("MonthGrossQuery :: {}", MonthlyGrossQuery);
//		
//		Query newq = entityManager.createNativeQuery(MonthlyGrossQuery);
//		List<Object[]> newresultSet = newq.getResultList();
//		logger.info("MonthGrossAmount :: {}", newresultSet);
		
//		Double MonthlyGross = employeeDetailsEntryEditRepository.GetMonthGross(MonthlyGrossQuery);
//		logger.info("MonthGrossAmount :: {}", MonthlyGross);
		
		if(CollectionUtils.isNotEmpty(emppersonallist)) {
		Tuple empjobinfodetforpayformula = employeeDetailsEntryEditRepository.GetEmployeeJobinfoForFormula(empcode);
		if(Objects.nonNull(empjobinfodetforpayformula)) {
			String coy = empjobinfodetforpayformula.get(0, String.class).trim();
			char emptype = empjobinfodetforpayformula.get(1, Character.class);
			char jobtype = empjobinfodetforpayformula.get(2, Character.class);
			String formulaMthlyGross = employeeDetailsEntryEditRepository.GetFormula(coy, emptype, jobtype, "MTHLYGROSS");
			
			Query queryMthlyGross = entityManager.createNativeQuery(formulaMthlyGross);
			queryMthlyGross.setParameter("empcode", empcode.trim());
			List mthlyGrossresultSet = queryMthlyGross.getResultList();
			employeeDetailsResponseBean.setMTHLYGROSS(mthlyGrossresultSet);
			
			
			String formulaCTC = employeeDetailsEntryEditRepository.GetFormula(coy, emptype, jobtype, "CTC");
			Query queryCTC = entityManager.createNativeQuery(formulaCTC);
			queryCTC.setParameter("empcode", empcode.trim());
			List CTCresultSet = queryCTC.getResultList();
			employeeDetailsResponseBean.setCTC(CTCresultSet);
		}
		}
		} else {
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No record found for your Employee Code").build());
		}
		if(Objects.nonNull(employeeDetailsResponseBean)) {
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(employeeDetailsResponseBean).build());	
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No record found for your Employee Code").build());
	}

	public ResponseEntity<?> fetchAllSalaryPackage(String empcode,Character currentAll){
		EmployeeSalDetailsResponseBean employeeSalDetailsResponseBean = new EmployeeSalDetailsResponseBean();
		if(currentAll == 'A') {
		//get salarypackage details for earnings
		List<Tuple> empsalarypackagelist = empsalarypackageRepository.findByAllEarnPackage(empcode);
		if(empsalarypackagelist.size()>0) {
			List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
					empsalarypackagelist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class),
							t.get(4,  BigDecimal.class).doubleValue(),
							t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(7, String.class).trim(),
							t.get(8, String.class).trim(),
							t.get(9, String.class).trim(),
							t.get(10, String.class).trim(),
							t.get(11, String.class).trim(),
							t.get(12, Timestamp.class).toLocalDateTime(),
							t.get(13, String.class).trim(),
							t.get(14, Character.class),
							t.get(15, Character.class)
							);
					}
							).collect(Collectors.toList());
			logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
			employeeSalDetailsResponseBean.setEmpsalarypackageResponseBean(empsalarypackageEarnDedBeanList);
		}

		//get salarypackage details for deductions
		List<Tuple> empsalarypackagededlist = empsalarypackageRepository.findByAllDedPackage(empcode);
		if(empsalarypackagededlist.size()>0) {
			List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
					empsalarypackagededlist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class),
							t.get(4,  BigDecimal.class).doubleValue(),
							t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
							t.get(7, String.class).trim(),
							t.get(8, String.class).trim(),
							t.get(9, String.class).trim(),
							t.get(10, String.class).trim(),
							t.get(11, String.class).trim(),
							t.get(12, Timestamp.class).toLocalDateTime(),
							t.get(13, String.class).trim(),
							t.get(14, Character.class),
							t.get(15, Character.class)
							);
					}
							).collect(Collectors.toList());
			logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
			employeeSalDetailsResponseBean.setEmpsalarypackagededResponseBean(empsalarypackageEarnDedBeanList);
		}
		} else {
			//get salarypackage details for earnings
			List<Tuple> empsalarypackagelist = empsalarypackageRepository.findByCurrentEarnPackage(empcode);
			if(empsalarypackagelist.size()>0) {
				List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
						empsalarypackagelist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
								t.get(0, String.class).trim(),
								t.get(1, String.class).trim(),
								t.get(2, Character.class),
								t.get(3, Character.class),
								t.get(4,  BigDecimal.class).doubleValue(),
								t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
								t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
								t.get(7, String.class).trim(),
								t.get(8, String.class).trim(),
								t.get(9, String.class).trim(),
								t.get(10, String.class).trim(),
								t.get(11, String.class).trim(),
								t.get(12, Timestamp.class).toLocalDateTime(),
								t.get(13, String.class).trim(),
								t.get(14, Character.class),
								t.get(15, Character.class)
								);
						}
								).collect(Collectors.toList());
				logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
				employeeSalDetailsResponseBean.setEmpsalarypackageResponseBean(empsalarypackageEarnDedBeanList);
			}

			//get salarypackage details for deductions
			List<Tuple> empsalarypackagededlist = empsalarypackageRepository.findByCurrentDedPackage(empcode);
			if(empsalarypackagededlist.size()>0) {
				List<EmpsalarypackageEarnDedBean> empsalarypackageEarnDedBeanList = 
						empsalarypackagededlist.stream().map(t -> {return new EmpsalarypackageEarnDedBean(
								t.get(0, String.class).trim(),
								t.get(1, String.class).trim(),
								t.get(2, Character.class),
								t.get(3, Character.class),
								t.get(4,  BigDecimal.class).doubleValue(),
								t.get(5, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
								t.get(6, Timestamp.class).toLocalDateTime().toLocalDate().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER).toString(),
								t.get(7, String.class).trim(),
								t.get(8, String.class).trim(),
								t.get(9, String.class).trim(),
								t.get(10, String.class).trim(),
								t.get(11, String.class).trim(),
								t.get(12, Timestamp.class).toLocalDateTime(),
								t.get(13, String.class).trim(),
								t.get(14, Character.class),
								t.get(15, Character.class)
								);
						}
								).collect(Collectors.toList());
				logger.info("empsalarypackage_Kalpana :: {}", empsalarypackageEarnDedBeanList);
				employeeSalDetailsResponseBean.setEmpsalarypackagededResponseBean(empsalarypackageEarnDedBeanList);
			}
		}

		if(Objects.nonNull(employeeSalDetailsResponseBean)) {
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(employeeSalDetailsResponseBean).build());	
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Salary Package found for your Employee Code").build());

	}


	public ResponseEntity<?> fetchCompanySalPackage(String coycode,String closeDate){
		List<Tuple> coysalarypackagelist = employeeDetailsEntryEditRepository.fetchCompanySalPackage(coycode,CommonConstraints.INSTANCE.closeDate);
		if (coysalarypackagelist.size()>0) {
			List<CoysalarypackageBean> coysalarypackageBean =
					coysalarypackagelist.stream().map(t -> {return new CoysalarypackageBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class)
							);
					}
							).collect(Collectors.toList());
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(coysalarypackageBean).build());
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Salary Package found for your Company").build());
	}
	
	public ResponseEntity<?> fetchCompanySalDedPackage(String coycode,String closeDate){
		List<Tuple> coysalarypackagelist = employeeDetailsEntryEditRepository.fetchCompanySalDedPackage(coycode,CommonConstraints.INSTANCE.closeDate);
		if (coysalarypackagelist.size()>0) {
			List<CoysalarypackageBean> coysalarypackageBean =
					coysalarypackagelist.stream().map(t -> {return new CoysalarypackageBean(
							t.get(0, String.class).trim(),
							t.get(1, String.class).trim(),
							t.get(2, Character.class),
							t.get(3, Character.class)
							);
					}
							).collect(Collectors.toList());
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(coysalarypackageBean).build());
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Salary Deduction Package found for your Company").build());
	}
}
