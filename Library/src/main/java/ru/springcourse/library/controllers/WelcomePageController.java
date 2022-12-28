package ru.springcourse.library.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.springcourse.library.models.SearchRequest;
import ru.springcourse.library.services.BooksService;

@Slf4j
@Controller
@RequestMapping()
public class WelcomePageController {
    private final BooksService booksService;
    private final SearchRequest searchRequest;

    public WelcomePageController(BooksService booksService, SearchRequest searchRequest) {
        this.booksService = booksService;
        this.searchRequest = searchRequest;
    }

    @GetMapping()
    public String show() {
        return "/home";
    }
    @GetMapping("/search")
    public String search(@RequestParam(required = false, value = "startsWith") String startsWith,
                         Model model) {
        model.addAttribute("searchRequest", searchRequest);
        if (startsWith == null) {
            return "search";
        }
        log.info(startsWith);
        model.addAttribute("books", booksService.findByTitleStartingWith(startsWith));
        return "search";
    }
}
