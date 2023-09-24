package com.alik.project2withboot.services;


import com.alik.project2withboot.models.Person;
import com.alik.project2withboot.repositories.PeopleRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    private final EntityManager entityManager;

    @Autowired
    public PeopleService(PeopleRepository peopleRepository, EntityManager entityManager) {
        this.peopleRepository = peopleRepository;
        this.entityManager = entityManager;
    }

    public List<Person> index() {
        Session session = entityManager.unwrap(Session.class);
        return session.createQuery("select p from Person p left join fetch p.books").getResultList();
    }

    public Person show(int person_id) {
        Person person = peopleRepository.findById(person_id).orElse(null);
        getBooks(person);
        return person;
    }

    public Person show(String name) {
        return peopleRepository.findByName(name).orElse(null);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void update(int person_id, Person updatePerson) {
        updatePerson.setPerson_id(person_id);
        peopleRepository.save(updatePerson);
    }

    @Transactional
    public void delete(int person_id) {
        peopleRepository.deleteById(person_id);
    }


    private void getBooks(Person person) {
        if (Objects.nonNull(person)) {
            Hibernate.initialize(person.getBooks());
        }
    }

}
