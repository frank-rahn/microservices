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

import de.rahn.finances.domains.config.DomainsConfiguration;
import de.rahn.finances.domains.entities.Security;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.rules.SpringClassRule;
import org.springframework.test.context.junit4.rules.SpringMethodRule;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests für {@link SecuritiesRepository}
 *
 * @author Frank W. Rahn
 */
@RunWith(Parameterized.class)
@ContextConfiguration(classes = DomainsConfiguration.class)
@DataJpaTest
public class SecuritiesRepositoryParameterizedTest {

	/**
	 * @return die Liste der Test Fixtures
	 */
	@Parameters(name = "find({1}) = WKN({0})")
	public static Iterable<Object[]> parameters() {
		List<Object[]> parameters = new ArrayList<>();

		for (int i = 1; i < 10; i++) {
			parameters.add(new Object[]{"10000" + i, "067e6162-3b6f-4ae2-a171-2470b63df00" + i});
		}

		return parameters;
	}

	/**
	 * Aktiviere die Spring {@link TestRule}s.
	 */
	@ClassRule
	public static final SpringClassRule SPRING_CLASS_RULE = new SpringClassRule();

	/**
	 * Aktiviere die Spring {@link MethodRule}s.
	 */
	@Rule
	public final SpringMethodRule springMethodRule = new SpringMethodRule();

	/**
	 * Die zu testende Klasse.
	 */
	@Autowired
	private SecuritiesRepository repository;

	/**
	 * Test Fixture: WKN des Wertpapiers
	 */
	@Parameter
	public String wkn;

	/**
	 * Test Fixture: ID des Wertpapiers
	 */
	@Parameter(1)
	public String id;

	/**
	 * Test method for {@link SecuritiesRepository#getOne(Object)}.
	 */
	@Test
	public void testFindOne() {
		Security security = repository.getOne(id);

		assertThat(security.getId()).isEqualTo(id);
		assertThat(security.getWkn()).isEqualTo(wkn);
	}

}