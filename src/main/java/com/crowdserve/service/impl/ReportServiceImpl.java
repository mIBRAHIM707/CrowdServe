package com.crowdserve.service.impl;

import com.crowdserve.model.Task;
import com.crowdserve.model.TaskStatus;
import com.crowdserve.model.User;
import com.crowdserve.repository.TaskRepository;
import com.crowdserve.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Lazy  //It will not be instantiated.It will when I will call the method named generateCompletedTasksReport().

public class ReportServiceImpl implements ReportService {
 
    @Autowired
    private TaskRepository taskRepository;
  
    @Override
    public byte[] generateCompletedTasksReport() {
        // ... (existing implementation) ...
        List<Task> completedTasks = taskRepository.findByStatus(TaskStatus.COMPLETED);

        String csvHeader = "Task ID,Title,Description,Reward,Status\n";
        String csvRows = completedTasks.stream()
            .map(task -> String.format("%d,\"%s\",\"%s\",%.2f,%s", 
                                       task.getId(), 
                                       task.getTitle(),
                                       task.getDescription().replace("\"", "\"\""), 
                                       task.getReward(), 
                                       task.getStatus()))
            .collect(Collectors.joining("\n"));

        String csvContent = csvHeader + csvRows;
        return csvContent.getBytes(StandardCharsets.UTF_8); 
    }

    @Override
    public byte[] generateTopContributorsReport() {
        // 1. Fetch all completed tasks
        // Ideally, this should be a custom JPQL query: "SELECT t.doer, COUNT(t) FROM Task t WHERE t.status = 'COMPLETED' GROUP BY t.doer"
        // For now, we will do the aggregation in Java for simplicity.
        List<Task> completedTasks = taskRepository.findByStatus(TaskStatus.COMPLETED);

        // 2. Group by Doer (User) and Count
        // Map<User, Long> -> Key: User (Doer), Value: Count of completed tasks
        Map<User, Long> contributorStats = completedTasks.stream()
            .filter(task -> task.getWorker() != null) // Ensure doer is not null
            .collect(Collectors.groupingBy(Task::getWorker, Collectors.counting()));

        // 3. Sort by Count (Descending) to find top contributors
        List<Map.Entry<User, Long>> sortedContributors = contributorStats.entrySet().stream()
            .sorted((e1, e2) -> e2.getValue().compareTo(e1.getValue())) // Sort by Value (Count) Descending
            .collect(Collectors.toList());

        // 4. Format as CSV
        String csvHeader = "User ID,Name,Email,Tasks Completed\n";
        String csvRows = sortedContributors.stream()
            .map(entry -> {
                User user = entry.getKey();
                Long count = entry.getValue();
                return String.format("%d,\"%s\",\"%s\",%d",
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        count);
            })
            .collect(Collectors.joining("\n"));

        String csvContent = csvHeader + csvRows;

        // 5. Return as byte array
        return csvContent.getBytes(StandardCharsets.UTF_8);
    }

    // @Override
    // public byte[] generateUserEarningsReport(Long userId) { ... }
}