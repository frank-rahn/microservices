/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service;

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

/**
 * Ein Aspekt für die Metriken des {@link SecuritiesService}.
 * @author Frank W. Rahn
 */
@Aspect
@Component
public class SecuritiesServiceMetricsAspect {

	/** Der Prefix für die Aufrufzähler. */
	public static final String PREFIX_METRICNAME_CALLS = "counter.calls.securities";

	/** Der Prefix für die Aufrufrate. */
	public static final String PREFIX_METRICNAME_EVENTS = "meter.calls.securities";

	/** Der Prefix für die Aufrufrate. */
	public static final String PREFIX_METRICNAME_TIMER = "timer.calls.securities";

	/** Der Prefix für die Fehlerzähler. */
	public static final String PREFIX_METRICNAME_ERRORS = "counter.errors.securities";

	/** Spring Boot Service für Counter und Meter. */
	@Autowired
	private CounterService counterService;

	/** Spring Boot Service für Guage, Timer und Histogram. */
	@Autowired
	private GaugeService gaugeService;

	/**
	 * Für den Zugriff auf die Schnittstelle.
	 */
	@Pointcut("this(de.rahn.finances.service.SecuritiesService)")
	private void onSecuritiesService() {
	}

	/**
	 * Zähle die erfolgreichen lesenden Zugriffe.
	 */
	@AfterReturning(pointcut = "execution(* de.rahn.finances.service.SecuritiesService.get*(..))")
	public void afterCallingSecuritiesServiceRead() {
		counterService.increment(PREFIX_METRICNAME_EVENTS);
		counterService.increment(PREFIX_METRICNAME_CALLS);
		counterService.increment(PREFIX_METRICNAME_CALLS + ".read");
	}

	/**
	 * Zähle die erfolgreichen verändernden Zugriffe.
	 */
	@AfterReturning(
		pointcut = "execution(* de.rahn.finances.service.SecuritiesService.save(..)) || execution(* de.rahn.finances.service.SecuritiesService.delete(..))")
	public void afterCallingSecuritiesServiceModified() {
		counterService.increment(PREFIX_METRICNAME_EVENTS);
		counterService.increment(PREFIX_METRICNAME_CALLS);
		counterService.increment(PREFIX_METRICNAME_CALLS + ".modified");
	}

	/**
	 * Zähle alle geworfenen Ausnahmen.
	 */
	@AfterThrowing(pointcut = "onSecuritiesService()", throwing = "exception")
	public void afterThrowsException(Exception exception) {
		counterService.increment(PREFIX_METRICNAME_ERRORS);
		counterService.increment(PREFIX_METRICNAME_ERRORS + "." + exception.getClass().getSimpleName().toLowerCase());
	}

	@Around("execution(org.springframework.data.domain.Page de.rahn.finances.service.SecuritiesService.getSecurities(org.springframework.data.domain.Pageable))")
	public Object doGetSecuritiesTimer(ProceedingJoinPoint joinPoint) throws Throwable {
		long start = currentTimeMillis();

		Object returnValue = joinPoint.proceed();

		gaugeService.submit(PREFIX_METRICNAME_TIMER + ".getsecurities", currentTimeMillis() - start);

		return returnValue;
	}

}