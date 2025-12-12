package com.crowdserve;

import com.crowdserve.dto.UserRegistrationDto;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthenticationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    void setup() {
        // Clear tasks first to avoid FK constraints, then users.
        taskRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void testRegistrationAndLoginFlow() throws Exception {
        // 1. Test Registration
        mockMvc.perform(post("/register")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .param("fullName", "Test User")
                .param("email", "test@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Verify user is in DB
        assert userRepository.findByEmail("test@example.com").isPresent();

        // 2. Test Login
        mockMvc.perform(post("/login")
                .with(csrf())
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                // Login accepts username or email; service uses email as username surrogate
                .param("username", "test@example.com")
                .param("password", "password123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }
}
