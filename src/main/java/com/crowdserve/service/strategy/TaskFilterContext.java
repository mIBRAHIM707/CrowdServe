package com.crowdserve.service.strategy;

import com.crowdserve.model.Task;
import java.util.List;
import java.util.Objects;

/**
 * Context class that uses a TaskFilterStrategy to filter tasks.
 * This class implements the Strategy design pattern by allowing
 * the filtering algorithm to be selected and changed at runtime.
 */
public class TaskFilterContext {

    private TaskFilterStrategy strategy;

    /**
     * Constructs a TaskFilterContext with the given filter strategy.
     *
     * @param strategy the TaskFilterStrategy to use for filtering
     * @throws IllegalArgumentException if strategy is null
     */
    public TaskFilterContext(TaskFilterStrategy strategy) {
        Objects.requireNonNull(strategy, "Filter strategy cannot be null");
        this.strategy = strategy;
    }

    /**
     * Sets the filter strategy to be used.
     * Allows switching strategies at runtime.
     *
     * @param strategy the new TaskFilterStrategy
     * @throws IllegalArgumentException if strategy is null
     */
    public void setStrategy(TaskFilterStrategy strategy) {
        Objects.requireNonNull(strategy, "Filter strategy cannot be null");
        this.strategy = strategy;
    }

    /**
     * Executes the filtering operation using the current strategy.
     *
     * @param tasks the list of tasks to filter
     * @return the filtered list of tasks based on the current strategy
     */
    public List<Task> executeFilter(List<Task> tasks) {
        return strategy.filter(tasks);
    }

    /**
     * Returns the name of the current filter strategy.
     *
     * @return the name of the active strategy
     */
    public String getCurrentFilterName() {
        return strategy.getFilterName();
    }
}