/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Die Startklasse für diesen Server.
 * @author Frank W. Rahn
 */
@SpringBootApplication
public class SecuritiesManagementApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecuritiesManagementApplication.class, args);
	}

}