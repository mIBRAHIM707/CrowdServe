package com.crowdserve.controller;

import com.crowdserve.model.User;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.NotificationService;
import com.crowdserve.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.Optional;

/**
 * Controller for handling dashboard and task-related pages.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final TaskService taskService;
    private final NotificationService notificationService;
    private final UserRepository userRepository;

    public DashboardController(TaskService taskService, NotificationService notificationService, UserRepository userRepository) {
        this.taskService = taskService;
        this.notificationService = notificationService;
        this.userRepository = userRepository;
    }

    /**
     * Dashboard page - displays all open tasks
     */
    @GetMapping
    public String dashboard(Principal principal, Model model) {
        model.addAttribute("tasks", taskService.getAllOpenTasks());
        
        // Add navbar attributes
        model.addAttribute("activePage", "dashboard");
        model.addAttribute("pageTitle", "CrowdServe Dashboard");
        model.addAttribute("pageSubtitle", "Open tasks feed — find work or assign workers");
        
        // Add unread notifications count if user is authenticated
        if (principal != null) {
            Optional<User> userOpt = userRepository.findByEmail(principal.getName());
            if (userOpt.isPresent()) {
                long unreadCount = notificationService.countUnreadNotificationsForUser(userOpt.get());
                model.addAttribute("unreadCount", unreadCount);
            }
        }
        
        return "dashboard";
    }
}

