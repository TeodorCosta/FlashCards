package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.azure.ai.openai.OpenAIClient;
import com.azure.ai.openai.OpenAIClientBuilder;
import com.azure.ai.openai.models.CompletionsOptions;
import com.azure.core.credential.AzureKeyCredential; // Keep AzureKeyCredential as is
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AzureOpenAIService {
    private final OpenAIClient openAIClient;

    public AzureOpenAIService(@Value("${azure.openai.endpoint}") String endpoint,
                              @Value("${azure.openai.apiKey}") String apiKey) {
        this.openAIClient = new OpenAIClientBuilder()
                .endpoint(endpoint)
                .credential(new AzureKeyCredential(apiKey)) // Correct method
                .buildClient();
    }


    public String generateText(String prompt) {
        CompletionsOptions options = new CompletionsOptions(Collections.singletonList(prompt))
                .setMaxTokens(150)
                .setTemperature(0.7)
                .setTopP(1.0);

        // Ensure "Davin-Teo" matches your actual deployment name
        return openAIClient.getCompletions("Davin-Teo", options)
                .getChoices()
                .get(0)
                .getText()
                .trim();
    }
}
