package com.bestseller.starbuxcoffee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class StarbuxCoffeeApplication {

	public static void main(final String[] args) {
		SpringApplication.run(StarbuxCoffeeApplication.class, args);
	}

//	@Bean
//	public OpenAPI customOpenAPI(//
//			@Value("${application-description}") final String appDesciption,
//			@Value("${application-version}") final String appVersion) {
//		return new OpenAPI().info(new Info().title("sample application API").version(appVersion)
//				.description(appDesciption).termsOfService("http://swagger.io/terms/")
//				.license(new License().name("Apache 2.0").url("http://springdoc.org")));
//	}
	/*
	 * @Bean public GroupedOpenApi userOpenApi() { final String packagesToscan[] = {
	 * "com.bestseller.starbuxcoffee.endpoint" }; return
	 * GroupedOpenApi.builder().group("users").packagesToScan(packagesToscan).build(
	 * ); }
	 */

}
