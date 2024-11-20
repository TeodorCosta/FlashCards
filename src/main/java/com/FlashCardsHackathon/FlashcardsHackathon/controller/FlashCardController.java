package com.FlashCardsHackathon.FlashcardsHackathon.controller;

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

@Controller
@RequestMapping("/flashcard")
public class FlashCardController {

    // Set the folder where images will be uploaded to (outside resources)
    private static final String IMAGE_DIR = "C:\\Users\\teodo\\Desktop\\FlashcardsHackathon\\src\\main\\resources\\static\\images";

    @Autowired
    private FlashCardService flashCardService;

    @Autowired
    private DeckService deckService;

    @PostMapping("/save")
    public String saveFlashCard(@ModelAttribute FlashCard flashCard,
                                @RequestParam("media") MultipartFile media) throws IOException {
        if (!media.isEmpty()) {
            // Get the original filename of the uploaded image
            String imageName = media.getOriginalFilename();

            // Ensure the directory exists, create if not
            File directory = new File(IMAGE_DIR);
            if (!directory.exists()) {
                directory.mkdirs();  // Create the directory if it doesn't exist
            }

            // Save the image to the static/images folder
            File file = new File(IMAGE_DIR + File.separator + imageName);
            media.transferTo(file);  // Save the file to disk

            // Set the relative image path in the FlashCard object
            flashCard.setImagePath("/images/" + imageName);  // Path to access the image publicly
        }
        flashCardService.saveFlashCard(flashCard);
        return "redirect:/flashcard/list";
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
}

