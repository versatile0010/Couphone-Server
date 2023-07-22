package com.example.couphoneserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class CouphoneServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(CouphoneServerApplication.class, args);
	}

}
