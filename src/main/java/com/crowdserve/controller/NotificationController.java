package com.crowdserve.controller;

import com.crowdserve.model.Notification;
import com.crowdserve.model.User;
import com.crowdserve.repository.NotificationRepository;
import com.crowdserve.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

/**
 * Controller responsible for listing and managing user notifications.
 */
@Controller
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    public NotificationController(NotificationRepository notificationRepository, UserRepository userRepository) {
        this.notificationRepository = notificationRepository;
        this.userRepository = userRepository;
    }

    /**
     * Display the notifications page for the currently authenticated user.
     * Fetches notifications, marks unread notifications as read, and adds them to the model.
     */
    @GetMapping
    public String viewNotifications(Principal principal, Model model) {
        if (principal == null) {
            // Not authenticated — redirect to login (or show empty)
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOpt.get();
        // Retrieve notifications ordered by creation date (newest first)
        List<Notification> notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);

        // Mark unread notifications as read and persist them
        List<Notification> unread = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        if (!unread.isEmpty()) {
            unread.forEach(n -> n.setRead(true));
            notificationRepository.saveAll(unread);

            // Refresh notifications list after marking as read
            notifications = notificationRepository.findByUserOrderByCreatedAtDesc(user);
        }

        model.addAttribute("notifications", notifications);

        // Optional: provide a human friendly timestamp formatter pattern
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("MMM d, yyyy HH:mm").withZone(ZoneId.systemDefault());
        model.addAttribute("tsFormatter", fmt);
        
        // Add navbar attributes
        model.addAttribute("activePage", "notifications");
        model.addAttribute("pageTitle", "Notifications");
        model.addAttribute("pageSubtitle", "Recent alerts about your tasks and activity");
        
        // Add unread count (now 0 since we just marked all as read, but kept for consistency)
        long unreadCount = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user).size();
        model.addAttribute("unreadCount", unreadCount);

        return "notifications";
    }

    /**
     * Mark a single notification as read and redirect back to notifications page.
     * (Server-side fallback for non-JS clients)
     */
    @PostMapping("/read/{id}")
    public String markAsRead(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Notification> nOpt = notificationRepository.findById(id);
        if (nOpt.isPresent()) {
            Notification n = nOpt.get();
            // Make sure the logged-in user owns this notification
            Optional<User> userOpt = userRepository.findByEmail(principal.getName());
            if (userOpt.isPresent() && n.getUser().getId().equals(userOpt.get().getId())) {
                if (!n.isRead()) {
                    n.setRead(true);
                    notificationRepository.save(n);
                }
            }
        }

        return "redirect:/notifications";
    }

    /**
     * AJAX endpoint: mark a single notification as read. Returns JSON result.
     */
    @PostMapping("/mark-read/{id}")
    public ResponseEntity<?> markReadAjax(@PathVariable("id") Long id, Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthenticated");
        }

        Optional<Notification> nOpt = notificationRepository.findById(id);
        if (nOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("not_found");
        }

        Notification n = nOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty() || !n.getUser().getId().equals(userOpt.get().getId())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("forbidden");
        }

        if (!n.isRead()) {
            n.setRead(true);
            notificationRepository.save(n);
        }

        return ResponseEntity.ok().body(java.util.Map.of("success", true, "id", id));
    }

    /**
     * AJAX endpoint: mark all unread notifications as read for the current user.
     */
    @PostMapping("/mark-all-read")
    @Transactional
    public ResponseEntity<?> markAllRead(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthenticated");
        }

        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("unauthenticated");
        }

        User user = userOpt.get();
        java.util.List<Notification> unread = notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
        if (unread.isEmpty()) {
            return ResponseEntity.ok(java.util.Map.of("success", true, "marked", 0));
        }

        unread.forEach(n -> n.setRead(true));
        notificationRepository.saveAll(unread);

        return ResponseEntity.ok(java.util.Map.of("success", true, "marked", unread.size()));
    }
}
