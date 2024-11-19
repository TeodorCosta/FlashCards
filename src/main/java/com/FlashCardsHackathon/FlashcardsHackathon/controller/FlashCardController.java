package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/flashcard") // Prefix for all mappings in this controller
public class FlashCardController {

    @Autowired
    FlashCardService flashCardService;

    @PostMapping("/save")
    public String saveFlashCard(FlashCard flashCard) {
        flashCardService.saveFlashCard(flashCard);
        return "redirect:/flashcard/list";
    }

    @GetMapping("/new")
    public String showCreateFlashCardForm(org.springframework.ui.Model model) {
        model.addAttribute("flashCard", new FlashCard());
        return "flashcard_form";
    }

    @GetMapping("/list")
    public String listFlashCards(org.springframework.ui.Model model) {
        model.addAttribute("flashCards", flashCardService.getAllFlashCards());
        return "flashcard_list";
    }
}
