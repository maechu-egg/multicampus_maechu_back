package com.multipjt.multi_pjt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MultiPjtApplication {

	public static void main(String[] args) {
		SpringApplication.run(MultiPjtApplication.class, args);
	}
}
