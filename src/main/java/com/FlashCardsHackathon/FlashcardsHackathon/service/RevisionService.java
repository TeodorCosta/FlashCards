package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RevisionService {
    private final Map<UUID, List<FlashCard>> sessionCards = new HashMap<>();
    private final Map<UUID, Integer> currentCardIndex = new HashMap<>();

    public boolean hasActiveSession(UUID deckId) {
        return sessionCards.containsKey(deckId);
    }

    public void startRevision(Deck deck) {
        if (deck == null || deck.getFlashcards() == null || deck.getFlashcards().isEmpty()) {
            throw new IllegalArgumentException("Deck cannot be null and must contain flashcards");
        }

        UUID deckId = deck.getId();
        List<FlashCard> cards = new ArrayList<>(deck.getFlashcards());
        sessionCards.put(deckId, cards);
        currentCardIndex.put(deckId, 0);
    }

    public FlashCard getCurrentCard(UUID deckId) {
        List<FlashCard> cards = sessionCards.get(deckId);
        int index = currentCardIndex.getOrDefault(deckId, 0);
        return cards != null && index < cards.size() ? cards.get(index) : null;
    }

    public void moveToNextCard(UUID deckId) {
        List<FlashCard> cards = sessionCards.get(deckId);
        if (cards != null) {
            int currentIndex = currentCardIndex.getOrDefault(deckId, 0);
            if (currentIndex < cards.size() - 1) {
                currentCardIndex.put(deckId, currentIndex + 1);
            }
        }
    }

    public void moveToPreviousCard(UUID deckId) {
        int currentIndex = currentCardIndex.getOrDefault(deckId, 0);
        if (currentIndex > 0) {
            currentCardIndex.put(deckId, currentIndex - 1);
        }
    }

    public void shuffleCards(UUID deckId) {
        List<FlashCard> cards = sessionCards.get(deckId);
        if (cards != null) {
            Collections.shuffle(cards);
            currentCardIndex.put(deckId, 0);
        }
    }

    public int getCurrentCardIndex(UUID deckId) {
        return currentCardIndex.getOrDefault(deckId, 0);
    }

    public int getTotalCards(UUID deckId) {
        List<FlashCard> cards = sessionCards.get(deckId);
        return cards != null ? cards.size():0;}
}