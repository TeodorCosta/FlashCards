package com.FlashCardsHackathon.FlashcardsHackathon.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Type;

import java.util.UUID;

@Entity
@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FlashCard {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String question;
    private String answer;

    private String materie;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id")
    private Deck deck;


    private String imagePath;

    private boolean shared;
}
