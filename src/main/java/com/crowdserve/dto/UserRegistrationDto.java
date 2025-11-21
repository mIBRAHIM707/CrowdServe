package com.crowdserve.dto;

/**
 * Data Transfer Object for user registration.
 * Contains the minimal required information to create a new user account.
 */
public record UserRegistrationDto(
    String username,
    String fullName,
    String email,
    String password
) {
}
