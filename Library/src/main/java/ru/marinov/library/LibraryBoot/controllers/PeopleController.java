package ru.marinov.library.LibraryBoot.controllers;

import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.services.PeopleService;
import ru.marinov.library.LibraryBoot.services.RegistrationService;
import ru.marinov.library.LibraryBoot.util.PersonValidator;

import java.util.Optional;

@Slf4j
@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final RegistrationService registrationService;
    private final PersonValidator personValidator;
    @Autowired
    public PeopleController(PeopleService peopleService, RegistrationService registrationService, PersonValidator personValidator) {
        this.peopleService = peopleService;
        this.registrationService = registrationService;
        this.personValidator = personValidator;
    }

    @GetMapping()
    public String index(Model model) {
        model.addAttribute("people", peopleService.findAll());
        log.info("Sends People index page");
        return "people/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
            model.addAttribute("books", peopleService.findBooks(id));
            log.info("Shows person {}", person.get());
        }
        return "people/show";
    }

    @GetMapping("/new")
    public String newPerson(@ModelAttribute("person") Person person) {
        log.info("Creates new person");
        return "people/new";
    }

    @PostMapping()
    public String create(@ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {

        personValidator.validate(person, bindingResult);
        if (bindingResult.hasErrors()) {
            log.warn("Unsuccessfully created new person {}", person);
            return "people/new";
        }
        log.info("Successfully created new person {}", person);
        registrationService.register(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        peopleService.deleteById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        Optional<Person> person = peopleService.findById(id);
        if (person.isPresent()) {
            model.addAttribute("person", person.get());
        }
        return "people/edit";
    }

    @PatchMapping("/{id}")
    public String update(@PathVariable("id") int id,
                         @ModelAttribute("person") @Valid Person person,
                         BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/people/edit";
        }
        peopleService.update(id, person);
        return "redirect:/people/" + id;
    }
}
