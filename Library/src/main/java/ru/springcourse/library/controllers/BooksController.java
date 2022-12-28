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
import ru.springcourse.library.models.SearchRequest;
import ru.springcourse.library.services.BooksService;
import ru.springcourse.library.services.PeopleService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/books")
public class BooksController {
    private final BookDAO bookDAO;
    private final PersonDAO personDAO;
    private final BooksService booksService;
    private final PeopleService peopleService;
    private final SearchRequest searchRequest;
    @Autowired
    public BooksController(BookDAO bookDAO, PersonDAO personDAO, BooksService booksService, PeopleService peopleService, SearchRequest searchRequest) {
        this.bookDAO = bookDAO;
        this.personDAO = personDAO;
        this.booksService = booksService;
        this.peopleService = peopleService;
        this.searchRequest = searchRequest;
    }


    @GetMapping()
    public String index(Model model,
                        @RequestParam(value = "page", required = false) Integer page,
                        @RequestParam(value = "books_per_page", required = false) Integer booksPerPage,
                        @RequestParam(value = "sort_by_year", required = false) Boolean sortByYear){
        List<Book> books;

        if (page == null && booksPerPage == null && sortByYear == null) {
            books = booksService.findAll();
        } else if (page != null && booksPerPage != null && sortByYear != null) {
            books = booksService.findAll(page, booksPerPage, sortByYear);
        } else if (page == null && booksPerPage == null && sortByYear != null) {
            books = booksService.findAll(sortByYear);
        } else {
            books = booksService.findAll(page, booksPerPage);
        }
        model.addAttribute("books", books);
        log.info("Books page was gotten");
        return "books/index";
    }

    @GetMapping("{id}")
    public String show(@PathVariable("id") int id,
                        Model model,
                       @ModelAttribute("person") Person person) {
        Book book = booksService.findById(id).get();
        model.addAttribute("book", book);

        Optional<Person> owner = booksService.getBookOwner(book);
        if (owner.isPresent()) {
            model.addAttribute("owner", owner.get());
        }
        else {
            model.addAttribute("people", peopleService.findAll());
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
        booksService.save(book);
        log.warn("Successfully created new book {}", book);
        return "redirect:/books";
    }

    @DeleteMapping("{id}")
    public String remove(@PathVariable("id") int id) {
        booksService.deleteById(id);
        return "redirect:/books";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model,
                       @PathVariable("id") int id) {
        model.addAttribute("book", booksService.findById(id).get());
        return "/books/edit";
    }

    @PatchMapping("/{id}")
    public  String update(@PathVariable("id") int id,
                          @ModelAttribute("book") @Valid Book book,
                          BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/books/edit";
        }
        booksService.update(id, book);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/remove_owner")
    public String removeOwner(@PathVariable("id") int id) {
        booksService.deleteOwner(id);

        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/add_owner")
    public String addOwner(@PathVariable("id") int id,
                           @ModelAttribute("person") Person person) {
        booksService.addOwner(id, person);
        return "redirect:/books/" + id;
    }
}
