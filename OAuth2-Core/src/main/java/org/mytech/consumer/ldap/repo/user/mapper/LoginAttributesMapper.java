package org.mytech.consumer.ldap.repo.user.mapper;

import static org.mytech.consumer.ldap.repo.user.impl.IConstant.S_AM_ACCOUNT_NAME;
import static org.mytech.consumer.ldap.repo.user.impl.IConstant.USER_PRINCIPAL_NAME;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import org.mytech.consumer.entity.user.Login;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ldap.core.AttributesMapper;

public class LoginAttributesMapper implements AttributesMapper<Login> {

	private static final Logger LOG = LoggerFactory
			.getLogger(LoginAttributesMapper.class);

	public Login mapFromAttributes(Attributes attributes)
			throws NamingException {
		final Login l = new Login();
		l.setName(attributes.get(USER_PRINCIPAL_NAME) != null ? (String) attributes
				.get(USER_PRINCIPAL_NAME).get() : "");

		final String userName = attributes.get(S_AM_ACCOUNT_NAME) != null ? (String) attributes
				.get(S_AM_ACCOUNT_NAME).get() : "";
		l.setContactId(userName);

		LOG.debug("############LOGIN OBJ" + l);

		return l;

	}

}
