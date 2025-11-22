package com.crowdserve.controller;

import com.crowdserve.dto.TaskCreationDto;
import com.crowdserve.service.TaskService;
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

@Controller
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private UserRepository userRepository; 

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        // TODO: Add empty TaskCreationDto to model
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
    public String createTask(@ModelAttribute TaskCreationDto taskDto, Principal principal) {

        // 1. Get identifier (email) of the currently logged-in user
        String email = principal.getName();

        // 2. Fetch User Entity from DB using the INJECTED instance (userRepository)
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("User not found with email: " + email));
        // 3. Call service method to create task
        taskService.createTask(taskDto, user);
        return "redirect:/tasks";
    }


    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        // TODO: Get task by ID and add to model
        return "task-details";
    }
}
