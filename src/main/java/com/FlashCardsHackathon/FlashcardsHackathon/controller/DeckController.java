package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.service.DeckService;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.service.FlashCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/deck")
public class DeckController {

    private static final String IMAGE_DIR = "C:\\Users\\teodo\\Desktop\\FlashcardsHackathon\\src\\main\\resources\\static\\images";

    @Autowired
    private DeckService deckService;

    @Autowired
    private FlashCardService flashCardService;

    @GetMapping("/create")
    public String showCreateDeckForm(Model model) {
        List<FlashCard> flashCards = flashCardService.getAllFlashCards();
        model.addAttribute("flashCards", flashCards);
        model.addAttribute("deck", new Deck());
        return "add_deck";
    }

    @GetMapping("/list")
    public String listDecks(Model model) {
        List<Deck> decks = deckService.getAllDecks();
        for (Deck deck : decks) {
            deck.setFlashcards(new ArrayList<>(deckService.getFlashCardsByDeckId(deck.getId())));
        }
        model.addAttribute("decks", decks);
        return "deck_list";
    }

    @PostMapping("/save")
    public String saveDeck(@ModelAttribute Deck deck,
                           @RequestParam(value = "selectedFlashcards", required = false) List<UUID> selectedFlashcards,
                           @RequestParam(value = "media", required = false) MultipartFile media) throws IOException {
        if (media != null && !media.isEmpty()) {
            String originalFilename = media.getOriginalFilename();
            String uniqueFilename = generateUniqueFilename(originalFilename);

            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            File file = new File(IMAGE_DIR + File.separator + uniqueFilename);
            if (!file.exists()) {
                media.transferTo(file);
            }

            deck.setImagePath("/images/" + uniqueFilename);
        }

        if (selectedFlashcards == null) {
            selectedFlashcards = new ArrayList<>();
        }

        deckService.saveDeck(deck, selectedFlashcards);
        return "redirect:/deck/list";
    }

    @GetMapping("{id}/delete")
    public String deleteDeck(@PathVariable UUID id) {
        boolean deleted = deckService.deleteDeck(id);
        if (deleted) {
            return "redirect:/deck/list";
        } else {
            return "error_page";
        }
    }

    @GetMapping("/{id}/edit")
    public String updateDeck(@PathVariable UUID id, Model model) {
        model.addAttribute("deck", deckService.getDeckById(id));
        List<FlashCard> flashCards = flashCardService.getAllFlashCards();
        model.addAttribute("flashCards", flashCards);
        return "add_deck";
    }

    private String generateUniqueFilename(String originalFilename) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        String uniqueName = baseName + "_" + System.currentTimeMillis() + "." + extension;
        return uniqueName;
    }
}