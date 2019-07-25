package org.mytech.consumer.ldap.repo.user;

import java.util.List;

import org.mytech.consumer.ldap.entity.user.Person;

public interface PersonRepository {
    List<Person> findAllPersons();
    List<Person> findAllPagedPersons();
}
