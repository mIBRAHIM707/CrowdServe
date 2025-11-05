package com.crowdserve.repository;

import com.crowdserve.model.Task;
import com.crowdserve.model.TaskStatus;
import com.crowdserve.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for Task entity database operations.
 * Provides CRUD operations and custom query methods for Task entities.
 */
@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    /**
     * Finds all tasks with a specific status.
     *
     * @param status the task status to filter by
     * @return a list of tasks with the specified status
     */
    List<Task> findByStatus(TaskStatus status);
    
    /**
     * Finds all tasks created by a specific user (poster).
     *
     * @param poster the user who created the tasks
     * @return a list of tasks created by the specified user
     */
    List<Task> findByPoster(User poster);
    
    /**
     * Finds all tasks assigned to a specific user (worker).
     *
     * @param worker the user assigned to the tasks
     * @return a list of tasks assigned to the specified user
     */
    List<Task> findByWorker(User worker);
}
