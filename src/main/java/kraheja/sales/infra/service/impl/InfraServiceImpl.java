package kraheja.sales.infra.service.impl;

import java.lang.invoke.MethodHandles;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.Tuple;
import javax.transaction.Transactional;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import kraheja.arch.projbldg.dataentry.entity.Building;
import kraheja.arch.projbldg.dataentry.repository.BuildingRepository;
import kraheja.commons.bean.AccountingBean;
import kraheja.commons.bean.ActrandBean;
import kraheja.commons.bean.ActranhBean;
import kraheja.commons.bean.request.PartyAddressRequestBean;
import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.commons.entity.Actrand;
import kraheja.commons.entity.Actranh;
import kraheja.commons.entity.Address;
import kraheja.commons.entity.Company;
import kraheja.commons.entity.Party;
import kraheja.commons.enums.PartyType;
import kraheja.commons.enums.TranTypeEnum;
import kraheja.commons.filter.GenericAuditContextHolder;
import kraheja.commons.mappers.entityentity.ActrandxEntityEntityMapper;
import kraheja.commons.mappers.entityentity.ActranhxEntityEntityMapper;
import kraheja.commons.mappers.pojoentity.AddPojoEntityMapper;
import kraheja.commons.mappers.pojoentity.AddressMapper;
import kraheja.commons.mappers.pojoentity.PartyMapper;
import kraheja.commons.mappers.pojoentity.UpdatePojoEntityMapper;
import kraheja.commons.repository.ActrandRepository;
import kraheja.commons.repository.ActrandxRepository;
import kraheja.commons.repository.ActranhRepository;
import kraheja.commons.repository.ActranhxRepository;
import kraheja.commons.repository.AddressRepository;
import kraheja.commons.repository.CompanyRepository;
import kraheja.commons.repository.EntityRepository;
import kraheja.commons.repository.PartyRepository;
import kraheja.commons.utils.CommonConstraints;
import kraheja.commons.utils.CommonUtils;
import kraheja.commons.utils.GenericAccountingLogic;
import kraheja.commons.utils.GenericCounterIncrementLogicUtil;
import kraheja.commons.utils.ValueContainer;
import kraheja.sales.infra.service.InfraService;
import kraheja.sales.bean.request.InfraDefaultersListRequestBean;

