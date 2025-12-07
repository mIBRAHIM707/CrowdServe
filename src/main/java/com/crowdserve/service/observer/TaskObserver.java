package com.crowdserve.service.observer;

import com.crowdserve.model.Task;

/**
 * Observer Pattern Interface for Task Events.
 * 
 * Implementations of this interface will be notified when
 * task-related events occur (e.g., task completion).
 * 
 * Design Pattern: Observer
 * - Defines update interface for objects that should be notified of changes
 * - Enables loose coupling between the subject (TaskWorkflowFacade) and observers
 * - Supports the Open/Closed Principle - new observers can be added without modifying existing code
 */
public interface TaskObserver {

    /**
     * Called when a task is completed.
     * Implementations should handle the notification appropriately
     * (e.g., send notification, update statistics, trigger workflows).
     *
     * @param task the task that was completed
     */
    void onTaskCompleted(Task task);
}
