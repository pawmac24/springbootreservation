package com.pm.reservation.service;

import com.pm.reservation.model.Person;
import com.pm.reservation.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
@Service
@Transactional
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void save(Person person) {
        personRepository.save(person);
    }

    @Override
    public void deleteAll() {
        personRepository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        personRepository.delete(id);
    }

    @Override
    public Long deleteByPesel(String pesel) {
        return personRepository.deleteByPesel(pesel);
    }

    @Override
    public Iterable<Person> findAll() {
        return personRepository.findAll();
    }

}
