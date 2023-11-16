package kraheja.sales.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;

import kraheja.sales.entity.OutBill;

public interface OutbillRepository extends JpaRepository<OutBill, Integer> {
	@Procedure(name = "OutBill.inserttempowner")
	static int inserttempowner() {
		return 0;
	}
}
