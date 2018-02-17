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
package de.rahn.finances.services.securities;

import static java.lang.System.currentTimeMillis;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import de.rahn.finances.services.SecuritiesService;

import io.micrometer.core.instrument.MeterRegistry;

/**
 * Ein Aspekt für die Metriken des {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@Component
@Aspect
public class SecuritiesServiceMetricsAspect {

	/** Der Prefix für die Aufrufzähler. */
	public static final String PREFIX_METRICNAME_CALLS = "counter.securities.services.securities.invoked";

	/** Der Prefix für die Aufrufzähler. */
	public static final String PREFIX_METRICNAME_CALL = "counter.securities.services.securities.invoke.";

	/** Der Prefix für die Aufrufrate. */
	public static final String PREFIX_METRICNAME_EVENTS = "meter.securities.services.securities.used";

	/** Der Prefix für die Aufrufrate. */
	public static final String PREFIX_METRICNAME_TIMER = "timer.securities.services.securities.getsecurities.executed";

	/** Der Prefix für die Fehlerzähler. */
	public static final String PREFIX_METRICNAME_ERRORS = "counter.securities.services.securities.failed";

	/** Der Prefix für die Fehlerzähler. */
	public static final String PREFIX_METRICNAME_ERROR = "counter.securities.services.securities.failure.";

	/** Spring Boot Service für Counter und Gauge. */
	@Autowired
	private MeterRegistry meterRegistry;

	/**
	 * Für den Zugriff auf die Schnittstelle.
	 */
	@Pointcut("this(de.rahn.finances.services.SecuritiesService)")
	private void onSecuritiesService() {
		// leer
	}

	/**
	 * Zähle die erfolgreichen lesenden Zugriffe.
	 */
	@AfterReturning(pointcut = "execution(* de.rahn.finances.services.SecuritiesService.get*(..))")
	public void afterCallingSecuritiesServiceRead() {
		meterRegistry.counter(PREFIX_METRICNAME_EVENTS).increment();
		meterRegistry.counter(PREFIX_METRICNAME_CALLS).increment();
		meterRegistry.counter(PREFIX_METRICNAME_CALL + "read").increment();
	}

	/**
	 * Zähle die erfolgreichen verändernden Zugriffe.
	 */
	@AfterReturning(
		pointcut = "execution(* de.rahn.finances.services.SecuritiesService.save(..)) || execution(* de.rahn.finances.services.SecuritiesService.delete(..))")
	public void afterCallingSecuritiesServiceModified() {
		meterRegistry.counter(PREFIX_METRICNAME_EVENTS).increment();
		meterRegistry.counter(PREFIX_METRICNAME_CALLS).increment();
		meterRegistry.counter(PREFIX_METRICNAME_CALL + "modified").increment();
	}

	/**
	 * Zähle alle geworfenen Ausnahmen.
	 */
	@AfterThrowing(pointcut = "onSecuritiesService()", throwing = "exception")
	public void afterThrowsException(Exception exception) {
		meterRegistry.counter(PREFIX_METRICNAME_ERRORS).increment();
		meterRegistry.counter(PREFIX_METRICNAME_ERROR + exception.getClass().getSimpleName().toLowerCase()).increment();
	}

	@Around("execution(org.springframework.data.domain.Page de.rahn.finances.services.SecuritiesService.getSecurities(org.springframework.data.domain.Pageable))")
	public Object doGetSecuritiesTimer(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = currentTimeMillis();

		Object returnValue = joinPoint.proceed();

		meterRegistry.gauge(PREFIX_METRICNAME_TIMER, currentTimeMillis() - start);

		return returnValue;
	}

}