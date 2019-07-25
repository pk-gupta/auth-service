package org.marlabs.consumer.ldap.repo.user;

import org.marlabs.consumer.entity.user.Login;

//<!-- ldap START -->
public interface LoginRepository {
    Login authenticateByEmail(final String email, final String pwd);

}
// <!-- ldap ENDS -->
