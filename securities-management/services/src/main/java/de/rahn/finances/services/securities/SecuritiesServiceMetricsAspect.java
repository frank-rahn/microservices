/*
 * Copyright 2011-2015 the original author or authors.
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
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.stereotype.Component;

import de.rahn.finances.services.SecuritiesService;

/**
 * Ein Aspekt für die Metriken des {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@Aspect
@Component
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

	/** Spring Boot Service für Counter und Meter. */
	@Autowired
	private CounterService counterService;

	/** Spring Boot Service für Guage, Timer und Histogram. */
	@Autowired
	private GaugeService gaugeService;

	/**
	 * Für den Zugriff auf die Schnittstelle.
	 */
	@Pointcut("this(de.rahn.finances.services.SecuritiesService)")
	private void onSecuritiesService() {
	}

	/**
	 * Zähle die erfolgreichen lesenden Zugriffe.
	 */
	@AfterReturning(pointcut = "execution(* de.rahn.finances.services.SecuritiesService.get*(..))")
	public void afterCallingSecuritiesServiceRead() {
		counterService.increment(PREFIX_METRICNAME_EVENTS);
		counterService.increment(PREFIX_METRICNAME_CALLS);
		counterService.increment(PREFIX_METRICNAME_CALL + "read");
	}

	/**
	 * Zähle die erfolgreichen verändernden Zugriffe.
	 */
	@AfterReturning(
		pointcut = "execution(* de.rahn.finances.services.SecuritiesService.save(..)) || execution(* de.rahn.finances.services.SecuritiesService.delete(..))")
	public void afterCallingSecuritiesServiceModified() {
		counterService.increment(PREFIX_METRICNAME_EVENTS);
		counterService.increment(PREFIX_METRICNAME_CALLS);
		counterService.increment(PREFIX_METRICNAME_CALL + "modified");
	}

	/**
	 * Zähle alle geworfenen Ausnahmen.
	 */
	@AfterThrowing(pointcut = "onSecuritiesService()", throwing = "exception")
	public void afterThrowsException(Exception exception) {
		counterService.increment(PREFIX_METRICNAME_ERRORS);
		counterService.increment(PREFIX_METRICNAME_ERROR + exception.getClass().getSimpleName().toLowerCase());
	}

	@Around("execution(org.springframework.data.domain.Page de.rahn.finances.services.SecuritiesService.getSecurities(org.springframework.data.domain.Pageable))")
	public Object doGetSecuritiesTimer(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = currentTimeMillis();

		Object returnValue = joinPoint.proceed();

		gaugeService.submit(PREFIX_METRICNAME_TIMER, currentTimeMillis() - start);

		return returnValue;
	}

}