@Service
@Transactional
public class InfraServiceImpl implements InfraService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());


	@Autowired
	private  CompanyRepository companyRepository;

	@Autowired
	private  AddressRepository addressRepository;

	@Autowired
	private  ActrandxRepository actrandxRepository;

	@Autowired
	private  ActranhxRepository actranhxRepository;

	@Autowired
	private  PartyRepository partyRepository;

	@PersistenceContext
	private  EntityManager entityManager;

	@Autowired
	private  ActranhRepository actranhRepository;

	@Autowired
	private  ActrandRepository actrandRepository;

	@Autowired
	private  BuildingRepository buildingRepository;

	@Autowired
	private EntityRepository entityRepository;

	ObjectMapper objectMapper = new ObjectMapper(); 

	@Override
	public ResponseEntity<?> addIntoInfraDefaultersListTempTable(InfraDefaultersListRequestBean infraDefaultersListRequestBean) {
		String sessionId = GenericCounterIncrementLogicUtil.generateTranNo("#SESS", "#SESS");

		String coyyyymm = infraDefaultersListRequestBean.getCutOffDate().substring(6,infraDefaultersListRequestBean.getCutOffDate().length()).concat(infraDefaultersListRequestBean.getCutOffDate().substring(3,5));

		

		if(Objects.isNull(infraDefaultersListRequestBean.getCutOffDate()) ){
			infraDefaultersListRequestBean.setCutOffDate(LocalDate.now().format(CommonConstraints.INSTANCE.DDMMYYYY_FORMATTER));
		}
		Query addIntoTempTableQuery = this.entityManager.createNativeQuery("INSERT INTO\r\n"
				+ "SAINRP05\r\n"
				+ "SELECT \r\n"
				+ "infr_ownerid,\r\n"
				+ "infr_bldgcode,\r\n"
				+ "infr_wing,\r\n"
				+ "infr_flatnum,\r\n"
				+ "infr_month,\r\n"
				+ "'' as recno,\r\n"
				+ " (select fown_city from flatowner where fown_ownerid=infr_ownerid and fown_ownertype='0') as city,\r\n"
				+ " (select fown_township from flatowner where fown_ownerid=infr_ownerid and fown_ownertype='0') as township,\r\n"
				+ " nvl((select case when infr_chargecode = :chargecode THEN nvl(OUTR_INFRRATE,0) ELSE nvl(OUTR_AUXIRATE,0) END from OUTRATE where OUTR_BLDGCODE =infr_bldgcode and OUTR_WING =infr_wing AND outr_flatnum = infr_flatnum and :cutoffyyyymm between outr_startdate and outr_enddate and outr_billtype = 'N'),0) as monrate,\r\n"
+ "(billamt - paidamt) AS billamt,\r\n"
+ "totecdamt,\r\n"
+ "totalbilledamt,\r\n"
+ "(select fown_title from flatowner where fown_ownerid=infr_ownerid and fown_ownertype='0') as title,\r\n"
+ "(select replace(replace(fown_name,',', ' '),'''', '`') from flatowner where fown_ownerid=infr_ownerid and fown_ownertype='0') as name1,\r\n"
+ "(select replace(replace(max(adr_phoneres),',', ' '),'''', '`') from address where adr_adowner= infr_ownerid and adr_adsegment='PARTY' and adr_adtype='PMT') as phoneres,\r\n"
+ "(select replace(replace(max(adr_phoneoff),',', ' '),'''', '`') from address where adr_adowner= infr_ownerid and adr_adsegment='PARTY' and adr_adtype='PMT') as phoneoff,\r\n"
+ "' ' as lastpaid,\r\n"
+ "0 as osage,\r\n"
+ "0 as totrec,\r\n"
+ "tot_int,\r\n"
+ ":sessionId as sesid\r\n"
+ "from\r\n"
+ "(\r\n"
+ "select \r\n"
+ "(select fown_custtype from flatowner where fown_ownerid= infr_ownerid  and fown_ownertype='0') AS custtype, \r\n"
+ "infr_bldgcode,\r\n"
+ "infr_wing,\r\n"
+ "infr_flatnum,\r\n"
+ "infr_ownerid,\r\n"
+ "infr_chargecode,\r\n"
+ "infr_month,\r\n"
+ "infr_billdate,\r\n"
+ "(nvl(infr_interest,0) +nvl(infr_intarrears,0)) as tot_int, \r\n"
+ "nvl(infr_billamt,0) + nvl(infr_Arrears,0) + nvl(infr_admincharges,0) + nvl(infr_servtax,0) + nvl(infr_swachhcess,0) + nvl(infr_krishicess,0) + nvl(infr_cgst,0) + nvl(infr_sgst,0) + nvl(infr_igst,0) as billamt,\r\n"
+ "(select sum(nvl(infr_billamt,0) + nvl(infr_admincharges,0) + nvl(infr_servtax,0) + nvl(infr_swachhcess,0)  + nvl(infr_krishicess,0) + nvl(infr_cgst,0) + nvl(infr_sgst,0)  + nvl(infr_igst,0)) \r\n"
+ "     from infrbill b\r\n"
+ "     where b.infr_ownerid= a.infr_ownerid\r\n"
+ "     and b.infr_billdate <= a.infr_billdate\r\n"
+ "     and infr_billtype = 'N' \r\n"
+ "	  and infr_chargecode IN (:chargecode) ) as totalbilledamt,\r\n"
+ "nvl((select sum(nvl(inf_amtpaid,0) + nvl(inf_servtax,0) + nvl(inf_swachhcess,0) + nvl(inf_krishicess,0) +  nvl(inf_admincharges,0) + nvl(inf_cgst,0) + nvl(inf_sgst,0) + nvl(inf_igst,0)) \r\n"
+ "from outinfra \r\n"
+ "where inf_ownerid=a.infr_ownerid and inf_recdate >= a.infr_billdate and inf_recdate <= :cutoffdt and inf_cancelledyn='N' and inf_rectype = 'N' \r\n"
+ "and inf_chargecode IN (:chargecode) ),0) as paidamt,\r\n"
+ "(select sum(nvl(inf_amtpaid,0) + nvl(inf_servtax,0) + nvl(inf_swachhcess,0) +  nvl(inf_krishicess,0) +  nvl(inf_admincharges,0) +  nvl(inf_cgst,0) +  nvl(inf_sgst,0) +  nvl(inf_igst,0)) \r\n"
+ " from outinfra  where inf_ownerid=a.infr_ownerid\r\n"
+ " and inf_recdate <= :cutoffdt\r\n"
+ " and inf_cancelledyn='N' and inf_rectype = 'N'  \r\n"
+ "and inf_chargecode IN (:chargecode) ) as totecdamt\r\n"
+ "from infrbill a\r\n"
+ "where \r\n"
+ "a.infr_ownerid || a.infr_billdate in \r\n"
+ "(select infr_ownerid || maxbilldate from\r\n"
+ "(select distinct \r\n"
+ "infr_bldgcode,\r\n"
+ "infr_ownerid,\r\n"
+ "max(infr_month) as infr_month,\r\n"
+ "max(infr_billdate) as maxbilldate \r\n"
+ "from infrbill \r\n"
+ "where \r\n"
+ " ('ALL' IN (".concat(String.join(",", infraDefaultersListRequestBean.getBldgCode().stream().map(name -> ("'" + name + "'")).collect(Collectors.toList()))).concat(") OR infr_bldgcode in (".concat(String.join(",", infraDefaultersListRequestBean.getBldgCode().stream().map(name -> ("'" + name + "'")).collect(Collectors.toList()))).concat(" )) AND \r\n"))
+ "infr_billdate <= :cutoffdt and \r\n"
+ "infr_billtype = 'N' \r\n"
+ "and infr_chargecode IN (:chargecode)\r\n"
+ "group by infr_bldgcode,infr_ownerid))\r\n"
+ ")\r\n"
+ "WHERE \r\n"
+ "('ALL' IN (:custtype) OR custtype =:custtype)  \r\n"
				);
		addIntoTempTableQuery.setParameter("sessionId", sessionId);
		addIntoTempTableQuery.setParameter("chargecode", infraDefaultersListRequestBean.getChargeCode());
		addIntoTempTableQuery.setParameter("cutoffyyyymm", coyyyymm);
		addIntoTempTableQuery.setParameter("cutoffdt", infraDefaultersListRequestBean.getCutOffDate());
		addIntoTempTableQuery.setParameter("custtype", infraDefaultersListRequestBean.getCustType());
//		addIntoTempTableQuery.setParameter("userId", GenericAuditContextHolder.getContext().getUserid());
//		addIntoTempTableQuery.setParameter("userId", "YVICKY");
//		addIntoTempTableQuery.setParameter("today", LocalDateTime.now());

		LOGGER.info("QUERY :: {}", addIntoTempTableQuery);
		Integer rowCount = addIntoTempTableQuery.executeUpdate();

		if(rowCount == 0) {
			this.entityRepository.updateIncrementCounter("#SESS", "#SESS", Double.valueOf(sessionId));
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).data(sessionId).message("Added successfully").build());
	}


	@Override
	public ResponseEntity<?> deleteInfraDefaultersListFromSessionId(Integer sessionId) {
		Query deleteBySessIdTempTableQuery = this.entityManager.createNativeQuery("delete from sainrp05 where sum_sesid = :sessionId");
		deleteBySessIdTempTableQuery.setParameter("sessionId", sessionId);
		deleteBySessIdTempTableQuery.executeUpdate();

		return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).build());
	}
}
