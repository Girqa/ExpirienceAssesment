package ru.springcourse.library.util;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.springcourse.library.models.Person;

public class PersonValidate implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return aClass.equals(Person.class);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Person person = (Person) o;

    }
}
