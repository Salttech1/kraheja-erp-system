package kraheja.commons.utils;

import java.lang.invoke.MethodHandles;
import java.math.BigInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import kraheja.commons.repository.EntityRepository;

@Component
public class GenericCounterIncrementLogicUtil {

	private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

	private static EntityRepository entityRepository;

	@Autowired
	public void setEntityRepository(EntityRepository entityRepository) {
		GenericCounterIncrementLogicUtil.entityRepository = entityRepository;
	}

	public static String generateTranNo(String entClass, String entId) {
		String entity = entityRepository.fetchByEntityCk_EntClassAndEntityCk_EntId(entClass, entId);
		logger.info("DB Entity :: {}", entity);
		
		// 1 index e.entNum1,
		// 2 e.entityCk.entChar1,
		// 3 e.entityCk.entChar2 
		
		String[] entityArray = entity.split(CommonConstraints.INSTANCE.COMMA_STRING);
		Integer lastCounterValue =  Double.valueOf(entityArray[0]).intValue();
		String starFormatTemplate = null;
		String checkSuccessiveSeries = null;
		if(!entityArray[1].equals("null")) {
			starFormatTemplate = entityArray[1].trim();
			Integer indexOfStar = starFormatTemplate.indexOf(CommonConstraints.INSTANCE.ASTERISK_STRING);

			if(!entityArray[2].equals("null"))
				checkSuccessiveSeries = entityArray[2];

			if(indexOfStar >= 0) {
				if(checkSuccessiveSeries == "Y")
					return lastCounterValue.toString();
				else {
					String strWithoutStar = starFormatTemplate.replace(CommonConstraints.INSTANCE.ASTERISK_STRING, CommonConstraints.INSTANCE.BLANK_STRING);
					String result=(starFormatTemplate.substring(lastCounterValue.toString().length(),starFormatTemplate.length())+lastCounterValue.toString()).replace(CommonConstraints.INSTANCE.ASTERISK_STRING, BigInteger.ZERO.toString());
					entityRepository.updateIncrementCounter(entClass, entId, lastCounterValue.doubleValue() + 1);
					return strWithoutStar.concat(result.substring(strWithoutStar.length()));
				}
			}
		}else {
			entityRepository.updateIncrementCounter(entClass, entId, lastCounterValue.doubleValue() + 1);
			return lastCounterValue.toString();
		}
		return null;
	}
	
	public static String generateTranNoWithSite(String entClass, String entId, String site) {
		String entity = entityRepository.fetchByEntityCk_EntClassAndEntityCk_EntIdAndEntityCk_EntChar1(entClass, entId, site);
		logger.info("DB Entity :: {}", entity);
		
		String[] entityArray = entity.split(CommonConstraints.INSTANCE.COMMA_STRING);
		Integer lastCounterValue =  Double.valueOf(entityArray[0]).intValue();
		String starFormatTemplate = null;
		String checkSuccessiveSeries = null;
		if(!entityArray[1].equals("null")) {
			starFormatTemplate = entityArray[1].trim();
			Integer indexOfStar = starFormatTemplate.indexOf(CommonConstraints.INSTANCE.ASTERISK_STRING);

			if(!entityArray[2].equals("null"))
				checkSuccessiveSeries = entityArray[2];

			if(indexOfStar >= 0) {
				if(checkSuccessiveSeries == "Y")
					return lastCounterValue.toString();
				else {
					String strWithoutStar = starFormatTemplate.replace(CommonConstraints.INSTANCE.ASTERISK_STRING, CommonConstraints.INSTANCE.BLANK_STRING);
					String result=(starFormatTemplate.substring(lastCounterValue.toString().length(), starFormatTemplate.length())+lastCounterValue.toString()).replace(CommonConstraints.INSTANCE.ASTERISK_STRING, BigInteger.ZERO.toString());
					entityRepository.updateIncrementCounterWithSite(entClass, entId, lastCounterValue.doubleValue() + 1, site);
					return strWithoutStar.concat(result.substring(strWithoutStar.length()));
				}
			}
		}else {
			entityRepository.updateIncrementCounterWithSite(entClass, entId, lastCounterValue.doubleValue() + 1, site);
			return lastCounterValue.toString();
		}
		return null;
	}
}