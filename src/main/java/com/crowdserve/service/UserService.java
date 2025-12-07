package com.crowdserve.service;

import com.crowdserve.dto.UserRegistrationDto;
import com.crowdserve.model.User;

import java.util.Optional;

/**
 * Service interface defining user-related business operations.
 * Provides contract for user registration, retrieval, and management.
 */
public interface UserService {
    
    /**
     * Registers a new user in the system.
     *
     * @param registrationDto the registration data containing user information
     * @return the newly created and persisted User entity
     */
    User registerNewUser(UserRegistrationDto registrationDto);
    
    /**
     * Finds a user by their email address.
     *
     * @param email the email address to search for
     * @return an Optional containing the user if found, or empty if not found
     */
    Optional<User> findByEmail(String email);
    
    /**
     * Retrieves a user by their ID.
     *
     * @param id the ID of the user to retrieve
     * @return the User entity
     * @throws RuntimeException if user is not found
     */
    User getUserById(Long id);

    /**
     * Updates the user's profile information.
     *
     * @param user the user entity with updated information
     * @return the updated User entity
     */
    User updateProfile(User user);
}
