package ru.marinov.library.LibraryBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.security.PersonDetails;
import ru.marinov.library.LibraryBoot.services.PeopleService;

import java.util.Optional;

@Controller
@RequestMapping("/user")
public class UserController {
    private final PeopleService peopleService;
    @Autowired
    public UserController(PeopleService peopleService) {
        this.peopleService = peopleService;
    }

    @GetMapping("")
    public String userPage(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        PersonDetails personDetails = (PersonDetails) authentication.getPrincipal();
        Optional<Person> person = peopleService.findByUsername(personDetails.getPerson().getUsername());
        if (person.isEmpty()) {
            return "redirect:/home";
        }
        model.addAttribute("person", person.get());
        model.addAttribute("books", peopleService.findBooks(person.get().getId()));
        return "users/show";
    }
}
