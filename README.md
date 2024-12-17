Flashcard App with Spring Boot and Thymeleaf 


This is a web application built with Spring Boot and Thymeleaf that allows users to create and manage flashcards and flashcard decks. It includes features for creating personalized flashcards,creating AI-made flashcards using an already trained model, taking quizzes, and revising them. The app also supports user authentication and the ability to share decks publicly.

Features


User Authentication: Each user has their own account, with secure login and registration handled by Spring Security.
Flashcards & Decks: Users can create, edit, and delete their own flashcards and organize them into decks.
Quizzes: Users can take quizzes based on their flashcards to test their knowledge.
Revision Mode: A revision feature that helps users review flashcards.
Public Sharing: Users can choose to make their flashcards and decks public for others to view and use.
Personalized Dashboard: Users can see their own decks and flashcards on a dashboard.
Technologies Used
Backend: Spring Boot, Spring Security, Spring Data JPA
Frontend: Thymeleaf, Bootstrap
Database: H2 Database (or your choice of SQL database)
Security: Spring Security for user authentication and authorization


Setup
Clone this repository:

bash
Copy code
git clone https://github.com/yourusername/flashcard-app.git
cd flashcard-app
Build and run the application:

bash
Copy code
./mvnw spring-boot:run
Open the application in your browser at http://localhost:80.

Create a user account to get started.

Usage


Create Flashcards & Decks: Navigate to the "My Flashcards" section to add new flashcards and group them into decks.
Take a Quiz: Select a deck and start a quiz. The app will randomly select flashcards for you to answer.
Revise Flashcards: Go to the revision section to review flashcards that you have previously created.
Public Decks: Explore decks made public by other users in the "Public Decks" section.
Contribution
Feel free to fork this repository, submit pull requests, or open issues if you'd like to contribute. Your feedback and contributions are welcome!
