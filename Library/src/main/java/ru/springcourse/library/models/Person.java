package ru.springcourse.library.models;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@Data
@NoArgsConstructor
public class Person {
    private int id;
    @NotEmpty(message = "ФИО не может быть пустым")
    @Size(min = 3, max = 100, message = "Полное имя слишком большое/маленькое")
    @Pattern(regexp = "([А-ЯЁ][а-яё]+[\\-\\s]?){3,}", message = "ФИО должно быть в формате: Фамилия Имя Отчество")
    private String fullName;
    @Min(value = 1666, message = "Минимальное значение - 1666")
    @Max(value = 2022, message = "Максимальное значение - 2022")
    private int yearOfBirth;

    public Person(int id, String fullName, int yearOfBirth) {
        this.id = id;
        this.fullName = fullName;
        this.yearOfBirth = yearOfBirth;
    }

}
