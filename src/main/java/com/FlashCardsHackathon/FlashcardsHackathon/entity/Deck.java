package com.FlashCardsHackathon.FlashcardsHackathon.entity;

import jakarta.persistence.*;
import lombok.*;

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

    // Use OneToMany to indicate that a deck can have multiple flashcards
    @OneToMany(mappedBy = "deck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<FlashCard> flashcards;

    private boolean shared;
    private String owner;
    private boolean paid;

}
