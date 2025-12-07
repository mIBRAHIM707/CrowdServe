package com.crowdserve.controller;

import com.crowdserve.model.Task;
import com.crowdserve.model.User;
import com.crowdserve.repository.TaskRepository;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.NotificationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

/**
 * Controller for managing user's task tracking page (/my-tasks).
 * Displays tasks posted by the user and tasks assigned to the user.
 */
@Controller
@RequestMapping("/my-tasks")
public class MyTasksController {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    public MyTasksController(TaskRepository taskRepository, UserRepository userRepository, NotificationService notificationService) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.notificationService = notificationService;
    }

    /**
     * Display the my-tasks page with both posted and assigned tasks for the logged-in user.
     */
    @GetMapping
    public String myTasks(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/login";
        }

        String email = principal.getName();
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        User user = userOpt.get();

        // Fetch tasks where this user is the poster
        List<Task> postedTasks = taskRepository.findByPoster(user);
        
        // Fetch tasks where this user is the assigned worker
        List<Task> assignedTasks = taskRepository.findByWorker(user);

        model.addAttribute("postedTasks", postedTasks);
        model.addAttribute("assignedTasks", assignedTasks);
        
        // Add navbar attributes
        model.addAttribute("activePage", "my-tasks");
        model.addAttribute("pageTitle", "My Tasks");
        model.addAttribute("pageSubtitle", "Track tasks you've posted and work you're assigned to");
        
        // Add unread notifications count
        long unreadCount = notificationService.countUnreadNotificationsForUser(user);
        model.addAttribute("unreadCount", unreadCount);

        return "my-tasks";
    }

    /**
     * Cancel a task posted by the current user.
     * Only allows cancellation if the task status is OPEN or ASSIGNED.
     */
    @PostMapping("/{id}/cancel")
    public String cancelTask(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return "redirect:/my-tasks";
        }

        Task task = taskOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        // Verify the logged-in user is the poster of this task
        if (!task.getPoster().getId().equals(userOpt.get().getId())) {
            return "redirect:/my-tasks";
        }

        // Only allow cancellation for OPEN or ASSIGNED tasks
        if (task.getStatus().name().equals("OPEN") || task.getStatus().name().equals("ASSIGNED")) {
            task.setStatus(com.crowdserve.model.TaskStatus.CANCELLED);
            taskRepository.save(task);
        }

        return "redirect:/my-tasks";
    }

    /**
     * Close a completed task (archive it).
     * Only allows if the task status is COMPLETED.
     */
    @PostMapping("/{id}/close")
    public String closeTask(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return "redirect:/my-tasks";
        }

        Task task = taskOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        // Verify the logged-in user is the poster of this task
        if (!task.getPoster().getId().equals(userOpt.get().getId())) {
            return "redirect:/my-tasks";
        }

        // Only allow closing for COMPLETED tasks (in a real system, you might archive or delete)
        if (task.getStatus().name().equals("COMPLETED")) {
            // For now, we'll just keep it as COMPLETED. In a real app, you might soft-delete or archive.
            // task.setStatus(com.crowdserve.model.TaskStatus.ARCHIVED);
            // taskRepository.save(task);
        }

        return "redirect:/my-tasks";
    }

    /**
     * Mark a task as complete (only for workers assigned to the task).
     */
    @PostMapping("/{id}/complete")
    public String completeTask(@PathVariable Long id, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        Optional<Task> taskOpt = taskRepository.findById(id);
        if (taskOpt.isEmpty()) {
            return "redirect:/my-tasks";
        }

        Task task = taskOpt.get();
        Optional<User> userOpt = userRepository.findByEmail(principal.getName());
        if (userOpt.isEmpty()) {
            return "redirect:/login";
        }

        // Verify the logged-in user is the assigned worker for this task
        if (task.getWorker() == null || !task.getWorker().getId().equals(userOpt.get().getId())) {
            return "redirect:/my-tasks";
        }

        // Only allow completion if task is in ASSIGNED status
        if (task.getStatus().name().equals("ASSIGNED")) {
            task.setStatus(com.crowdserve.model.TaskStatus.COMPLETED);
            taskRepository.save(task);
        }

        return "redirect:/my-tasks";
    }
}
