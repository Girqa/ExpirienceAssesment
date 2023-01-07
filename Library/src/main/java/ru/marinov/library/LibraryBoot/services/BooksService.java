package ru.marinov.library.LibraryBoot.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.marinov.library.LibraryBoot.models.Book;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.repositories.BooksRepository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
public class BooksService {
    private final BooksRepository booksRepository;
    private final EntityManagerFactory entityManagerFactory;
    @Autowired
    public BooksService(BooksRepository booksRepository, EntityManagerFactory entityManagerFactory) {
        this.booksRepository = booksRepository;
        this.entityManagerFactory = entityManagerFactory;
    }

    public List<Book> findAll() {
        return booksRepository.findAll();
    }
    public List<Book> findAll(Integer page, Integer booksPerPage) {
        if (page == null || booksPerPage == null) {
            return findAll();
        }
        return booksRepository.findAll(PageRequest.of(page, booksPerPage)).getContent();
    }

    public List<Book> findAll(Boolean sortByYear) {
        if (sortByYear == null) {
            return findAll();
        }
        return sortByYear ? booksRepository.findAll(Sort.by("year")) : findAll();
    }

    public List<Book> findAll(Integer page, Integer booksPerPage, Boolean sortByYear) {
        if (page == null || booksPerPage == null || sortByYear == null) {
            return findAll();
        } else if (sortByYear) {
            return booksRepository.findAll(PageRequest.of(page, booksPerPage, Sort.by("year"))).getContent();
        }
        return findAll(page, booksPerPage);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Book> findById(int id) {
        return booksRepository.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public Optional<Person> getBookOwner(Book book) {
        if (book == null) return Optional.empty();
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.merge(book);
        Optional<Person> owner = Optional.ofNullable(book.getOwner());
        return owner;
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void save(Book book) {
        booksRepository.save(book);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteById(int id) {
        booksRepository.deleteById(id);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void update(int id, Book updatedBook) {
        updatedBook.setId(id);
        booksRepository.save(updatedBook);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteOwner(int id) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent() && book.get().getOwner() != null) {
            book.get().setOwner(null);
            book.get().setBookedAt(null);
        }
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void addOwner(int id, Person person) {
        Optional<Book> book = booksRepository.findById(id);
        if (book.isPresent() && book.get().getOwner() == null) {
            book.get().setOwner(person);
            book.get().setBookedAt(new Date());
        }
    }

    public List<Book> findByTitleStartingWith(String startingWith) {
        return booksRepository.findByTitleIgnoreCaseStartingWithOrderByTitle(startingWith);
    }
}
