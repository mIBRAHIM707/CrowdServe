package com.crowdserve.service;

import com.crowdserve.dto.TaskCreationDto;
import com.crowdserve.model.Task;
import com.crowdserve.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Service interface defining task-related business operations.
 * Provides contract for task creation, retrieval, and management.
 */
public interface TaskService {
    
    /**
     * Creates a new task posted by a user.
     *
     * @param taskDto the task creation data
     * @param poster the user creating the task
     * @return the newly created and persisted Task entity
     */
    Task createTask(TaskCreationDto taskDto, User poster);
    
    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return an Optional containing the task if found, or empty if not found
     */
    Optional<Task> getTaskById(Long id);
    
    /**
     * Retrieves all tasks with OPEN status.
     *
     * @return a list of all open tasks
     */
    List<Task> getAllOpenTasks();
    
    /**
     * Retrieves all tasks associated with a specific user.
     * This includes tasks posted by the user and tasks assigned to the user.
     *
     * @param user the user whose tasks to retrieve
     * @return a list of tasks associated with the user
     */
    List<Task> getTasksForUser(User user);
    
    /**
     * Deletes a task if the current user is authorized to do so.
     *
     * @param taskId the ID of the task to delete
     * @param currentUser the user attempting to delete the task
     * @throws RuntimeException if user is not authorized or task not found
     */
    void deleteTask(Long taskId, User currentUser);
}
