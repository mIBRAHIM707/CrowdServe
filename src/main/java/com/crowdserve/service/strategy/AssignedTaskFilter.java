package com.crowdserve.service.strategy;
import com.crowdserve.model.Task;
import java.util.List;


public class AssignedTaskFilter implements TaskFilterStrategy {
    @Override
    public List<Task> filter(List<Task> tasks) {
        return tasks.stream()
                .filter(task -> task.getStatus() == com.crowdserve.model.TaskStatus.ASSIGNED)
                .toList();
    }

    @Override
    public String getFilterName() {
        return "AssignedTaskFilter";
    }

    
}