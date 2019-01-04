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

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.step.StepMeterRegistry;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static java.util.Comparator.naturalOrder;
import static org.apache.commons.lang3.StringUtils.join;
import static org.slf4j.LoggerFactory.getLogger;

/**
 * Diese Komponente exportiert die Messwerte periodisch nach SLF4J.
 *
 * @author Frank W. Rahn
 */
@Component
public class Slf4jMeterRegistry extends StepMeterRegistry implements MeterAsStringFeatures {

	private static final Logger LOGGER = getLogger("reporting-metrics");

	public Slf4jMeterRegistry() {
		this(Clock.SYSTEM);
	}

	public Slf4jMeterRegistry(Clock micrometerClock) {
		super(new StepRegistryConfig() {
			@Override
			public String prefix() {
				return "slf4j";
			}

			@Override
			public String get(String key) {
				return null;
			}

			@Override
			public Duration step() {
				return Duration.ofHours(1);
			}
		}, micrometerClock);
	}


	@Override
	protected TimeUnit getBaseTimeUnit() {
		return TimeUnit.SECONDS;
	}

	/**
	 * Schreibe die Metriken in das Log.
	 */
	public final String[] exportMeters() {
		return publishMeters();
	}

	/**
	 * Schreibe die Metriken in das Log.
	 *
	 * @return die Liste der Metriken als Zeichenkette
	 */
	protected String[] publishMeters() {
		String[] metersAsStrings = getMetersAsStrings();

		LOGGER.info("\n***** Metrics Report Start *****\n" + join(metersAsStrings, "\n")
				+ "\n***** Metrics Report End *******");

		return metersAsStrings;
	}

	@Override
	protected final void publish() {
		publishMeters();
	}

	@Override
	public String[] getMetersAsStrings() {
		return getMeters().stream().map(this::getMeterAsString).sorted(naturalOrder()).toArray(String[]::new);
	}

}