package com.crowdserve.service.facade;

import com.crowdserve.model.Task;
import com.crowdserve.model.User;
import com.crowdserve.service.TaskService;
import com.crowdserve.service.UserService;
import com.crowdserve.service.observer.TaskObserver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Facade Pattern Implementation for Task Workflow Operations.
 * 
 * This class provides a simplified, unified interface for complex task operations
 * that involve multiple services. It acts as the "Subject" in the Observer pattern,
 * notifying registered observers when tasks are completed.
 * 
 * Design Pattern: Facade
 * - Simplifies complex subsystem interactions
 * - Provides a single entry point for task workflow operations
 * - Coordinates between TaskService and UserService
 * 
 * Design Pattern: Observer (Subject role)
 * - Maintains list of observers
 * - Notifies observers on task completion events
 */
@Service
@Transactional
public class TaskWorkflowFacade {

    private final TaskService taskService;
    private final UserService userService;
    
    // Observer pattern: list of observers to notify on task events
    private final List<TaskObserver> observers = new ArrayList<>();

    @Autowired
    public TaskWorkflowFacade(TaskService taskService, UserService userService) {
        this.taskService = taskService;
        this.userService = userService;
    }

    /**
     * Registers an observer to be notified of task events.
     * Observer Pattern: attach operation
     *
     * @param observer the observer to register
     */
    public void addObserver(TaskObserver observer) {
        observers.add(observer);
    }

    /**
     * Removes an observer from the notification list.
     * Observer Pattern: detach operation
     *
     * @param observer the observer to remove
     */
    public void removeObserver(TaskObserver observer) {
        observers.remove(observer);
    }

    /**
     * Notifies all registered observers of a task completion event.
     * Observer Pattern: notify operation
     *
     * @param task the completed task
     */
    private void notifyTaskCompleted(Task task) {
        for (TaskObserver observer : observers) {
            observer.onTaskCompleted(task);
        }
    }

    /**
     * Accepts a task for a worker.
     * 
     * This operation:
     * 1. Validates the worker exists
     * 2. Assigns the worker to the task
     * 3. Changes task status to ASSIGNED
     *
     * @param taskId the ID of the task to accept
     * @param workerId the ID of the user accepting the task
     * @return the updated Task entity
     * @throws RuntimeException if task or worker not found
     * @throws IllegalStateException if task cannot be accepted (wrong status, same user as poster)
     */
    public Task acceptTask(Long taskId, Long workerId) {
        // Get the worker from UserService
        User worker = userService.getUserById(workerId);
        
        // Delegate to TaskService for the actual assignment
        return taskService.assignWorker(taskId, worker);
    }

    /**
     * Marks a task as completed and notifies all observers.
     * 
     * This operation:
     * 1. Marks the task as completed
     * 2. Notifies all registered observers (e.g., NotificationService)
     *
     * @param taskId the ID of the task to complete
     * @return the updated Task entity
     * @throws RuntimeException if task not found
     * @throws IllegalStateException if task cannot be completed (wrong status)
     */
    public Task completeTask(Long taskId) {
        // Mark task as completed via TaskService
        Task completedTask = taskService.markCompleted(taskId);
        
        // Observer Pattern: notify all observers of completion
        notifyTaskCompleted(completedTask);
        
        return completedTask;
    }

    /**
     * Gets a task by ID through the facade.
     * Convenience method for controllers.
     *
     * @param taskId the ID of the task
     * @return the Task entity
     * @throws RuntimeException if task not found
     */
    public Task getTask(Long taskId) {
        return taskService.getTaskById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
    }
}
