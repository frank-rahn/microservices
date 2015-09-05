/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service.impl;

import static javax.transaction.Transactional.TxType.REQUIRED;
import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Description;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import de.rahn.finances.domain.Security;
import de.rahn.finances.repository.SecuritiesRepository;
import de.rahn.finances.service.SecuritiesService;

/**
 * Die Implementierung des {@link SecuritiesService}.
 * @author Frank W. Rahn
 */
@Service
@Transactional(SUPPORTS)
@Description("Die Implementierung des SecuritiesService")
public class SecuritiesServiceImpl implements SecuritiesService {
	@Autowired
	private SecuritiesRepository repository;

	/**
	 * {@inheritDoc}
	 * @see SecuritiesService#getSecurities()
	 */
	@Override
	public List<Security> getSecurities() {
		return repository.findAll();
	}

	/**
	 * {@inheritDoc}
	 * @see SecuritiesService#getSecurity(String)
	 */
	@Override
	public Security getSecurity(String id) {
		return repository.findOne(id);
	}

	/**
	 * {@inheritDoc}
	 * @see SecuritiesService#getSecurities(Pageable)
	 */
	@Override
	public Page<Security> getSecurities(Pageable pageable) {
		return repository.findByInventoryOrType(pageable, false, null);
	}

	/**
	 * {@inheritDoc}
	 * @see SecuritiesService#save(Security)
	 */
	@Override
	@Transactional(REQUIRED)
	public Security save(Security security) {
		return repository.save(security);
	}

	/**
	 * {@inheritDoc}
	 * @see SecuritiesService#delete(Security)
	 */
	@Override
	@Transactional(REQUIRED)
	public void delete(Security security) {
		repository.delete(security.getId());
	}
}