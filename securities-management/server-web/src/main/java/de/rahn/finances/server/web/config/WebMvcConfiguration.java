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

import static org.springframework.boot.autoconfigure.condition.SearchStrategy.CURRENT;

import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import de.rahn.finances.server.web.ui.PackageMarker;

/**
 * Die Konfiguration für den Webserver.
 *
 * @author Frank W. Rahn
 */
@Configuration
@ComponentScan(basePackageClasses = { PackageMarker.class })
@EnableSpringDataWebSupport
public class WebMvcConfiguration extends WebMvcConfigurerAdapter {

	/**
	 * {@inheritDoc}
	 *
	 * @see WebMvcConfigurerAdapter#addViewControllers(ViewControllerRegistry)
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
	 * @see WebMvcConfigurerAdapter#addArgumentResolvers(List)
	 */
	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
		PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
		resolver.setFallbackPageable(new PageRequest(0, 10));

		argumentResolvers.add(resolver);
	}

	@Bean
	@ConditionalOnMissingBean(value = ErrorAttributes.class, search = CURRENT)
	public DefaultErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes();
	}

}