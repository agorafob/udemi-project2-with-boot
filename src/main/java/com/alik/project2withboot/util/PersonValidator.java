package com.alik.project2withboot.util;


import com.alik.project2withboot.models.Person;
import com.alik.project2withboot.services.PeopleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Objects;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;

    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Person.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Person person = (Person) target;
        if (Objects.nonNull(peopleService.show(person.getName())) &&
                peopleService.show(person.getPerson_id()).getPerson_id() != person.getPerson_id()) {
            errors.rejectValue("name", "", "Name should be unique");
        }
    }
}
