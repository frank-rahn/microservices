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
package de.rahn.finances.domains.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import de.rahn.finances.domains.entities.EntityPackageMarker;
import de.rahn.finances.domains.security.SpringSecurityAuditorAwareBean;

/**
 * Die Spring Configuration für die Domains.
 *
 * @author Frank W. Rahn
 */
@Configuration
@EntityScan(basePackageClasses = { EntityPackageMarker.class, Jsr310JpaConverters.class })
@EnableJpaRepositories(basePackageClasses = { de.rahn.finances.domains.repositories.PackageMarker.class })
@EnableJpaAuditing
public class DomainsConfiguration {

	/**
	 * @return Über diese Bean holt sich Spring Data JPA den aktuellen Benutzer
	 */
	@Bean
	public SpringSecurityAuditorAwareBean auditorAwareBean() {
		return new SpringSecurityAuditorAwareBean();
	}

}