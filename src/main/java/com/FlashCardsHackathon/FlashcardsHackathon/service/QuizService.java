package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class QuizService {
    private final Map<UUID, Integer> quizProgress = new HashMap<>();
    private final Map<UUID, List<FlashCard>> shuffledCards = new HashMap<>();
    private final Map<UUID, Integer> quizScores = new HashMap<>();
    private final Map<UUID, List<QuizAnswer>> quizAnswers = new HashMap<>();
    private final Map<UUID, Boolean> earlyEndStatus = new HashMap<>();

    @Autowired
    private DeckService deckService;

    public static class QuizAnswer {
        private final String question;
        private final String correctAnswer;
        private final String userAnswer;
        private final boolean correct;

        public QuizAnswer(String question, String correctAnswer, String userAnswer, boolean correct) {
            this.question = question;
            this.correctAnswer = correctAnswer;
            this.userAnswer = userAnswer;
            this.correct = correct;
        }

        public String getQuestion() { return question; }
        public String getCorrectAnswer() { return correctAnswer; }
        public String getUserAnswer() { return userAnswer; }
        public boolean isCorrect() { return correct; }
    }

    public void startQuiz(Deck deck) {
        if (deck == null || deck.getFlashcards() == null) {
            throw new IllegalArgumentException("Deck cannot be null and must contain flashcards");
        }
        List<FlashCard> cards = new ArrayList<>(deck.getFlashcards());
        Collections.shuffle(cards);
        UUID deckId = deck.getId();
        shuffledCards.put(deckId, cards);
        quizProgress.put(deckId, 0);
        quizScores.put(deckId, 0);
        quizAnswers.put(deckId, new ArrayList<>());
        earlyEndStatus.put(deckId, false);
    }

    public FlashCard getCurrentCard(Deck deck) {
        if (deck == null) return null;
        List<FlashCard> cards = shuffledCards.get(deck.getId());
        int currentIndex = quizProgress.getOrDefault(deck.getId(), 0);
        return cards != null && currentIndex < cards.size() ? cards.get(currentIndex) : null;
    }

    public boolean checkAnswer(UUID deckId, String userAnswer) {
        if (userAnswer == null) return false;

        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) return false;

        FlashCard currentCard = getCurrentCard(deck);
        if (currentCard == null) return false;

        boolean isCorrect = currentCard.getAnswer().trim().equalsIgnoreCase(userAnswer.trim());
        if (isCorrect) {
            incrementScore(deckId);
        } else {
            earlyEndStatus.put(deckId, true);
        }

        // Store the answer
        quizAnswers.get(deckId).add(new QuizAnswer(
                currentCard.getQuestion(),
                currentCard.getAnswer(),
                userAnswer,
                isCorrect
        ));

        return isCorrect;
    }

    public void endQuiz(UUID deckId) {
        // Mark all remaining cards as skipped
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) return;

        List<FlashCard> cards = shuffledCards.get(deckId);
        int currentIndex = quizProgress.getOrDefault(deckId, 0);

        if (cards != null && currentIndex < cards.size() - 1) {
            for (int i = currentIndex + 1; i < cards.size(); i++) {
                FlashCard card = cards.get(i);
                quizAnswers.get(deckId).add(new QuizAnswer(
                        card.getQuestion(),
                        card.getAnswer(),
                        "Skipped",
                        false
                ));
            }
        }
    }

    public boolean endedEarly(UUID deckId) {
        return earlyEndStatus.getOrDefault(deckId, false);
    }

    public List<QuizAnswer> getQuizResults(UUID deckId) {
        return quizAnswers.getOrDefault(deckId, new ArrayList<>());
    }

    private void incrementScore(UUID deckId) {
        quizScores.merge(deckId, 1, Integer::sum);
    }

    public int getScore(UUID deckId) {
        return quizScores.getOrDefault(deckId, 0);
    }

    public boolean isQuizComplete(Deck deck) {
        if (deck == null) return true;
        List<FlashCard> cards = shuffledCards.get(deck.getId());
        int currentIndex = quizProgress.getOrDefault(deck.getId(), 0);
        return cards != null && currentIndex >= cards.size();
    }

    public int getCurrentCardIndex(UUID deckId) {
        return quizProgress.getOrDefault(deckId, 0);
    }

    public void moveToNextCard(UUID deckId) {
        quizProgress.merge(deckId, 1, Integer::sum);
    }

    public int getTotalCards(UUID deckId) {
        List<FlashCard> cards = shuffledCards.get(deckId);
        return cards != null ? cards.size() : 0;
    }

    public void resetQuiz(UUID deckId) {
        quizProgress.remove(deckId);
        shuffledCards.remove(deckId);
        quizScores.remove(deckId);
        quizAnswers.remove(deckId);
        earlyEndStatus.remove(deckId);
    }
}