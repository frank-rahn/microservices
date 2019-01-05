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

import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.config.ServicesConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.util.UUID.randomUUID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.Mockito.when;

/**
 * Der Test für die Implementierung des Services {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {ServicesConfiguration.class})
@DataJpaTest
public class SecuritiesServiceImplSecurityTest {

	@MockBean
	private SecuritiesRepository securitiesRepository;

	@Autowired
	private SecuritiesService classUnderTests;

	private Security testSecurity;

	/**
	 * Initialisiere diesen Test
	 */
	@Before
	public void setUp() {
		testSecurity = new Security();
		testSecurity.setId(randomUUID().toString());
		testSecurity.setIsin("DE0000000000");
		testSecurity.setWkn("000000");
		testSecurity.setSymbol("ABC");
		testSecurity.setName("ABC AG");
		testSecurity.setType(stock);

		when(securitiesRepository.findById(testSecurity.getId())).thenReturn(Optional.of(testSecurity));
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test
	@WithMockUser
	public void testGetSecurityWithRoleUser() {
		Security security = classUnderTests.getSecurity(testSecurity.getId());

		assertThat(security).isNotNull().isEqualTo(testSecurity);
	}

	/**
	 * Test method for {@link SecuritiesServiceImpl#getSecurity(String)}.
	 */
	@Test(expected = AccessDeniedException.class)
	@WithMockUser(roles = {"Blubber"})
	public void testGetSecurityWithRoleBlubber() {
		classUnderTests.getSecurity(testSecurity.getId());

		fail("Hier hätte eine Exception geworfen werden müssen");
	}

}