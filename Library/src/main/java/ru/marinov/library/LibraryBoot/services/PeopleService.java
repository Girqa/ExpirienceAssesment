package ru.marinov.library.LibraryBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marinov.library.LibraryBoot.models.Book;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.repositories.PeopleRepository;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class PeopleService {
    private final PeopleRepository peopleRepository;
    @Autowired
    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    public List<Person> findAll() {
        return peopleRepository.findAll();
    }

    public Optional<Person> findById(int id) {
        return peopleRepository.findById(id);
    }

    public Optional<Person> findByUsername(String username) {
        return peopleRepository.findByUsername(username);
    }

    @Transactional
    public void save(Person person) {
        peopleRepository.save(person);
    }

    @Transactional
    public void deleteById(int id) {
        peopleRepository.deleteById(id);
    }

    @Transactional
    public void update(int id, Person person) {
        person.setId(id);
        peopleRepository.save(person);
    }
    public List<Book> findBooks(int id) {
        Optional<Person> person = peopleRepository.findById(id);
        if (person.isPresent()) {
            List<Book> books = person.get().getBooks();
            long currentTime = new Date().getTime();
            long overdueInterval = 1000 * 3600 * 24 * 10;  // Просрочено через 10 и более дней
            for (Book book: books) {
                if (book.getBookedAt() != null && currentTime - book.getBookedAt().getTime() >= overdueInterval) {
                    book.setOverdue(true);
                }
            }
            return person.get().getBooks();
        }
        return Collections.emptyList();
    }
}
