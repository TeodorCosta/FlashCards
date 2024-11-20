package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.*;
import com.FlashCardsHackathon.FlashcardsHackathon.repository.QuizAttemptRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class QuizService {
    private final Map<UUID, QuizAttempt> activeQuizzes = new HashMap<>();
    private final Map<UUID, List<FlashCard>> shuffledCards = new HashMap<>();
    private final Map<UUID, Integer> currentCardIndex = new HashMap<>();

    @Autowired
    private QuizAttemptRepository quizAttemptRepository;

    @Transactional
    public void startQuiz(Deck deck) {
        if (deck == null || deck.getFlashcards() == null || deck.getFlashcards().isEmpty()) {
            throw new IllegalArgumentException("Deck cannot be null and must contain flashcards");
        }

        List<FlashCard> cards = new ArrayList<>(deck.getFlashcards());
        Collections.shuffle(cards);

        QuizAttempt attempt = new QuizAttempt();
        attempt.setDeck(deck);
        attempt.setStartTime(LocalDateTime.now());
        attempt.setTotalQuestions(cards.size());
        attempt.setCorrectAnswers(0);
        attempt.setEndedEarly(false);

        UUID deckId = deck.getId();
        shuffledCards.put(deckId, cards);
        currentCardIndex.put(deckId, 0);
        activeQuizzes.put(deckId, attempt);
    }

    public FlashCard getCurrentCard(UUID deckId) {
        List<FlashCard> cards = shuffledCards.get(deckId);
        int index = currentCardIndex.getOrDefault(deckId, 0);
        return cards != null && index < cards.size() ? cards.get(index) : null;
    }

    @Transactional
    public boolean checkAnswer(UUID deckId, String userAnswer) {
        if (userAnswer == null) return false;

        QuizAttempt attempt = activeQuizzes.get(deckId);
        FlashCard currentCard = getCurrentCard(deckId);

        if (attempt == null || currentCard == null) return false;

        boolean isCorrect = currentCard.getAnswer().trim().equalsIgnoreCase(userAnswer.trim());

        QuizAnswer answer = new QuizAnswer();
        answer.setFlashCard(currentCard);
        answer.setUserAnswer(userAnswer);
        answer.setCorrect(isCorrect);
        answer.setQuestionOrder(currentCardIndex.get(deckId));
        attempt.addAnswer(answer);

        if (isCorrect) {
            attempt.setCorrectAnswers(attempt.getCorrectAnswers() + 1);
        } else {
            attempt.setEndedEarly(true);
            endQuiz(deckId);
        }

        return isCorrect;
    }

    @Transactional
    public void endQuiz(UUID deckId) {
        QuizAttempt attempt = activeQuizzes.get(deckId);
        if (attempt == null) return;

        // Mark remaining cards as skipped
        List<FlashCard> cards = shuffledCards.get(deckId);
        int current = currentCardIndex.getOrDefault(deckId, 0);

        for (int i = current + 1; i < cards.size(); i++) {
            QuizAnswer skippedAnswer = new QuizAnswer();
            skippedAnswer.setFlashCard(cards.get(i));
            skippedAnswer.setUserAnswer("Skipped");
            skippedAnswer.setCorrect(false);
            skippedAnswer.setQuestionOrder(i);
            attempt.addAnswer(skippedAnswer);
        }

        attempt.setEndTime(LocalDateTime.now());
        quizAttemptRepository.save(attempt);

        // Cleanup
        activeQuizzes.remove(deckId);
        shuffledCards.remove(deckId);
        currentCardIndex.remove(deckId);
    }

    public QuizAttempt getQuizAttempt(UUID deckId) {
        return activeQuizzes.get(deckId);
    }

    public boolean isQuizComplete(UUID deckId) {
        List<FlashCard> cards = shuffledCards.get(deckId);
        int index = currentCardIndex.getOrDefault(deckId, 0);
        return cards == null || index >= cards.size();
    }

    public void moveToNextCard(UUID deckId) {
        currentCardIndex.merge(deckId, 1, Integer::sum);
    }

    public int getCurrentCardIndex(UUID deckId) {
        return currentCardIndex.getOrDefault(deckId, 0);
    }

    public int getTotalCards(UUID deckId) {
        List<FlashCard> cards = shuffledCards.get(deckId);
        return cards != null ? cards.size() : 0;
    }
}