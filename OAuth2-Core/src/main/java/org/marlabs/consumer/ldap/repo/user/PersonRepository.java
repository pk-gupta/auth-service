package org.marlabs.consumer.ldap.repo.user;

import java.util.List;

import org.marlabs.consumer.ldap.entity.user.Person;

public interface PersonRepository {
    List<Person> findAllPersons();
    List<Person> findAllPagedPersons();
}
