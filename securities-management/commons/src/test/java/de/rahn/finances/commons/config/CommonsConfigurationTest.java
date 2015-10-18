/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.commons.config;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.MetricRepositoryAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import de.rahn.finances.commons.metrics.MetricsExporterService;

/**
 * Test der Spring Configuration.
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { CommonsConfiguration.class, MetricRepositoryAutoConfiguration.class })
public class CommonsConfigurationTest {

	@Autowired
	private MetricsExporterService exporter;

	/**
	 * Rufe ein Bean aus der Configuartion auf.
	 */
	@Test
	public void testSpringConfiguration() {
		exporter.exportMetrics();
	}

}