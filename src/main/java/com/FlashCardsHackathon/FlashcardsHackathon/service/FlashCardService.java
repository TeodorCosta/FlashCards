package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.FlashCardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class FlashCardService {
    @Autowired
    FlashCardRepository flashCardsRepository;
    public List<FlashCard> getAllFlashCards() {
        return flashCardsRepository.findAll();
    }

    public void saveFlashCard(FlashCard flashCard){
        flashCardsRepository.save(flashCard);
    }
    public FlashCard getFlashCardById(UUID id){
        return flashCardsRepository.getReferenceById(id);
    }
    public FlashCard getFlashCardByName(String name) {
        return flashCardsRepository.findByQuestion(name).orElse(null);  // Assuming a method to find by name
    }
}
