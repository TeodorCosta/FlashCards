package com.FlashCardsHackathon.FlashcardsHackathon.repository;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface FlashCardRepository extends JpaRepository<FlashCard, UUID> {

    // Modify the method to accept UUID
    Set<FlashCard> findByDeckId(UUID deckId);

    Optional<FlashCard> findByQuestion(String question);
}
