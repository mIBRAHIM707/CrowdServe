package com.crowdserve.service.impl;

import com.crowdserve.dto.UserRegistrationDto;
import com.crowdserve.model.User;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Implementation of UserService interface.
 * Handles user registration, authentication, and user management operations.
 */
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Implementation of UserService interface.
 * Handles user registration, authentication, and user management operations.
 */
@Service
@Transactional
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Constructor-based dependency injection for required dependencies.
     *
     * @param userRepository the repository for user data access
     * @param passwordEncoder the BCrypt password encoder for secure password hashing
     */
    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Registers a new user in the system.
     * Validates that the email is unique, hashes the password, and persists the user.
     *
     * @param registrationDto the registration data containing user information
     * @return the newly created and persisted User entity
     * @throws IllegalStateException if a user with the given email already exists
     */
    @Override
    public User registerNewUser(UserRegistrationDto registrationDto) {
        // Check if user with this email already exists
        Optional<User> existingUser = userRepository.findByEmail(registrationDto.getEmail());
        if (existingUser.isPresent()) {
            throw new IllegalStateException(
                "User with email '" + registrationDto.getEmail() + "' already exists"
            );
        }

        // Create new user entity
        User newUser = new User();
        // No explicit username field in the DTO; use email as a unique username surrogate.
        newUser.setUsername(registrationDto.getEmail());
        newUser.setFullName(registrationDto.getFullName());
        newUser.setEmail(registrationDto.getEmail());
        
        // Hash the password before saving (CRITICAL for security)
        String hashedPassword = passwordEncoder.encode(registrationDto.getPassword());
        newUser.setPassword(hashedPassword);

        // Save and return the new user
        return userRepository.save(newUser);
    }

    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User entity
     * @throws RuntimeException if user is not found
     */
    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
    }
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        // Try finding by username first
        User user = userRepository.findByUsername(usernameOrEmail);
        
        // If not found, try finding by email
        if (user == null) {
            user = userRepository.findByEmail(usernameOrEmail).orElse(null);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getUsername())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }

    
    @Override
    public User updateProfile(User user) {
    // Step 1: Fetch existing user
    User existingUser = userRepository.findById(user.getId())
            .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

    // Step 2: Update allowed fields only
    existingUser.setFullName(user.getFullName());
    existingUser.setBio(user.getBio());
    // Step 3: Save updated user
    return userRepository.save(existingUser);
}

}
