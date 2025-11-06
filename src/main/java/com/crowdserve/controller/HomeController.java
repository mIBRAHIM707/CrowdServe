package com.crowdserve.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for handling home and public pages.
 */
@Controller
public class HomeController {

    /**
     * Home page - accessible to everyone
     */
    @GetMapping("/")
    public String home() {
        return "index";
    }

    /**
     * Login page
     */
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    /**
     * Registration page
     */
    @GetMapping("/register")
    public String register() {
        return "register";
    }
}
