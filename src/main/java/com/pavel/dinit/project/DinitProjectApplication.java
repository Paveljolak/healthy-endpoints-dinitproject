package com.pavel.dinit.project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DinitProjectApplication {
	public static void main(String[] args) {
		SpringApplication.run(DinitProjectApplication.class, args);
	}
}
