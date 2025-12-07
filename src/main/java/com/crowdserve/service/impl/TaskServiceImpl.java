package com.crowdserve.service.impl;

import com.crowdserve.dto.TaskCreationDto;
import com.crowdserve.model.Task;
import com.crowdserve.model.TaskStatus;
import com.crowdserve.model.User;
import com.crowdserve.repository.TaskRepository;
import com.crowdserve.repository.UserRepository;
import com.crowdserve.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Implementation of TaskService interface.
 * Handles task creation, retrieval, assignment, and deletion operations.
 */
@Service
@Transactional
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    /**
     * Constructor-based dependency injection for required dependencies.
     *
     * @param taskRepository the repository for task data access
     * @param userRepository the repository for user data access
     */
    @Autowired
    public TaskServiceImpl(TaskRepository taskRepository, UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
    }

    /**
     * Creates a new task posted by a user.
     * The task is created with OPEN status and assigned to the poster.
     *
     * @param taskDto the task creation data
     * @param poster the user creating the task
     * @return the newly created and persisted Task entity
     */
    @Override
    public Task createTask(TaskCreationDto taskDto, User poster) {
        Task newTask = new Task();
        newTask.setTitle(taskDto.title());
        newTask.setDescription(taskDto.description());
        newTask.setLocation(taskDto.location());
        newTask.setReward(taskDto.reward());
        newTask.setStatus(TaskStatus.OPEN);
        newTask.setPoster(poster);
        newTask.setWorker(null); // No worker assigned initially

        return taskRepository.save(newTask);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param id the ID of the task to retrieve
     * @return an Optional containing the task if found, or empty if not found
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Task> getTaskById(Long id) {
        return taskRepository.findById(id);
    }

    /**
     * Retrieves all tasks with OPEN status.
     * These are tasks available for workers to accept.
     *
     * @return a list of all open tasks
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> getAllOpenTasks() {
        return taskRepository.findByStatus(TaskStatus.OPEN);
    }

    /**
     * Retrieves all tasks associated with a specific user.
     * This includes tasks posted by the user and tasks assigned to the user.
     *
     * @param user the user whose tasks to retrieve
     * @return a list of tasks associated with the user
     */
    @Override
    @Transactional(readOnly = true)
    public List<Task> getTasksForUser(User user) {
        List<Task> userTasks = new ArrayList<>();
        
        // Get tasks posted by the user
        List<Task> postedTasks = taskRepository.findByPoster(user);
        userTasks.addAll(postedTasks);
        
        // Get tasks assigned to the user (as worker)
        List<Task> assignedTasks = taskRepository.findByWorker(user);
        userTasks.addAll(assignedTasks);
        
        return userTasks;
    }

    /**
     * Deletes a task if the current user is authorized to do so.
     * Only the original poster can delete a task.
     *
     * @param taskId the ID of the task to delete
     * @param currentUser the user attempting to delete the task
     * @throws RuntimeException if task is not found
     * @throws IllegalStateException if user is not authorized to delete the task
     */
    @Override
    public void deleteTask(Long taskId, User currentUser) {
        // Find the task
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        // Check if the current user is the original poster
        if (!task.getPoster().getId().equals(currentUser.getId())) {
            throw new IllegalStateException(
                "Only the original poster can delete this task. " +
                "Task was posted by user ID: " + task.getPoster().getId() +
                ", but current user ID is: " + currentUser.getId()
            );
        }
        
        // Delete the task
        taskRepository.delete(task);
    }

    /**
     * Assigns a worker to a task and changes status to ASSIGNED.
     * Only tasks with OPEN status can be assigned.
     *
     * @param taskId the ID of the task to assign
     * @param worker the user to assign as worker
     * @return the updated Task entity
     * @throws RuntimeException if task not found
     * @throws IllegalStateException if task is not in OPEN status
     */
    @Override
    public Task assignWorker(Long taskId, User worker) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        // Validate task is in OPEN status
        if (task.getStatus() != TaskStatus.OPEN) {
            throw new IllegalStateException(
                "Cannot assign worker to task. Task status must be OPEN, but was: " + task.getStatus()
            );
        }
        
        // Prevent poster from accepting their own task
        if (task.getPoster().getId().equals(worker.getId())) {
            throw new IllegalStateException("You cannot accept your own task");
        }
        
        // Assign worker and update status
        task.setWorker(worker);
        task.setStatus(TaskStatus.ASSIGNED);
        
        return taskRepository.save(task);
    }

    /**
     * Marks a task as completed.
     * Only tasks with ASSIGNED status can be completed.
     *
     * @param taskId the ID of the task to complete
     * @return the updated Task entity
     * @throws RuntimeException if task not found
     * @throws IllegalStateException if task is not in ASSIGNED status
     */
    @Override
    public Task markCompleted(Long taskId) {
        Task task = taskRepository.findById(taskId)
            .orElseThrow(() -> new RuntimeException("Task not found with id: " + taskId));
        
        // Validate task is in ASSIGNED status
        if (task.getStatus() != TaskStatus.ASSIGNED) {
            throw new IllegalStateException(
                "Cannot complete task. Task status must be ASSIGNED, but was: " + task.getStatus()
            );
        }
        
        // Update status to COMPLETED
        task.setStatus(TaskStatus.COMPLETED);
        
        return taskRepository.save(task);
    }
}
