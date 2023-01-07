package ru.marinov.library.LibraryBoot.config;

import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.marinov.library.LibraryBoot.services.PeopleService;
import ru.marinov.library.LibraryBoot.util.PersonValidator;

@Configuration
public class TestConfiguration {
    @Bean
    public PeopleService peopleService() {
        return Mockito.mock(PeopleService.class);
    }

    @Bean
    public PersonValidator userValidator() {
        return new PersonValidator(peopleService());
    }
}
