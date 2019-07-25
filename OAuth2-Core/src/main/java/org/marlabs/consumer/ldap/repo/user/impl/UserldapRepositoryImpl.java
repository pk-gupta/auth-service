package org.marlabs.consumer.ldap.repo.user.impl;

import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.EMPTY_STR;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.MAIL;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.OBJECTCLASS;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.PERSON;
import static org.marlabs.consumer.ldap.repo.user.impl.IConstant.S_AM_ACCOUNT_NAME;
import static org.springframework.ldap.query.LdapQueryBuilder.query;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.naming.directory.SearchControls;

import org.marlabs.consumer.entity.user.Login;
import org.marlabs.consumer.ldap.entity.user.Person;
import org.marlabs.consumer.ldap.repo.user.LoginRepository;
import org.marlabs.consumer.ldap.repo.user.PersonRepository;
import org.marlabs.consumer.ldap.repo.user.mapper.LoginAttributesMapper;
import org.marlabs.consumer.ldap.repo.user.mapper.PersonMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.control.PagedResultsCookie;
import org.springframework.ldap.control.PagedResultsDirContextProcessor;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.filter.EqualsFilter;
import org.springframework.ldap.filter.Filter;
import org.springframework.stereotype.Repository;

@Repository
public class UserldapRepositoryImpl implements LoginRepository,
	PersonRepository {

    private static final Logger LOG = LoggerFactory
	    .getLogger(UserldapRepositoryImpl.class);

    @Autowired
    private LdapTemplate ldapTemplate;

    public Login authenticateByEmail(final String email, final String password) {

	LOG.info("########################authenticateByEmail###########################################START>>>>>");
	final Filter f = new EqualsFilter(MAIL, email);
	final boolean authed = ldapTemplate.authenticate(EMPTY_STR, f.encode(),
		password);
	LOG.debug("########################LDAP AUTH RESULT###########################################"
		+ authed);
	final Login login = (authed == true) ? new Login(email, password)
		: null;
	LOG.info("########################authenticateByEmail###########################################END>>>>>");
	return login;

    }

    public Login findByContactId(final String contactId) {

	// http://blog.javachap.com/index.php/ldap-user-management-with-spring-ldap/
	LOG.info("########################findByContactId###########################################START>>>>>");
	final Optional<List<Login>> login = Optional
		.ofNullable(ldapTemplate
			.search(query().base("").where(S_AM_ACCOUNT_NAME)
				.is(contactId), new LoginAttributesMapper()));

	LOG.debug("########################QUERY STRING########################"
		+ query().filter().toString());
	if (login.isPresent() && login.get().size() == 1) {
	    final Login l = login.get().get(0);
	    LOG.info("########################findByContactId###########################################END with>>>>>"
		    + l);
	    return l;
	}
	LOG.info("########################findByContactId###########################################END with null>>>>>");
	return null;

    }

    public List<Person> findAllPagedPersons() {

	PagedResultsCookie cookie = null;
	
	final SearchControls searchControls =  new SearchControls();
	searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
	
	int page = 1;
	final List<Person> ps =  new ArrayList<>();
	
	do{
	    LOG.info("########################page size###########################################>>>>>"+page);
	    final PagedResultsDirContextProcessor processor = new PagedResultsDirContextProcessor(20,cookie);
	    final EqualsFilter equalsFilter =  new EqualsFilter(OBJECTCLASS, PERSON);
	    final List<Person> persons = ldapTemplate.search("",equalsFilter.encode(), searchControls,new PersonMapper(),processor);
	    ps.addAll(persons);
	    cookie = processor.getCookie();
	    page = page + 1;
	    
	}while(null!=cookie.getCookie());
	
	return ps;
    }

    public void setLdapTemplate(LdapTemplate ldapTemplate) {
	this.ldapTemplate = ldapTemplate;
    }

    @Override
    public List<Person> findAllPersons() {

	LOG.info("########################findAllPagedPersons###########################################START>>>>>");
	final List<Person> ps = ldapTemplate.search(query().where(OBJECTCLASS)
		.is(PERSON), new PersonMapper());
	LOG.info("########################findAllPagedPersons###########################################ENDS>>>>>"
		+ ps);
	return ps;
    }

}
