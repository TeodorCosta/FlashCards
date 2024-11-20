package com.FlashCardsHackathon.FlashcardsHackathon.service;

import com.FlashCardsHackathon.FlashcardsHackathon.entity.AppUser;  // Correct import
import com.FlashCardsHackathon.FlashcardsHackathon.repository.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> user = userRepository.findByUsername(username);  // Corrected to AppUser
        if (user.isPresent()) {
            return User.withUsername(user.get().getUsername())
                    .password(user.get().getPassword())
                    .roles(user.get().getRole())  // Set role (USER, ADMIN, etc.)
                    .build();
        } else {
            throw new UsernameNotFoundException("User not found");
        }
    }

    public void saveUser(AppUser appUser){
        userRepository.save(appUser);
    }
}
