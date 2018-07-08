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

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import de.rahn.finances.domains.config.DomainsConfiguration;
import de.rahn.finances.domains.entities.Entry;

/**
 * Tests für {@link EntriesRepository}
 *
 * @author Frank W. Rahn
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = DomainsConfiguration.class,
	properties = { "logging.level.org.hibernate.type=trace", "spring.jpa.properties.hibernate.format_sql=true" })
@DataJpaTest
public class EntriesRepositoryTest {

	@Autowired
	private EntriesRepository repository;

	@Test
	public void findAll() {

		List<Entry> entries = repository.findAll();

		assertThat(entries).isNotNull();
		assertThat(entries).hasSize(2);
	}

}