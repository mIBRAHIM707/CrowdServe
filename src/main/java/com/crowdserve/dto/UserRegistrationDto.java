package com.crowdserve.dto;

/**
 * Data Transfer Object for user registration.
 * Contains the minimal required information to create a new user account.
 */
public class UserRegistrationDto {
    private String fullName;
    private String email;
    private String password;

    // No-arg constructor (Spring needs this for form binding)
    public UserRegistrationDto() {
    }

    // All-args constructor
    public UserRegistrationDto(String fullName, String email, String password) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
    }

    // Getters and setters
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
