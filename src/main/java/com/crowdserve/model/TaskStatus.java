package com.crowdserve.model;

/**
 * Enum representing the various states a task can be in throughout its lifecycle.
 */
public enum TaskStatus {
    /**
     * Task has been created and is waiting to be assigned to a worker
     */
    OPEN,
    
    /**
     * Task has been assigned to a worker but not yet completed
     */
    ASSIGNED,
    
    /**
     * Task has been successfully completed
     */
    COMPLETED,
    
    /**
     * Task has been cancelled and will not be completed
     */
    CANCELLED
}
