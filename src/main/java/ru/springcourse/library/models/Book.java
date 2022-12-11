package ru.springcourse.library.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class Book {
    private int id;
    @NotEmpty(message = "Название книги не может быть пустым")
    private String title;

    @NotEmpty(message = "Имя автора не может быть пустым")
    @Pattern(regexp = "([А-ЯЁ][а-яё]+[\\-\\s]?){2,}", message = "Автор указывается как: Фамилия Имя")
    private String author;

    @Min(value = 1666, message = "Год написания книги не может быть меньше 1666")
    @Max(value = 2021, message = "Год написания книги не может быть больше 2021")
    private int year;

    public Book(int id, String title, String author, int year) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
    }
}
