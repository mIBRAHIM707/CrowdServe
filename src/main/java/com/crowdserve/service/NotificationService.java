package com.crowdserve.service;

import com.crowdserve.model.Notification;
import com.crowdserve.model.Task;
import com.crowdserve.model.User;

import java.util.List;

/**
 * Service interface for notification operations.
 */
public interface NotificationService {

    /**
     * Creates a notification for a user.
     *
     * @param user the user to notify
     * @param title notification title
     * @param message notification message
     * @param relatedTask optional related task
     * @return the created notification
     */
    Notification createNotification(User user, String title, String message, Task relatedTask);

    /**
     * Gets all notifications for a user.
     *
     * @param user the user
     * @return list of notifications
     */
    List<Notification> getNotificationsForUser(User user);

    /**
     * Gets unread notifications for a user.
     *
     * @param user the user
     * @return list of unread notifications
     */
    List<Notification> getUnreadNotifications(User user);

    /**
     * Marks a notification as read.
     *
     * @param notificationId the notification ID
     */
    void markAsRead(Long notificationId);

    /**
     * Gets count of unread notifications.
     *
     * @param user the user
     * @return count of unread notifications
     */
    long getUnreadCount(User user);
}
