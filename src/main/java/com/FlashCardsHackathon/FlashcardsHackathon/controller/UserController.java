package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.AppUser;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.AppUserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


@Controller
public class UserController {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("appUser", new AppUser());
        return "register";
    }

    @PostMapping("/save")
    public String saveUser(@Valid AppUser user, BindingResult result, Model model) {

        System.out.println("Attempting to register user: " + user.getUsername());

        if (result.hasErrors()) {
            return "register";
        }

        if (appUserRepository.existsByUsername(user.getUsername())) {
            System.out.println("Username already exists: " + user.getUsername());
            model.addAttribute("error", "Username already exists");
            return "register";
        }
        user.setRole("USER");

        // Encode the password before saving the user
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        appUserRepository.save(user);
        System.out.println("User saved successfully!");
        return "redirect:/login";
    }

}
