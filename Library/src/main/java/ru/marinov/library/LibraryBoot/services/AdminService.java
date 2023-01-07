package ru.marinov.library.LibraryBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.repositories.PeopleRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class AdminService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public AdminService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAllWithRoleUser() {
        return peopleRepository.findAllByRole("ROLE_USER");
    }

    @Transactional
    public void promoteUser(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isEmpty()) {
            return;
        }
        person.get().setRole("ROLE_ADMIN");
    }
}
