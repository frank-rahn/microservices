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
package de.rahn.finances.services.securities;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;
import static org.springframework.context.annotation.FilterType.REGEX;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.repositories.EntriesRepository;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.SecuritiesService;

/**
 * Der Test für die Implementierung des Services {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
public class SecuritiesServiceImplSecurityTest {

	@MockBean
	private SecuritiesRepository securitiesRepository;

	@MockBean
	private EntriesRepository entriesRepository;

	@Autowired
	private SecuritiesService classUnderTests;

	private Security testSecurity = new Security();

	@TestConfiguration
	@ComponentScan(basePackageClasses = { ServicePackageMarker.class },
		excludeFilters = { @Filter(type = REGEX, pattern = { ".*Aspect" }) })
	@EnableGlobalMethodSecurity(prePostEnabled = true)
	static class Config {
		// Leer
	}

	/**
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		testSecurity.setId(randomUUID().toString());
		testSecurity.setIsin("DE0000000000");
		testSecurity.setWkn("000000");
		testSecurity.setSymbol("ABC");
		testSecurity.setName("ABC AG");
		testSecurity.setType(stock);

		when(securitiesRepository.findOne(testSecurity.getId())).thenReturn(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test
	@WithMockUser(roles = { "USER" })
	public void testGetSecurityWithRoleUser() {
		Security security = classUnderTests.getSecurity(testSecurity.getId());

		assertThat(security).isNotNull().isEqualTo(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test(expected = AccessDeniedException.class)
	@WithMockUser(roles = { "Blubber" })
	public void testGetSecurityWithRoleBlubber() {
		classUnderTests.getSecurity(testSecurity.getId());

		fail("Hier hätte eine Exception geworfen werden müssen");
	}

}