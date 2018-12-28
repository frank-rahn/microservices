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

import static java.lang.String.join;
import static java.util.Comparator.naturalOrder;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * Diese Komponente exportiert die Messwerte periodisch nach SLF4J.
 *
 * @author Frank W. Rahn
 */
@Component
public class Slf4jMeterRegistry extends SimpleMeterRegistry implements MeterAsStringFeatures {

	private static final Logger LOGGER = getLogger("reporting-metrics");

	/**
	 * Schreibe jede volle Stunde die aktuellen Messwerte in den Log.
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public String[] exportMeters() {
		final String[] logs = getMetersAsStrings();

		LOGGER.info("\n***** Metrics Report Start *****\n" + join("\n", logs)
				+ "\n***** Metrics Report End *******");

		return logs;
	}

	/**
	 * Liefere die aktuellen Messwerte.
	 *
	 * @return die aktuellen Messwerte als Zeichenketten
	 */
	@Override
	public String[] getMetersAsStrings() {
		return getMeters().stream().map(this::getMeterAsString).sorted(naturalOrder()).toArray(String[]::new);
	}

}