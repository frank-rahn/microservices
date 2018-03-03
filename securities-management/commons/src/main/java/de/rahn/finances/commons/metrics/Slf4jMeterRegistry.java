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

import static java.util.Arrays.stream;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.stream.Collectors.joining;
import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.FunctionCounter;
import io.micrometer.core.instrument.FunctionTimer;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.LongTaskTimer;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;

/**
 * Diese Komponente exportiert die Metriken periodisch nach SLF4J.
 *
 * @author Frank W. Rahn
 */
@Component
public class Slf4jMeterRegistry extends SimpleMeterRegistry {

	private static final Logger LOGGER = getLogger("reporting-metrics");

	/**
	 * Schreibe jede volle Stunde die aktuellen Metriken in den Log.
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public String[] exportMetrics() {
		String[] logs = getMeters().stream().map(m -> {
			String name = "name=" + m.getId().getName() + ", tags=" + m.getId().getTags();

			if (m instanceof Counter) {
				Counter counter = (Counter) m;
				return "metric=COUNTER,             " + name + ", count=" + counter.count();
			} else if (m instanceof FunctionCounter) {
				FunctionCounter counter = (FunctionCounter) m;
				return "metric=FUNCTIONCOUNTER,     " + name + ", count=" + counter.count();
			} else if (m instanceof Gauge) {
				Gauge gauge = (Gauge) m;
				return "metric=GAUGE,               " + name + ", value=" + gauge.value();
			} else if (m instanceof Timer) {
				Timer timer = (Timer) m;
				return "metric=TIMER,               " + name + ", baseTimeUnit=" + timer.baseTimeUnit() + ", count="
					+ timer.count() + ", mean=" + timer.mean(MILLISECONDS) + " ms, max=" + timer.max(MILLISECONDS)
					+ " ms, total-time=" + timer.totalTime(MILLISECONDS) + " ms, median (50 %)="
					+ timer.percentile(0.5, MILLISECONDS) + " ms, percentile (95 %)=" + timer.percentile(0.95, MILLISECONDS)
					+ " ms";
			} else if (m instanceof FunctionTimer) {
				FunctionTimer timer = (FunctionTimer) m;
				return "metric=FUNCTIONTIMER,       " + name + ", baseTimeUnit=" + timer.baseTimeUnit() + ", count="
					+ timer.count() + ", mean=" + timer.mean(MILLISECONDS) + " ms, total-time=" + timer.totalTime(MILLISECONDS)
					+ " ms";
			} else if (m instanceof LongTaskTimer) {
				LongTaskTimer timer = (LongTaskTimer) m;
				return "metric=LONGTASKTIMER,       " + name + ", activeTask=" + timer.activeTasks() + ", duration="
					+ timer.duration(MILLISECONDS) + " ms";
			} else if (m instanceof DistributionSummary) {
				DistributionSummary summary = (DistributionSummary) m;
				return "metric=DISTRIBUTIONSUMMARY, " + name + ", count=" + summary.count() + ", mean=" + summary.mean()
					+ ", max=" + summary.max() + ", total-amount=" + summary.totalAmount() + ", median (50%)="
					+ summary.percentile(0.5) + ", percentile (95 %)=" + summary.percentile(0.95);
			} else {
				return "metric=OTHER,               " + name;
			}
		}).sorted((a, b) -> a.compareTo(b)).toArray(String[]::new);

		LOGGER.info("\n***** Metrics Report *****\n" + stream(logs).collect(joining("\n")) + "\n***************************");

		return logs;
	}

}