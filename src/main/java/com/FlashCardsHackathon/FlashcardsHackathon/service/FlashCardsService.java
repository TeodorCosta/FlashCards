package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCards;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.FlashCardsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FlashCardsService {
    @Autowired
    FlashCardsRepository flashCardsRepository;
    public List<FlashCards> getAllFlashCards() {
        return flashCardsRepository.findAll();
    }
}
