package de.rahn.finances.commons.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import de.rahn.finances.commons.metrics.MetricsExporterService;

/**
 * Die Spring Configuration für die Utilities.
 * @author Frank W. Rahn
 */
@Configuration
@EnableScheduling
public class CommonsConfiguration {

	/**
	 * @return den Exporter für die Metriken
	 */
	@Bean
	public MetricsExporterService metricsExporterService() {
		return new MetricsExporterService();
	}

}