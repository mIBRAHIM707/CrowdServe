package com.crowdserve.controller;

import com.crowdserve.dto.TaskCreationDto;
import com.crowdserve.model.Task;
import com.crowdserve.model.User;
import com.crowdserve.service.TaskService;
import com.crowdserve.service.facade.TaskWorkflowFacade;
import com.crowdserve.repository.UserRepository;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskWorkflowFacade taskWorkflowFacade; 

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        // Always create a fresh DTO to ensure form is empty
        TaskCreationDto freshDto = new TaskCreationDto(null, null, null, null);
        model.addAttribute("taskDto", freshDto);
        // Clear any previous error/success messages
        model.addAttribute("successMessage", null);
        model.addAttribute("errorMessage", null);
        return "create-task";
    }

    @GetMapping
    public String showDashboard(Model model) {
        // 1. Call the Service Layer to retrieve data based on business rules (e.g., only OPEN tasks)
        // This method assumes TaskService has the signature: List<Task> getAllOpenTasks()
        var taskList = taskService.getAllOpenTasks();

        // 2. Add the list of tasks to the Model object, making it available to the view template
        // The key "tasks" is used by your Thymeleaf/JSP template to iterate over the data.
        model.addAttribute("tasks", taskList); 
        
        // 3. Return the name of the template (View Layer)
        // Spring will look for 'src/main/resources/templates/dashboard.html'
        return "dashboard"; 
    }

@PostMapping("/create")
    public String createTask(@ModelAttribute("taskDto") TaskCreationDto taskDto, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Get current user - try username first, then email
            User user = userRepository.findByUsername(principal.getName());
            if (user == null) {
                user = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            }
            
            // Create task
            taskService.createTask(taskDto, user);
            redirectAttributes.addFlashAttribute("successMessage", "Task created successfully!");
            return "redirect:/tasks";
            
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Error creating task: " + e.getMessage());
            return "redirect:/tasks/create";
        }
    }


    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model, Principal principal) {
        // Get task by ID
        Task task = taskWorkflowFacade.getTask(id);
        model.addAttribute("task", task);
        
        // Get current user for authorization checks in template
        if (principal != null) {
            User currentUser = userRepository.findByUsername(principal.getName());
            if (currentUser == null) {
                currentUser = userRepository.findByEmail(principal.getName()).orElse(null);
            }
            model.addAttribute("currentUser", currentUser);
        }
        
        return "task-details";
    }

    /**
     * Accept a task - assigns the current user as the worker.
     * Only available for tasks with OPEN status.
     */
    @PostMapping("/{id}/accept")
    public String acceptTask(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Get current user
            User worker = userRepository.findByUsername(principal.getName());
            if (worker == null) {
                worker = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            }
            
            // Use facade to accept the task
            taskWorkflowFacade.acceptTask(id, worker.getId());
            redirectAttributes.addFlashAttribute("successMessage", "You have accepted this task!");
            
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/tasks/" + id;
    }

    /**
     * Complete a task - marks the task as completed.
     * Only available for tasks with ASSIGNED status.
     * Only the assigned worker or poster can complete the task.
     */
    @PostMapping("/{id}/complete")
    public String completeTask(@PathVariable Long id, Principal principal, RedirectAttributes redirectAttributes) {
        try {
            // Get current user for validation
            User currentUser = userRepository.findByUsername(principal.getName());
            if (currentUser == null) {
                currentUser = userRepository.findByEmail(principal.getName())
                    .orElseThrow(() -> new IllegalStateException("User not found"));
            }
            
            // Verify the current user is either the worker or poster
            Task task = taskWorkflowFacade.getTask(id);
            boolean isWorker = task.getWorker() != null && task.getWorker().getId().equals(currentUser.getId());
            boolean isPoster = task.getPoster().getId().equals(currentUser.getId());
            
            if (!isWorker && !isPoster) {
                throw new IllegalStateException("Only the assigned worker or task poster can complete this task");
            }
            
            // Use facade to complete the task (this also triggers Observer notifications)
            taskWorkflowFacade.completeTask(id);
            redirectAttributes.addFlashAttribute("successMessage", "Task has been marked as completed!");
            
        } catch (IllegalStateException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        
        return "redirect:/tasks/" + id;
    }
}
