package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.DeckRepository;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.FlashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class DeckService {

    @Autowired
    private DeckRepository deckRepository;

    @Autowired
    private FlashCardRepository flashCardRepository;


    public List<Deck> getAllDecks() {
        return deckRepository.findAll();
    }


    public Deck saveDeck(Deck deck, List<UUID> selectedFlashcards) {
        // First, save the deck
        Deck savedDeck = deckRepository.save(deck);


        for (UUID flashCardId : selectedFlashcards) {
            Optional<FlashCard> flashCardOpt = flashCardRepository.findById(flashCardId);
            flashCardOpt.ifPresent(flashCard -> {
                flashCard.setDeck(savedDeck); // Set the deck to the flashcard
                flashCardRepository.save(flashCard); // Save the updated flashcard
            });
        }

        return savedDeck;
    }


    public Deck getDeckById(UUID id) {
        Optional<Deck> deckOptional = deckRepository.findById(id);
        return deckOptional.orElseThrow(() -> new RuntimeException("Deck not found with id " + id));
    }
}
