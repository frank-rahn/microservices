/*
 * Copyright 2011-2016 Frank W. Rahn and the project authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.rahn.finances.services.securities;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import de.rahn.finances.domains.entities.Entry;
import de.rahn.finances.domains.entities.Security;
import de.rahn.finances.domains.entities.SecurityType;
import de.rahn.finances.domains.repositories.EntriesRepository;
import de.rahn.finances.domains.repositories.SecuritiesRepository;
import de.rahn.finances.services.EntryNotFoundException;
import de.rahn.finances.services.SecuritiesService;
import de.rahn.finances.services.SecurityNotFoundException;

/**
 * Die Implementierung des {@link SecuritiesService}.
 *
 * @author Frank W. Rahn
 */
@Service
@Transactional(SUPPORTS)
@PreAuthorize("hasRole('USER')")
@Description("Die Implementierung des SecuritiesService")
public class SecuritiesServiceImpl implements SecuritiesService {

	private final SecuritiesRepository securitiesRepository;

	private final EntriesRepository entriesRepository;

	/**
	 * Konstruktor.
	 */
	@Autowired
	public SecuritiesServiceImpl(SecuritiesRepository securitiesRepository, EntriesRepository entriesRepository) {
		super();

		this.securitiesRepository = securitiesRepository;
		this.entriesRepository = entriesRepository;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#getSecurities()
	 */
	@Override
	public List<Security> getSecurities() {
		return securitiesRepository.findAll();
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#getSecurity(String)
	 */
	@Override
	public Security getSecurity(String id) {
		Security security = securitiesRepository.findOne(id);

		if (security == null) {
			throw new SecurityNotFoundException(id);
		}

		return security;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#getSecurities(boolean, SecurityType, Pageable)
	 */
	@Override
	public Page<Security> getSecurities(boolean inventory, SecurityType type, Pageable pageable) {
		Page<Security> page;
		if (type == null) {
			page = securitiesRepository.findByInventoryOrderByIsin(pageable, inventory);
		} else {
			page = securitiesRepository.findByInventoryAndTypeOrderByIsin(pageable, inventory, type);
		}

		if (pageable != null) {
			if (page.getTotalPages() == 0 && pageable.getPageNumber() > 0
				|| page.getTotalPages() != 0 && page.getTotalPages() <= pageable.getPageNumber()) {
				// Angeforderte Page außerhalb des zulässigen Bereiches
				int maxPage = page.getTotalPages() == 0 ? 0 : page.getTotalPages() - 1;
				return getSecurities(inventory, type, new PageRequest(maxPage, pageable.getPageSize(), pageable.getSort()));
			}
		}

		return page;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#save(Security)
	 */
	@Override
	@Transactional(REQUIRED)
	public Security save(Security security) {
		return securitiesRepository.save(security);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#delete(Security)
	 */
	@Override
	@Transactional(REQUIRED)
	public void delete(Security security) {
		securitiesRepository.delete(security);
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#getEntry(java.lang.String)
	 */
	@Override
	@Transactional(REQUIRED)
	public Entry getEntry(String id) {
		Entry entry = entriesRepository.findOne(id);

		if (entry == null) {
			throw new EntryNotFoundException(id);
		}

		return entry;
	}

	/**
	 * {@inheritDoc}
	 *
	 * @see SecuritiesService#save(Entry)
	 */
	@Override
	@Transactional(REQUIRED)
	public Entry save(Entry entry) {
		if (entry.getId() != null && entry.getSecurity() == null) {
			// Das Wertpapier wieder hinzufügen
			return entriesRepository.save(getEntry(entry.getId()).update(entry));
		}

		return entriesRepository.save(entry);
	}

}