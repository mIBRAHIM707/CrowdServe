package com.crowdserve.controller;

import com.crowdserve.model.User;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class UserController {

    @Autowired
    private UserService userService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/profile")
    public String viewProfile(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            // No authenticated principal available; return profile view without model
            return "profile";
        }

        try {
            String principalName = userDetails.getUsername();

            // Try email first, then username as fallback
            var optionalUser = userService.findByEmail(principalName);
            if (optionalUser.isPresent()) {
                model.addAttribute("user", optionalUser.get());
                return "profile";
            }

            // Fallback: try lookup by username
            var byUsername = userRepository.findByUsername(principalName);
            if (byUsername != null) {
                model.addAttribute("user", byUsername);
                return "profile";
            }

        } catch (Exception ex) {
            // Swallow exception to avoid Whitelabel 500; in production log this
        }

        return "profile";
    }

    @PostMapping("/profile/update")
    public String updateProfile(@ModelAttribute User user) {
        if (user == null || user.getId() == null) {
            // Missing id: do not attempt update to avoid internal server error
            return "redirect:/profile?error";
        }

        try {
            userService.updateProfile(user);
            return "redirect:/profile?success";
        } catch (Exception ex) {
            // In production log the exception. Here we return to profile with error flag.
            return "redirect:/profile?error";
        }
    }
}
