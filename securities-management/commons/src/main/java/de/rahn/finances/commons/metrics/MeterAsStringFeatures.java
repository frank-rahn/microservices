package de.rahn.finances.commons.metrics;

import io.micrometer.core.instrument.*;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.apache.commons.lang3.StringUtils.rightPad;

public interface MeterAsStringFeatures {
	/**
	 * Liefere die aktuellen Messwerte.
	 *
	 * @return die aktuellen Messwerte als Zeichenketten
	 */
	String[] getMetersAsStrings();

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getMeterAsString(Meter meter) {
		if (meter instanceof Counter) {
			return getCounterAsString((Counter) meter);
		} else if (meter instanceof FunctionCounter) {
			return getCounterAsString((FunctionCounter) meter);
		} else if (meter instanceof Gauge) {
			return getTimerAsString((Gauge) meter);
		} else if (meter instanceof Timer) {
			return getTimerAsString((Timer) meter);
		} else if (meter instanceof FunctionTimer) {
			return getTimerAsString((FunctionTimer) meter);
		} else if (meter instanceof LongTaskTimer) {
			return getTimerAsString((LongTaskTimer) meter);
		} else if (meter instanceof DistributionSummary) {
			return getDistributionSummaryAsString((DistributionSummary) meter);
		} else {
			return getGeneralMeterAsString(meter);
		}
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getCounterAsString(Counter meter) {
		return getMeterName(meter, "COUNTER") + ", count=" + meter.count();
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getCounterAsString(FunctionCounter meter) {
		return getMeterName(meter, "FUNCTIONCOUNTER") + ", count=" + meter.count();
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getTimerAsString(Gauge meter) {
		return getMeterName(meter, "GAUGE") + ", value=" + meter.value();
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getTimerAsString(Timer meter) {
		return getMeterName(meter, "TIMER") + ", baseTimeUnit=" + meter.baseTimeUnit() + ", count="
				+ meter.count() + ", mean=" + meter.mean(MILLISECONDS) + " ms, max=" + meter.max(MILLISECONDS)
				+ " ms, total-time=" + meter.totalTime(MILLISECONDS) + " ms, median (50 %)="
				+ meter.percentile(0.5, MILLISECONDS) + " ms, percentile (95 %)=" + meter.percentile(0.95, MILLISECONDS)
				+ " ms";
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getTimerAsString(FunctionTimer meter) {
		return getMeterName(meter, "FUNCTIONTIMER") + ", baseTimeUnit=" + meter.baseTimeUnit() + ", count="
				+ meter.count() + ", mean=" + meter.mean(MILLISECONDS) + " ms, total-time=" + meter.totalTime(MILLISECONDS)
				+ " ms";
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getTimerAsString(LongTaskTimer meter) {
		return getMeterName(meter, "LONGTASKTIMER") + ", activeTask=" + meter.activeTasks() + ", duration="
				+ meter.duration(MILLISECONDS) + " ms";
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getDistributionSummaryAsString(DistributionSummary meter) {
		return getMeterName(meter, "DISTRIBUTIONSUMMARY") + ", count=" + meter.count() + ", mean=" + meter.mean()
				+ ", max=" + meter.max() + ", total-amount=" + meter.totalAmount() + ", median (50%)="
				+ meter.percentile(0.5) + ", percentile (95 %)=" + meter.percentile(0.95);
	}

	/**
	 * Liefere den Messwert als Zeichenkette.
	 *
	 * @param meter der Messwert
	 * @return den Messwert als Zeichenkette
	 */
	default String getGeneralMeterAsString(Meter meter) {
		return getMeterName(meter, "OTHER");
	}

	/**
	 * Ermittel den Namen des Messwertes.
	 *
	 * @param meter  der Messwert
	 * @param prefix der Prefix des Messwertes oder <code>null</code>
	 * @return der Name des Messwertes
	 */
	default String getMeterName(Meter meter, String prefix) {
		return "metric=" + rightPad(prefix + ",", 20) + " name=" + meter.getId().getName() + ", tags=" + meter.getId().getTags();
	}

}