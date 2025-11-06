package com.crowdserve.service.impl;

import com.crowdserve.dto.UserRegistrationDto;
import com.crowdserve.model.User;
import com.crowdserve.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

/**
 * Unit tests for UserServiceImpl.
 * Tests user registration, retrieval, and error handling using Mockito.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto testRegistrationDto;
    private User testUser;

    @BeforeEach
    void setUp() {
        // Set up test data
        testRegistrationDto = new UserRegistrationDto(
            "John Doe",
            "john.doe@example.com",
            "password123"
        );

        testUser = new User();
        testUser.setId(1L);
        testUser.setFullName("John Doe");
        testUser.setEmail("john.doe@example.com");
        testUser.setPassword("hashedPassword123");
    }

    /**
     * Test successful user registration.
     * Verifies that:
     * - Email uniqueness is checked
     * - Password is hashed before saving
     * - User is saved to repository
     * - Returned user has hashed password
     */
    @Test
    void testRegisterNewUser_Success() {
        // Arrange
        when(userRepository.findByEmail(testRegistrationDto.email()))
            .thenReturn(Optional.empty());
        
        when(passwordEncoder.encode(testRegistrationDto.password()))
            .thenReturn("hashedPassword123");
        
        when(userRepository.save(any(User.class)))
            .thenReturn(testUser);

        // Act
        User result = userService.registerNewUser(testRegistrationDto);

        // Assert
        assertNotNull(result, "Registered user should not be null");
        assertEquals(testUser.getId(), result.getId(), "User ID should match");
        assertEquals(testUser.getFullName(), result.getFullName(), "Full name should match");
        assertEquals(testUser.getEmail(), result.getEmail(), "Email should match");
        assertEquals("hashedPassword123", result.getPassword(), 
            "Password should be hashed, not plain text");

        // Verify interactions
        verify(userRepository, times(1)).findByEmail(testRegistrationDto.email());
        verify(passwordEncoder, times(1)).encode(testRegistrationDto.password());
        verify(userRepository, times(1)).save(any(User.class));
    }

    /**
     * Test user registration with duplicate email.
     * Verifies that IllegalStateException is thrown when email already exists.
     */
    @Test
    void testRegisterNewUser_EmailAlreadyExists() {
        // Arrange
        User existingUser = new User();
        existingUser.setId(1L);
        existingUser.setEmail(testRegistrationDto.email());
        
        when(userRepository.findByEmail(testRegistrationDto.email()))
            .thenReturn(Optional.of(existingUser));

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> userService.registerNewUser(testRegistrationDto),
            "Should throw IllegalStateException when email already exists"
        );

        assertTrue(exception.getMessage().contains(testRegistrationDto.email()),
            "Exception message should contain the duplicate email");
        assertTrue(exception.getMessage().contains("already exists"),
            "Exception message should indicate the user already exists");

        // Verify that password encoding and save were never called
        verify(userRepository, times(1)).findByEmail(testRegistrationDto.email());
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    /**
     * Test finding user by email - successful case.
     */
    @Test
    void testFindByEmail_Success() {
        // Arrange
        when(userRepository.findByEmail(testUser.getEmail()))
            .thenReturn(Optional.of(testUser));

        // Act
        Optional<User> result = userService.findByEmail(testUser.getEmail());

        // Assert
        assertTrue(result.isPresent(), "User should be found");
        assertEquals(testUser.getEmail(), result.get().getEmail(), "Email should match");
        
        verify(userRepository, times(1)).findByEmail(testUser.getEmail());
    }

    /**
     * Test finding user by email - not found case.
     */
    @Test
    void testFindByEmail_NotFound() {
        // Arrange
        when(userRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.findByEmail("nonexistent@example.com");

        // Assert
        assertFalse(result.isPresent(), "User should not be found");
        
        verify(userRepository, times(1)).findByEmail("nonexistent@example.com");
    }

    /**
     * Test getting user by ID - successful case.
     */
    @Test
    void testGetUserById_Success() {
        // Arrange
        when(userRepository.findById(1L))
            .thenReturn(Optional.of(testUser));

        // Act
        User result = userService.getUserById(1L);

        // Assert
        assertNotNull(result, "User should not be null");
        assertEquals(testUser.getId(), result.getId(), "User ID should match");
        assertEquals(testUser.getEmail(), result.getEmail(), "Email should match");
        
        verify(userRepository, times(1)).findById(1L);
    }

    /**
     * Test getting user by ID - not found case.
     * Verifies that RuntimeException is thrown when user doesn't exist.
     */
    @Test
    void testGetUserById_NotFound() {
        // Arrange
        Long nonExistentId = 999L;
        when(userRepository.findById(nonExistentId))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
            RuntimeException.class,
            () -> userService.getUserById(nonExistentId),
            "Should throw RuntimeException when user is not found"
        );

        assertTrue(exception.getMessage().contains("not found"),
            "Exception message should indicate user not found");
        assertTrue(exception.getMessage().contains(String.valueOf(nonExistentId)),
            "Exception message should contain the ID that was not found");

        verify(userRepository, times(1)).findById(nonExistentId);
    }

    /**
     * Test that password is never stored in plain text.
     * Verifies the password encoder is always called during registration.
     */
    @Test
    void testRegisterNewUser_PasswordIsHashed() {
        // Arrange
        String plainPassword = "mySecretPassword";
        String hashedPassword = "$2a$10$hashedPasswordValue";
        
        UserRegistrationDto dto = new UserRegistrationDto(
            "Test User",
            "test@example.com",
            plainPassword
        );

        when(userRepository.findByEmail(dto.email()))
            .thenReturn(Optional.empty());
        
        when(passwordEncoder.encode(plainPassword))
            .thenReturn(hashedPassword);
        
        User savedUser = new User();
        savedUser.setPassword(hashedPassword);
        when(userRepository.save(any(User.class)))
            .thenReturn(savedUser);

        // Act
        User result = userService.registerNewUser(dto);

        // Assert
        assertNotEquals(plainPassword, result.getPassword(),
            "Plain password should never be stored");
        assertEquals(hashedPassword, result.getPassword(),
            "Only hashed password should be stored");
        
        verify(passwordEncoder, times(1)).encode(plainPassword);
    }
}
