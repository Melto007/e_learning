package com.Elearning.eLearning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ELearningApplication {

	public static void main(String[] args) {
		SpringApplication.run(ELearningApplication.class, args);
	}

}
