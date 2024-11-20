package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
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

        quizService.startQuiz(deck);
        return showQuizCard(id, model);
    }

    @PostMapping("/{deckId}/quiz/check")
    public String checkAnswer(
            @PathVariable UUID deckId,
            @RequestParam String userAnswer,
            RedirectAttributes redirectAttributes) {

        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            redirectAttributes.addFlashAttribute("error", "Deck not found");
            return "redirect:/decks";
        }

        boolean isCorrect = quizService.checkAnswer(deckId, userAnswer);

        // If answer is incorrect or quiz is complete, end the quiz
        if (!isCorrect || quizService.isQuizComplete(deck)) {
            quizService.endQuiz(deckId); // Mark quiz as complete
            return "redirect:/deck/" + deckId + "/quiz/results";
        }

        // Only move to next card if answer was correct
        quizService.moveToNextCard(deckId);
        redirectAttributes.addFlashAttribute("lastAnswerCorrect", true);

        return "redirect:/deck/" + deckId + "/quiz";
    }

    @GetMapping("/{deckId}/quiz/results")
    public String showResults(@PathVariable UUID deckId, Model model) {
        Deck deck = deckService.getDeckById(deckId);
        if (deck == null) {
            return "redirect:/deck/list ";
        }

        model.addAttribute("deck", deck);
        model.addAttribute("quizResults", quizService.getQuizResults(deckId));
        model.addAttribute("totalCards", quizService.getTotalCards(deckId));
        model.addAttribute("score", quizService.getScore(deckId));
        model.addAttribute("endedEarly", quizService.endedEarly(deckId));

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
        model.addAttribute("currentScore", quizService.getScore(deckId));
        model.addAttribute("quizComplete", quizService.isQuizComplete(deck));

        if (!quizService.isQuizComplete(deck)) {
            model.addAttribute("currentFlashCard", quizService.getCurrentCard(deck));
        }

        return "quiz";
    }
}