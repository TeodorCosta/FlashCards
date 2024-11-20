package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.service.DeckService;
import com.FlashCardsHackathon.FlashcardsHackathon.service.RevisionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/deck")
public class RevisionController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private RevisionService revisionService;

    @GetMapping("/{id}/revision")
    public String startRevision(@PathVariable UUID id, Model model) {
        Deck deck = deckService.getDeckById(id);
        if (deck == null) {
            return "redirect:/decks";
        }

        // Initialize revision session if not already active
        if (!revisionService.hasActiveSession(id)) {
            revisionService.startRevision(deck);
        }

        return showRevisionCard(id, model);
    }

    @PostMapping("/{deckId}/revision/next")
    public String nextCard(@PathVariable UUID deckId) {
        revisionService.moveToNextCard(deckId);
        return "redirect:/deck/" + deckId + "/revision";
    }

    @PostMapping("/{deckId}/revision/previous")
    public String previousCard(@PathVariable UUID deckId) {
        revisionService.moveToPreviousCard(deckId);
        return "redirect:/deck/" + deckId + "/revision";
    }

    @PostMapping("/{deckId}/revision/shuffle")
    public String shuffleCards(@PathVariable UUID deckId) {
        revisionService.shuffleCards(deckId);
        return "redirect:/deck/" + deckId + "/revision";
    }

    private String showRevisionCard(UUID deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return "redirect:/decks";
        }

        FlashCard currentCard = revisionService.getCurrentCard(deckId);
        int currentIndex = revisionService.getCurrentCardIndex(deckId);
        int totalCards = revisionService.getTotalCards(deckId);

        model.addAttribute("deck", deck);
        model.addAttribute("currentCard", currentCard);
        model.addAttribute("currentIndex", currentIndex);
        model.addAttribute("totalCards", totalCards);
        model.addAttribute("isFlipped", false);

        return "revision";
}
}