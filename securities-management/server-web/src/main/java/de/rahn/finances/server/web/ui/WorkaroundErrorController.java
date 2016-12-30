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
package de.rahn.finances.server.web.ui;

import static org.springframework.boot.autoconfigure.web.ErrorProperties.IncludeStacktrace.ALWAYS;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.BasicErrorController;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.stereotype.Controller;

/**
 * <strong>Workaround für <a href="https://github.com/spring-projects/spring-boot/issues/5638">Bug 5638</a></strong>.<br>
 * <br>
 * Durch diesen Bug wird auf der Error-Seite kein Security Context zu Verfügung gestellt.<br>
 * Angeblich nur wenn <code>spring-boot-actuator</code> im Klassenpfad ist ...
 *
 * @author Frank W. Rahn
 */
@Controller
public class WorkaroundErrorController extends BasicErrorController {

	/**
	 * {@inheritDoc}
	 */
	@Autowired
	public WorkaroundErrorController(ErrorAttributes errorAttributes) {
		super(errorAttributes, new ErrorProperties() {
			{
				setIncludeStacktrace(ALWAYS);
			}
		});
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getErrorPath() {
		return "/__dummyErrorPath";
	}

}