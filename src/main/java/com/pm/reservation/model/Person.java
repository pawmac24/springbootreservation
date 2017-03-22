package com.pm.reservation.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * Created by pmackiewicz on 2015-09-17.
 */
@Entity
@Table(name = "person")
public class Person implements Serializable {

    private static final long serialVersionUID = 5791617637831229650L;

    @Id
    //@GeneratedValue(strategy= GenerationType.AUTO)
    @SequenceGenerator(name="person_seq", sequenceName="person_seq", allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="person_seq")
    private Long id;

    private String name;

    private String surname;

    private String pesel;

    @OneToMany(fetch = FetchType.LAZY, mappedBy="person", cascade= CascadeType.ALL, orphanRemoval=true)
    private Set<Reservation> reservations;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public Set<Reservation> getReservations() {
        return reservations;
    }

    public void setReservations(Set<Reservation> reservations) {
        this.reservations = reservations;
    }

    @Override
    public String toString() {
        return "Person{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", pesel='" + pesel + '\'' +
                '}';
    }
}
