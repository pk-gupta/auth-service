package org.marlabs.consumer.ldap.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.core.support.LdapContextSource;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;

@Deprecated
//@Configuration
@PropertySource("classpath:application.properties")
public class LdapConfiguration {
    
    @Autowired
    private Environment env;

    private static final Logger LOG = LoggerFactory
	    .getLogger(LdapConfiguration.class);

    @Bean
    public LdapContextSource contextSource() {
	LOG.debug("***********ldap.url*****************"
		+ env.getRequiredProperty("ldap.url"));
	LdapContextSource contextSource = new LdapContextSource();
	contextSource.setUrl(env.getRequiredProperty("ldap.url"));
	contextSource.setBase(env.getRequiredProperty("ldap.base"));
	contextSource.setUserDn(env.getRequiredProperty("ldap.userDn"));
	contextSource.setPassword(env.getRequiredProperty("ldap.password"));
	contextSource.setReferral(env.getRequiredProperty("ldap.referral"));
	return contextSource;
    }

    @Bean
    public LdapTemplate ldapTemplate() {
	return new LdapTemplate(contextSource());
    }
    
    @Bean
    public FilterBasedLdapUserSearch ldapUserSearch() {
	LOG.debug("***********ldapUserSearch*************************************************************************************************"
		+ env.getRequiredProperty("ldap.url"));
	final FilterBasedLdapUserSearch ldapsearch =  new FilterBasedLdapUserSearch("", "(&amp;(objectclass=person)(sAMAccountName={0}))", contextSource());
	return ldapsearch;
    }

 
    public void setEnvironment(Environment env) {
	this.env = env;
	
    }
}
