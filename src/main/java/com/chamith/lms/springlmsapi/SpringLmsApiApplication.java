package com.chamith.lms.springlmsapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@EnableWebSecurity
@SpringBootApplication
public class SpringLmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringLmsApiApplication.class, args);
	}

}
