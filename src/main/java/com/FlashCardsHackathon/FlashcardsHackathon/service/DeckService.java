    package com.FlashCardsHackathon.FlashcardsHackathon.service;

    import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
    import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
    import com.FlashCardsHackathon.FlashcardsHackathon.repository.DeckRepository;
    import com.FlashCardsHackathon.FlashcardsHackathon.repository.FlashCardRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.stereotype.Service;

    import java.util.*;

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
            // Save the deck first to ensure it has an ID
            Deck savedDeck = deckRepository.save(deck);

            // Now associate selected flashcards with the deck
            for (UUID flashCardId : selectedFlashcards) {
                Optional<FlashCard> flashCardOpt = flashCardRepository.findById(flashCardId);
                flashCardOpt.ifPresent(flashCard -> {
                    flashCard.setDeck(savedDeck); // Associate flashcard with the deck
                    flashCardRepository.save(flashCard); // Save the updated flashcard
                });
            }

            return savedDeck;
        }

        public List<FlashCard> getFlashCardsByDeckId(UUID deckId) {
            // Find the flashcards by deckId, which is of type UUID
            Set<FlashCard> flashcardsSet = flashCardRepository.findByDeckId(deckId);

            // Convert Set to List before returning
            List<FlashCard> flashcardsList = new ArrayList<>(flashcardsSet);

            return flashcardsList;
        }

        public Deck getDeckById(UUID id) {
            Optional<Deck> deckOptional = deckRepository.findById(id);
            return deckOptional.orElseThrow(() -> new RuntimeException("Deck not found with id " + id));
        }
    }
