/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances;

import java.util.List;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Die Startklasse für diesen Server.
 * @author Frank W. Rahn
 */
@SpringBootApplication
@EnableSpringDataWebSupport
public class SecuritiesManagementApplication extends WebMvcConfigurerAdapter implements HealthIndicator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecuritiesManagementApplication.class, args);
	}

	/**
	 * {@inheritDoc}
	 * @see WebMvcConfigurerAdapter#addArgumentResolvers(List)
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(0, 10));

		argumentResolvers.add(resolver);

		super.addArgumentResolvers(argumentResolvers);
	}

	/**
	 * {@inheritDoc}
	 * @see HealthIndicator#health()
	 */
	@Override
	public Health health() {
		return Health.up().withDetail("test", "UP").build();
	}

}