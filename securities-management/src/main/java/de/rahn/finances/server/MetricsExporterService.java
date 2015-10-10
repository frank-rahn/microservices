/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.server;

import static org.slf4j.LoggerFactory.getLogger;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.metrics.repository.MetricRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

/**
 * Diser Service exportiert die Metriken.<br>
 * <br>
 * In diesem Fall loggt der Service die Metriken periodisch.
 * @author Frank W. Rahn
 */
@Service
public class MetricsExporterService {

	private static final Logger LOGGER = getLogger("reporting-metrics");

	@Autowired
	private MetricRepository repository;

	/**
	 * Schreibe jede volle Stunde die aktuellen Metriken und setze sie zurück.
	 */
	@Scheduled(cron = "0 0 * * * ?")
	public void exportMetrics() {
		StringBuilder builder = new StringBuilder("\n***** Metriken Report *****\n");
		repository.findAll().forEach(m -> {
			builder.append("Metrik ").append(m.getName()).append(" = ").append(m.getValue()).append('\n');
			repository.reset(m.getName());
		});
		LOGGER.info(builder.append("***************************").toString());
	}

}