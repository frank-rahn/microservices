/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.domains.config;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.VALIDATOR_BEAN_NAME;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.rahn.finances.domains.config.DomainsConfigurationTest.Appication;
import de.rahn.finances.domains.repositories.SecuritiesRepository;

/**
 * Test der Spring Configuration.
 * @author Frank W. Rahn
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Appication.class)
public class DomainsConfigurationTest {

	@SpringBootApplication
	@Import(DomainsConfiguration.class)
	static class Appication {
		public static void main(String[] args) throws Exception {
			run(Appication.class, args);
		}

		/**
		 * @return eine NO_OPT {@link Validator}
		 */
		@Bean(name = VALIDATOR_BEAN_NAME)
		public Validator validator() {
			return new Validator() {

				/**
				 * {@inheritDoc}
				 * @see Validator#validate(Object, Errors)
				 */
				@Override
				public void validate(Object target, Errors errors) {
					// Leer
				}

				/**
				 * {@inheritDoc}
				 * @see Validator#supports(Class)
				 */
				@Override
				public boolean supports(Class<?> clazz) {
					return false;
				}
			};
		}
	}

	@Autowired
	private SecuritiesRepository repository;

	/**
	 * Test, ob ein {@link ApplicationContext} erstellt werden kann.
	 */
	@Test
	public void testSpringConfiguration() {
		assertThat(repository, notNullValue());
	}

}