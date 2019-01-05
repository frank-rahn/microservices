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
package de.rahn.finances.services.config;

import de.rahn.finances.domains.config.DomainsConfiguration;
import de.rahn.finances.domains.repositories.EntriesRepository;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.securities.SecuritiesServiceImpl;
import de.rahn.finances.services.securities.SecuritiesServiceMetricsAspect;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;

/**
 * Die Spring Configuration für die Services.
 *
 * @author Frank W. Rahn
 */
@Configuration
@Import({DomainsConfiguration.class})
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class ServicesConfiguration {

	/**
	 * @param securitiesRepository Das Repository für die Wertpapiere
	 * @param entriesRepository    Das Repository für die Buchungen
	 * @return Das Bean zum Service {@link SecuritiesService}
	 */
	@Bean
	SecuritiesService securitiesService(SecuritiesRepository securitiesRepository, EntriesRepository entriesRepository) {
		return new SecuritiesServiceImpl(securitiesRepository, entriesRepository);
	}

	/**
	 * Der Aspekt zum einsammeln der Daten für die Metriken.
	 *
	 * @param meterRegistry Das Spring Boot Bean für die Metriken
	 * @return Das Bean des Aspekt
	 */
	@Bean
	@ConditionalOnBean(MeterRegistry.class)
	SecuritiesServiceMetricsAspect securitiesServiceMetricsAspect(MeterRegistry meterRegistry) {
		return new SecuritiesServiceMetricsAspect(meterRegistry);
	}

}