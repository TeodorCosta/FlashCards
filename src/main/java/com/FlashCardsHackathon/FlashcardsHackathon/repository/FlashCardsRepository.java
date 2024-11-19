package com.FlashCardsHackathon.FlashcardsHackathon.repository;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCards;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface FlashCardsRepository extends JpaRepository<FlashCards, UUID> {
}
