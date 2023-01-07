package ru.marinov.library.LibraryBoot.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.repositories.PeopleRepository;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {
    @Mock
    private PeopleRepository peopleRepository;

    @InjectMocks
    private AdminService adminService;

    @Test
    public void shouldChangeRoleToAdmin() {
        Person person = new Person();
        person.setRole("ROLE_USER");
        Mockito.when(peopleRepository.findById(0)).thenReturn(Optional.of(person));

        adminService.promoteUser(0);

        Assertions.assertEquals("ROLE_ADMIN", person.getRole());
    }
}
