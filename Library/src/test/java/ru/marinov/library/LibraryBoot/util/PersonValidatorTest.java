package ru.marinov.library.LibraryBoot.util;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;
import org.springframework.validation.Errors;
import ru.marinov.library.LibraryBoot.config.TestConfiguration;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.services.PeopleService;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestConfiguration.class,
        loader = AnnotationConfigContextLoader.class)
public class PersonValidatorTest {

    @Autowired
    private PersonValidator personValidator;

    // mocked dependencies
    @Autowired
    private PeopleService peopleService;

    private static final String username = "username";
    private static final Person person = mock(Person.class);

    @BeforeAll
    public static void setup() {
        when(person.getUsername()).thenReturn(username);
    }

    @Test
    public void validateShouldAcceptPersonWithNewUsername() {
        when(peopleService.findByUsername(username)).thenReturn(Optional.ofNullable(null));
        Errors errors = mock(Errors.class);

        personValidator.validate(person, errors);
        verify(errors, never()).rejectValue(any(), any(), any());
    }

    @Test
    public void validateShouldRejectPersonWithOldUsername() {
        when(peopleService.findByUsername(username)).thenReturn(Optional.of(person));
        Errors errors = mock(Errors.class);

        personValidator.validate(person, errors);
        verify(errors, times(1)).rejectValue(any(), any(), any());
    }
}
