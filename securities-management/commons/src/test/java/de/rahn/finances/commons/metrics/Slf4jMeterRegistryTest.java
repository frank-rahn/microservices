/*
 * Copyright 2011-2018 Frank W. Rahn and the project authors.
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

import static io.micrometer.core.instrument.Meter.Type.OTHER;
import static java.util.Collections.emptyList;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.metrics.MetricsAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.commons.config.CommonsConfiguration;

import io.micrometer.core.instrument.Meter;

/**
 * Test, des Exporters.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = { CommonsConfiguration.class, MetricsAutoConfiguration.class })
public class Slf4jMeterRegistryTest {

	@Autowired
	private Slf4jMeterRegistry registry;

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_Counter() {
		final String name = "test-counter";
		registry.counter(name).increment();
		registry.counter(name).increment();

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=COUNTER,             name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_Timer() {
		final String name = "test-timer";
		registry.timer(name).record(1000, MILLISECONDS);
		registry.timer(name).record(2000, MILLISECONDS);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=TIMER,               name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_Gauge() {
		final String name = "test-gauge";
		registry.gauge(name, 1);
		registry.gauge(name, 2);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=GAUGE,               name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_Summary() {
		final String name = "test-summary";
		registry.summary(name).record(1);
		registry.summary(name).record(2);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=DISTRIBUTIONSUMMARY, name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_LongTaskTimer() {
		final String name = "test-longtasktimer";
		registry.more().longTaskTimer(name).record(() -> {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException exception) {
				throw new RuntimeException(exception.getLocalizedMessage());
			}
		});
		registry.more().longTaskTimer(name).record(() -> {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException exception) {
				throw new RuntimeException(exception.getLocalizedMessage());
			}
		});

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=LONGTASKTIMER,       name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_TimeGauge() {
		final String name = "test-timegauge";
		registry.more().timeGauge(name, emptyList(), "1000", MILLISECONDS, Double::parseDouble);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=GAUGE,               name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_FunctionCounter() {
		final String name = "test-functioncounter";
		registry.more().counter(name, emptyList(), 1000);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=FUNCTIONCOUNTER,     name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_FunctionTimer() {
		final String name = "test-functiontimer";
		registry.more().timer(name, emptyList(), "1000", Long::parseLong, Double::parseDouble, MILLISECONDS);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=FUNCTIONTIMER,       name=" + name + ", tags=[]"));
	}

	/**
	 * Test method for {@link Slf4jMeterRegistry#exportMeters()}.
	 */
	@Test
	public void testExportMetrics_Other() {
		final String name = "test-other";
		Meter.builder(name, OTHER, null).register(registry);

		String[] logs = registry.exportMeters();

		assertThat(logs).anyMatch(s -> s.startsWith("metric=OTHER,               name=" + name + ", tags=[]"));
	}

}