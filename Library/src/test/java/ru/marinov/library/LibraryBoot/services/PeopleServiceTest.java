package ru.marinov.library.LibraryBoot.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.marinov.library.LibraryBoot.models.Book;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.repositories.PeopleRepository;
import ru.marinov.library.LibraryBoot.services.PeopleService;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class PeopleServiceTest {
    @Mock
    private PeopleRepository peopleRepository;
    @InjectMocks
    private PeopleService peopleService;

    @Test
    public void shouldReturnFourBooksTwoWithOverdueTrue() {
        Person person = new Person();
        person.setBooks(getBooks());
        Mockito.when(peopleRepository.findById(0)).thenReturn(Optional.of(person));

        List<Book> result = peopleService.findBooks(0);
        assertEquals(4, result.size());
        assertEquals(2, result.stream().filter(Book::isOverdue).count());
    }

    private List<Book> getBooks() {
        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();
        Book book4 = new Book();

        Person owner = new Person("Owner Owner Owner", 1999);

        book1.setAuthor("Брюс Эккель");
        book1.setTitle("Философия Java");
        book1.setYear(1998);
        book1.setOwner(owner);
        book1.setBookedAt(new Date(System.currentTimeMillis() - 11*24*3600*1000));

        book2.setAuthor("Николай Гоголь");
        book2.setTitle("Мертвые души");
        book2.setYear(1842);
        book2.setOwner(owner);
        book2.setBookedAt(new Date());

        book3.setAuthor("Эрнест Хемингуэй");
        book3.setTitle("Старик и море");
        book3.setYear(1952);
        book3.setOwner(owner);
        book3.setBookedAt(new Date(System.currentTimeMillis() - 11*24*3600*1000));

        book4.setAuthor("Михаил Шолохов");
        book4.setTitle("Судьба человека");
        book4.setYear(1957);
        book4.setOwner(owner);
        book4.setBookedAt(new Date());


        return List.of(book1, book2, book3, book4);
    }
}
