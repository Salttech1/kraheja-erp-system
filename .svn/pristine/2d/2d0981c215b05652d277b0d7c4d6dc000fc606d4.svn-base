package kraheja.sales.bookings.dataentry.service.serviceimpl;

import java.lang.invoke.MethodHandles;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kraheja.commons.bean.response.ServiceResponseBean;
import kraheja.sales.bookings.dataentry.service.FlatsService;
import kraheja.sales.entity.Flats;
import kraheja.sales.repository.FlatsRepository;

@Service
@Transactional
public class FlatsServiceImpl implements FlatsService {

	private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
	
	@Autowired 
	private FlatsRepository flatsRepository;
	@Override
	public ResponseEntity<?> deleteFlatByBldgCodeAndWingAndFlatnum(String bldgCode, String wing, String flatnum) {
		// TODO Auto-generated method stub
		Flats existingFlat = this.flatsRepository.findByFlatsCK_FlatBldgcodeAndFlatsCK_FlatWingAndFlatsCK_FlatFlatnum(bldgCode, "  ", flatnum);
		LOGGER.info("Flat Entity :: {}" , existingFlat);
		if(Objects.nonNull(existingFlat))
		{
			this.flatsRepository.delete(existingFlat);
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.TRUE).message("Flat Deleted Successfully!").build());	
		}
			return ResponseEntity.ok(ServiceResponseBean.builder().status(Boolean.FALSE).message("No Flat Found.").build());
	}

}
