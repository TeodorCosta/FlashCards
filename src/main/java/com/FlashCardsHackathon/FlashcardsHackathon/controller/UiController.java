package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UiController {

    @GetMapping("/home")
    public String showHomePage() {
        return "home";
    }
}
