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

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.slf4j.LoggerFactory.getLogger;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Snapshot;

/**
 * Diser Service exportiert die Dropwizard Metriken.<br>
 * <br>
 * In diesem Fall loggt der Service die Metriken periodisch.
 * 
 * @author Frank W. Rahn
 */
@Service
public class MetricsExporterService {

	private static final Logger LOGGER = getLogger("reporting-metrics");

	@Autowired
	private MetricRegistry registry;

	private double rateFactor;

	private String rateUnit;

	private double durationFactor;

	private String durationUnit;

	/**
	 * Initialisiere den Reporter.
	 */
	@PostConstruct
	public void initialize() {
		rateFactor = SECONDS.toSeconds(1);
		rateUnit = "second";
		durationFactor = 1.0 / MILLISECONDS.toNanos(1);
		durationUnit = "milliseconds";
	}

	/**
	 * Schreibe jede volle Stunde die aktuellen Metriken und setze sie zurück.
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void exportMetrics() {
		StringBuilder builder = new StringBuilder();

		registry.getGauges().forEach((s, m) -> {
			registry.remove(s);

			builder.append("metric=GAUGE,     name=").append(s).append(", value=").append(m.getValue()).append('\n');
		});

		registry.getCounters().forEach((s, m) -> {
			registry.remove(s);

			builder.append("metric=COUNTER,   name=").append(s).append(", count=").append(m.getCount()).append('\n');
		});

		registry.getHistograms().forEach((s, m) -> {
			registry.remove(s);

			Snapshot snapshot = m.getSnapshot();

			builder.append("metric=HISTOGRAM, name=").append(s).append(", count=").append(m.getCount()).append(", min=")
				.append(snapshot.getMin()).append(", max=").append(snapshot.getMax()).append(", mean=")
				.append(snapshot.getMean()).append(", stddev=").append(snapshot.getStdDev()).append(", median=")
				.append(snapshot.getMedian()).append(", 75%=").append(snapshot.get75thPercentile()).append(", 95%=")
				.append(snapshot.get95thPercentile()).append(", 98%=").append(snapshot.get98thPercentile()).append(", 99%=")
				.append(snapshot.get99thPercentile()).append(", 99.9%=").append(snapshot.get999thPercentile()).append('\n');
		});

		registry.getMeters().forEach((s, m) -> {
			registry.remove(s);

			builder.append("metric=METER,     name=").append(s).append(", count=").append(m.getCount()).append(", mean-rate=")
				.append(convertRate(m.getMeanRate())).append(", 1-minute-rate=").append(convertRate(m.getOneMinuteRate()))
				.append(", 5-minute-rate=").append(convertRate(m.getFiveMinuteRate())).append(", 15-minute-rate=")
				.append(convertRate(m.getFifteenMinuteRate())).append(", rate-unit=events/").append(rateUnit).append('\n');
		});

		registry.getTimers().forEach((s, m) -> {
			registry.remove(s);

			Snapshot snapshot = m.getSnapshot();

			builder.append("metric=TIMER,     name=").append(s).append(", count=").append(m.getCount()).append(", mean-rate=")
				.append(convertRate(m.getMeanRate())).append(", 1-minute-rate=").append(convertRate(m.getOneMinuteRate()))
				.append(", 5-minute-rate=").append(convertRate(m.getFiveMinuteRate())).append(", 15-minute-rate=")
				.append(convertRate(m.getFifteenMinuteRate())).append(", rate-unit=calls/").append(rateUnit).append(", min=")
				.append(convertDuration(snapshot.getMin())).append(", max=").append(convertDuration(snapshot.getMax()))
				.append(", mean=").append(convertDuration(snapshot.getMean())).append(", stddev=")
				.append(convertDuration(snapshot.getStdDev())).append(", median=").append(convertDuration(snapshot.getMedian()))
				.append(", 75%=").append(convertDuration(snapshot.get75thPercentile())).append(", 95%=")
				.append(convertDuration(snapshot.get95thPercentile())).append(", 98%=")
				.append(convertDuration(snapshot.get98thPercentile())).append(", 99%=")
				.append(convertDuration(snapshot.get99thPercentile())).append(", 99.9%=")
				.append(convertDuration(snapshot.get999thPercentile())).append(", duration-unit=").append(durationUnit)
				.append('\n');
		});

		if (builder.length() > 0) {
			LOGGER.info(builder.insert(0, "\n***** Metrics Report *****\n").append("***************************").toString());
		}
	}

	private double convertDuration(double duration) {
		return duration * durationFactor;
	}

	private double convertRate(double rate) {
		return rate * rateFactor;
	}
}