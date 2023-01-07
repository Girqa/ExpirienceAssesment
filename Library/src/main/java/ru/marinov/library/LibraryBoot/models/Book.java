package ru.marinov.library.LibraryBoot.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "book")
public class Book {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Название книги не может быть пустым")
    @Column(name = "title")
    private String title;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @Pattern(regexp = "([А-ЯЁ][а-яё]+[\\-\\s]?){2,}", message = "Автор указывается как: Фамилия Имя")
    @Column(name = "author")
    private String author;

    @Min(value = 1666, message = "Год написания книги не может быть меньше 1666")
    @Max(value = 2021, message = "Год написания книги не может быть больше 2021")
    @Column(name = "year")
    private int year;

    @Column(name = "booked_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date bookedAt;

    @Transient
    private boolean isOverdue;

    @ManyToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id")
    private Person owner;

    public Book(String title, String author, int year) {
        this.title = title;
        this.author = author;
        this.year = year;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                '}';
    }
}
