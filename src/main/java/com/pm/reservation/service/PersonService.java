package com.pm.reservation.service;

import com.pm.reservation.model.Person;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
public interface PersonService {
    void save(Person person);

    void deleteAll();

    void deleteById(Long id);

    Long deleteByPesel(String pesel);

    Iterable<Person> findAll();
}
