package com.crowdserve.controller;

import com.crowdserve.dto.TaskCreationDto;
import com.crowdserve.service.TaskService;
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

    @GetMapping("/create")
    public String showCreateTaskForm(Model model) {
        // TODO: Add empty TaskCreationDto to model
        return "create-task";
    }

    @GetMapping
    public String showDashboard(Model model) {
        // TODO: Get all open tasks and add to model
        return "dashboard";
    }

    @PostMapping("/create")
    public String createTask(@ModelAttribute TaskCreationDto taskDto) {
        // TODO: Call taskService.createTask(taskDto)
        return "redirect:/dashboard";
    }

    @GetMapping("/{id}")
    public String viewTask(@PathVariable Long id, Model model) {
        // TODO: Get task by ID and add to model
        return "task-details";
    }
}
