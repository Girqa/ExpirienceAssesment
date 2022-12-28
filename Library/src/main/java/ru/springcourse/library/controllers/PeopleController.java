package ru.springcourse.library.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.springcourse.library.models.Person;
import ru.springcourse.library.services.PeopleService;
import ru.springcourse.library.util.PersonValidator;

import javax.validation.Valid;

@Slf4j
@Controller
@RequestMapping("/people")
public class PeopleController {
    private final PeopleService peopleService;
    private final PersonValidator personValidator;
    @Autowired
    public PeopleController(PeopleService peopleService, PersonValidator personValidator) {
        this.peopleService = peopleService;
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
        Person person = peopleService.findById(id).get();
        model.addAttribute("person", person);
        model.addAttribute("books", peopleService.findBooks(id));
        log.info("Shows person {}", person);
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
        peopleService.save(person);
        return "redirect:/people";
    }

    @DeleteMapping("/{id}")
    public String remove(@PathVariable("id") int id) {
        peopleService.deleteById(id);
        return "redirect:/people";
    }

    @GetMapping("/{id}/edit")
    public String edit(Model model, @PathVariable("id") int id) {
        model.addAttribute("person", peopleService.findById(id).get());
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
