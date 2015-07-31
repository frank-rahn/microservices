/*
 * Copyright © 2015 by Frank W. Rahn. Alle Rechte vorbehalten. All rights reserved.
 */
package de.rahn.finances.service.impl;

import static javax.transaction.Transactional.TxType.SUPPORTS;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import de.rahn.finances.domain.Security;
import de.rahn.finances.repository.SecuritiesRepository;
import de.rahn.finances.service.SecuritiesService;

/**
 * Die Implementierung des {@link SecuritiesService}.
 * @author Frank W. Rahn
 */
@Service
public class SecuritiesServiceImpl implements SecuritiesService {

	@Autowired
	private SecuritiesRepository repository;

	/**
	 * {@inheritDoc}
	 * @see de.rahn.finances.service.SecuritiesService#getSecurities()
	 */
	@Override
	@Transactional(SUPPORTS)
	public List<Security> getSecurities() {
		return repository.findAll();
	}

}