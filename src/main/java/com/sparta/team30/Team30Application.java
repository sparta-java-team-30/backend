package com.sparta.team30;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@SpringBootApplication
@EnableJpaAuditing
public class Team30Application {

	public static void main(String[] args) {
		SpringApplication.run(Team30Application.class, args);
	}

}
