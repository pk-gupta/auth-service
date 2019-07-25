package org.marlabs.consumer.ldap.repo.user.mapper;

import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.CN;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.DEPARTMENT;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.GIVEN_NAME;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.LOC;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.MAIL;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.MANAGER;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.S_AM_ACCOUNT_NAME;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.TELEPHONE_NUMBER;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.USER_PRINCIPAL_NAME;

import java.util.Arrays;
import java.util.List;

import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;

import org.marlabs.consumer.ldap.entity.user.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;

public class PersonMapper implements AttributesMapper<Person> {

	private static final Logger LOG = LoggerFactory
			.getLogger(PersonMapper.class);

	public Person mapFromAttributes(Attributes attributes)
			throws NamingException {

		final Person p = new Person();

		final String givenName = attributes.get(GIVEN_NAME) != null ? (String) attributes
				.get(GIVEN_NAME).get() : "";
		p.setGivenName(givenName);

		final String fullName = attributes.get(CN) != null ? (String) attributes
				.get(CN).get() : "";
		p.setCn(fullName);

		final String userName = attributes.get(S_AM_ACCOUNT_NAME) != null ? (String) attributes
				.get(S_AM_ACCOUNT_NAME).get() : "";
		p.setUid(userName);

		final String email = attributes.get(MAIL) != null ? (String) attributes
				.get(MAIL).get() : "";
		p.setMail(email);

		final String telephoneNumber = attributes.get(TELEPHONE_NUMBER) != null ? (String) attributes
				.get(TELEPHONE_NUMBER).get() : "";
		p.setTelephoneNumber(telephoneNumber);

		final String department = attributes.get(DEPARTMENT) != null ? (String) attributes
				.get(DEPARTMENT).get() : "";
		p.setDepartment(department);

		final String off = attributes.get(LOC) != null ? (String) attributes
				.get(LOC).get() : "";
		p.setLoc(off);

		final String userPrincipalName = attributes.get(USER_PRINCIPAL_NAME) != null ? (String) attributes
				.get(USER_PRINCIPAL_NAME).get() : "";
		p.setUserPrincipalName(userPrincipalName);

		final Attribute mAt = attributes.get(MANAGER);

		if (mAt != null) {
			final NamingEnumeration<?> mNas = mAt.getAll();
			while (mNas.hasMore()) {
				final String v = (String) mNas.next();
				final List<String> vs = Arrays.asList(v.split(","));

				final String mngr = vs.get(0).split("CN=")[1];
				p.setManager(mngr);
			}

		}

		LOG.debug("############Person=" + p);
		return p;
	}
}
