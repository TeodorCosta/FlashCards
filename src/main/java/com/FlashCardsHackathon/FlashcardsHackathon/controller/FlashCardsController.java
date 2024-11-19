package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCards;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.FlashCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class FlashCardsController {
    @Autowired
    private FlashCardsRepository flashCardsRepository;

    @GetMapping
    public List<FlashCards> getAllFlashCards() {
        return flashCardsRepository.findAll();
    }

}
