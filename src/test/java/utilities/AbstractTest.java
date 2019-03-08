/*
 * AbstractTest.java
 * 
 * Copyright (C) 2019 Universidad de Sevilla
 * 
 * The use of this project is hereby constrained to the conditions of the
 * TDG Licence, a copy of which you may download from
 * http://www.tdg-seville.info/License.html
 */

package utilities;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import security.LoginService;
import utilities.internal.EclipseConsole;

public abstract class AbstractTest {

	// Supporting services --------------------------------

	@Autowired
	private LoginService						loginService;
	@Autowired
	private JpaTransactionManager				transactionManager;

	// Internal state -------------------------------------

	private final DefaultTransactionDefinition	transactionDefinition;
	private TransactionStatus					currentTransaction;
	private final Properties					entityMap;


	// Constructor ----------------------------------------

	public AbstractTest() {
		EclipseConsole.fix();
		LogManager.getLogger("org.hibernate").setLevel(Level.OFF);

		this.transactionDefinition = new DefaultTransactionDefinition();
		this.transactionDefinition.setName("TestTransaction");
		this.transactionDefinition.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRES_NEW);

		try (InputStream stream = new FileInputStream(DatabaseConfig.entityMapFilename)) {
			this.entityMap = new Properties();
			this.entityMap.load(stream);
		} catch (final Throwable oops) {
			throw new RuntimeException(oops);
		}
	}
	// Set up and tear down -------------------------------

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	// Supporting methods ---------------------------------

	protected void authenticate(final String username) {
		UserDetails userDetails;
		TestingAuthenticationToken authenticationToken;
		SecurityContext context;

		if (username == null)
			authenticationToken = null;
		else {
			userDetails = this.loginService.loadUserByUsername(username);
			authenticationToken = new TestingAuthenticationToken(userDetails, null);
		}

		context = SecurityContextHolder.getContext();
		context.setAuthentication(authenticationToken);
	}

	protected void unauthenticate() {
		this.authenticate(null);
	}

	protected void checkExceptions(final Class<?> expected, final Class<?> caught) {
		if (expected != null && caught == null)
			throw new RuntimeException(expected.getName() + " was expected");
		else if (expected == null && caught != null)
			throw new RuntimeException(caught.getName() + " was unexpected");
		else if (expected != null && caught != null && !expected.equals(caught))
			throw new RuntimeException(expected.getName() + " was expected, but " + caught.getName() + " was thrown");
	}

	protected void startTransaction() {
		this.currentTransaction = this.transactionManager.getTransaction(this.transactionDefinition);
	}

	protected void commitTransaction() {
		this.transactionManager.commit(this.currentTransaction);
	}

	protected void rollbackTransaction() {
		this.transactionManager.rollback(this.currentTransaction);
	}

	protected void flushTransaction() {
		this.currentTransaction.flush();
	}

	protected boolean existsEntity(final String beanName) {
		assert beanName != null && beanName.matches("^[A-Za-z0-9\\-]+$");

		boolean result;

		result = this.entityMap.containsKey(beanName);

		return result;
	}

	protected int getEntityId(final String beanName) {
		assert beanName != null && beanName.matches("^[A-Za-z0-9\\-]+$");
		assert this.existsEntity(beanName);

		int result;
		String id;

		id = (String) this.entityMap.get(beanName);
		result = Integer.valueOf(id);

		return result;
	}

}
