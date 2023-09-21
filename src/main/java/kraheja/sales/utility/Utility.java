package kraheja.sales.utility;

import java.time.LocalDateTime;

import kraheja.commons.entity.Inchq;
import kraheja.commons.entity.InchqCK;
import kraheja.sales.infra.payload.request.IncheqRequest;

public class Utility {

	public static Inchq incheqMapper(IncheqRequest request) {
		InchqCK ck = InchqCK.builder()
				.inchqNum(request.getInchqNum())
				.inchqBank(request.getInchqBank())
				.inchqTranser(request.getInchqTranser())
				.build();
		return Inchq.builder()
				.inchqCk(ck)
				.inchqAmount(request.getInchqAmount())
				.inchqSite(request.getInchqSite())
				.inchqUserid(request.getInchqUserid())
				.inchqToday(LocalDateTime.now())
				.inchqDate(LocalDateTime.parse(request.getInchqDate()))
				.inchqSlipnum(request.getInchqSlipnum())
				.inchqOutstat(request.getInchqOutstat())
				.inchqProprietor(request.getInchqProprietor())
				.inchqCoybank(request.getInchqCoy())
				.inchqPartycode(request.getInchqPartycode())
				.inchqFund(request.getInchqFund())
				.inchqActype(request.getInchqActype())
				.build();
	}

}
