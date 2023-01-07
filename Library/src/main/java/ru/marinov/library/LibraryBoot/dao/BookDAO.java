package ru.marinov.library.LibraryBoot.dao;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.marinov.library.LibraryBoot.models.Book;
import ru.marinov.library.LibraryBoot.models.Person;

import java.util.List;
import java.util.Optional;

@Component
public class BookDAO {
    private final JdbcTemplate jdbcTemplate;

    public BookDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Book> index() {
        return jdbcTemplate.query("SELECT * FROM Book Order BY id", new BeanPropertyRowMapper<>(Book.class));
    }
    public Optional<Book> show(int id) {
        return jdbcTemplate.query("SELECT * FROM Book WHERE id=?", new Object[]{id},
                new BeanPropertyRowMapper<>(Book.class)).stream().findAny();
    }

    public void save(Book book) {
        jdbcTemplate.update("INSERT INTO Book(title, author, year) VALUES(?, ?, ?)",
                book.getTitle(), book.getAuthor(), book.getYear());
    }

    public void remove(int id) {
        jdbcTemplate.update("DELETE FROM Book WHERE id=?", id);
    }

    public void update(int id, Book updatedBooks) {
        jdbcTemplate.update("UPDATE Book SET title=?, author=?, year=? WHERE id=?",
                updatedBooks.getTitle(), updatedBooks.getAuthor(), updatedBooks.getYear(), id);
    }

    public Optional<Person> getOwner(int id) {
        return jdbcTemplate.query("SELECT full_name, year_of_birth FROM person\n" +
                        "JOIN book b on person.id = b.person_id\n" +
                        "WHERE b.id = ?",
                new Object[]{id}, new BeanPropertyRowMapper<>(Person.class))
                .stream().findAny();
    }

    public void removeOwner(int bookId) {
        jdbcTemplate.update("UPDATE Book SET person_id = null WHERE id = ?", bookId);
    }

    public void addOwner(int id, Person person) {
        jdbcTemplate.update("UPDATE Book SET person_id=? WHERE id=?", person.getId(), id);
    }
}
