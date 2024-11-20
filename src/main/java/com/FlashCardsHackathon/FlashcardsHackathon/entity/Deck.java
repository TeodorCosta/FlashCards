package com.FlashCardsHackathon.FlashcardsHackathon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Deck {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;
    private String description;

    // Use OneToMany to indicate that a deck can have multiple flashcards
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<FlashCard> flashcards;

    private boolean shared;
    private String owner;
    private boolean paid;

    private String imagePath;

    public void addFlashCard(FlashCard flashCard) {
        this.flashcards.add(flashCard);
    }
}
