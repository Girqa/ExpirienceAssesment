package ru.marinov.library.LibraryBoot.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.services.PeopleService;

import java.util.Optional;

@Component
public class PersonValidator implements Validator {
    private final PeopleService peopleService;
    @Autowired
    public PersonValidator(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Person.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;
        Optional<Person> dataBasePerson = peopleService.findByUsername(person.getUsername());
        if (dataBasePerson.isPresent()) {
            errors.rejectValue("username", "", "Человек с таким именем пользователя существует");
        }
        return;
    }
}
