package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.QuizAttempt;
import com.FlashCardsHackathon.FlashcardsHackathon.service.DeckService;
import com.FlashCardsHackathon.FlashcardsHackathon.service.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.UUID;

@Controller
@RequestMapping("/deck")
public class QuizController {

    @Autowired
    private DeckService deckService;

    @Autowired
    private QuizService quizService;

    @GetMapping("/{id}/quiz")
    public String startQuiz(@PathVariable UUID id, Model model) {
        Deck deck = deckService.getDeckById(id);
        if (deck == null) {
            return "redirect:/decks";
        }

        // Only start a new quiz if there isn't an active one
        if (!quizService.hasActiveQuiz(id)) {
            quizService.startQuiz(deck);
        }

        return showQuizCard(id, model);
    }

    @PostMapping("/{deckId}/quiz/check")
    public String checkAnswer(
            @PathVariable UUID deckId,
            @RequestParam String userAnswer,
            RedirectAttributes redirectAttributes) {

        boolean isCorrect = quizService.checkAnswer(deckId, userAnswer);

        // Move to next card before checking if quiz is complete
        if (isCorrect) {
            quizService.moveToNextCard(deckId);
            redirectAttributes.addFlashAttribute("lastAnswerCorrect", true);
        }

        // Check if quiz should end (either due to incorrect answer or completion)
        if (!isCorrect || quizService.isQuizComplete(deckId)) {
            quizService.endQuiz(deckId);
            return "redirect:/deck/" + deckId + "/quiz/results";
        }

        return "redirect:/deck/" + deckId + "/quiz";
    }

    @GetMapping("/{deckId}/quiz/results")
    public String showResults(@PathVariable UUID deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return "redirect:/decks";
        }

        QuizAttempt attempt = quizService.getQuizAttempt(deckId);
        if (attempt == null) {
            return "redirect:/decks";
        }

        model.addAttribute("deck", deck);
        model.addAttribute("attempt", attempt);
        model.addAttribute("totalCards", attempt.getTotalQuestions());
        model.addAttribute("score", attempt.getCorrectAnswers());
        model.addAttribute("endedEarly", attempt.isEndedEarly());

        return "quiz-results";
    }

    private String showQuizCard(UUID deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return "redirect:/decks";
        }

        model.addAttribute("deck", deck);
        model.addAttribute("currentCard", quizService.getCurrentCardIndex(deckId));
        model.addAttribute("totalCards", quizService.getTotalCards(deckId));
        model.addAttribute("currentFlashCard", quizService.getCurrentCard(deckId));
        model.addAttribute("quizComplete", quizService.isQuizComplete(deckId));

        return "quiz";
    }
}