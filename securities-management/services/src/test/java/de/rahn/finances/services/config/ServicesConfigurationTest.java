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
package de.rahn.finances.services.config;

import de.rahn.finances.domains.repositories.EntriesRepository;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.rules.ExpectedException.none;
import static org.mockito.Mockito.when;

/**
 * Test der Spring Configuration.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServicesConfiguration.class})
@DataJpaTest
@WithMockUser
public class ServicesConfigurationTest {

	/**
	 * Security ID
	 */
	private static final String ID = "4711";

	@Rule
	public final ExpectedException thrown = none();

	@MockBean
	private SecuritiesRepository securitiesRepository;

	@Autowired
	private SecuritiesService service;

	/**
	 * Test, ob ein {@link ApplicationContext} erstellt werden kann.
	 */
	@Test
	public void testSpringConfiguration_01() {
		when(securitiesRepository.findById(ID)).thenReturn(Optional.empty());

		thrown.expect(SecurityNotFoundException.class);
		thrown.expectMessage(ID);

		service.getSecurity(ID);
	}

}