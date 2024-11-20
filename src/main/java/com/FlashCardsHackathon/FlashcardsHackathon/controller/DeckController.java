package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.service.DeckService;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/deck")
public class DeckController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private FlashCardService flashCardService;

    // Display page to create deck and select flashcards
    @GetMapping("/create")
    public String showCreateDeckForm(Model model) {
        List<FlashCard> flashCards = flashCardService.getAllFlashCards();
        model.addAttribute("flashCards", flashCards);
        model.addAttribute("deck", new Deck());
        return "add_deck";  // Thymeleaf view template
    }
    @GetMapping("/list")
    public String listDecks(Model model) {
        // Fetch all decks
        List<Deck> decks = deckService.getAllDecks();

        // Fetch flashcards for each deck
        for (Deck deck : decks) {
            // Add the flashcards for each deck to the model
            deck.setFlashcards(new ArrayList<>(deckService.getFlashCardsByDeckId(deck.getId())));
        }

        // Add decks with flashcards to the model
        model.addAttribute("decks", decks);
        return "deck_list";  // Thymeleaf template name
    }
    // Handle form submission to save the deck
    @PostMapping("/save")
    public String saveDeck(@ModelAttribute Deck deck,
                           @RequestParam("selectedFlashcards") List<UUID> selectedFlashcards) {
        // Save deck and associate selected flashcards with it
        deckService.saveDeck(deck, selectedFlashcards);
        return "redirect:/deck/list";  // Redirect to list of decks or some other page
    }

}
