package com.crowdserve.controller;

import com.crowdserve.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for handling dashboard and task-related pages.
 */
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    @Autowired
    private TaskService taskService;

    /**
     * Dashboard page - displays all open tasks
     */
    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("tasks", taskService.getAllOpenTasks());
        return "dashboard";
    }
}
