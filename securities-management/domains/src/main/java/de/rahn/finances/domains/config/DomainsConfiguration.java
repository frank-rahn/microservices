/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domains.config;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Die Spring Configuration für die Domains.
 * @author Frank W. Rahn
 */
@Configuration
@EntityScan(basePackages = { "de.rahn.finances.domains.entities" })
@EnableJpaRepositories(basePackages = { "de.rahn.finances.domains.repositories" })
public class DomainsConfiguration {
	// Leer
}