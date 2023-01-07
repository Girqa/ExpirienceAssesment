package ru.marinov.library.LibraryBoot.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.marinov.library.LibraryBoot.models.Person;
import ru.marinov.library.LibraryBoot.services.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("")
    public String adminPage(@ModelAttribute("person") Person person,
                            Model model) {
        model.addAttribute("people", adminService.findAllWithRoleUser());
        return "admins/show";
    }

    @PatchMapping("/promote_user")
    public String promoteUser(@ModelAttribute("person") Person person) {
        adminService.promoteUser(person.getId());
        return "redirect:/admin";
    }
}
