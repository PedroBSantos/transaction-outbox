package br.com.pedro;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class TransactionOutboxApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionOutboxApplication.class, args);
	}
}
