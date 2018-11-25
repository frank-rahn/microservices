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
package de.rahn.finances.server.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Die Konfiguration für Spring Security Web.
 *
 * @author Frank W. Rahn
 */
@Configuration
public class WebSecurityConfiguration {

	/**
	 * Die globale Konfiguration für Spring Security.<br>
	 * Hier wird der {@link AuthenticationProvider} für InMemory definiert.
	 *
	 * @see WebSecurityConfigurerAdapter#configure(AuthenticationManagerBuilder)
	 */
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		// User mit erweiterten Rechten
		auth.inMemoryAuthentication().withUser("admin").password("{noop}admin").roles("USER", "ADMIN", "ACTUATOR");

		// User nur mit Leserechten
		auth.inMemoryAuthentication().withUser("user").password("{noop}user").roles("USER");

		// User mit einem ungültigem Rechten
		auth.inMemoryAuthentication().withUser("gast").password("{noop}gast").roles("GAST");
	}

	/**
	 * Diese WebSecurity-Konfiguration ist für das Management-API der Anwendung (/actuator/*) mit HTTP-basic Authentication.
	 *
	 * @author Frank W. Rahn
	 */
	@Configuration
	@Order(1)
	public class ManagementAPIWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		/**
		 * {@inheritDoc}
		 *
		 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off

			// Konfiguration der Requests auf dise URL
			http.antMatcher("/actuator/*")
				.authorizeRequests()
					.anyRequest()
						// Alle Request benötigen einen User mit der Rolle USER
						.hasRole("ADMIN")
			;

			// Konfiguration der HTTP Basic Authentication
			http.httpBasic()
				// Vergabe des Realm Namens
				.realmName("Management-API")
			;

			// @formatter:on
		}

	}

	/**
	 * Diese WebSecurity-Konfiguration ist für die Konsole der Datenbank (H2) (/h2-console/*).<br>
	 * Die Konsole kommt mit einer eigenen Security, daher wird hier <code>permitAll()</code> gesetzt.
	 *
	 * @author Frank W. Rahn
	 */
	@Configuration
	@Order(2)
	public class H2ConsoleWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		/**
		 * {@inheritDoc}
		 *
		 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off

			// Konfiguration der Requests auf diese URL
			http.antMatcher("/h2-console/**")
				.authorizeRequests()
					// Alle dürfen auf die Console zugreifen
					.anyRequest().permitAll()
			;

			// Konfiguration der HTTP Header-Attribute
			http.headers()
				// Attribut "X-Frame-Options" abschalten
				.frameOptions().disable()
			;

			// Konfiguration des CSRF-Supports
			http.csrf()
				// Den CSRF-Support abschalten
				.disable()
			;

			// @formatter:on
		}

	}

	/**
	 * Diese WebSecurity-Konfiguration für die Anwendung mit Form-based Authentication.
	 *
	 * @author Frank W. Rahn
	 */
	@Configuration
	public class FormLoginWebSecurityConfiguration extends WebSecurityConfigurerAdapter {

		/**
		 * {@inheritDoc}
		 *
		 * @see WebSecurityConfigurerAdapter#configure(HttpSecurity)
		 */
		@Override
		protected void configure(HttpSecurity http) throws Exception {
			// @formatter:off

			// Konfiguration der freien Ressources
			http.authorizeRequests()
				// Die Login-Seite benötigen die globalen Ressourcen der Seite
				.antMatchers("/css/style.css", "/js/script.js").permitAll()
			;

			// Konfiguration der berechtigten Requests
			http.authorizeRequests()
				// Alle Request benötigen einen User mit der Rolle USER
				.anyRequest().hasRole("USER")
			;

			// Konfiguration der Form-based Authentication
			http.formLogin()
				// Die URL der Login-Seite
				.loginPage("/login")
				// Alle dürfen auf die Login-Seite zugreifen
				.permitAll()
				// Diese Seite wird nach einem Fehler bei der Anmeldung angezeigt (Default)
				//.failureUrl("/login?error")
			;

			// Konfiguration der Abmeldung
			http.logout()
				// Alle dürfen sich abmelden
				.permitAll()
				// Diese Seite wird nach einem Fehler bei der Abmeldung angezeigt (Default)
				//.logoutSuccessUrl("/login?logout")
				// Beim Logout alle gesetzten Cookies wieder löschen
				.deleteCookies()
				// Request Mapper für /logout zum Abmelden
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
			;

			// @formatter:on
		}
	}

}