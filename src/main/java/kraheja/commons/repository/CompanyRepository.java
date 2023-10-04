package kraheja.commons.repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import kraheja.commons.entity.Company;
import kraheja.commons.entity.CompanyCK;

@Repository
public interface CompanyRepository extends JpaRepository<Company, CompanyCK>{

	@Query("SELECT company FROM Company company WHERE trim(company.companyCK.coyCode)=:companyCode AND (company.companyCK.coyClosedate = :closeDate OR company.companyCK.coyClosedate is null)")
	Company findByCompanyCK_CoyCodeAndCompanyCK_CoyClosedate(String companyCode, Date closeDate); 
	
	@Query("SELECT company.companyCK.coyClosedate FROM Company company WHERE trim(company.companyCK.coyCode)=:companyCode")
	LocalDateTime findCloseDateByCompanyCK_CoyCode(String companyCode); 
	
	@Query("SELECT trim(company.companyCK.coyProp) FROM Company company WHERE trim(company.companyCK.coyCode)=:companyCode AND trim(company.coyActive) = 'Y' AND (company.companyCK.coyClosedate = :closeDate OR company.companyCK.coyClosedate is null)")
	String findProprietorByCompanyCK_CoyCodeAndCompanyCK_CoyClosedate(String companyCode, Date closeDate);
	
	List<Company> findByCompanyCK_CoyClosedate(Date closeDate);


	 @Query("SELECT trim(company.companyCK.coyProp) FROM Company company WHERE trim(company.companyCK.coyCode) = :coyCode AND company.companyCK.coyClosedate = to_date('01.01.2050','dd.mm.yyyy')")
	   String findCoyPropByCodeAndClosedate(String coyCode);

}
