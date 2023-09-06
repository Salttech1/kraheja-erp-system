package kraheja;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class KrahejaApplication {

	public static void main(String[] args) {
		SpringApplication.run(KrahejaApplication.class, args);
	}
}
