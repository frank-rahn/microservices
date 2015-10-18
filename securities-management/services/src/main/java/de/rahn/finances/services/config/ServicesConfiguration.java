/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.services.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Die Spring Configuration für die Services.
 * @author Frank W. Rahn
 */
@Configuration
@ComponentScan(basePackages = { "de.rahn.finances.services.securities" })
public class ServicesConfiguration {
	// Leer
}