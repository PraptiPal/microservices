package com.icress.cards;

import com.icress.cards.dto.CardsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableDiscoveryClient
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
@OpenAPIDefinition(
		info = @Info(
				title = "Cards microservice REST API Documentation",
				description = "AliBank Cards microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "MD Ali",
						email = "md.ali@icress.com",
						url = "http://www.icress.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "http://www.icress.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description = "Ali Bank Cards microservice REST API Documentation",
				url = "http://www.alibank.com/swagger-ui.html"
		)
)
@EnableConfigurationProperties(value = {CardsContactInfoDto.class})
public class CardsApplication {

	public static void main(String[] args) {
		SpringApplication.run(CardsApplication.class, args);
	}

}
