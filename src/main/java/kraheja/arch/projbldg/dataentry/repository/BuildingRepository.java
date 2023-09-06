package kraheja.arch.projbldg.dataentry.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.arch.projbldg.dataentry.entity.Building ;
import kraheja.arch.projbldg.dataentry.entity.BuildingCK;
@Repository
public interface BuildingRepository extends JpaRepository<Building, BuildingCK> {

	 
	Building findByBuildingCK_BldgCode(String code) ; 
	
	List<Building> findByBldgClosedateIsNullOrBldgClosedate(LocalDate closeDate) ; 
	
	@Query("select e.projCompany  from Project e WHERE trim(e.projectCK.projCode) = :code")
	String findProjectCompanyByCode(String code) ; //NS 16.03.2023 

}