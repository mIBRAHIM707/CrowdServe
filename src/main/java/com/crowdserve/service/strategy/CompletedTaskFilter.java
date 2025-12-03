package com.crowdserve.service.strategy;

import com.crowdserve.model.Task;
import com.crowdserve.model.TaskStatus;
import java.util.List;

/**
 * Filter strategy for retrieving tasks with COMPLETED status.
 * Completed tasks are those that have been successfully finished by workers.
 */
public class CompletedTaskFilter implements TaskFilterStrategy {

    /**
     * Filters tasks to return only those with COMPLETED status.
     *
     * @param tasks the list of tasks to filter
     * @return a list containing only COMPLETED tasks
     */
    @Override
    public List<Task> filter(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.COMPLETED)
                .toList();
    }

    /**
     * Returns the name of this filter strategy.
     *
     * @return "CompletedTaskFilter"
     */
    @Override
    public String getFilterName() {
        return "CompletedTaskFilter";
    }
}