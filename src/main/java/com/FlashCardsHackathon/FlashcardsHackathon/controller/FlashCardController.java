package com.FlashCardsHackathon.FlashcardsHackathon.controller;

import com.FlashCardsHackathon.FlashcardsHackathon.service.AzureAIService;
import com.FlashCardsHackathon.FlashcardsHackathon.service.AzureOpenAIService;
import org.apache.commons.io.FilenameUtils;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.Deck;
import com.FlashCardsHackathon.FlashcardsHackathon.entity.FlashCard;
import com.FlashCardsHackathon.FlashcardsHackathon.service.FlashCardService;
import com.FlashCardsHackathon.FlashcardsHackathon.service.DeckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/flashcard")
public class FlashCardController {

    // Set the folder where images will be uploaded to (outside resources)
    private static final String IMAGE_DIR = "C:\\Users\\teodo\\Desktop\\FlashcardsHackathon\\src\\main\\resources\\static\\images";

    @Autowired
    private FlashCardService flashCardService;

    @Autowired
    private DeckService deckService;

    @Autowired
    private AzureAIService azureAIService;


    @GetMapping("/generate-flashcards")
    public String generateFlashcards(
            @RequestParam(defaultValue = "") String question,
            @RequestParam(defaultValue = "") String materie,
            Model model) {
        if (question.isEmpty() || materie.isEmpty()) {
            model.addAttribute("error", "Both question and materie must be provided.");
            return "error-page"; // Return an error page or handle accordingly
        }

        // Generate the flashcards but do not save them initially
        List<FlashCard> generatedFlashcards = azureAIService.generateFlashcards(question, materie);
        model.addAttribute("flashcards", generatedFlashcards);

        // Flag to handle whether to save the flashcards or not
        model.addAttribute("saveFlashcards", false); // Default to false, meaning not saved yet

        return "flashcards-ai"; // The view where the flashcards will be shown
    }
    @GetMapping("/form")
    public String showFlashcardForm() {
        return "flashcards-ai-form"; // The view for the form
    }


    @PostMapping("/save")
    public String saveFlashCard(@ModelAttribute FlashCard flashCard,
                                @RequestParam("media") MultipartFile media) throws IOException {
        if (!media.isEmpty()) {
            // Get the original filename of the uploaded image
            String originalFilename = media.getOriginalFilename();

            // Generate a unique filename to avoid conflicts
            String uniqueFilename = generateUniqueFilename(originalFilename);

            // Ensure the directory exists, create if not
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Check if the file already exists
            File file = new File(IMAGE_DIR + File.separator + uniqueFilename);
            if (!file.exists()) {
                // If the file doesn't exist, save the new file
                media.transferTo(file);  // Save the file to disk
            }
            // If the file exists, we'll use the existing file (no action needed)

            // Set the relative image path in the FlashCard object
            flashCard.setImagePath("/images/" + uniqueFilename);  // Path to access the image publicly
        }
        flashCardService.saveFlashCard(flashCard);
        return "redirect:/flashcard/list";
    }

    // Helper method to generate a unique filename
    private String generateUniqueFilename(String originalFilename) {
        String baseName = FilenameUtils.getBaseName(originalFilename);
        String extension = FilenameUtils.getExtension(originalFilename);
        String uniqueName = baseName + "_" + System.currentTimeMillis() + "." + extension;
        return uniqueName;
    }

    @GetMapping("/new")
    public String showCreateFlashCardForm(Model model) {
        model.addAttribute("flashCard", new FlashCard());
        model.addAttribute("decks", deckService.getAllDecks());  // Fetch all decks for the dropdown
        return "flashcard_form";
    }

    @GetMapping("/image/{fileName}")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException {
        // Locate the image file on the server
        File imageFile = new File(IMAGE_DIR + File.separator + fileName);

        if (imageFile.exists()) {
            byte[] imageData = Files.readAllBytes(imageFile.toPath());
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.valueOf("image/png"))  // Set correct MIME type
                    .body(imageData);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Image file not found");
        }
    }

    @GetMapping("/list")
    public String listFlashCards(Model model) {
        model.addAttribute("flashCards", flashCardService.getAllFlashCards());
        return "flashcard_list";
    }

    @GetMapping("/delete/{id}")
    public String deleteFlashCard(@PathVariable UUID id) {
        boolean deleted = flashCardService.deleteFlashCard(id);
        if (deleted) {
            return "redirect:/flashcard/list";  // Redirect to list of flashcards if successful
        } else {
            // Optionally return an error page or message if deletion failed
            return "error_page";  // Replace with actual error page view
        }
    }
    @GetMapping("/edit/{id}")
    public String updateFlashCard(@PathVariable UUID id, Model model) {
        model.addAttribute("flashCard",flashCardService.getFlashCardById(id));
        model.addAttribute("decks", deckService.getAllDecks());
        return "flashcard_form";
    }
}

