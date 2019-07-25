package org.marlabs.consumer.ldap.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.marlabs.consumer.entity.user.Login;
import org.marlabs.consumer.ldap.entity.user.Person;
import org.marlabs.consumer.ldap.repo.user.LoginRepository;
import org.marlabs.consumer.ldap.repo.user.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.resource.OAuth2AccessDeniedException;

//@Service
public class CustomLdapAuthManager implements AuthenticationManager {
    
    private static final Logger LOG = LoggerFactory
		.getLogger(CustomLdapAuthManager.class);


    @Autowired
    private LoginRepository userLdapRepository;
    
    @Autowired
    private PersonRepository personRepository;


    public Authentication authenticate(Authentication authentication)
	    throws AuthenticationException {
	final String name = authentication.getName();
	final String password = authentication.getCredentials().toString();

	final Login lo = userLdapRepository.authenticateByEmail(name, password);
	if (null != lo) {
		 return new UsernamePasswordAuthenticationToken(
				    new LdapLoginUserDetails(new Login(name, null)), password,
				    new ArrayList<>());
	} else {
	    throw new OAuth2AccessDeniedException("Ldap Authentication failed");
	}
    }

    public boolean supports(Class<?> authentication) {
	return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }

    private final static class LdapLoginUserDetails extends Login implements
	    UserDetails {

	private static final long serialVersionUID = 1L;

	private LdapLoginUserDetails(Login user) {
	    super(user);
	}


	
	public String getUsername() {
	    return getContactId();
	}

	
	public boolean isAccountNonExpired() {
	    return true;
	}

	
	public boolean isAccountNonLocked() {
	    return true;
	}

	
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	
	public boolean isEnabled() {
	    return true;
	}



	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return getRoles();
	}

    }
    
        /*List<Person> persons = personRepository.findAllPagedPersons();
	
	ObjectMapper mapper = new ObjectMapper();
	

	//Object to JSON in file
	try {
	    mapper.writeValue(new File("d:\\file.json"), persons);
	} catch (JsonGenerationException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (JsonMappingException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	} catch (IOException e) {
	    // TODO Auto-generated catch block
	    e.printStackTrace();
	}

	
	LOG.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+persons.size());
	LOG.debug(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>"+persons.size());*/

}
