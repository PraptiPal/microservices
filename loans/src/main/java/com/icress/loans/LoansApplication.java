package com.icress.loans;

import com.icress.loans.dto.LoansContactInfoDto;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Loans microservice REST API Documentation",
				description = "AliBank Loans microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Md Ali",
						email = "md.ali@icress.com",
						url = "http://www.icress.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.icress.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "AliBank Loans microservice REST API Documentation",
				url = "http://www.icress.com/swagger-ui.html"
		)
)
@EnableConfigurationProperties(value = {LoansContactInfoDto.class})
public class LoansApplication {

	public static void main(String[] args) {
		SpringApplication.run(LoansApplication.class, args);
	}

}
