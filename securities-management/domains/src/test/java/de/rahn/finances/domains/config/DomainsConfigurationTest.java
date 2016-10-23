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
package de.rahn.finances.domains.config;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.springframework.beans.BeanUtils.copyProperties;
import static org.springframework.boot.SpringApplication.run;
import static org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor.VALIDATOR_BEAN_NAME;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import de.rahn.finances.domains.config.DomainsConfigurationTest.Application;
import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.repositories.SecuritiesRepository;

/**
 * Test der Spring Configuration.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class DomainsConfigurationTest {

	@SpringBootApplication
	@Import(DomainsConfiguration.class)
	static class Application {
		public static void main(String[] args) throws Exception {
			run(Application.class, args);
		}

		/**
		 * @return eine NO_OPT {@link Validator}
		 */
		@Bean(name = VALIDATOR_BEAN_NAME)
		public Validator validator() {
			return new Validator() {

				/**
				 * {@inheritDoc}
				 *
				 * @see Validator#validate(Object, Errors)
				 */
				@Override
				public void validate(Object target, Errors errors) {
					// Leer
				}

				/**
				 * {@inheritDoc}
				 *
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
	@Transactional
	public void testSpringConfiguration_01() {
		assertThat(repository, notNullValue());

		List<Security> all = repository.findAll();

		assertThat(all, notNullValue());
		assertThat(all, hasSize(greaterThan(0)));
		assertThat(all.get(0).isNew(), is(false));
		assertThat(all.get(0).getId(), is("067e6162-3b6f-4ae2-a171-2470b63df001"));
		assertThat(all.get(0).getIsin(), is("DE0001000010"));
		assertThat(all.get(0).getWkn(), is("100001"));
		assertThat(all.get(0).getName(), is("Firma 1 AG"));
		assertThat(all.get(0).getSymbol(), is("A01"));
		assertThat(all.get(0).getType(), is(stock));

		assertThat(all.get(0).hashCode(), not(all.get(1).hashCode()));
		assertThat(all.get(0).toString(), not(all.get(1).toString()));
		assertThat(all.get(0), not(all.get(1)));
	}

	/**
	 * Test, ob ein {@link ApplicationContext} erstellt werden kann.
	 */
	@Test
	public void testSpringConfiguration_02() {
		assertThat(repository, notNullValue());

		Security testSecurity = new Security();
		testSecurity.setIsin("DE0000000000");
		testSecurity.setWkn("000000");
		testSecurity.setSymbol("ABC");
		testSecurity.setName("ABC AG");
		testSecurity.setType(stock);
		testSecurity.setInventory(true);

		Security security = repository.save(testSecurity);

		assertThat(security, notNullValue());
		assertThat(security.isNew(), is(testSecurity.isNew()));
		assertThat(security.getId(), is(testSecurity.getId()));
		assertThat(security.getIsin(), is(testSecurity.getIsin()));
		assertThat(security.getName(), is(testSecurity.getName()));
		assertThat(security.getSymbol(), is(testSecurity.getSymbol()));
		assertThat(security.getType(), is(testSecurity.getType()));
		assertThat(security.getWkn(), is(testSecurity.getWkn()));
		assertThat(security.isInventory(), is(testSecurity.isInventory()));
		assertThat(security, is(testSecurity));

		security = new Security();
		copyProperties(testSecurity, security);

		assertThat(security, is(testSecurity));
	}

}