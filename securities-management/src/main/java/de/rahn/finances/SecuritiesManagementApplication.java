/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Die Startklasse für diesen Server.
 * @author Frank W. Rahn
 */
@SpringBootApplication
@ComponentScan
public class SecuritiesManagementApplication {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		SpringApplication.run(SecuritiesManagementApplication.class, args);
	}

}