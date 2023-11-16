package kraheja.payroll.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import kraheja.payroll.entity.Emppersonal ;
import kraheja.payroll.entity.EmppersonalCK;

@Repository
public interface EmppersonalRepository extends JpaRepository<Emppersonal, EmppersonalCK> {
	List<Emppersonal> findByEmppersonalCK_EperEmpcode(String empcode) ;
	
	Emppersonal findByEmppersonalCK_EperEmpcodeAndEmppersonalCK_EperEffectivefrom(String empcode, LocalDateTime effectivefrom) ; 
}
