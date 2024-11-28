package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.azure.ai.textanalytics.TextAnalyticsClient;
import com.azure.ai.textanalytics.TextAnalyticsClientBuilder;
import com.azure.core.credential.AzureKeyCredential;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AzureAIService {

    private final AzureOpenAIService azureOpenAIService;
    private final TextAnalyticsClient textAnalyticsClient;

    @Autowired
    public AzureAIService(AzureOpenAIService azureOpenAIService,
                          @Value("${azure.openai.endpoint}") String textAnalyticsEndpoint,
                          @Value("${azure.openai.apiKey}") String textAnalyticsApiKey) {
        this.azureOpenAIService = azureOpenAIService;

        // Proper initialization of the textAnalyticsClient
        this.textAnalyticsClient = new TextAnalyticsClientBuilder()
                .endpoint(textAnalyticsEndpoint)
                .credential(new AzureKeyCredential(textAnalyticsApiKey))
                .buildClient();
    }

    public List<FlashCard> generateFlashcards(String question, String materie) {
        List<FlashCard> flashcards = new ArrayList<>();

        // Refined prompt for generating concise answers
        String answerPrompt = "Answer the question concisely in one or two sentences: \"" + question.trim() + "\".";
        String answer = azureOpenAIService.generateText(answerPrompt);

        // Extract only the first sentence
        String firstSentence = extractFirstSentence(answer);

        // Create a FlashCard object
        FlashCard flashcard = FlashCard.builder()
                .question(question.trim())
                .answer(firstSentence)
                .materie(materie)
                .isAIGenerated(true)
                .generationTopic("Based on question: " + question.trim())
                .shared(false)
                .build();

        flashcards.add(flashcard);

        return flashcards;
    }

    private String extractFirstSentence(String text) {
        // Split the response at periods, ensure at least one sentence is returned
        String[] sentences = text.split("\\.");
        return sentences.length > 0 ? sentences[0].trim() + "." : text.trim();
    }





}