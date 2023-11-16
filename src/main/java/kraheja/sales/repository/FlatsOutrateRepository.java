package kraheja.sales.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import kraheja.sales.entity.Flats;
import kraheja.sales.entity.FlatsCK;

@Repository
public interface FlatsOutrateRepository extends JpaRepository<Flats, FlatsCK> {

    @Query(value = "select distinct flats0_.flat_ownerid as col_0_0_ from flats flats0_ cross join outrate outrate1_ where flats0_.flat_bldgcode=outrate1_.outr_bldgcode and flats0_.flat_wing=outrate1_.outr_wing and flats0_.flat_flatnum=outrate1_.outr_flatnum and flats0_.flat_soldyn='Y' and (substr(flats0_.flat_flatnum, 1, 1) in ('F' , 'H' , 'U')) and (? between outrate1_.outr_startdate and outrate1_.outr_enddate or ? between outrate1_.outr_startdate and outrate1_.outr_enddate) and (trim(flats0_.flat_ownerid) between ? and ?) and outrate1_.outr_billtype=? order by flats0_.flat_ownerid", nativeQuery = true)
	List<String> findDistinctFlatOwnerIds(String date, String qDate, String ownerStart, String ownerEnd, String billType);
}
