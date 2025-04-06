package spring.boot.config;

import java.util.List;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@SecurityScheme(name = "Authorization", description = "Access Token", type = SecuritySchemeType.HTTP, bearerFormat = "JWT", scheme = "bearer")
@Configuration
public class SwaggerConfigurer {
  @Bean
  GroupedOpenApi publicApi() {
    return GroupedOpenApi.builder().group("public").packagesToScan("spring.boot.apis.controller").build();
  }

  @Bean
  OpenAPI myOpenAPI() {
    Server dev = new Server().url("http://localhost:8000").description("Dev");
    Contact contact = new Contact().email("hoalx30@gmail.com").name("Lê Xuân Hòa");
    License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");
    Info info = new Info()
        .title("Spring Boot Authentication and Authorization Service")
        .version("1.0")
        .contact(contact)
        .description("Spring Boot Authentication and Authorization Service by Monolithic Architecture")
        .termsOfService("https://www.spring.com/terms")
        .license(mitLicense);
    return new OpenAPI().info(info).servers(List.of(dev));
  }
}
