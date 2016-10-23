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

import static de.rahn.finances.domains.entities.EntryType.buy;
import static de.rahn.finances.domains.entities.SecurityType.stock;
import static java.math.RoundingMode.HALF_UP;
import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.math.MathContext;
import java.time.LocalDate;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.domains.config.DomainsConfiguration;
import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.Security;

/**
 * Tests für {@link SecuritiesRepository}
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DomainsConfiguration.class,
	properties = { "logging.level.org.hibernate.type=trace", "spring.jpa.properties.hibernate.format_sql=true" })
@DataJpaTest
public class SecuritiesRepositoryTest {

	private static final MathContext MC = new MathContext(16, HALF_UP);

	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	private SecuritiesRepository repository;

	@Test
	public void findAll() throws Exception {
		Security security = new Security();
		security.setName("Name");
		security.setWkn("123456");
		security.setSymbol("654321");
		security.setIsin("DE0012345600");
		security.setType(stock);
		security.setInventory(true);

		Entry entry = new Entry();
		entry.setAmount(new BigDecimal("152.415568", MC));
		entry.setDate(LocalDate.now());
		entry.setNumberOf(new BigDecimal("123.4567", MC));
		entry.setPrice(new BigDecimal("1.234567", MC));
		entry.setType(buy);

		security.addEntry(entry);

		security = entityManager.persist(security);
		entityManager.flush();

		security = repository.findOne(security.getId());

		assertThat(security).isNotNull();
		assertThat(security.getId()).isNotEmpty();
		assertThat(security.getName()).isEqualTo("Name");
		assertThat(security.getWkn()).isEqualTo("123456");
		assertThat(security.getSymbol()).isEqualTo("654321");
		assertThat(security.getIsin()).isEqualTo("DE0012345600");
		assertThat(security.getType()).isEqualTo(stock);
		assertThat(security.isInventory()).isTrue();
		assertThat(security.getEntries()).isNotEmpty();
		assertThat(security.getEntries()).hasSize(1);
		assertThat(security.getEntries().get(0)).isNotNull();
		assertThat(security.getEntries().get(0).getId()).isNotEmpty();
		assertThat(security.getEntries().get(0).getType()).isEqualTo(buy);

	}

}