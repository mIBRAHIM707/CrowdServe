package com.crowdserve.service.strategy;

import com.crowdserve.model.Task;
import java.util.List;

/**
 * Strategy interface for filtering tasks based on different criteria.
 * Implementations define different filtering strategies (e.g., by status).
 */
public interface TaskFilterStrategy {
    /**
     * Filters a list of tasks based on the strategy implementation.
     *
     * @param tasks the list of tasks to filter
     * @return a filtered list of tasks
     */
    List<Task> filter(List<Task> tasks);

    /**
     * Returns the name/description of this filter strategy.
     *
     * @return the filter name
     */
    String getFilterName();
}
