package com.FlashCardsHackathon.FlashcardsHackathon.repository;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.AppUser;  // Correct import
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {  // Using AppUser
    Optional<AppUser> findByUsername(String username);  // This returns an Optional<AppUser>

    boolean existsByUsername(String username);
}
