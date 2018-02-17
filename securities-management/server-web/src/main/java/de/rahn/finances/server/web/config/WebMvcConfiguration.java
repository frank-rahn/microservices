/*
 * Copyright 2011-2016 Frank W. Rahn and the project authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.finances.server.web.config;

import java.util.List;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import de.rahn.finances.server.web.ui.EntriesController;
import de.rahn.finances.server.web.ui.SecuritiesController;
import de.rahn.finances.services.SecuritiesService;

/**
 * Die Konfiguration für den Webserver.
 *
 * @author Frank W. Rahn
 */
@Configuration
@EnableSpringDataWebSupport
public class WebMvcConfiguration implements WebMvcConfigurer {

	/**
	 * {@inheritDoc}
	 *
	 * @see WebMvcConfigurer#addViewControllers(ViewControllerRegistry)
	 */
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/info").setViewName("info");
		registry.addViewController("/login").setViewName("login");
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see WebMvcConfigurer#addArgumentResolvers(List)
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(PageRequest.of(0, 10));

		argumentResolvers.add(resolver);
	}

	/**
	 * <strong>Workaround für <a href="https://github.com/spring-projects/spring-boot/issues/5638">Bug 5638</a></strong>.<br>
	 * Durch diesen Bug wird auf der Error-Seite kein Security Context zu Verfügung gestellt.<br>
	 * Angeblich nur wenn <code>spring-boot-actuator</code> im Klassenpfad ist ...<br>
	 * <br>
	 * Erzeuge einen Controller für die Fehlerseite.
	 */
	@Bean
	public BasicErrorController basicErrorController() {
		ErrorProperties errorProperties = new ErrorProperties();
		errorProperties.setIncludeStacktrace(ErrorProperties.IncludeStacktrace.ALWAYS);
		errorProperties.setPath("/__dummyErrorPath");

		return new BasicErrorController(new DefaultErrorAttributes(), errorProperties);
	}

	/**
	 * Erzeuge den Controller für die Wertpapiere.
	 */
	@Bean
	public SecuritiesController securitiesController(SecuritiesService service) {
		return new SecuritiesController(service);
	}

	/**
	 * Erzeuge den Controller für die Buchungen.
	 */
	@Bean
	public EntriesController entriesController(SecuritiesService service) {
		return new EntriesController(service);
	}

}