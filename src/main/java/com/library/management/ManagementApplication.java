package com.library.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

//import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication

//Annotations for swagger
//@EnableSwagger2
//@EnableWebMvc

public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
	}
	
	// Method for creating API end point for swagger
//	@Bean
//	 public Docket productApi() {
//	      return new Docket(DocumentationType.SWAGGER_2).select()
//	         .apis(RequestHandlerSelectors.basePackage("com.library.management")).build();
//	 }
}
