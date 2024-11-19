package com.FlashCardsHackathon.FlashcardsHackathon.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
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


    private boolean shared ;
}
