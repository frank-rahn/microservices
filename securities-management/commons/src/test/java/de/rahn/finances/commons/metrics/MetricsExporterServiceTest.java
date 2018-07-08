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
package de.rahn.finances.commons.metrics;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import com.codahale.metrics.Counter;
import com.codahale.metrics.Histogram;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.codahale.metrics.UniformReservoir;

import de.rahn.finances.commons.config.CommonsConfiguration;

/**
 * Test, des Exporters.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = CommonsConfiguration.class)
public class MetricsExporterServiceTest {

	@MockBean
	private MetricRegistry registry;

	@Autowired
	private MetricsExporterService exporter;

	private <V> SortedMap<String, V> singletonMap(String name, V value) {
		TreeMap<String, V> map = new TreeMap<>();
		map.put(name, value);

		return map;
	}

	/**
	 * Initialisiere diesen Test
	 */
	@Before
	public void setUp() {
		when(registry.remove(anyString())).thenReturn(true);
		when(registry.getGauges()).thenReturn(singletonMap("gauges", () -> 4711));
		when(registry.getCounters()).thenReturn(singletonMap("counter", new Counter()));
		when(registry.getHistograms()).thenReturn(singletonMap("histogram", new Histogram(new UniformReservoir())));
		when(registry.getMeters()).thenReturn(singletonMap("meter", new Meter()));
		when(registry.getTimers()).thenReturn(singletonMap("timer", new Timer()));
	}

	/**
	 * Test method for {@link MetricsExporterService#exportMetrics()}.
	 */
	@Test
	public void testExportMetrics() {
		exporter.exportMetrics();

		assertThat(registry.getMetrics()).isNotNull();
		assertThat(registry.getMetrics().keySet()).isEmpty();
	}

}