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
package de.rahn.finances.domains.repositories;

import static de.rahn.finances.domains.entities.SecurityType.stock;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.domains.config.DomainsConfiguration;
import de.rahn.finances.domains.entities.Security;

/**
 * Tests für {@link SecuritiesRepository}
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DomainsConfiguration.class)
@DataJpaTest
public class SecuritiesRepositoryTest {

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SecuritiesRepository repository;

	@Test
	public void findAll() throws Exception {
		Security security = new Security();
		security.setName("Name");
		security.setWkn("123456");
		security.setIsin("DE0012345600");
		security.setType(stock);

		security = entityManager.persist(security);

		security = repository.findOne(security.getId());

		assertThat(security).isNotNull();
		assertThat(security.getName()).isEqualTo("Name");
	}

}