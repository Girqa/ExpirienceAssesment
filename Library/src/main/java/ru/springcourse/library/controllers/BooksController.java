package ru.springcourse.library.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.springcourse.library.dao.BookDAO;
import ru.springcourse.library.dao.PersonDAO;
import ru.springcourse.library.models.Book;
import ru.springcourse.library.models.Person;

import javax.validation.Valid;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
    }


    @GetMapping()
    public String index(Model model){
        model.addAttribute("books", bookDAO.index());
        log.info("Books page was gotten");
        return "books/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id,
                        Model model,
                       @ModelAttribute("person") Person person) {
        Book book = bookDAO.show(id).get();
        model.addAttribute("book", book);

        Optional<Person> owner = bookDAO.getOwner(id);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        }
        else {
            model.addAttribute("owner", null);
            model.addAttribute("people", personDAO.index());
        }

        return "books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book) {
        log.info("Creating new book");
        return "books/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("book") @Valid Book book,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.warn("Unsuccessfully created new book {}", book);
            return "books/new";
        }
        bookDAO.save(book);
        log.warn("Successfully created new book {}", book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String remove(@PathVariable("id") int id) {
        bookDAO.remove(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("book", bookDAO.show(id).get());
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public  String update(@PathVariable("id") int id,
                          @ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        bookDAO.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/remove_owner")
    public String removeOwner(@PathVariable("id") int id) {
        bookDAO.removeOwner(id);

        return "redirect:/books";
    }

    @PatchMapping("/{id}/add_owner")
    public String addOwner(@PathVariable("id") int id,
                           @ModelAttribute("person") Person person) {
        bookDAO.addOwner(id, person);
        return "redirect:/books";
    }
}
