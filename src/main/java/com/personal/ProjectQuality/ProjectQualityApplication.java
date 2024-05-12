package com.personal.ProjectQuality;

import datahub.shaded.org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.personal.ProjectQuality")
@ComponentScan(basePackages = "com.personal.ProjectQuality")
public class ProjectQualityApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectQualityApplication.class, args);
	}

}
