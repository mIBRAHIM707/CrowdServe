package com.crowdserve.service.strategy;

import com.crowdserve.model.Task;
import com.crowdserve.model.TaskStatus;
import java.util.List;

/**
 * Filter strategy for retrieving tasks with OPEN status.
 * Open tasks are available for workers to accept and complete.
 */
public class OpenTaskFilter implements TaskFilterStrategy {

    /**
     * Filters tasks to return only those with OPEN status.
     *
     * @param tasks the list of tasks to filter
     * @return a list containing only OPEN tasks
     */
    @Override
    public List<Task> filter(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.OPEN)
                .toList();
    }

    /**
     * Returns the name of this filter strategy.
     *
     * @return "OpenTaskFilter"
     */
    @Override
    public String getFilterName() {
        return "OpenTaskFilter";
    }
}