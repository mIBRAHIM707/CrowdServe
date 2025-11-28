package com.crowdserve.service.impl;

import com.crowdserve.model.Notification;
import com.crowdserve.model.Task;
import com.crowdserve.model.User;
import com.crowdserve.repository.NotificationRepository;
import com.crowdserve.service.NotificationService;
import com.crowdserve.service.facade.TaskWorkflowFacade;
import com.crowdserve.service.observer.TaskObserver;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Implementation of NotificationService that also acts as a TaskObserver.
 * 
 * Design Pattern: Observer
 * - Implements TaskObserver interface to receive task completion events
 * - Automatically creates notifications when tasks are completed
 * - Demonstrates loose coupling - NotificationService doesn't need to know
 *   about the task workflow internals
 */
@Service
@Transactional
public class NotificationServiceImpl implements NotificationService, TaskObserver {

    private static final Logger logger = LoggerFactory.getLogger(NotificationServiceImpl.class);

    private final NotificationRepository notificationRepository;
    private final TaskWorkflowFacade taskWorkflowFacade;

    @Autowired
    public NotificationServiceImpl(
            NotificationRepository notificationRepository,
            @Lazy TaskWorkflowFacade taskWorkflowFacade) {
        this.notificationRepository = notificationRepository;
        this.taskWorkflowFacade = taskWorkflowFacade;
    }

    /**
     * Register this service as an observer when the bean is initialized.
     */
    @PostConstruct
    public void registerAsObserver() {
        taskWorkflowFacade.addObserver(this);
        logger.info("NotificationService registered as TaskObserver");
    }

    /**
     * Observer Pattern: Called when a task is completed.
     * Creates notifications for both the poster and the worker.
     *
     * @param task the completed task
     */
    @Override
    public void onTaskCompleted(Task task) {
        logger.info("Task completed event received for task: {} (ID: {})", task.getTitle(), task.getId());

        // Notify the poster that their task was completed
        if (task.getPoster() != null) {
            createNotification(
                task.getPoster(),
                "Task Completed!",
                "Your task '" + task.getTitle() + "' has been completed by " + 
                    (task.getWorker() != null ? task.getWorker().getFullName() : "a worker") + ".",
                task
            );
        }

        // Notify the worker about successful completion
        if (task.getWorker() != null) {
            createNotification(
                task.getWorker(),
                "Task Completed!",
                "You have successfully completed the task '" + task.getTitle() + 
                    "'. Reward: $" + task.getReward(),
                task
            );
        }
    }

    @Override
    public Notification createNotification(User user, String title, String message, Task relatedTask) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setTitle(title);
        notification.setMessage(message);
        notification.setRelatedTask(relatedTask);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setRead(false);

        Notification saved = notificationRepository.save(notification);
        logger.info("Created notification for user {}: {}", user.getUsername(), title);
        return saved;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getNotificationsForUser(User user) {
        return notificationRepository.findByUserOrderByCreatedAtDesc(user);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notification> getUnreadNotifications(User user) {
        return notificationRepository.findByUserAndIsReadFalseOrderByCreatedAtDesc(user);
    }

    @Override
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId).ifPresent(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    @Override
    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {
        return notificationRepository.countByUserAndIsReadFalse(user);
    }
}
