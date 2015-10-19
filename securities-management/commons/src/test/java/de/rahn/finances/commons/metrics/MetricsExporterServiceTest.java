/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.commons.metrics;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.UniformReservoir;

/**
 * Test, des Exporters.
 * @author Frank W. Rahn
 */
@RunWith(MockitoJUnitRunner.class)
public class MetricsExporterServiceTest {

	@Mock
	private MetricRegistry registry;

	@InjectMocks
	private MetricsExporterService exporter = new MetricsExporterService();

	private <V> SortedMap<String, V> singletonMap(String name, V value) {
		TreeMap<String, V> map = new TreeMap<>();
		map.put(name, value);

		return map;
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		when(registry.remove(anyString())).thenReturn(true);
		when(registry.getGauges()).thenReturn(singletonMap("gauges", () -> 4711));
		when(registry.getCounters()).thenReturn(singletonMap("counter", new Counter()));
		when(registry.getHistograms()).thenReturn(singletonMap("histogram", new Histogram(new UniformReservoir())));
		when(registry.getMeters()).thenReturn(singletonMap("meter", new Meter()));
		when(registry.getTimers()).thenReturn(singletonMap("timer", new Timer()));

		exporter.initialize();
	}

	/**
	 * Test method for {@link MetricsExporterService#exportMetrics()}.
	 */
	@Test
	public void testExportMetrics() {
		exporter.exportMetrics();

		assertThat(registry.getMetrics(), notNullValue());
		assertThat(registry.getMetrics().keySet(), empty());
	}

}