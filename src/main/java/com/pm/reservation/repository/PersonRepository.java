package com.pm.reservation.repository;

import com.pm.reservation.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

/**
 * Created by pmackiewicz on 2015-09-21.
 */
public interface PersonRepository extends JpaRepository<Person, Long> {
    Long deleteByPesel(String pesel);
}
